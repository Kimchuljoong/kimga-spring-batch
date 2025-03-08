package kr.co.kimga.ecommerce.api.domain.order;


import jakarta.persistence.*;
import kr.co.kimga.ecommerce.api.domain.payment.Payment;
import kr.co.kimga.ecommerce.api.domain.payment.PaymentMethod;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Timestamp orderDate;
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    public static Order createOrder(Long customerId) {
        return new Order(null, new Timestamp(System.currentTimeMillis()), customerId,
                OrderStatus.PENDING_PAYMENT, new ArrayList<>(), null);
    }

    public OrderItem addOrderItem(String productId, Integer quantity, Integer unitPrice) {
        OrderItem orderItem = OrderItem.createOrderItem(productId, quantity, unitPrice, this);
        orderItems.add(orderItem);
        return orderItem;
    }

    public void initPayment(PaymentMethod paymentMethod) {
        payment = Payment.createPayment(paymentMethod, calculateTotalAmount(), this);
    }

    private Integer calculateTotalAmount() {
        return orderItems.stream()
                .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }
    

}
