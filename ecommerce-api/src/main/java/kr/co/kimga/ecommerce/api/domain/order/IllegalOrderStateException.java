package kr.co.kimga.ecommerce.api.domain.order;

public class IllegalOrderStateException extends RuntimeException {

    public IllegalOrderStateException(String message) {
        super(message);
    }
}
