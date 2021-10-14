package ru.noughtscrosses.db.model;

import java.util.Objects;
import java.util.UUID;

public class RoundUser {

  private UUID userId;
  private UUID roundId;

  public RoundUser() {
  }

  public RoundUser(UUID userId, UUID roundId) {
    this.userId = userId;
    this.roundId = roundId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getRoundId() {
    return roundId;
  }

  public void setRoundId(UUID roundId) {
    this.roundId = roundId;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    RoundUser userRound = (RoundUser) o;
    return Objects.equals(userId, userRound.userId) && Objects.equals(roundId, userRound.roundId);
  }

  @Override public int hashCode() {
    return Objects.hash(userId, roundId);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("RoundUser{");
    sb.append("userId=").append(userId);
    sb.append(", roundId=").append(roundId);
    sb.append('}');
    return sb.toString();
  }
}
