# interval-manager
Manage intervals: add, remove intervals

## Requirements
 - Java 21

## Build and test
```shell
./mvnw clean test
```

## Usage examples
- Add interval

```java
IntervalManager intervalManager = new IntervalManager();
intervalManager.addInterval(1, 2); // add an interval
intervalManager.addInterval(3, 5);
List<Interval> intervals = intervalManager.getIntervals(); // get intervals
```

## Discussion points
- I have slightly changed the method structure to accept interval `start`, `end` instead of an `Array`. We can overload methods, if `Array` input/output is required.  
- Possibility of using `LinkedList` instead of `ArrayList` to avoid extra memory?