package kr.co.kimga.ecommerce.api.controller.order;

import kr.co.kimga.ecommerce.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return OrderResponse.from(orderService.order(orderRequest.getCustomerId(),
                orderRequest.getOrderItemCommands(), orderRequest.getPaymentMethod())
        );
    }

    @PostMapping("/{orderId}/payment")
    public OrderResponse completePayment(
            @PathVariable("orderId") Long orderId,
            @RequestBody PaymentRequest paymentRequest) {
        return OrderResponse.from(orderService.completePayment(orderId, paymentRequest.isSuccess()));
    }
}
