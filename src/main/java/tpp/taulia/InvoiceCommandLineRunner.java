package tpp.taulia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tpp.taulia.model.InvoiceOutputType;
import tpp.taulia.service.InvoiceFileService;
import tpp.taulia.util.FileUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class InvoiceCommandLineRunner implements CommandLineRunner {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");

  private final InvoiceFileService invoiceFileService;

  @Autowired
  public InvoiceCommandLineRunner(InvoiceFileService invoiceFileService) {
    this.invoiceFileService = invoiceFileService;
  }

  @Override
  public void run(String... args) {
    var inputFile = "./input/invoices.csv";
    var fileType = InvoiceOutputType.XML;

    String outputDirectory = getOutputDirectory();

    log.info(
        "Started processing [{}] with output directory [{}] and file type [{}]",
        inputFile,
        outputDirectory,
        fileType);

    var startTime = System.currentTimeMillis();

    invoiceFileService.processFile(inputFile, fileType, outputDirectory);

    var endTime = System.currentTimeMillis();

    log.info("The process was executed for [{}] millis", endTime - startTime);
  }

  private String getOutputDirectory() {
    String outputDirectory = "./output/" + LocalDateTime.now().format(FORMATTER) + "/";

    FileUtil.createMissingSubDirectories(outputDirectory);

    return outputDirectory;
  }
}
