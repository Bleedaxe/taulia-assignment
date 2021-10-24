package tpp.taulia.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tpp.taulia.exception.InvalidInvoiceFileWriterServiceTypeException;
import tpp.taulia.model.InvoiceOutputType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InvoiceFileWriterServiceFactory {

  private final Map<InvoiceOutputType, InvoiceFileWriterService> invoiceFileWriterServiceMap;

  @Autowired
  public InvoiceFileWriterServiceFactory(List<InvoiceFileWriterService> invoiceFileWriterServices) {
    this.invoiceFileWriterServiceMap =
        invoiceFileWriterServices.stream()
            .collect(
                Collectors.toMap(InvoiceFileWriterService::getSupportedType, Function.identity()));
  }

  public InvoiceFileWriterService createByType(@NonNull InvoiceOutputType type) {
    var fileWriterService = invoiceFileWriterServiceMap.get(type);

    if (fileWriterService == null) {
      throw new InvalidInvoiceFileWriterServiceTypeException(type);
    }

    return fileWriterService;
  }
}
