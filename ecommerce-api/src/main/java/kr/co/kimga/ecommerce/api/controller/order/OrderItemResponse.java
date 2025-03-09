package kr.co.kimga.ecommerce.api.controller.order;

import kr.co.kimga.ecommerce.api.service.order.OrderItemResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemResponse {

    private Long orderItemId;
    private Integer quantity;
    private Integer unitPrice;
    private String productId;

    public static OrderItemResponse from(OrderItemResult result) {
        return new OrderItemResponse(result.getOrderItemId(), result.getQuantity(),
                result.getUnitPrice(), result.getProductId());
    }
}
