package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SqlUtil {
    private SqlUtil() {
    }

    public static String createTableSql(Class<?> clazz) {
        return "CREATE TABLE IF NOT EXISTS %s (%s);".formatted(
            getTableName(clazz),
            getJoinedString(clazz, e -> getFieldColumnName(e) + " " + getFieldColumnValue(e))
        );
    }

    public static String dropTableSql(Class<?> clazz) {
        return "DROP TABLE IF EXISTS %s".formatted(getTableName(clazz));
    }

    public static String insertSql(Class<?> clazz) {
        String id = getIDColumnName(clazz);
        return "INSERT INTO %s (%s) VALUES (%s);".formatted(
            getTableName(clazz),
            getJoinedString(clazz, SqlUtil::getFieldColumnName, id),
            getJoinedString(clazz, f -> "?", id)
        );
    }

    public static String deleteByIdSql(Class<?> clazz) {
        return "DELETE FROM %s WHERE %s = ?".formatted(getTableName(clazz), getIDColumnName(clazz));
    }

    public static String selectAllSql(Class<?> clazz) {
        return "SELECT * FROM %s".formatted(getTableName(clazz));
    }

    public static String truncateTableSql(Class<?> clazz) {
        return "TRUNCATE TABLE %s".formatted(getTableName(clazz));
    }

    public static String getTableName(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Table.class))
            .map(Table::name)
            .orElse(clazz.getSimpleName().toLowerCase());
    }

    public static String getIDColumnName(Class<?> clazz) {
        Optional<Field> f = Arrays.stream(User.class.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Id.class))
            .findFirst();
        return f.map(Field::getName).orElse(null);
    }

    public static String getFieldColumnName(Field field) {
        return Optional.ofNullable(field.getAnnotation(Column.class))
            .map(e -> e.name().isEmpty() ? null : e.name())
            .orElse(field.getName());
    }

    private static String getFieldColumnValue(Field field) {
        return Optional.ofNullable(field.getAnnotation(ColumnValue.class))
            .map(ColumnValue::value)
            .orElse("");
    }

    private static String getJoinedString(Class<?> clazz, Function<? super Field, ? extends String> f) {
        return getJoinedString(clazz, f, "");
    }

    private static String getJoinedString(
        Class<?> clazz,
        Function<? super Field, ? extends String> f,
        String string
    ) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> !getFieldColumnName(field).equals(string))
            .map(f)
            .collect(Collectors.joining(", "));
    }
}
