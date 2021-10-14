package ru.noughtscrosses.db.model;

import ru.noughtscrosses.constant.PositionType;
import ru.noughtscrosses.constant.StepType;

import java.util.Objects;

public class RoundStep extends RoundUser {

  private StepType type;
  private PositionType position;
  private Integer order;

  public StepType getType() {
    return type;
  }

  public void setType(StepType type) {
    this.type = type;
  }

  public PositionType getPosition() {
    return position;
  }

  public void setPosition(PositionType position) {
    this.position = position;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    if (!super.equals(o))
      return false;
    RoundStep step = (RoundStep) o;
    return type == step.type && position == step.position && Objects.equals(order, step.order);
  }

  @Override public int hashCode() {
    return Objects.hash(super.hashCode(), type, position, order);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("RoundStep{");
    sb.append("type=").append(type);
    sb.append(", position=").append(position);
    sb.append(", order=").append(order);
    sb.append('}');
    return sb.toString();
  }
}
