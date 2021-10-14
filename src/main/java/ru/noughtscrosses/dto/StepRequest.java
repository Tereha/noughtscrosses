package ru.noughtscrosses.dto;

import ru.noughtscrosses.constant.PositionType;

import java.util.Objects;

public class StepRequest {

  private PositionType locationType;

  public PositionType getLocationType() {
    return locationType;
  }

  public void setLocationType(PositionType locationType) {
    this.locationType = locationType;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    StepRequest that = (StepRequest) o;
    return locationType == that.locationType;
  }

  @Override public int hashCode() {
    return Objects.hash(locationType);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("StepRequest{");
    sb.append("locationType=").append(locationType);
    sb.append('}');
    return sb.toString();
  }
}
