# interval-manager
Manage intervals: add, remove intervals

## Requirements
 - Java 21

## Build and test
```shell
./mvnw clean test
```

## Usage examples
```java
IntervalManager intervalManager = new IntervalManager();

// add an interval
intervalManager.addInterval(1, 2); // [[1, 2]]
intervalManager.addInterval(3, 5); // [[1, 2], [3, 5]]

// get intervals
List<Interval> intervals = intervalManager.getIntervals(); // [[1, 2], [3, 5]

// remove interval
intervalManger.remove(3, 5); // [[1, 2]]
```

## Discussion points
- Interval element consists from start to end, including start and excluding end
- Interval boundaries and size are within `Integer` range
- I have slightly changed the method signature to accept interval `start`, `end` instead of an `Array`. We can overload methods, if `Array` input/output is required.  
- Possibility of using `LinkedList` instead of `ArrayList` to avoid extra memory?