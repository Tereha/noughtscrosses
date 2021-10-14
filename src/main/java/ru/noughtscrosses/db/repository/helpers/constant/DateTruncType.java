package ru.noughtscrosses.db.repository.helpers.constant;

/** @author Dmitry Tereschenko [tereha.d.y@gmail.com] */
public enum DateTruncType {
  MICROSECONDS("MICROSECONDS"),
  MILLISECONDS("MILLISECONDS"),
  SECOND("SECOND"),
  MINUTE("MINUTE"),
  HOUR("HOUR"),
  DAY("DAY"),
  WEEK("WEEK"),
  MONTH("MONTH"),
  QUARTER("QUARTER"),
  YEAR("YEAR"),
  DECADE("DECADE"),
  CENTURY("CENTURY"),
  MILLENNIUM("MILLENNIUM");

  private String field;

  private DateTruncType(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }
}
