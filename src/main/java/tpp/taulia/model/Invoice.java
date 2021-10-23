package tpp.taulia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Invoice {

  @JsonProperty("buyer")
  private String buyer;

  @JsonProperty("image_name")
  private String imageName;

  @JsonProperty("invoice_image")
  private String invoiceImage;

  @JsonProperty("invoice_due_date")
  private LocalDate invoiceDueDate;

  @JsonProperty("invoice_number")
  private String invoiceNumber;

  @JsonProperty("invoice_amount")
  private BigDecimal invoiceAmount;

  @JsonProperty("invoice_currency")
  private String invoiceCurrency;

  @JsonProperty("invoice_status")
  private String invoiceStatus;

  @JsonProperty("supplier")
  private String supplier;
}
