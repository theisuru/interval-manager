package org.example.intervalmanager;

import java.util.ArrayList;
import java.util.List;

public class IntervalManager {
  private final List<Interval> intervals = new ArrayList<>();

  public List<Interval> getIntervals() {
    return intervals;
  }

  public void addInterval(int start, int end) {
    validateInterval(start, end);

    List<Interval> newIntervals = new ArrayList<>();
    int i = 0;

    while (i < intervals.size() && intervals.get(i).end() < start) {
      newIntervals.add(intervals.get(i));
      i++;
    }

    // merge overlapping
    while (i < intervals.size() && intervals.get(i).start() <= end) {
      start = Math.min(start, intervals.get(i).start());
      end = Math.max(end, intervals.get(i).end());
      i++;
    }
    newIntervals.add(new Interval(start, end));

    while (i < intervals.size()) {
      newIntervals.add(intervals.get(i));
      i++;
    }

    intervals.clear();
    intervals.addAll(newIntervals);
  }

  private void validateInterval(int start, int end) {
    if (start > end) {
      throw new IntervalValidationException("Start should be less than or equal to end");
    }
  }
}