package kr.co.kimga.ecommerce.api.service.order;

import kr.co.kimga.ecommerce.api.domain.order.Order;
import kr.co.kimga.ecommerce.api.domain.order.OrderItem;
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

    @Transactional
    public OrderResult completePayment(Long orderId, boolean success) {
        Order order = findOrderById(orderId);
        order.completePayment(success);
        decreaseStock(success, order);
        return save(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private void decreaseStock(boolean success, Order order) {
        if (success) {
            for (OrderItem orderItem : order.getOrderItems()) {
                productService.decreaseStock(orderItem.getProductId(), orderItem.getQuantity());
            }
        }
    }

    @Transactional
    public OrderResult completeOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.complete();
        return save(order);
    }

    @Transactional
    public OrderResult cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.cancel();
        increaseStock(order);
        return save(order);
    }

    private void increaseStock(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            productService.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
        }
    }
}
