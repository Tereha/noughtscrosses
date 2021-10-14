package ru.noughtscrosses.db.repository.helpers.constant;

/** @author Dmitry Tereschenko [tereha.d.y@gmail.com] */
public enum AggregateFunction {
  SUM("SUM"),
  DATE_TRUNC("DATE_TRUNC"),
  MAX("MAX"),
  MIN("MIN"),
  UPPER("UPPER"),
  FIELD("FIELD"),
  DATE("DATE");

  private String function;

  private AggregateFunction(String function) {
    this.function = function;
  }

  public String getFunction() {
    return function;
  }
}
