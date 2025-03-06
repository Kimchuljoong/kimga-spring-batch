package kr.co.kimga.ecommerce.api.domain.payment;

public class IllegalPaymentStatusException extends RuntimeException {

    public IllegalPaymentStatusException(String message) {
        super(message);
    }
}
