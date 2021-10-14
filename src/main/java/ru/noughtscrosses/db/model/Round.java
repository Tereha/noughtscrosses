package ru.noughtscrosses.db.model;

import ru.noughtscrosses.constant.RoundType;
import ru.noughtscrosses.constant.Status;

import java.util.Objects;
import java.util.UUID;

public class Round {

  protected UUID id;
  protected RoundType type;
  protected Status status;
  protected UUID winnerUserId;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public RoundType getType() {
    return type;
  }

  public void setType(RoundType type) {
    this.type = type;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public UUID getWinnerUserId() {
    return winnerUserId;
  }

  public void setWinnerUserId(UUID winnerUserId) {
    this.winnerUserId = winnerUserId;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Round round = (Round) o;
    return Objects.equals(id, round.id) && type == round.type && status == round.status && Objects
        .equals(winnerUserId, round.winnerUserId);
  }

  @Override public int hashCode() {
    return Objects.hash(id, type, status, winnerUserId);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("Round{");
    sb.append("id=").append(id);
    sb.append(", type=").append(type);
    sb.append(", status=").append(status);
    sb.append(", winnerUserId=").append(winnerUserId);
    sb.append('}');
    return sb.toString();
  }
}
