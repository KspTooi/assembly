package com.ksptool.assembly.blueprint.collector;

import com.ksptool.assembly.blueprint.entity.field.RawField;
import com.ksptool.assembly.blueprint.entity.field.RawTable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MysqlCollector implements RawCollector {

    private static final Logger logger = LoggerFactory.getLogger(MysqlCollector.class);

    //数据库URL
    @Setter
    private String url;

    //数据库用户名
    @Setter
    private String username;

    //数据库密码
    @Setter
    private String password;

    //数据库名称
    @Setter
    private String database;

    /**
     * 构造函数
     *
     * @param url      数据库URL
     * @param username 数据库用户名
     * @param password 数据库密码
     * @param database 数据库名称
     */
    public MysqlCollector(String url, String username, String password, String database) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public MysqlCollector() {
    }

    @Override
    public List<RawTable> collect() {
        logger.info("开始收集MySQL数据库表信息，数据库：{}", database);
        List<RawTable> tables = new ArrayList<>();
        Connection connection = null;
        try {
            logger.debug("连接MySQL数据库，URL：{}", url);
            connection = DriverManager.getConnection(url, username, password);
            List<String> tableNames = getTableNames(connection);
            logger.info("获取到{}张表", tableNames.size());
            int seq = 0;
            for (String tableName : tableNames) {
                logger.debug("处理表：{}", tableName);
                RawTable table = buildTable(connection, tableName, seq++);
                if (table == null) {
                    logger.warn("表{}构建失败，跳过", tableName);
                    continue;
                }
                tables.add(table);
            }
            logger.info("收集完成，共收集{}张表", tables.size());
        } catch (SQLException e) {
            logger.error("收集MySQL表信息失败，数据库：{}", database, e);
            throw new RuntimeException("收集MySQL表信息失败", e);
        } finally {
            closeConnection(connection);
        }
        return tables;
    }

    private List<String> getTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? ORDER BY TABLE_NAME";
        logger.debug("查询表名列表，SQL：{}，数据库：{}", sql, database);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, database);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    if (StringUtils.isBlank(tableName)) {
                        logger.warn("获取到空表名，跳过");
                        continue;
                    }
                    tableNames.add(tableName);
                }
            }
        }
        return tableNames;
    }

    private RawTable buildTable(Connection connection, String tableName, int seq) throws SQLException {
        logger.debug("构建表信息，表名：{}，序号：{}", tableName, seq);
        RawTable table = new RawTable();
        table.setName(tableName);
        table.setSeq(seq);

        String comment = getTableComment(connection, tableName);
        table.setComment(comment);

        List<RawField> fields = getFields(connection, tableName);
        table.setFields(fields);
        logger.debug("表{}构建完成，字段数：{}", tableName, fields.size());

        return table;
    }

    private String getTableComment(Connection connection, String tableName) throws SQLException {
        String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        logger.debug("查询表注释，表名：{}", tableName);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, database);
            ps.setString(2, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String comment = rs.getString("TABLE_COMMENT");
                    logger.debug("表{}注释：{}", tableName, comment);
                    return comment;
                }
            }
        }
        logger.debug("表{}无注释", tableName);
        return null;
    }

    private List<RawField> getFields(Connection connection, String tableName) throws SQLException {
        List<RawField> fields = new ArrayList<>();
        String sql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, IS_NULLABLE, COLUMN_KEY, ORDINAL_POSITION " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
        logger.debug("查询表字段信息，表名：{}", tableName);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, database);
            ps.setString(2, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                int fieldSeq = 0;
                while (rs.next()) {
                    RawField field = new RawField();
                    String columnName = rs.getString("COLUMN_NAME");
                    field.setName(columnName);
                    field.setType(rs.getString("DATA_TYPE"));
                    field.setComment(rs.getString("COLUMN_COMMENT"));
                    field.setRequired("NO".equals(rs.getString("IS_NULLABLE")));
                    field.setPrimaryKey("PRI".equals(rs.getString("COLUMN_KEY")));
                    field.setSeq(fieldSeq++);
                    fields.add(field);
                    logger.debug("字段：{}，类型：{}，必填：{}，主键：{}", columnName, field.getType(), field.isRequired(), field.isPrimaryKey());
                }
            }
        }
        logger.debug("表{}共{}个字段", tableName, fields.size());
        return fields;
    }

    private void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
            logger.debug("数据库连接已关闭");
        } catch (SQLException e) {
            logger.warn("关闭数据库连接失败", e);
        }
    }

}

