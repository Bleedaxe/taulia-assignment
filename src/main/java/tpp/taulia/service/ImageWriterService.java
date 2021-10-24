package tpp.taulia.service;

import java.io.IOException;

public interface ImageWriterService {

    void writeImageToFileSystem(String path, String encodedContent) throws IOException;
}
