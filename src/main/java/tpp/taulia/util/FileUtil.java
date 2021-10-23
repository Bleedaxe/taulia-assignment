package tpp.taulia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

  public static OutputStream createFileOutputStream(
      String parent, String fileName, String extension) throws IOException {

    var file = new File(parent, fileName + extension);
    return new BufferedOutputStream(new FileOutputStream(file));
  }
}
