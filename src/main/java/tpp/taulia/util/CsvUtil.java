package tpp.taulia.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tpp.taulia.model.serialization.LocalDateDeserializer;
import tpp.taulia.model.serialization.LocalDateSerializer;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvUtil {

  public static final String FILE_EXTENSION = "csv";

  public static ObjectReader getCsvReaderFor(Class<?> clazz) {
    var csvMapper = getCsvMapper();
    var csvSchema = getCsvMapperSchema(csvMapper, clazz);

    return csvMapper.readerFor(clazz).with(csvSchema);
  }

  public static ObjectWriter getCsvWriterFor(Class<?> clazz) {
    var csvMapper = getCsvMapper();
    var csvSchema = getCsvMapperSchema(csvMapper, clazz);

    return csvMapper.writerFor(clazz).with(csvSchema);
  }

  private static CsvMapper getCsvMapper() {
    var javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

    var csvMapper =
        CsvMapper.builder()
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .addModule(javaTimeModule)
            .build();

    csvMapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    return csvMapper;
  }

  private static CsvSchema getCsvMapperSchema(CsvMapper csvMapper, Class<?> clazz) {
    return csvMapper.schemaFor(clazz).withHeader().withColumnSeparator(',');
  }
}
