package tpp.taulia.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpp.taulia.io.InvoiceWriter;
import tpp.taulia.io.XmlInvoiceWriter;
import tpp.taulia.model.Invoice;
import tpp.taulia.model.InvoiceOutputType;

import java.nio.file.Path;

@Slf4j
@Service
public class InvoiceFileWriterServiceXmlImpl implements InvoiceFileWriterService {

  private final ImageWriterService imageWriterService;

  @Autowired
  public InvoiceFileWriterServiceXmlImpl(ImageWriterService imageWriterService) {
    this.imageWriterService = imageWriterService;
  }

  @Override
  public InvoiceOutputType getSupportedType() {
    return InvoiceOutputType.XML;
  }

  @Override
  public InvoiceWriter getInvoiceWriter(String outputDirectory) {
    return new XmlInvoiceWriter(outputDirectory);
  }

  @Override
  public void writeInvoice(Invoice invoice, InvoiceWriter invoiceWriter) throws Exception {
    invoiceWriter.writeInvoice(invoice);
    imageWriterService.writeImageToFileSystem(
        getImagePath(invoice, invoiceWriter), invoice.getInvoiceImage());
  }

  private String getImagePath(Invoice invoice, InvoiceWriter invoiceWriter) {
    return Path.of(invoiceWriter.getOutputDirectory(), invoice.getImageName()).toString();
  }
}
