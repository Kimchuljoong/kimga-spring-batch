package kr.co.kimga.ecommerce.api.domain.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTest {

    Payment payment;

    private static Payment getPayment() {
        return Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, null);
    }

    @BeforeEach
    void setUp() {
        payment = getPayment();
    }

    @Test
    void testPaymentPending() {
        Payment payment = getPayment();

        Assertions.assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void testPaymentComplete() {
        Payment payment = getPayment();

        payment.complete();

        Assertions.assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void testPaymentCompleteException() {
        payment.complete();
        Assertions.assertThatThrownBy(payment::complete)
                .isInstanceOf(IllegalPaymentStatusException.class);
    }

    @Test
    void testPaymentFailed() {
        payment.fail();

        Assertions.assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);

    }

    @Test
    void testPaymentFailException() {
        payment.fail();
        Assertions.assertThatThrownBy(payment::fail)
                .isInstanceOf(IllegalPaymentStatusException.class);
    }

    @Test
    void testPaymentCancelAfterComplete() {
        payment.complete();
        payment.cancel();
        Assertions.assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    void testPaymentCancelAfterPending() {
        payment.cancel();
        Assertions.assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    void testPaymentCancelAfterFail() {
        payment.fail();
        payment.cancel();
        Assertions.assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    void testPaymentCancelAfterRefund() {
        payment.complete();
        payment.cancel();

        Assertions.assertThatThrownBy(payment::cancel)
                .isInstanceOf(IllegalPaymentStatusException.class);
    }

    @Test
    void testPaymentCancelAfterCancel() {
        payment.fail();
        payment.cancel();

        Assertions.assertThatThrownBy(payment::cancel)
                .isInstanceOf(IllegalPaymentStatusException.class);
    }


}