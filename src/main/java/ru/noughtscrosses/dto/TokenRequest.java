package ru.noughtscrosses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TokenRequest {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TokenRequest that = (TokenRequest) o;
    return Objects.equals(name, that.name);
  }

  @Override public int hashCode() {
    return Objects.hash(name);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("TokenRequest{");
    sb.append("name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
