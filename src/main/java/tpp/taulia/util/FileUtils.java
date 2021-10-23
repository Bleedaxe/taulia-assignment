package tpp.taulia.util;

import java.io.*;

public class FileUtils {

  public static OutputStream createFileOutputStream(
      String parent, String fileName, String extension) throws IOException {

    var file = new File(parent, fileName + extension);
    return new BufferedOutputStream(new FileOutputStream(file));
  }
}
