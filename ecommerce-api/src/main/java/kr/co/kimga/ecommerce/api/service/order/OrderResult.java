package kr.co.kimga.ecommerce.api.service.order;

import kr.co.kimga.ecommerce.api.domain.order.Order;
import kr.co.kimga.ecommerce.api.domain.order.OrderStatus;
import kr.co.kimga.ecommerce.api.domain.payment.PaymentMethod;
import kr.co.kimga.ecommerce.api.domain.payment.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResult {
    private Long orderId;
    private Timestamp orderDate;
    private Long customerId;
    private OrderStatus orderStatus;

    private List<OrderItemResult> orderItems;
    private Long productCount;
    private Long totalItemQuantity;

    private Long paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Timestamp paymentDate;
    private Integer totalAmount;
    private boolean paymentSuccess;

    public static OrderResult from(Order order) {
        return new OrderResult(
                order.getOrderId(),
                order.getOrderDate(),
                order.getCustomerId(),
                order.getOrderStatus(),
                order.getOrderItems().stream()
                        .map(OrderItemResult::from)
                        .toList(),
                order.countProducts(),
                order.calculateTotalItemQuantity(),
                order.getPaymentId(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getPaymentDate(),
                order.calculateTotalAmount(),
                order.isPaymentSuccess()
        );
    }
}
