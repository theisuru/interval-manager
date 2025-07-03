# interval-manager
Manage intervals: add, remove and get intervals

## Requirements
 - Java 21

## Build and test
```shell
./mvnw clean test
```
Alternatively, if you don't have Java installed, but has docker installed. Run tests using the following.
```shell
docker run --rm -v "$PWD":/app -w /app eclipse-temurin:21-jdk-jammy ./mvnw test
```

## Usage examples
```java
IntervalManager intervalManager = new IntervalManager();

// add interval
intervalManager.addInterval(1, 2); // [[1, 2]]
intervalManager.addInterval(3, 5); // [[1, 2], [3, 5]]

// get intervals
List<Interval> intervals = intervalManager.getIntervals(); // [[1, 2], [3, 5]

// remove interval
intervalManger.remove(3, 5); // [[1, 2]]
```

## Design
### Add Intervals
1. Store sorted intervals in the list, therefore, it is easier and efficient to do add, get and remove operations. 
2. Create a new list to temporarily hold new interval elements.
3. Keep an `index`, then copy non-overlapping intervals starting from the start of the list. 
4. Add the new interval to the list.
   - Now `index` is pointing to where new index should be inserted. 
   - Here, the new interval could be a non-overlapping, overlapping, adjacent or nested compared to the current interval at the `index`.  
   - By tracking `MIN` for the `start` and `MAX` for the `end` until the non-conflicting index range we can get the new interval range.
5. Copy rest of the list to the new list from `index` value
6. Finally, replace the old list of intervals with the newly calculated list

### Remove Intervals
Here we follow a similar pattern to "add interval". Only step 4 above will be changed to remove, split or trim intervals
- While the intervals at `index` are in conflicting range
  - Skip intervals nested to the interval to remove.
  - If overlapping from either start or end direction, split/trim and add to the temporary list. 

### Get intervals
We are keeping intervals sorted. Therefore, we could just return the interval. Return a non-modifiable copy of the list. 


## Further points
- Interval element consists from start to end, including start and excluding end.
- Interval boundaries and internal list are within `Integer` range.
- I have slightly changed the method signature to accept interval `start`, `end` instead of an `Array`. We can overload methods, if `Array` input/output is required.  
- Methods are small and follow clean code. There is no need for any comments to explain what each method does. 
- Possible to improve memory usage by using a `LinkedList` instead of `ArrayList`. 