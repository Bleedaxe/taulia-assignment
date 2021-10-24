package tpp.taulia.io;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tpp.taulia.model.Invoice;
import tpp.taulia.util.DateUtil;
import tpp.taulia.util.FileUtil;
import tpp.taulia.util.XmlUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class XmlInvoiceWriter implements InvoiceWriter {

  @Getter
  private final String outputDirectory;

  private final Map<String, XmlOutputStream> xmlOutputStreamMap;

  public XmlInvoiceWriter(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    this.xmlOutputStreamMap = new HashMap<>();
  }

  @Override
  public void writeInvoice(Invoice invoice) throws XMLStreamException, IOException {
    var xmlOutputStream = pickXmlOutputStream(invoice);
    writeInvoiceToXmlStream(invoice, xmlOutputStream.getXmlStreamWriter());
  }

  @Override
  public void close() {
    for (var entrySet : xmlOutputStreamMap.entrySet()) {
      try {
        closeXmlStreamWriter(entrySet.getValue());
      } catch (Exception e) {
        log.error("Failed to close output stream for file [{}]", entrySet.getKey(), e);
      }
    }

    xmlOutputStreamMap.clear();
  }

  private XmlOutputStream pickXmlOutputStream(Invoice invoice)
      throws XMLStreamException, IOException {
    var stream = xmlOutputStreamMap.get(invoice.getBuyer());

    if (stream == null) {
      stream = createXmlWriter(invoice);
      xmlOutputStreamMap.put(invoice.getBuyer(), stream);
    }

    return stream;
  }

  private void writeInvoiceToXmlStream(Invoice invoice, XMLStreamWriter writer)
      throws XMLStreamException {

    writer.writeStartElement("Invoice");

    writeElementToStream("Buyer", invoice.getBuyer(), writer);
    writeElementToStream("ImageName", invoice.getImageName(), writer);

    writeElementToStream(
        "InvoiceDueDate", DateUtil.dateToString(invoice.getInvoiceDueDate()), writer);
    writeElementToStream("InvoiceNumber", invoice.getInvoiceNumber(), writer);
    writeElementToStream("InvoiceAmount", invoice.getInvoiceAmount().toString(), writer);
    writeElementToStream("InvoiceCurrency", invoice.getInvoiceCurrency(), writer);
    writeElementToStream("InvoiceStatus", invoice.getInvoiceStatus(), writer);
    writeElementToStream("Supplier", invoice.getSupplier(), writer);

    writer.writeEndElement();
  }

  private void writeElementToStream(String elementName, String value, XMLStreamWriter writer)
      throws XMLStreamException {

    if (value == null || value.isBlank()) {
      writer.writeEmptyElement(elementName);
    } else {
      writer.writeStartElement(elementName);
      writer.writeCharacters(value);
      writer.writeEndElement();
    }

  }

  private XmlOutputStream createXmlWriter(Invoice invoice) throws XMLStreamException, IOException {

    var outputStream =
        FileUtil.createFileOutputStream(
            outputDirectory, invoice.getBuyer(), XmlUtil.FILE_EXTENSION);
    var writer = XmlUtil.createXmlStreamWriter(outputStream);

    writer.writeStartDocument(XmlUtil.FILE_ENCODING, XmlUtil.FILE_VERSION);
    writer.writeStartElement("InvoiceList");

    return new XmlOutputStream(outputStream, writer);
  }

  private void closeXmlStreamWriter(XmlOutputStream xmlOutputStream) throws Exception {
    addClosingTagsToXmlDocument(xmlOutputStream.getXmlStreamWriter());
    xmlOutputStream.close();
  }

  private void addClosingTagsToXmlDocument(XMLStreamWriter writer) throws XMLStreamException {
    writer.writeEndElement();
    writer.writeEndDocument();

    log.info("Finished writing to xml file. Closing..");
  }
}
