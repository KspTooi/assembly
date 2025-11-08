package com.ksptool.assembly.blueprint.converter;

import com.ksptool.assembly.blueprint.entity.field.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class MysqlToJavaPolyConverter implements PolyConverter {

    private static final Logger logger = LoggerFactory.getLogger(MysqlToJavaPolyConverter.class);

    private static class TypeInfo {
        String simpleName;
        String fullName;

        TypeInfo(String simpleName, String fullName) {
            this.simpleName = simpleName;
            this.fullName = fullName;
        }
    }

    private static final Map<String, TypeInfo> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("VARCHAR", new TypeInfo("String", String.class.getName()));
        TYPE_MAP.put("CHAR", new TypeInfo("String", String.class.getName()));
        TYPE_MAP.put("TEXT", new TypeInfo("String", String.class.getName()));
        TYPE_MAP.put("TINYTEXT", new TypeInfo("String", String.class.getName()));
        TYPE_MAP.put("MEDIUMTEXT", new TypeInfo("String", String.class.getName()));
        TYPE_MAP.put("LONGTEXT", new TypeInfo("String", String.class.getName()));
        TYPE_MAP.put("INT", new TypeInfo("Integer", Integer.class.getName()));
        TYPE_MAP.put("INTEGER", new TypeInfo("Integer", Integer.class.getName()));
        TYPE_MAP.put("BIGINT", new TypeInfo("Long", Long.class.getName()));
        TYPE_MAP.put("DECIMAL", new TypeInfo("BigDecimal", BigDecimal.class.getName()));
        TYPE_MAP.put("NUMERIC", new TypeInfo("BigDecimal", BigDecimal.class.getName()));
        TYPE_MAP.put("FLOAT", new TypeInfo("Float", Float.class.getName()));
        TYPE_MAP.put("DOUBLE", new TypeInfo("Double", Double.class.getName()));
        TYPE_MAP.put("BOOLEAN", new TypeInfo("Boolean", Boolean.class.getName()));
        TYPE_MAP.put("TINYINT", new TypeInfo("Integer", Integer.class.getName()));
        TYPE_MAP.put("SMALLINT", new TypeInfo("Integer", Integer.class.getName()));
        TYPE_MAP.put("MEDIUMINT", new TypeInfo("Integer", Integer.class.getName()));
        TYPE_MAP.put("DATE", new TypeInfo("LocalDate", LocalDate.class.getName()));
        TYPE_MAP.put("DATETIME", new TypeInfo("LocalDateTime", LocalDateTime.class.getName()));
        TYPE_MAP.put("TIMESTAMP", new TypeInfo("LocalDateTime", LocalDateTime.class.getName()));
        TYPE_MAP.put("TIME", new TypeInfo("LocalTime", LocalTime.class.getName()));
        TYPE_MAP.put("BLOB", new TypeInfo("byte[]", "byte[]"));
        TYPE_MAP.put("BINARY", new TypeInfo("byte[]", "byte[]"));
        TYPE_MAP.put("VARBINARY", new TypeInfo("byte[]", "byte[]"));
    }

    @Override
    public PolyTable toPolyTable(RawTable rawTable) {
        if (rawTable == null) {
            logger.warn("原始表为null，无法转换");
            return null;
        }
        logger.debug("转换表：{}", rawTable.getName());
        PolyTable polyTable = new PolyTable();
        polyTable.setRawTable(rawTable);
        String javaClassName = toJavaClassName(rawTable.getName());
        polyTable.setStdName(javaClassName);
        polyTable.setComment(rawTable.getComment());
        polyTable.setSeq(rawTable.getSeq());

        List<PolyField> polyFields = new ArrayList<>();
        Set<String> importSet = new HashSet<>();
        if (rawTable.getFields() != null) {
            for (RawField rawField : rawTable.getFields()) {
                PolyField polyField = toPolyField(rawField);
                if (polyField == null) {
                    continue;
                }
                polyFields.add(polyField);
                String fullTypeName = getFullTypeName(rawField.getType());
                if (fullTypeName != null && !isPrimitiveType(fullTypeName) && !isArrayType(fullTypeName)) {
                    importSet.add(fullTypeName);
                }
            }
        }
        polyTable.setFields(polyFields);

        List<PolyImport> imports = new ArrayList<>();
        List<String> sortedImports = new ArrayList<>(importSet);
        sortedImports.sort(String::compareTo);
        for (String importPath : sortedImports) {
            PolyImport polyImport = new PolyImport();
            polyImport.setPath(importPath);
            imports.add(polyImport);
        }
        polyTable.setImports(imports);
        logger.debug("表{}转换完成，字段数：{}，导入数：{}", rawTable.getName(), polyFields.size(), imports.size());
        return polyTable;
    }

    @Override
    public PolyField toPolyField(RawField rawField) {
        if (rawField == null) {
            logger.warn("原始字段为null，无法转换");
            return null;
        }
        PolyField polyField = new PolyField();
        polyField.setRawField(rawField);
        String javaFieldName = toJavaFieldName(rawField.getName());
        String javaStdName = capitalize(javaFieldName);
        polyField.setStdName(javaStdName);
        polyField.setType(toJavaType(rawField.getType()));
        polyField.setComment(rawField.getComment());
        polyField.setRequired(rawField.isRequired());
        polyField.setPrimaryKey(rawField.isPrimaryKey());
        polyField.setSeq(rawField.getSeq());
        logger.debug("字段{}转换为{}，类型：{}", rawField.getName(), polyField.getName(), polyField.getType());
        return polyField;
    }

    private String toJavaClassName(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return tableName;
        }
        String camelCase = toCamelCase(tableName);
        return capitalize(camelCase);
    }

    private String toJavaFieldName(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            return fieldName;
        }
        return toCamelCase(fieldName);
    }

    private String toCamelCase(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        String[] parts = name.toLowerCase().split("_");
        if (parts.length == 0) {
            return name;
        }
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (StringUtils.isNotBlank(parts[i])) {
                result.append(capitalize(parts[i]));
            }
        }
        return result.toString();
    }

    private String capitalize(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String toJavaType(String mysqlType) {
        if (StringUtils.isBlank(mysqlType)) {
            return "String";
        }
        String type = mysqlType.toUpperCase().trim();
        int parenIndex = type.indexOf('(');
        String baseType = parenIndex > 0 ? type.substring(0, parenIndex) : type;
        TypeInfo typeInfo = TYPE_MAP.get(baseType);
        if (typeInfo != null) {
            if ("TINYINT".equals(baseType) && type.contains("1")) {
                return "Boolean";
            }
            return typeInfo.simpleName;
        }
        logger.warn("未知的MySQL类型：{}，默认使用String", mysqlType);
        return "String";
    }

    private String getFullTypeName(String mysqlType) {
        if (StringUtils.isBlank(mysqlType)) {
            return String.class.getName();
        }
        String type = mysqlType.toUpperCase().trim();
        int parenIndex = type.indexOf('(');
        String baseType = parenIndex > 0 ? type.substring(0, parenIndex) : type;
        TypeInfo typeInfo = TYPE_MAP.get(baseType);
        if (typeInfo != null) {
            if ("TINYINT".equals(baseType) && type.contains("1")) {
                return Boolean.class.getName();
            }
            return typeInfo.fullName;
        }
        return String.class.getName();
    }

    private boolean isPrimitiveType(String fullTypeName) {
        if (StringUtils.isBlank(fullTypeName)) {
            return true;
        }
        return fullTypeName.startsWith("java.lang.");
    }

    private boolean isArrayType(String fullTypeName) {
        if (StringUtils.isBlank(fullTypeName)) {
            return false;
        }
        return "byte[]".equals(fullTypeName) || fullTypeName.endsWith("[]");
    }

}

