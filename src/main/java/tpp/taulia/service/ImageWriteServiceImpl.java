package tpp.taulia.service;

import lombok.extern.slf4j.Slf4j;
import tpp.taulia.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class ImageWriteServiceImpl implements ImageWriteService {

  private final String outputDirectory;

  public ImageWriteServiceImpl(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  @Override
  public void writeImageToFileSystem(String name, String encodedContent) throws IOException {
    if (isNullOrEmpty(name) || isNullOrEmpty(encodedContent)) {
      log.info("Image name or content is not present..");
      return;
    }

    var file = new File(outputDirectory, name);
    if (file.exists()) {
      log.info("File with name [{}] already exists", name);
      return;
    }

    var decodedContent = Base64.getDecoder().decode(encodedContent);
    try (var writer = FileUtil.createFileOutputStream(file)) {
      writer.write(decodedContent);
    }
  }

  private boolean isNullOrEmpty(String value) {
    return value == null || value.isBlank();
  }
}
