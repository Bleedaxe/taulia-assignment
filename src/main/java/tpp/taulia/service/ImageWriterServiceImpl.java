package tpp.taulia.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpp.taulia.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class ImageWriterServiceImpl implements ImageWriterService {

  @Override
  public void writeImageToFileSystem(String path, String encodedContent) throws IOException {
    if (isNullOrEmpty(path) || isNullOrEmpty(encodedContent)) {
      log.info("Image path or content is not present..");
      return;
    }

    var file = new File(path);
    if (file.exists()) {
      log.info("File with path [{}] already exists", path);
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
