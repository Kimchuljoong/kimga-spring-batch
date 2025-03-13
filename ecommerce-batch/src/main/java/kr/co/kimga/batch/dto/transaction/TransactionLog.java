package kr.co.kimga.batch.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionLog {
    private String timestamp;
    private String level;
    private String thread;
    private String logger;
    private String message;
    private TransactionMdc mdc;

    public String getTransactionType() {
        return mdc.getTransactionType();
    }

    public String getTransactionStatus() {
        return mdc.getTransactionStatus();
    }

    public String getTotalAmount() {
        return mdc.getTotalAmount();
    }

    public String getPaymentMethod() {
        return mdc.getPaymentMethod();
    }

    public String getProductCount() {
        return mdc.getProductCount();
    }

    public String getTotalItemQuantity() {
        return mdc.getTotalItemQuantity();
    }

    public String getOrderId() {
        return mdc.getOrderId();
    }

    public String getCustomerId() {
        return mdc.getCustomerId();
    }
}
