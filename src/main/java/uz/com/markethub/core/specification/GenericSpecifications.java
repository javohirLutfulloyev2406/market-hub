package uz.com.markethub.core.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.*;
import java.util.Date;
import java.util.Map;

public class GenericSpecifications {

    public static <T> Specification<T> byFilters(Map<String, String> filters) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            // Asosiy filterlar
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                if ("startDate".equals(field) || "endDate".equals(field)) continue;

                if (!hasField(root, field)) {
                    if (field.endsWith("Id")) {
                        String relationName = field.substring(0, field.length() - 2);
                        if (hasField(root, relationName)) {
                            predicate = cb.and(predicate,
                                    cb.equal(root.get(relationName).get("id"),
                                            convertValue(Long.class, value)));
                        }
                    }
                    continue;
                }

                if (root.get(field).getJavaType() == String.class) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
                } else {
                    predicate = cb.and(predicate,
                            cb.equal(root.get(field), convertValue(root.get(field).getJavaType(), value)));
                }
            }

            if (filters.containsKey("startDate") && filters.containsKey("endDate") && hasField(root, "createdAt")) {
                long startMillis = Long.parseLong(filters.get("startDate"));
                long endMillis = Long.parseLong(filters.get("endDate"));

                Class<?> fieldType = root.get("createdAt").getJavaType();

                if (fieldType.equals(LocalDateTime.class)) {
                    predicate = cb.and(predicate,
                            cb.between(
                                    root.get("createdAt"),
                                    LocalDateTime.ofInstant(Instant.ofEpochMilli(startMillis), ZoneId.systemDefault()),
                                    LocalDateTime.ofInstant(Instant.ofEpochMilli(endMillis), ZoneId.systemDefault())
                            ));
                } else if (fieldType.equals(Instant.class)) {
                    predicate = cb.and(predicate,
                            cb.between(
                                    root.get("createdAt"),
                                    Instant.ofEpochMilli(startMillis),
                                    Instant.ofEpochMilli(endMillis)
                            ));
                } else if (fieldType.equals(Date.class)) {
                    predicate = cb.and(predicate,
                            cb.between(
                                    root.get("createdAt"),
                                    new Date(startMillis),
                                    new Date(endMillis)
                            ));
                } else if (fieldType.equals(LocalDate.class)) {
                    predicate = cb.and(predicate,
                            cb.between(
                                    root.get("createdAt"),
                                    Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDate(),
                                    Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                            ));
                }
            }

            return predicate;
        };
    }

    private static boolean hasField(Root<?> root, String field) {
        try {
            root.get(field);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static Object convertValue(Class<?> type, String value) {
        if (type.equals(Long.class)) return Long.valueOf(value);
        if (type.equals(Integer.class)) return Integer.valueOf(value);
        if (type.equals(Boolean.class)) return Boolean.valueOf(value);
        if (type.equals(Double.class)) return Double.valueOf(value);
        return value;
    }
}
