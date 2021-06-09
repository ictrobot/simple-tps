package me.ethanjones.simpletps;

import java.util.LinkedList;

class TPSHistory {
  private static final int MAX_TPS = 20;
  private static final int WINDOW_SIZE = MAX_TPS * 5;

  private final LinkedList<Double> msHistory = new LinkedList<>();
  private final LinkedList<Double> tpsHistory = new LinkedList<>();

  void update(double ms) {
    msHistory.addFirst(ms);
    while (msHistory.size() > WINDOW_SIZE) msHistory.removeLast();

    tpsHistory.addFirst(Math.min(1_000d / ms, MAX_TPS));
    while (tpsHistory.size() > WINDOW_SIZE) tpsHistory.removeLast();
  }

  double getMsPerTick() {
    return average(msHistory);
  }

  double getTPS() {
    return average(tpsHistory);
  }

  private double average(LinkedList<Double> c) {
    return c.stream().mapToDouble(Double::doubleValue).average().orElse(0d);
  }
}
