package tpp.taulia;

import lombok.extern.slf4j.Slf4j;
import tpp.taulia.service.InvoiceFileService;
import tpp.taulia.service.InvoiceFileServiceImpl;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Application {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");

  public static void main(String[] args) {
    var inputFile = "C:\\dev\\java\\invoices.csv";
    var fileType = "xml";

    String outputDirectory = "./output/" + LocalDateTime.now().format(FORMATTER) + "/";
    new File(outputDirectory).mkdirs();

    log.info(
        "Started processing [{}] with output directory [{}] and file type [{}]",
        inputFile,
        outputDirectory,
        fileType);

    var startTime = System.currentTimeMillis();

    InvoiceFileService invoiceFileService = new InvoiceFileServiceImpl();
    invoiceFileService.processFile(inputFile, fileType, outputDirectory);

    var endTime = System.currentTimeMillis();

    log.info("The process was executed for [{}] millis", endTime - startTime);
  }
}
