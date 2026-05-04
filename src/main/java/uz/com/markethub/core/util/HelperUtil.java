package uz.com.markethub.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

public class HelperUtil {
    private static ObjectMapper objectMapper = null;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");

    public static <E extends Enum<E>> E lookupEnumValue(Class<E> enumClass, String value) {
        E result;

        try {
            result = Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            //todo error message to'g'ri qaytarish
            throw new RuntimeException(
                    "Invalid value for enum " + e.getMessage() + ": " + value);
        }

        return result;
    }

    public static Long generateLogId() {
        Random random = new Random(System.currentTimeMillis());
        long value = random.nextLong();
        return value < 1 ? Math.abs(value) : value;
    }

    public static boolean versionCompare(String requiredVersion, String version) {
        return requiredVersion.equals(version);
    }

    public static String object2JSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapperInstance();
        return objectMapper.writeValueAsString(object);

    }

    public static ObjectMapper getObjectMapperInstance() {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(dateFormat);
            objectMapper.registerModule(new JavaTimeModule());
        }


        return objectMapper;
    }

    public static String generateExportFileName(String prefix, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        return prefix + "_" + now.format(formatter) + "." + extension;
    }
}
