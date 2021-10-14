package ru.noughtscrosses.db.repository.helpers.constant;

public enum JoinType {
  JOIN("JOIN"),
  INNER_JOIN("INNER JOIN"),
  LEFT_JOIN("LEFT JOIN"),
  RIGHT_JOIN("RIGHT JOIN"),
  LEFT_OUTER_JOIN("LEFT OUTER JOIN");

  private String join;

  private JoinType(String join) {
    this.join = join;
  }

  public String getJoin() {
    return join;
  }
}
