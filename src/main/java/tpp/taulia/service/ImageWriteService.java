package tpp.taulia.service;

import java.io.IOException;

public interface ImageWriteService {

    void writeImageToFileSystem(String name, String encodedContent) throws IOException;
}
