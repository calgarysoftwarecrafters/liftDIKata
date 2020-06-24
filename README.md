# Lift Kata for Dependency Injection
There is a man called Larry. Larry operates lifts. He listens to the floors requested by customers and moves the lift up or down depending on the floors they request. When Larry retires, corporate would like to replace him with *Larry 2000* A new and improved automated system for ensuring that people can ride lifts safely, and get to the floor they've requested. In order to provide the quickest and safest journey from one floor to the next, *Larry 2000* will integrate with several sensors and controls.

- The interior button panel sits inside each elevator, and notifies *Larry 2000* when a person requests a floor by pressing a button
- The door system opens and closes the doors to the elevator when requested
- The floor sensor senses floors as the elevator moves between them.
- The timer makes sure the doors stay open long enough
- The exterior button panel sensor senses if any of the buttons on the outside have been pressed

## Lift Rules

1. The interfaces for the sensors & controls have already been provided and defined. You must use these interfaces without changing them.
2. A lift begins initially at rest.
3. A lift moving in a particular direction will stop at all requested floors in that direction before switching to move in the opposite directon.
4. Once a lift has stopped at all the requested floors, it will not move until a new floor is requested.
5. No matter when a new floor is requested, if it is in the current direction of travel, the lift will stop at the requested door
6. When the lift stops at a requested floor, the doors should open for 3 seconds
7. The exterior buttons at each floor only hve the option to request an elevator heading up, or an elevator heading down. A moving elevator will only stop at a floor if it is moving in the requested direction.
8. If there are no other floor requests pending, and someone presses an exterior button panel to request a lift at a specific floor, the elevator will move to the floor requested by the user.
