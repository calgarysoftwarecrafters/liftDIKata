import spock.lang.Specification

class LiftSystemSpecification extends Specification{
    private floorSensor
    private interiorButtonPanel
    private doorSystem
    private lift

    def "Lift is stationary if no buttons are pressed"() {
        given:
            setupLiftTest(0, 0, 0)
        expect:
            lift.direction == Lift.Directions.STATIONARY
    }

    def "Lift moves towards the requested floor"() {
        given:
            setupLiftTest(startFloor, startFloor, requestedFloor)
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
            setupLiftTest(startFloor, endFloor, requestedFloor)
        when:
            liftMovesTowardsRequestedFloor(startFloor, endFloor)
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

    def "Lift doors open when lift arrives at requested floor" (){
        given:
            setupLiftTest(0, 10, 10)
        when:
            liftMovesTowardsRequestedFloor(0, 10)
        then:
            1 * doorSystem.openDoors()
    }

    def createFloorSensorMock(IntRange floors) {
        def floorSensor = Mock(FloorSensor)
        floorSensor.getCurrentFloor() >>> floors
        return floorSensor
    }

    def createInteriorButtonPanelMock(Integer requestedFloor){
        def interiorButtonPanel = Mock(InteriorButtonPanel)
        interiorButtonPanel.getRequestedFloor() >> requestedFloor
        return interiorButtonPanel
    }

    def setupLiftTest(Integer startFloor, Integer endFloor, Integer requestedFloor) {
        doorSystem = Mock(DoorSystem)
        floorSensor = createFloorSensorMock(startFloor..endFloor)
        interiorButtonPanel = createInteriorButtonPanelMock(requestedFloor)
        lift = new Lift(this.floorSensor, interiorButtonPanel, this.doorSystem)
    }

    def liftMovesTowardsRequestedFloor(Integer startFloor, Integer endFloor){
        lift.buttonPushed()
        (endFloor - startFloor).abs().times {
            lift.floorChanged()
        }
    }

}
