package tpp.taulia.service;

import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpp.taulia.exception.ProcessInvoiceException;
import tpp.taulia.model.Invoice;
import tpp.taulia.model.InvoiceOutputType;
import tpp.taulia.util.CsvUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Iterator;

@Slf4j
@Service
public class InvoiceFileServiceImpl implements InvoiceFileService {

  private final ObjectReader objectReader;

  private final InvoiceFileWriterServiceFactory invoiceFileWriterServiceFactory;

  @Autowired
  public InvoiceFileServiceImpl(InvoiceFileWriterServiceFactory invoiceFileWriterServiceFactory) {
    this.invoiceFileWriterServiceFactory = invoiceFileWriterServiceFactory;

    objectReader = CsvUtil.getCsvReaderFor(Invoice.class);
  }

  @Override
  public void processFile(String path, InvoiceOutputType invoiceOutputType, String outputDirectory) {
    var fileWriterService = invoiceFileWriterServiceFactory.createByType(invoiceOutputType);

    try (var inputStream = new FileInputStream(path);
        var bufferedInputStream = new BufferedInputStream(inputStream);
        var invoiceWriter = fileWriterService.getInvoiceWriter(outputDirectory)) {

      Iterator<Invoice> objectIterator = objectReader.readValues(bufferedInputStream);

      while (objectIterator.hasNext()) {
        var invoice = objectIterator.next();
        log.info("Read invoice with buyer: [{}]", invoice.getBuyer());

        fileWriterService.writeInvoice(invoice, invoiceWriter);
      }
    } catch (Exception e) {
      log.error("Failed to process the file", e);
      throw new ProcessInvoiceException(e);
    }
  }
}
