package kr.co.kimga.ecommerce.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kimga.ecommerce.api.controller.order.OrderItemRequest;
import kr.co.kimga.ecommerce.api.controller.order.OrderRequest;
import kr.co.kimga.ecommerce.api.controller.order.OrderResponse;
import kr.co.kimga.ecommerce.api.controller.order.PaymentRequest;
import kr.co.kimga.ecommerce.api.domain.payment.PaymentMethod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRequester {

    private static final String BASE_URL = "http://localhost:8080/v1";
    private static final String PRODUCTS_URL = BASE_URL + "/products";
    private static final String ORDERS_URL = BASE_URL + "/orders";
    private static final PaymentMethod[] PAYMENT_METHODS = PaymentMethod.values();
    private static final Random RANDOM = new Random();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        int maxWorker = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(maxWorker);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try {
            int page = 0;
            int size = 1000;
            boolean hasNextPage = true;

            while (hasNextPage && page < 10000) {
                String productJson = fetchProduct(page, size);
                JsonNode productsNode = OBJECT_MAPPER.readTree(productJson);
                JsonNode contentNode = productsNode.get("content");

                for (JsonNode productNode : contentNode) {
                    String productId = productNode.get("productId").asText();
                    int stockQuantity = productsNode.get("stockQuantity").asInt();

                    CompletableFuture<Void> future = CompletableFuture.runAsync(
                            () -> processProduct(productId, stockQuantity), executorService);
                    futures.add(future);
                }

                hasNextPage = !productsNode.get("last").asBoolean();
                page++;
            }

        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }


        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
    }

    private static void processProduct(String productId, int stockQuantity) {
        int quantity = Math.max((int) Math.floor(stockQuantity / 10.0), 1);
        int randomNum = RANDOM.nextInt(16);

        OrderResponse orderResponse = createOrder(productId, quantity);
        if (orderResponse != null) {
            if (randomNum % 4 < 2) {
                completePayment(orderResponse.getOrderId(), randomNum % 2 == 0);
            }
            if (randomNum % 8 < 4) {
                completeOrder(orderResponse.getOrderId());
            }
            if (randomNum % 16 < 8) {
                cancelOrder(orderResponse.getOrderId());
            }
        }
    }

    private static String fetchProduct(int page, int size) throws IOException, InterruptedException {
        String url = String.format("%s?page=%d&size=%d&sort=productId,asc", PRODUCTS_URL, page, size);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static OrderResponse createOrder(String productId, int quantity) {
        OrderRequest orderRequest = OrderRequest.of((long) randomCustomerId(),
                List.of(OrderItemRequest.of(productId, quantity)),
                PAYMENT_METHODS[RANDOM.nextInt(PAYMENT_METHODS.length)]);

        try {
            String requestBody = OBJECT_MAPPER.writeValueAsString(orderRequest);
            HttpResponse<String> response = sendPostRequest(ORDERS_URL, requestBody);
            if (response.statusCode() != 200) {
                OrderResponse orderResponse = OBJECT_MAPPER.readValue(response.body(), OrderResponse.class);
                System.out.println("주문 성공");
                return orderResponse;
            } else {
                System.out.println("주문 생성 중 API 응답 실패");
                return null;
            }
        } catch (Exception e) {
            System.out.println("주문 생성 중 예외 발생");
            return null;
        }
    }

    private static HttpResponse<String> sendPostRequest(String url, String requestBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static int randomCustomerId() {
        return RANDOM.nextInt(1000) + 1;
    }

    private static void completePayment(Long orderId, boolean success) {
        PaymentRequest paymentRequest = PaymentRequest.of(success);

        try {
            String requestBody = OBJECT_MAPPER.writeValueAsString(paymentRequest);
            HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/payment",
                    requestBody);
            if (response.statusCode() != 200) {
                if (success) {
                    System.out.println("결제 처리 완료");
                } else {
                    System.out.println("결제 처리 실패");
                }
            } else {
                System.out.println("결제 처리 중 API 응답 실패");
            }
        } catch (Exception e) {
            System.out.println("결제 처리 중 예외 발생");
        }
    }

    private static void completeOrder(Long orderId) {
        try {
            HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/complete", "");
            if (response.statusCode() != 200) {
                System.out.println("주문 완료 성공");
            } else {
                System.out.println("주문 완료 중 API 응답 실패");
            }
        } catch (Exception e) {
            System.out.println("주문 완료 중 예외 발생");
        }
    }

    private static void cancelOrder(Long orderId) {
        try {
            HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/cancel", "");
            if (response.statusCode() != 200) {
                System.out.println("주문 취소 처리 완료");
            } else {
                System.out.println("주문 취소 중 API 응답 실패");
            }
        } catch (Exception e) {
            System.out.println("주문 취소 중 예외 발생");
        }
    }
}
