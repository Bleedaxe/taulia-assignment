package tpp.taulia.model;

import lombok.Value;

import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

@Value
public class XmlOutputStream implements AutoCloseable {

  OutputStream fileOutputStream;

  XMLStreamWriter xmlStreamWriter;

  @Override
  public void close() throws Exception {
    fileOutputStream.flush();
    xmlStreamWriter.flush();

    fileOutputStream.close();
    xmlStreamWriter.close();
  }
}
