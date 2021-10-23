package tpp.taulia.service;

import lombok.extern.slf4j.Slf4j;
import tpp.taulia.model.Invoice;
import tpp.taulia.model.XmlOutputStream;
import tpp.taulia.util.DateUtil;
import tpp.taulia.util.FileUtil;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class InvoiceFileWriterServiceXmlImpl implements InvoiceFileWriterService {

  private static final String XML_FILE_EXTENSION = ".xml";
  private static final String NEW_LINE = System.lineSeparator();

  private static final XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newInstance();

  private final String outputDirectory;
  private final Map<String, XmlOutputStream> xmlStreamWriterByName;

  public InvoiceFileWriterServiceXmlImpl(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    this.xmlStreamWriterByName = new HashMap<>();
  }

  @Override
  public void writeInvoice(Invoice invoice) throws IOException, XMLStreamException {
    var currentStream = xmlStreamWriterByName.get(invoice.getBuyer());

    if (currentStream == null) {
      currentStream = createXmlWriter(invoice);
      xmlStreamWriterByName.put(invoice.getBuyer(), currentStream);
    }

    writeInvoiceToXmlStream(invoice, currentStream.getXmlStreamWriter());
  }

  @Override
  public void close() {
    for (var entrySet : xmlStreamWriterByName.entrySet()) {
      try {
        closeXmlStreamWriter(entrySet.getValue());
      } catch (Exception e) {
        log.error("Failed to close output stream for file [{}]", entrySet.getKey(), e);
      }
    }

    xmlStreamWriterByName.clear();
  }

  private void writeInvoiceToXmlStream(Invoice invoice, XMLStreamWriter writer)
      throws XMLStreamException {

    writer.writeCharacters("\t");
    writer.writeStartElement("Invoice");
    writer.writeCharacters(NEW_LINE);

    writeElementToStream("Buyer", invoice.getBuyer(), writer);
    writeElementToStream("ImageName", invoice.getImageName(), writer);
    writeElementToStream(
        "InvoiceDueDate", DateUtil.dateToString(invoice.getInvoiceDueDate()), writer);
    writeElementToStream("InvoiceNumber", invoice.getInvoiceNumber(), writer);
    writeElementToStream("InvoiceAmount", invoice.getInvoiceAmount().toString(), writer);
    writeElementToStream("InvoiceCurrency", invoice.getInvoiceCurrency(), writer);
    writeElementToStream("InvoiceStatus", invoice.getInvoiceStatus(), writer);
    writeElementToStream("Supplier", invoice.getSupplier(), writer);

    writer.writeCharacters("\t");
    writer.writeEndElement();
    writer.writeCharacters(NEW_LINE);
  }

  private XmlOutputStream createXmlWriter(Invoice invoice) throws XMLStreamException, IOException {

    var outputStream =
        FileUtil.createFileOutputStream(outputDirectory, invoice.getBuyer(), XML_FILE_EXTENSION);
    var writer = XML_OUTPUT_FACTORY.createXMLStreamWriter(outputStream, UTF_8.name());

    writer.writeStartDocument(UTF_8.name(), "1.0");
    writer.writeCharacters(NEW_LINE);
    writer.writeStartElement("InvoiceList");
    writer.writeCharacters(NEW_LINE);

    return new XmlOutputStream(outputStream, writer);
  }

  private void writeElementToStream(String elementName, String value, XMLStreamWriter writer)
      throws XMLStreamException {

    writer.writeCharacters("\t\t");

    if (value == null || value.isBlank()) {
      writer.writeEmptyElement(elementName);
    } else {
      writer.writeStartElement(elementName);
      writer.writeCharacters(value);
      writer.writeEndElement();
    }

    writer.writeCharacters(NEW_LINE);
  }

  private void closeXmlStreamWriter(XmlOutputStream xmlOutputStream) throws Exception {
    var writer = xmlOutputStream.getXmlStreamWriter();

    writer.writeEndElement();
    writer.writeCharacters(NEW_LINE);
    writer.writeEndDocument();

    log.info("Finished writing to xml file. Closing..");

    xmlOutputStream.close();
  }
}
