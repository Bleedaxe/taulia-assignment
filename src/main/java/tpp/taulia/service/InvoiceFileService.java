package tpp.taulia.service;

public interface InvoiceFileService {

  void processFile(String path, String outputFileType, String outputDirectory);
}
