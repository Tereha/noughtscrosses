package ru.noughtscrosses.db.repository.helpers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class RowParameterMapper<T> implements RowMapper<T> {

  protected List<BiConsumer<T, ResultSet>> resultSetters = new ArrayList<>();

  protected Map<String, String> nameMap = Collections.emptyMap();

  protected abstract T getInstance();

  protected abstract void setMapping();

  public RowParameterMapper(Map<String, String> paramNameMap) {
    this();
    this.nameMap = paramNameMap;
  }

  public RowParameterMapper() {
    setMapping();
  }

  @Override
  public T mapRow(ResultSet resultSet, int i) throws SQLException {
    T data = getInstance();
    for (BiConsumer<T, ResultSet> entry : resultSetters) {
      entry.accept(data, resultSet);
    }
    return data;
  }

  protected void setStringValue(String key, BiConsumer<T, String> setter) {
    resultSetters.add(
        (data, rs) -> {
          String v = getString(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, v);
          }
        });
  }

  protected void setLongValue(String key, BiConsumer<T, Long> setter) {
    resultSetters.add(
        (data, rs) -> {
          Long v = getLong(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, v);
          }
        });
  }

  protected void setUUIDValue(String key, BiConsumer<T, UUID> setter) {
    resultSetters.add(
        (data, rs) -> {
          String v = getString(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, UUID.fromString(v));
          }
        });
  }

  protected String getString(String key, ResultSet rs) {
    String v;
    try {
      v = rs.getString(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }

  private Long getLong(String key, ResultSet rs) {
    Long v;
    try {
      v = rs.getLong(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }

  protected void setIntValue(String key, BiConsumer<T, Integer> setter) {
    resultSetters.add(
        (data, rs) -> {
          Integer v = getInt(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, v);
          }
        });
  }

  private Integer getInt(String key, ResultSet rs) {
    Integer v;
    try {
      v = rs.getInt(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }

  protected void setDoubleValue(String key, BiConsumer<T, Double> setter) {
    resultSetters.add(
        (data, rs) -> {
          Double v = getDouble(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, v);
          }
        });
  }

  private Double getDouble(String key, ResultSet rs) {
    Double v;
    try {
      v = rs.getDouble(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }

  protected void setBooleanValue(String key, BiConsumer<T, Boolean> setter) {
    resultSetters.add(
        (data, rs) -> {
          Object v = getObject(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, (Boolean) v);
          }
        });
  }

  private Boolean getBoolean(String key, ResultSet rs) {
    Boolean v;
    try {
      v = rs.getBoolean(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }

  protected void setFloatValue(String key, BiConsumer<T, Float> setter) {
    resultSetters.add(
        (data, rs) -> {
          Float v = getFloat(nameMap.getOrDefault(key, key), rs);
          if (v != null) {
            setter.accept(data, v);
          }
        });
  }

  private Float getFloat(String key, ResultSet rs) {
    Float v;
    try {
      v = rs.getFloat(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }

  private Object getObject(String key, ResultSet rs) {
    Object v;
    try {
      v = rs.getObject(key);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return v;
  }
}
