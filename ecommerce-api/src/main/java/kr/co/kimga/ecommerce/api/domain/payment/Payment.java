package kr.co.kimga.ecommerce.api.domain.payment;


import jakarta.persistence.*;
import kr.co.kimga.ecommerce.api.domain.order.Order;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Timestamp paymentDate;
    private Integer amount;

    @OneToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    public static Payment createPayment(PaymentMethod paymentMethod, Integer amount, Order order) {
        return new Payment(null, paymentMethod, PaymentStatus.PENDING,
                new Timestamp(System.currentTimeMillis()), amount, order);
    }

    public void complete() {
        if (paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalPaymentStatusException("결제 대기중에만 완료할 수 있습니다.");
        }
        paymentStatus = PaymentStatus.COMPLETED;
    }

    public void fail() {
        if (paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalPaymentStatusException("결제 대기중에만 실패할 수 있습니다.");
        }
        paymentStatus = PaymentStatus.FAILED;
    }

    public void cancel() {
        switch (paymentStatus) {
            case COMPLETED -> paymentStatus = PaymentStatus.REFUNDED;
            case PENDING, FAILED -> paymentStatus = PaymentStatus.CANCELLED;
            case CANCELLED -> throw new IllegalPaymentStatusException("이미 취소가 완료되었습니다.");
            case REFUNDED -> throw new IllegalPaymentStatusException("이미 환불이 완료되었습니다.");
        }
    }

    public boolean isSuccess() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }
}
