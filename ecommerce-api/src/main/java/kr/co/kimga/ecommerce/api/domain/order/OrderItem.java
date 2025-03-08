package kr.co.kimga.ecommerce.api.domain.order;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private Integer quantity;
    private Integer unitPrice;
    private String productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    public static OrderItem createOrderItem(String productId, Integer quantity, Integer unitPrice, Order order) {
        return new OrderItem(null, quantity, unitPrice, productId, order);
    }
}
