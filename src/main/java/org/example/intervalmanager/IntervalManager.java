package org.example.intervalmanager;

import java.util.ArrayList;
import java.util.List;

public class IntervalManager {
  private final List<Interval> intervals = new ArrayList<>();

  public List<Interval> getIntervals() {
    return List.copyOf(intervals);
  }

  public synchronized void addInterval(int start, int end) {
    validateInterval(start, end);

    List<Interval> newIntervals = new ArrayList<>();
    int index = copyNonOverlappingIntervalsAtHead(start, newIntervals);
    index = mergeAndCopyOverlappingIntervals(start, end, newIntervals, index);
    copyNonOverlappingIntervalsAtTail(newIntervals, index);

    intervals.clear();
    intervals.addAll(newIntervals);
  }

  public synchronized void removeInterval(int start, int end) {
    validateInterval(start, end);

    List<Interval> newIntervals = new ArrayList<>();
    int index = copyNonOverlappingIntervalsAtHead(start, newIntervals);
    index = splitAndCopyOverlappingIntervals(start, end, newIntervals, index);
    copyNonOverlappingIntervalsAtTail(newIntervals, index);

    intervals.clear();
    intervals.addAll(newIntervals);
  }

  private void validateInterval(int start, int end) {
    if (start > end) {
      throw new IntervalValidationException("Start should be less than or equal to end");
    }
  }

  private int copyNonOverlappingIntervalsAtHead(int start, List<Interval> target) {
    int index = 0;
    while (index < intervals.size() && intervals.get(index).end() < start) {
      target.add(intervals.get(index));
      index++;
    }
    return index;
  }

  private int mergeAndCopyOverlappingIntervals(int start, int end, List<Interval> target, int index) {
    while (index < intervals.size() && intervals.get(index).start() <= end) {
      start = Math.min(start, intervals.get(index).start());
      end = Math.max(end, intervals.get(index).end());
      index++;
    }
    target.add(new Interval(start, end));
    return index;
  }

  private void copyNonOverlappingIntervalsAtTail(List<Interval> target, int index) {
    while (index < intervals.size()) {
      target.add(intervals.get(index));
      index++;
    }
  }

  private int splitAndCopyOverlappingIntervals(int start, int end, List<Interval> target, int index) {
    while(index < intervals.size() && intervals.get(index).end() > start && intervals.get(index).start() < end) {
      Interval interval = intervals.get(index);
      if (interval.start() < start) {
        target.add(new Interval(interval.start(), start));
      }
      if (interval.end() > end) {
        target.add(new Interval(end, interval.end()));
      }
      index++;
    }
    return index;
  }
}