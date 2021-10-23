package tpp.taulia.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import tpp.taulia.model.Invoice;
import tpp.taulia.util.CsvUtil;
import tpp.taulia.util.FileUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InvoiceFileWriterServiceCsvImpl implements InvoiceFileWriterService {

  private static final String CSV_FILE_EXTENSION = ".csv";

  private final String outputDirectory;

  private final Map<String, OutputStream> outputStreamByName;

  private final ObjectWriter objectWriter;

  public InvoiceFileWriterServiceCsvImpl(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    this.outputStreamByName = new HashMap<>();
    this.objectWriter = CsvUtil.getCsvWriterFor(Invoice.class);
  }

  @Override
  public void writeInvoice(Invoice invoice) throws IOException {
    var stream = outputStreamByName.get(invoice.getBuyer());

    if (stream == null) {
      stream =
          FileUtils.createFileOutputStream(outputDirectory, invoice.getBuyer(), CSV_FILE_EXTENSION);
      outputStreamByName.put(invoice.getBuyer(), stream);
    }

    objectWriter.writeValue(stream, invoice);
  }

  @Override
  public void close() {
    for (var entrySet : outputStreamByName.entrySet()) {
      try {
        entrySet.getValue().flush();
        entrySet.getValue().close();
      } catch (Exception e) {
        log.error("Failed to close output stream for file [{}]", entrySet.getKey(), e);
      }
    }
  }
}
