package tpp.taulia.service;

import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import tpp.taulia.exception.ProcessInvoiceException;
import tpp.taulia.model.Invoice;
import tpp.taulia.util.CsvUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Iterator;

@Slf4j
public class InvoiceFileProcessor {

  private final ObjectReader objectReader = CsvUtil.getCsvReaderFor(Invoice.class);

  public void processFile(String path, String outputFileType, String outputDirectory) {
    try (var inputStream = new FileInputStream(path);
        var bufferedInputStream = new BufferedInputStream(inputStream);
        var fileWriterService =
            InvoiceFileWriterServiceFactory.createByType(outputFileType, outputDirectory)) {

      Iterator<Invoice> objectIterator = objectReader.readValues(bufferedInputStream);

      while (objectIterator.hasNext()) {
        var invoice = objectIterator.next();
        log.info("Read invoice with buyer: [{}]", invoice.getBuyer());

        fileWriterService.writeInvoice(invoice);
      }
    } catch (Exception e) {
      log.error("Failed to process the file", e);
      throw new ProcessInvoiceException(e);
    }
  }
}
