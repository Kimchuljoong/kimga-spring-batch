package kr.co.kimga.ecommerce.api.service.order;

import kr.co.kimga.ecommerce.api.domain.order.Order;
import kr.co.kimga.ecommerce.api.domain.order.OrderRepository;
import kr.co.kimga.ecommerce.api.domain.payment.PaymentMethod;
import kr.co.kimga.ecommerce.api.service.product.ProductResult;
import kr.co.kimga.ecommerce.api.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderResult order(Long customerId, List<OrderItemCommand> orderItems, PaymentMethod paymentMethod) {
        Order order = Order.createOrder(customerId);
        for (OrderItemCommand orderItem : orderItems) {
            ProductResult product = productService.findProduct(orderItem.getProductId());
            order.addOrderItem(product.getProductId(), orderItem.getQuantity(), product.getSalesPrice());
        }
        order.initPayment(paymentMethod);
        return save(order);
    }

    private OrderResult save(Order order) {
        return OrderResult.from(orderRepository.save(order));
    }
}
