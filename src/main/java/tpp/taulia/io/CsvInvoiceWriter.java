package tpp.taulia.io;

import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tpp.taulia.model.Invoice;
import tpp.taulia.util.CsvUtil;
import tpp.taulia.util.FileUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CsvInvoiceWriter implements InvoiceWriter {

  @Getter
  private final String outputDirectory;

  private final Map<String, OutputStream> outputStreamMap;

  private final ObjectWriter objectWriter;

  public CsvInvoiceWriter(String outputDirectory) {
    this.outputDirectory = outputDirectory;

    this.outputStreamMap = new HashMap<>();
    this.objectWriter = CsvUtil.getCsvWriterFor(Invoice.class);
  }

  @Override
  public void writeInvoice(Invoice invoice) throws IOException {
    var stream = pickOutputStream(invoice);
    writeInvoiceToStream(invoice, stream);
  }

  private void writeInvoiceToStream(Invoice invoice, OutputStream stream) throws IOException {
    objectWriter.writeValue(stream, invoice);
  }

  @Override
  public void close() {
    for (var entrySet : outputStreamMap.entrySet()) {
      try {
        entrySet.getValue().flush();
        entrySet.getValue().close();
      } catch (Exception e) {
        log.error("Failed to close output stream for file [{}]", entrySet.getKey(), e);
      }
    }
  }

  private OutputStream pickOutputStream(Invoice invoice) throws IOException {
    var stream = outputStreamMap.get(invoice.getBuyer());

    if (stream == null) {
      stream = createOutputStream(invoice);
      outputStreamMap.put(invoice.getBuyer(), stream);
    }

    return stream;
  }

  private OutputStream createOutputStream(Invoice invoice) throws IOException {
    return FileUtil.createFileOutputStream(
        outputDirectory, invoice.getBuyer(), CsvUtil.FILE_EXTENSION);
  }
}
