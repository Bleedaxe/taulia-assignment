package tpp.taulia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

  public static OutputStream createFileOutputStream(
      String parent, String fileName, String extension) throws IOException {

    return createFileOutputStream(parent, fileName + extension);
  }

  public static OutputStream createFileOutputStream(String parent, String fileName)
      throws IOException {

    var file = new File(parent, fileName);
    return createFileOutputStream(file);
  }

  public static OutputStream createFileOutputStream(File file) throws IOException {
    return new BufferedOutputStream(new FileOutputStream(file));
  }
}
