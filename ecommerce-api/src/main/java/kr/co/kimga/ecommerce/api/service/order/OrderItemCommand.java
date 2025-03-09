package kr.co.kimga.ecommerce.api.service.order;

import kr.co.kimga.ecommerce.api.controller.order.OrderItemRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemCommand {
    private Integer quantity;
    private String productId;

    public static OrderItemCommand of(OrderItemRequest orderItemRequest) {
        return new OrderItemCommand(orderItemRequest.getQuantity(), orderItemRequest.getProductId());
    }
}
