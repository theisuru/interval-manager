package org.example.intervalmanager;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class IntervalManagerTest {

  private static void addAllIntervals(IntervalManager intervalManager, List<Interval> intervals) {
    for (Interval interval : intervals) {
      intervalManager.addInterval(interval.start(), interval.end());
    }
  }

  // addInterval
  @Test
  void given_noIntervals_when_addingFirstInterval_then_addInterval() {
    IntervalManager intervalManager = new IntervalManager();
    intervalManager.addInterval(1, 2);
    List<Interval> intervals = intervalManager.getIntervals();
    assertThat(intervals).hasSize(1);
    assertThat(intervals.getFirst().start()).isEqualTo(1);
  }

  @Test
  void given_anInvalidInterval_when_addingInterval_then_throwValidationException() {
    IntervalManager intervalManager = new IntervalManager();
    assertThatExceptionOfType(IntervalValidationException.class)
        .isThrownBy(() -> intervalManager.addInterval(2, 1));
  }

  @Test
  void given_setOfIntervals_when_addingANonOverlappingInterval_then_addInterval() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 2), new Interval(3, 4));
    addAllIntervals(intervalManager, input);

    List<Interval> intervals = intervalManager.getIntervals();
    assertThat(intervals).hasSize(2);
    assertThat(intervals.getFirst().start()).isEqualTo(1);
  }

  @Test
  void given_setOfIntervals_when_addingAnOverlappingInterval_then_intervalsShouldMerge() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 3), new Interval(5, 7), new Interval(2, 6));
    addAllIntervals(intervalManager, input);

    List<Interval> expected = List.of(new Interval(1, 7));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void given_setOfIntervals_when_addingAnAdjacentInterval_then_intervalsShouldMergeCorrectly() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 3), new Interval(3, 9));
    addAllIntervals(intervalManager, input);

    List<Interval> expected = List.of(new Interval(1, 9));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void given_setOfIntervals_when_addingAnOverlappingIntervalSpanningMultipleIntervals_then_intervalsShouldMergeCorrectly() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 5), new Interval(-10, -5), new Interval(20, 70), new Interval(-8, 71), new Interval(1, 1));
    addAllIntervals(intervalManager, input);

    List<Interval> expected = List.of(new Interval(-10, 71));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

  // get intervals
  @Test
  void given_setOfIntervals_when_callingGetIntervalsAfterAdding_then_shouldReturnSortedIntervalsByStartDate() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 5), new Interval(-10, -5), new Interval(17, 18), new Interval(7, 9));
    addAllIntervals(intervalManager, input);

    List<Interval> expected = List.of(new Interval(-10, -5), new Interval(1, 5), new Interval(7, 9), new Interval(17, 18));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

  // removeInterval
  @Test
  void given_noIntervals_when_RemovingInterval_then_shouldHaveNoEffect() {
    IntervalManager intervalManager = new IntervalManager();
    intervalManager.removeInterval(1, 2);
    List<Interval> intervals = intervalManager.getIntervals();
    assertThat(intervals).hasSize(0);
  }

  @Test
  void given_anInvalidInterval_when_removingInterval_then_throwValidationException() {
    IntervalManager intervalManager = new IntervalManager();
    assertThatExceptionOfType(IntervalValidationException.class)
        .isThrownBy(() -> intervalManager.removeInterval(2, 1));
  }

  @Test
  void given_setOfIntervals_when_removingANonOverlappingInterval_then_shouldHaveNoEffect() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 2), new Interval(3, 4));
    addAllIntervals(intervalManager, input);

    intervalManager.removeInterval(5, 6);

    List<Interval> intervals = intervalManager.getIntervals();
    assertThat(intervals).hasSize(2);
    assertThat(intervals.getFirst().start()).isEqualTo(1);
  }

  @Test
  void given_setOfIntervals_when_removingAnOverlappingInterval_then_intervalsShouldSplit() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 3), new Interval(5, 7), new Interval(2, 6));
    addAllIntervals(intervalManager, input);

    intervalManager.removeInterval(5, 6);

    List<Interval> expected = List.of(new Interval(1, 5), new Interval(6, 7));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void given_setOfIntervals_when_removingAnOverlappingIntervalSpanningMultipleIntervals_then_intervalsShouldSplit() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 5), new Interval(7, 9), new Interval(12, 18), new Interval(20, 30), new Interval(40, 50));
    addAllIntervals(intervalManager, input);

    intervalManager.removeInterval(8, 22);

    List<Interval> expected = List.of(new Interval(1, 5), new Interval(7, 8), new Interval(22, 30), new Interval(40, 50));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void given_setOfIntervals_when_removingAnOverlappingIntervalAtBoundary_then_intervalsShouldHaveCorrectBoundaries() {
    IntervalManager intervalManager = new IntervalManager();
    List<Interval> input = List.of(new Interval(1, 5), new Interval(7, 9), new Interval(12, 18));
    addAllIntervals(intervalManager, input);

    intervalManager.removeInterval(9, 12);

    List<Interval> expected = List.of(new Interval(1, 5), new Interval(7, 9), new Interval(12, 18));
    List<Interval> actual = intervalManager.getIntervals();
    assertThat(actual).isEqualTo(expected);
  }

}