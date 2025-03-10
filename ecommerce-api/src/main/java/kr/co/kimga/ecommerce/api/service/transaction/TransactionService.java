package kr.co.kimga.ecommerce.api.service.transaction;

import kr.co.kimga.ecommerce.api.domain.transaction.TransactionStatus;
import kr.co.kimga.ecommerce.api.domain.transaction.TransactionType;
import kr.co.kimga.ecommerce.api.service.order.OrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TransactionService {

    protected static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private static final String NA = "N/A";

    public void logTransaction(
            TransactionType transactionType, TransactionStatus transactionStatus,
            String message, OrderResult order
    ) {
        try {
            putMdc(transactionType, transactionStatus, order);
            log(transactionStatus, message);
        } finally {
            MDC.clear();
        }
    }

    private void putMdc(
            TransactionType transactionType,
            TransactionStatus transactionStatus,
            OrderResult order) {

        Optional.ofNullable(order)
                .ifPresentOrElse(this::putOrder, this::putNAOrder);
        putTransaction(transactionType, transactionStatus);
    }

    private void putTransaction(
            TransactionType transactionType,
            TransactionStatus transactionStatus
    ) {
        MDC.put("TransactionType", transactionType.name());
        MDC.put("TransactionStatus", transactionStatus.name());
    }


    private void putOrder(OrderResult order) {
        MDC.put("orderId", order.getOrderId().toString());
        MDC.put("customerId", order.getCustomerId().toString());
        MDC.put("totalAmount", order.getTotalAmount().toString());
        MDC.put("paymentMethod", order.getPaymentMethod().toString());
        MDC.put("productCount", order.getProductCount().toString());
        MDC.put("totalItemQuantity", order.getTotalItemQuantity().toString());
    }

    private void putNAOrder() {
        MDC.put("orderId", NA);
        MDC.put("customerId", NA);
        MDC.put("totalAmount", NA);
        MDC.put("paymentMethod", NA);
        MDC.put("productCount", NA);
        MDC.put("totalItemQuantity", NA);
    }

    private void log(TransactionStatus transactionStatus, String message) {
        if (transactionStatus == TransactionStatus.SUCCESS) {
            logger.info(message);
        } else {
            logger.error(message);
        }
    }
}
