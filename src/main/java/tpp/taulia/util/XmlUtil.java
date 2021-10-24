package tpp.taulia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtil {

  public static final String FILE_ENCODING = UTF_8.name();

  public static final String FILE_VERSION = "1.0";

  public static final String FILE_EXTENSION = "xml";

  private static final String NEW_LINE = System.lineSeparator();
  private static final String INDENTATION = "\t";

  private static final XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newInstance();

  public static XMLStreamWriter createXmlStreamWriter(OutputStream outputStream)
      throws XMLStreamException {
    return XML_OUTPUT_FACTORY.createXMLStreamWriter(outputStream, FILE_ENCODING);
  }

  public static void addNewLine(XMLStreamWriter writer) throws XMLStreamException {
    writer.writeCharacters(NEW_LINE);
  }

  public static void addIndentation(XMLStreamWriter writer, int count) throws XMLStreamException {
    writer.writeCharacters(INDENTATION.repeat(count));
  }
}
