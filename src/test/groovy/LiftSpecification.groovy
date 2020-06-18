import spock.lang.Specification

class LiftSystemSpecification extends Specification {
    private floorSensor
    private interiorButtonPanel
    private doorSystem
    private lift

    def "Lift is stationary and doors remain closed if no buttons are pressed"() {
        given:
        setupLiftTest(0, 0, [0])
        expect:
        lift.direction == Lift.Directions.STATIONARY
        lift.isStopped == true
        lift.doorsOpen == false
    }

    def "Lift stops only when it arrives at a requested floor"() {
        given:
        setupLiftTestAndPressButtons(startFloor, endFloor, [requestedFloor])

        when:
        liftMovesNumberOfFloors(Math.abs(endFloor - startFloor))
        then:
        lift.direction == expectedDirection

        where:
        startFloor | requestedFloor | endFloor | expectedDirection
        0          | 10             | 10       | Lift.Directions.STATIONARY
        10         | 0              | 0        | Lift.Directions.STATIONARY
        10         | 10             | 10       | Lift.Directions.STATIONARY
        0          | 10             | 5        | Lift.Directions.UP
        10         | 0              | 5        | Lift.Directions.DOWN

    }

    def "Lift doors open when lift arrives at requested floor"() {
        given:
        setupLiftTestAndPressButtons(0, 10, [10])
        when:
        liftMovesTowardsRequestedFloor()
        then:
        1 * doorSystem.openDoors()
        lift.doorsOpen == true
    }

    def "Lift moves on to next floor in the original direction of travel when doors are closed"() {
        given:
        setupLiftTestAndPressButtons(initialFloor, finalFloor, requestedFloors)
        when:
        floorsVisited.times {
            liftMovesTowardsRequestedFloor()
            lift.doorsClosed()
        }
        then:
        floorsVisited * doorSystem.openDoors()
        lift.direction == expectedDirection
        where:
        initialFloor | finalFloor | requestedFloors | expectedDirection    | floorsVisited
        5            | 19         | [10, 3, 20]     | Lift.Directions.UP   | 1
        15           | 8          | [9, 17, 2]      | Lift.Directions.DOWN | 1
    }


    def "Lift starts in direction of original button press"() {
        given:
        setupLiftTestAndPressButtons(initialFloor, finalFloor, requestedFloors)

        expect:
        lift.direction == expectedDirection
        lift.isStopped == isStopped

        where:
        initialFloor | finalFloor | requestedFloors | expectedDirection    | isStopped
        5            | 6          | [10, 4]         | Lift.Directions.UP   | false
        5            | 4          | [1, 8]          | Lift.Directions.DOWN | false
        5            | 5          | [5, 6]          | Lift.Directions.STATIONARY   | true
    }

    def "Lift pauses at current floor before moving on to next, if the current floor is requested at any time"() {

    }

    def "Doors do not open if lift is in motion" () {

    }

    def createFloorSensorMock(IntRange floors) {
        def floorSensor = Mock(FloorSensor)
        floorSensor.getCurrentFloor() >>> floors
        return floorSensor
    }

    def createInteriorButtonPanelMock(ArrayList<Integer> requestedFloors) {
        def interiorButtonPanel = Mock(InteriorButtonPanel)
        interiorButtonPanel.getRequestedFloor() >>> requestedFloors

        return interiorButtonPanel
    }

    def setupLiftTestAndPressButtons(Integer startFloor, Integer endFloor, ArrayList<Integer> requestedFloors) {
        lift = setupLiftTest(startFloor, endFloor, requestedFloors)
        requestedFloors.size().times {
            lift.buttonPushed()
        }
    }

    private Lift setupLiftTest(int startFloor, int endFloor, ArrayList<Integer> requestedFloors) {
        doorSystem = Mock(DoorSystem)
        floorSensor = createFloorSensorMock(startFloor..endFloor)
        interiorButtonPanel = createInteriorButtonPanelMock(requestedFloors)
        lift = new Lift(this.floorSensor, interiorButtonPanel, this.doorSystem)
    }

    def liftMovesNumberOfFloors(Integer numberOfFloors) {
        numberOfFloors.times {
            lift.floorChanged()
        }
    }

    def liftMovesTowardsRequestedFloor() {
        def times = 0
        while (lift.direction != Lift.Directions.STATIONARY && times < 200) {
            lift.floorChanged()
            times ++
        }
    }

}
