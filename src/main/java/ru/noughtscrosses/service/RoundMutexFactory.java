package ru.noughtscrosses.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.UUID;

@Service
public class RoundMutexFactory {

  private ConcurrentReferenceHashMap<UUID, Object> map;

  public RoundMutexFactory() {
    this.map = new ConcurrentReferenceHashMap<>();
  }

  public Object getMutex(UUID key) {
    return this.map.computeIfAbsent(key, k -> new Object());
  }
}
