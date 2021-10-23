package tpp.taulia.util;

import tpp.taulia.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUtil {

    public static final String INVOICE_CSV_HEADER = "buyer,image_name,invoice_image,invoice_due_date,invoice_number,invoice_amount,invoice_currency,invoice_status,supplier";

    public static Invoice getInvoice(String buyer) {
        return getInvoice(buyer, "image-name");
    }

    public static Invoice getInvoice(String buyer, String imageName) {
        var invoice = new Invoice();

        invoice.setBuyer(buyer);
        invoice.setImageName(imageName);
        invoice.setInvoiceImage("invoice-image");
        invoice.setInvoiceDueDate(LocalDate.of(2021, 10, 22));
        invoice.setInvoiceNumber("invoice-number");
        invoice.setInvoiceAmount(BigDecimal.valueOf(42));
        invoice.setInvoiceCurrency("BGN");
        invoice.setInvoiceStatus("status");
        invoice.setSupplier("taulia");

        return invoice;
    }
}
