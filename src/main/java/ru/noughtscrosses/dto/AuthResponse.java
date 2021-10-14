package ru.noughtscrosses.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AuthResponse {

  private String accessToken;

  public AuthResponse() {
  }

  public AuthResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    AuthResponse that = (AuthResponse) o;
    return Objects.equals(accessToken, that.accessToken);
  }

  @Override public int hashCode() {
    return Objects.hash(accessToken);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("AuthResponse{");
    sb.append("accessToken='").append(accessToken).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
