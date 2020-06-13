import spock.lang.Specification

class LiftSystemSpecification extends Specification {
    private floorSensor
    private interiorButtonPanel
    private doorSystem
    private lift

    def "Lift is stationary if no buttons are pressed"() {
        given:
        setupLiftTest(0, 0, [0])
        expect:
        lift.direction == Lift.Directions.STATIONARY
    }

    def "Lift moves towards the requested floor"() {
        given:
        setupLiftTest(startFloor, startFloor, [requestedFloor])
        lift.buttonPushed()
        expect:
        lift.direction == expectedDirection
        where:
        startFloor | requestedFloor | expectedDirection
        0          | 10             | Lift.Directions.UP
        10         | 0              | Lift.Directions.DOWN
        10         | 10             | Lift.Directions.STATIONARY
    }

    def "Lift stops only when it arrives at a requested floor"() {
        given:
        setupLiftTest(startFloor, endFloor, [requestedFloor])

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
        setupLiftTest(0, 10, [10])
        when:
        liftMovesTowardsRequestedFloor()
        then:
        1 * doorSystem.openDoors()
    }

    def "Lift moves on to next floor in the original direction of travel when doors are closed"() {
        given:
        setupLiftTest(initialFloor, finalFloor, requestedFloors)
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

    def setupLiftTest(Integer startFloor, Integer endFloor, ArrayList<Integer> requestedFloors) {
        doorSystem = Mock(DoorSystem)
        floorSensor = createFloorSensorMock(startFloor..endFloor)
        interiorButtonPanel = createInteriorButtonPanelMock(requestedFloors)
        lift = new Lift(this.floorSensor, interiorButtonPanel, this.doorSystem)
        requestedFloors.size().times {
            lift.buttonPushed()
        }
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
