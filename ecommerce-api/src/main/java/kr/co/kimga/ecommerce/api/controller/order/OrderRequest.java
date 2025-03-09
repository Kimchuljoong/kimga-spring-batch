package kr.co.kimga.ecommerce.api.controller.order;

import kr.co.kimga.ecommerce.api.domain.payment.PaymentMethod;
import kr.co.kimga.ecommerce.api.service.order.OrderItemCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRequest {

    private Long customerId;
    private List<OrderItemRequest> orderItems;
    private PaymentMethod paymentMethod;

    public List<OrderItemCommand> getOrderItemCommands() {
        return orderItems.stream()
                .map(OrderItemCommand::of)
                .toList();
    }
}
