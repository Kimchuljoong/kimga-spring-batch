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
public class TransactionMdc {
    private String transactionType;
    private String transactionStatus;
    private String totalAmount;
    private String orderId;
    private String customerId;
    private String paymentMethod;
    private String productCount;
    private String totalItemQuantity;

    public String getTotalAmount() {
        if ("N/A".equals(totalAmount)) {
            return "0";
        }
        return totalAmount;
    }

}
