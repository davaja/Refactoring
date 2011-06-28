package com.develogical.examples;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class InvoiceGenerator {
    public static final int UK = 1;
    public static final int FRANCE = 2;
    
    private Integer country;
    private List<LineItem> items = new ArrayList<LineItem>();

    public String getInvoiceText() {
        StringBuilder text = new StringBuilder();
        BigDecimal total = new BigDecimal(0);

        for (LineItem item : items) {
            if (country == null) {
                total = itemDetailsAndTotal(text, total, item);
            } else if (country == UK) {
                double vatRate = 0.2;
                total = itemPriceAndVatDetails(text, total, item, vatRate);
            } else if (country == FRANCE) {
                double vatRate = 0.196;
                total = itemPriceAndVatDetails(text, total, item, vatRate);
            }
        }
        text.append("Total").append(" ").append(total.setScale(2, RoundingMode.HALF_EVEN).toPlainString());
        return text.toString();
    }

    private BigDecimal itemPriceAndVatDetails(StringBuilder text, BigDecimal total, LineItem item, double vatRate) {
        total = itemDetailsAndTotal(text, total, item);
        BigDecimal vat = item.price.multiply(new BigDecimal(vatRate));
        text.append("VAT").append(" ").append(vat.setScale(2, RoundingMode.HALF_EVEN).toPlainString()).append('\n');
        total = total.add(vat);
        return total;
    }

    private BigDecimal itemDetailsAndTotal(StringBuilder text, BigDecimal total, LineItem item) {
        formatItemDetails(text, item);
        total = total.add(item.price);
        return total;
    }

    private void formatItemDetails(StringBuilder text, LineItem item) {
        text.append(item.name).append(" ").append(item.price).append('\n');
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public void addLineItem(LineItem item) {
        items.add(item);
    }
}
