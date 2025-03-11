package kr.co.kimga.ecommerce.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kimga.ecommerce.api.controller.order.OrderResponse;
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

        return null;
    }

    private static void completePayment(Long orderId, boolean success) {

    }

    private static void completeOrder(Long orderId) {
    }

    private static void cancelOrder(Long orderId) {

    }
}
