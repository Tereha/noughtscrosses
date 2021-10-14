package ru.noughtscrosses.db.repository.helpers.constant;

/** @author Dmitry Tereschenko [tereha.d.y@gmail.com] */
public enum OrderByType {
  ASC("ASC"),
  DESC("DESC");

  private String order;

  private OrderByType(String order) {
    this.order = order;
  }

  public String getOrder() {
    return order;
  }
}
