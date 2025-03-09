package kr.co.kimga.ecommerce.api.service.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("주문을 찾을 수 없습니다: " + orderId);
    }
}
