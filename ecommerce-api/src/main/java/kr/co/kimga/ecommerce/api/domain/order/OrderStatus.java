package kr.co.kimga.ecommerce.api.domain.order;

public enum OrderStatus {

    PENDING_PAYMENT("결제 대기중"),
    PROCESSING("처리중"),
    COMPLETED("완료된"),
    CANCELED("취소됨");
    
    final String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }
}
