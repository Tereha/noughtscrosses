package ru.noughtscrosses.db.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.AopTestUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.noughtscrosses.db.repository.helpers.constant.JoinType;
import ru.noughtscrosses.db.repository.helpers.RowParameterMapper;
import ru.noughtscrosses.db.repository.helpers.RowParameterSource;
import ru.noughtscrosses.db.repository.helpers.constant.AggregateFunction;
import ru.noughtscrosses.db.repository.helpers.constant.DateTruncType;
import ru.noughtscrosses.db.repository.helpers.constant.JoinType;
import ru.noughtscrosses.db.repository.helpers.constant.OrderByType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> implements IRepository<T> {
  protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  @Autowired
  @Qualifier("noughtsCrossesJdbcTemplate")
  NamedParameterJdbcTemplate jdbcTemplate;

  protected static final String UPDATE_OPER = "UPDATE ";
  protected static final String SELECT_OPER = "SELECT ";
  protected static final String INSERT_OPER = "INSERT INTO ";
  protected static final String DELETE_OPER = "DELETE FROM ";

  protected static final String FROM_OPER = " FROM ";
  protected static final String JOIN_OPER = " JOIN ";
  protected static final String USING_OPER = " USING ";
  protected static final String WHERE_OPER = " WHERE ";
  protected static final String SET_OPER = " SET ";
  protected static final String AS_OPER = " AS ";
  protected static final String AND_OPER = " AND ";
  protected static final String OR_OPER = " OR ";
  protected static final String NOT_OPER = " NOT ";
  protected static final String VALUES_OPER = " VALUES ";
  protected static final String ON_OPER = " ON ";
  protected static final String IN_OPER = " IN ";
  protected static final String IS_OPER = " IS ";
  protected static final String IS_NULL_OPER = " IS NULL ";
  protected static final String IS_NOT_NULL_OPER = " IS NOT NULL ";
  protected static final String LIMIT_OPER = " LIMIT ";
  protected static final String OFFSET_OPER = " OFFSET ";
  protected static final String GROUP_BY_OPER = " GROUP BY ";
  protected static final String ORDER_BY_OPER = " ORDER BY ";
  protected static final String DISTINCT_OPER = " DISTINCT ";
  protected static final String BETWEEN_OPER = " BETWEEN ";
  protected static final String HAVING_OPER = " HAVING ";
  protected static final String COUNT_OPER = " COUNT";
  protected static final String SQL_CALC_FOUND_ROWS_OPER = " SQL_CALC_FOUND_ROWS ";
  protected static final String UNION_ALL_OPER = " UNION ALL ";
  protected static final String FOUND_ROWS = " SELECT FOUND_ROWS() ";
  protected static final String ON_CONFLICT_OPER = " ON CONFLICT ";
  protected static final String DO_NOTHING_OPER = " DO NOTHING ";
  protected static final String DO_UPDATE_OPER = " DO UPDATE ";
  protected static final String LIKE_OPER = " LIKE ";

  protected static final String NULL = "null";

  protected static UUID parseUUID(Object id) {
    if (id instanceof UUID) return (UUID) id;
    else return UUID.fromString(id.toString());
  }

  protected static Date parseDate(Object id) {
    if (id instanceof Date) return (Date) id;
    else return null;
  }

  protected static Long parseLong(Object id) {
    if (id instanceof Long) return (Long) id;
    return null;
  }

  protected static String parseString(Object id) {
    if (id instanceof String) return (String) id;
    return null;
  }

  protected static String assembleInsert(String tableName, List<String> columns) {
    return assembleInsert(tableName, null, columns);
  }

  protected static String assembleInsert(String tableName, String alias, List<String> columns) {
    StringBuilder sb = new StringBuilder();
    sb.append(INSERT_OPER).append(tableName);
    if (alias != null) {
      sb.append(AS_OPER).append(alias);
    }
    sb.append(" (");
    sb.append(String.join(",", columns));
    sb.append(")").append(VALUES_OPER).append("(");
    sb.append(columns.stream().map(c -> ":" + c).collect(Collectors.joining(", ")));
    sb.append(")");
    return sb.toString();
  }

  protected static String assembleUpdate(
      String tableName, List<String> columns, List<String> where) {
    StringBuilder sb = new StringBuilder();
    sb.append(assembleUpdate(tableName, columns));
    assembleWhere(where, sb);
    return sb.toString();
  }

  protected static String assembleUpdate(String tableName, List<String> columns) {
    StringBuilder sb = new StringBuilder();
    sb.append(UPDATE_OPER).append(tableName).append(SET_OPER);
    if (columns != null && !columns.isEmpty()) {
      sb.append(
          columns.stream().map(AbstractRepository::bindColumn).collect(Collectors.joining(", ")));
    }
    return sb.toString();
  }

  protected static String assembleSelect(String tableName, List<String> columns) {
    return assembleSelect(tableName, columns, null, false);
  }

  protected static String assembleSelect(String tableName, List<String> columns, boolean distinct) {
    return assembleSelect(tableName, columns, null, distinct);
  }

  protected static String assembleSelect(
      String tableName, List<String> columns, List<String> where) {
    return assembleSelect(tableName, columns, where, false);
  }

  protected static String assembleSelect(
      String tableName, List<String> columns, List<String> where, boolean distinct) {
    StringBuilder sb = new StringBuilder();
    sb.append(SELECT_OPER);
    if (distinct) sb.append(DISTINCT_OPER);
    sb.append(String.join(",", columns));
    sb.append(FROM_OPER).append(tableName);
    assembleWhere(where, sb);
    return sb.toString();
  }

  protected static String assembleDelete(String tableName, List<String> columns) {
    StringBuilder sb = new StringBuilder();
    sb.append(DELETE_OPER).append(tableName);
    assembleWhere(columns, sb);
    return sb.toString();
  }

  protected static String assembleWhere(String column) {
    StringBuilder sb = new StringBuilder();
    assembleWhere(List.of(column), sb);
    return sb.toString();
  }

  protected static String assembleWhere(List<String> where) {
    StringBuilder sb = new StringBuilder();
    assembleWhere(where, sb);
    return sb.toString();
  }

  protected static String assembleWhereWithNot(List<String> where, List<String> whereNot) {
    StringBuilder sb = new StringBuilder();
    assembleWhere(where, sb);

    if (!CollectionUtils.isEmpty(whereNot)) {
      if (sb.length() == 0) sb.append(WHERE_OPER);
      else sb.append(AND_OPER);

      sb.append(
          whereNot
              .stream()
              .map(w -> NOT_OPER + bindColumn(w))
              .collect(Collectors.joining(AND_OPER)));
    }
    return sb.toString();
  }

  private static void assembleWhere(List<String> where, StringBuilder sb) {
    if (where != null && !where.isEmpty()) {
      sb.append(WHERE_OPER);
      sb.append(
          where.stream().map(AbstractRepository::bindColumn).collect(Collectors.joining(AND_OPER)));
    }
  }

  protected static String assembleJoin(
      String aliasTable,
      String column,
      JoinType joinType,
      String joinTable,
      String aliasJoinTable,
      String joinColumn) {
    return assembleJoin(
        aliasTable, List.of(column), joinType, joinTable, aliasJoinTable, List.of(joinColumn));
  }

  protected static String assembleJoin(
      String aliasTable,
      List<String> columns,
      JoinType joinType,
      String joinTable,
      String aliasJoinTable,
      List<String> joinColumns) {
    Assert.isTrue(
        joinColumns.size() == columns.size(), "Columns list should match join columns list.");
    StringBuilder sb = new StringBuilder();
    sb.append(" ").append(joinType.getJoin());
    sb.append(" ").append(joinTable);
    sb.append(" ").append(aliasJoinTable);
    sb.append(" ").append(ON_OPER);

    //    StringBuilder join = new StringBuilder();
    int columnIndex = 0;
    do {
      if (columnIndex > 0) {
        sb.append(AND_OPER);
      }
      sb.append(aliasTable)
          .append(".")
          .append(columns.get(columnIndex))
          .append(" = ")
          .append(aliasJoinTable)
          .append(".")
          .append(joinColumns.get(columnIndex));
      columnIndex++;
    } while (columnIndex < columns.size());
    //    sb.append(join);
    return sb.toString();
  }

  protected static String assembleLimit(int limit) {
    StringBuilder sb = new StringBuilder();
    sb.append(LIMIT_OPER).append(limit).append(" ");
    return sb.toString();
  }

  protected static String assembleOffset(int offset) {
    StringBuilder sb = new StringBuilder();
    sb.append(OFFSET_OPER).append(offset).append(" ");
    return sb.toString();
  }

  protected static String assembleAggregateFunction(
      AggregateFunction aggregateFunction, String expression) {
    StringBuilder sb = new StringBuilder();
    sb.append(aggregateFunction.getFunction()).append("(").append(expression).append(")");
    return sb.toString();
  }

  protected static String assembleDateTimeFunction(DateTruncType dateTruncType, String column) {
    StringBuilder sb = new StringBuilder();
    sb.append(AggregateFunction.DATE_TRUNC.getFunction());
    sb.append("('").append(dateTruncType.getField()).append("',").append(column).append(") = :");
    sb.append(column.indexOf(".") > 0 ? column.substring(column.indexOf(".") + 1) : column);
    return sb.toString();
  }

  protected static String assembleAlias(String data, String aliasName) {
    StringBuilder sb = new StringBuilder();
    sb.append(data).append(AS_OPER).append(aliasName);
    return sb.toString();
  }

  protected static String assembleCount(String column) {
    StringBuilder sb = new StringBuilder();
    sb.append(COUNT_OPER).append("(").append(column).append(")");
    return sb.toString();
  }

  protected static String assembleGroupBy(List<String> columns) {
    StringBuilder sb = new StringBuilder();
    sb.append(GROUP_BY_OPER).append(String.join(",", columns));
    return sb.toString();
  }

  protected static String assembleOrderBy(String column, OrderByType type) {
    return assembleOrderBy(column, type, null, null);
  }

  protected static String assembleOrderBy(String column, OrderByType type, String column2,
      OrderByType type2) {
    StringBuilder order = new StringBuilder(ORDER_BY_OPER);
    order.append(column).append(" ").append(type.getOrder());
    if (StringUtils.hasText(column2)) {
      order.append(", ").append(column2).append(" ").append(type2.getOrder());
    }
    return order.toString();
  }

  protected static String assembleIn(String column) {
    StringBuilder sql = new StringBuilder();
    sql.append(column).append(IN_OPER).append("(:");
    String[] parts = column.split("\\.");
    if (parts.length == 1) {
      sql.append(parts[0]);
    } else {
      sql.append(parts[1]);
    }
    sql.append(") ");
    return sql.toString();
  }

  protected static String allColumns(String[] allColumns) {
    return allColumns(null, allColumns);
  }

  protected static String allColumns(String alias, String[] allColumns) {
    StringBuilder sb = new StringBuilder();
    for (int colindex = 0; colindex < allColumns.length; colindex++) {
      if (colindex > 0) {
        sb.append(",");
      }
      sb.append(col(alias, allColumns[colindex]));
    }
    return sb.toString();
  }

  protected static String col(String alias, String colname) {
    if (alias != null) {
      return alias + "." + colname;
    } else {
      return colname;
    }
  }

  Integer getInteger(ResultSet rs, String column) throws SQLException {
    int nValue = rs.getInt(column);
    return rs.wasNull() ? null : nValue;
  }

  protected static String bindColumn(String column) {
    return bindColumn(column, " = :");
  }

  protected static String bindColumn(String column, String symbol) {
    StringBuilder sb = new StringBuilder();
    sb.append(column).append(symbol);
    sb.append(column.indexOf(".") > 0 ? column.substring(column.indexOf(".") + 1) : column);
    return sb.toString();
  }

  protected static String assembleLike(String column) {
    StringBuilder sql = new StringBuilder();
    sql.append(column).append(LIKE_OPER).append(":");
    sql.append(column.indexOf(".") > 0 ? column.substring(column.indexOf(".") + 1) : column);
    return sql.toString();
  }

}
