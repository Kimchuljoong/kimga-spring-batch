package kr.co.kimga.batch.util;

import kr.co.kimga.batch.domain.product.ProductStatus;
import kr.co.kimga.batch.dto.ProductUploadCsvRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

public class ProductGenerator {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        String csvFilePath = "data/random_product.csv";

        int recordCount = 10_000_000;

        try (
                FileWriter fw = new FileWriter(csvFilePath);
                CSVPrinter cp = new CSVPrinter(fw, CSVFormat.DEFAULT.builder()
                        .setHeader(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class).toArray(String[]::new))
                        .build())
        ) {

            for (int i = 0; i < recordCount; i++) {
                cp.printRecord(generateRecord());
                if (i % 100_000 == 0) {
                    System.out.println("Generated " + i + " records");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    private static Object[] generateRecord() {
        ProductUploadCsvRow row = randomProductRow();
        return new Object[]{
                row.getSellerId(),
                row.getCategory(),
                row.getProductName(),
                row.getSalesStartDate(),
                row.getSalesEndDate(),
                row.getProductStatus(),
                row.getBrand(),
                row.getManufacturer(),
                row.getSalesPrice(),
                row.getStockQuantity()
        };
    }

    private static ProductUploadCsvRow randomProductRow() {

        String[] CATEGORIES = {"가전", "가구", "패션", "식품", "화장품", "서적", "스포츠", "완구", "음악", "디지털"};
        String[] PRODUCT_NAMES = {"TV", "소파", "셔츠", "햇반", "스킨케어 세트", "소설", "축구공", "레고", "기타", "스마트폰"};
        String[] BRANDS = {"삼성", "LG", "나이키", "아모레퍼시픽", "현대", "BMW", "롯데", "스타벅스", "도미노", "맥도날드"};
        String[] MANUFACTURERS = {"삼성전자", "LG전자", "나이키코리아", "아모레퍼시픽", "현대자동차", "BMW코리아", "롯데제과", "스타벅스코리아", "도미노피자", "맥도날드코리아"};
        String[] STATUSES = Arrays.stream(ProductStatus.values()).map(Enum::name).toArray(String[]::new);

        return ProductUploadCsvRow.of(
                randomSellerId(),
                randomChoice(CATEGORIES),
                randomChoice(PRODUCT_NAMES),
                randomDate(2020, 2023),
                randomDate(2024, 2026),
                randomChoice(STATUSES),
                randomChoice(BRANDS),
                randomChoice(MANUFACTURERS),
                randomSalesPrice(),
                randomStockQuantity()
        );
    }

    private static int randomStockQuantity() {
        return RANDOM.nextInt(1, 1001);
    }

    private static int randomSalesPrice() {
        return RANDOM.nextInt(100_000, 500_001);
    }

    private static String randomDate(int startYear, int endYear) {
        int year = RANDOM.nextInt(startYear, endYear);
        int month = RANDOM.nextInt(1, 13);
        int day = RANDOM.nextInt(1, 29);
        return LocalDate.of(year, month, day).toString();
    }

    private static String randomChoice(String[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    private static Long randomSellerId() {
        return RANDOM.nextLong(1, 101);
    }
}
