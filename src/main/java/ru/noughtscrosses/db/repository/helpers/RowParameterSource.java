package ru.noughtscrosses.db.repository.helpers;

import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

import java.sql.Time;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public abstract class RowParameterSource<T> extends AbstractSqlParameterSource {

  protected Map<String, Object> params = new HashMap<>();

  protected Map<String, String> nameMap;

  protected T data;

  public RowParameterSource(T data, Map<String, String> paramNameMap) {
    this.data = data;
    this.nameMap = paramNameMap;
    setMapping();
  }

  public RowParameterSource(T data) {
    this(data, Collections.emptyMap());
  }

  @Override
  public boolean hasValue(String paramName) {
    return params.containsKey(paramName);
  }

  @Override
  public Object getValue(String paramName) throws IllegalArgumentException {
    return params.get(paramName);
  }

  public void setUUIDValue(String key, Function<T, UUID> func) {
    key = nameMap.getOrDefault(key, key);
    UUID value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.OTHER);
    // }
  }

  public void setStringValue(String key, Function<T, String> func) {
    key = nameMap.getOrDefault(key, key);
    String value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.VARCHAR);
    // }
  }

  protected void setLongValue(String key, Function<T, Long> func) {
    key = nameMap.getOrDefault(key, key);
    Long value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.BIGINT);
    // }
  }

  protected void setIntValue(String key, Function<T, Integer> func) {
    key = nameMap.getOrDefault(key, key);
    Integer value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.INTEGER);
    // }
  }

  protected void setDoubleValue(String key, Function<T, Double> func) {
    key = nameMap.getOrDefault(key, key);
    Double value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.DOUBLE);
    // }
  }

  protected void setFloatValue(String key, Function<T, Float> func) {
    key = nameMap.getOrDefault(key, key);
    Float value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.FLOAT);
    // }
  }

  protected void setBooleanValue(String key, Function<T, Boolean> func) {
    key = nameMap.getOrDefault(key, key);
    Boolean value = func.apply(this.data);
    // if (value != null) {
    params.put(key, value);
    registerSqlType(key, Types.BIT);
    // }
  }

  protected void setTimeValue(String key, Function<T, Time> func) {
    key = nameMap.getOrDefault(key, key);
    Time value = func.apply(this.data);
    if (value != null) {
      params.put(key, new java.sql.Timestamp(value.getTime()));

    } else {
      params.put(key, null);
    }
    registerSqlType(key, Types.TIME);
  }

  protected abstract void setMapping();
}
