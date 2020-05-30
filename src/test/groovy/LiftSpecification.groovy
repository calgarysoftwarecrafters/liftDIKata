import spock.lang.Specification

class LiftSystemSpecification extends Specification{
    def "Lift is stationary if no buttons are pressed"() {
        given:
            def floorSensor = new FloorSensor()
            def interiorButtonPanel = new InteriorButtonPanel()
            def lift = new Lift(floorSensor, interiorButtonPanel)
        expect:
            lift.direction == Lift.Directions.STATIONARY
    }

    def "Lift moves towards the requested floor"() {
        given:
            def floorSensor = Mock(FloorSensor)
            floorSensor.getCurrentFloor() >> currentFloor

            def interiorButtonPanel = Mock(InteriorButtonPanel)
            interiorButtonPanel.getRequestedFloor() >> requestedFloor

            def lift = new Lift(floorSensor, interiorButtonPanel)
            lift.buttonPushed()
        expect:
            lift.direction == expectedDirection
        where:
            currentFloor | requestedFloor | expectedDirection
            0            | 10             | Lift.Directions.UP
            10           | 0              | Lift.Directions.DOWN
            10           | 10             | Lift.Directions.STATIONARY
    }

    def "Lift stops only when it arrives at a requested floor"() {
        given:
            def floorSensor = Mock(FloorSensor)
            floorSensor.getCurrentFloor() >> currentFloor >> newFloor

            def interiorButtonPanel = Mock(InteriorButtonPanel)
            interiorButtonPanel.getRequestedFloor() >> requestedFloor

            def lift = new Lift(floorSensor, interiorButtonPanel)
            lift.buttonPushed()
            lift.floorChanged()

        expect:
            lift.direction == expectedDirection

        where:
        currentFloor | requestedFloor | newFloor | expectedDirection
        0            | 10             | 10       | Lift.Directions.STATIONARY
        10           | 0              | 0        | Lift.Directions.STATIONARY
        10           | 10             | 10       | Lift.Directions.STATIONARY
        0            | 10             | 5        | Lift.Directions.UP
        10           | 0              | 5        | Lift.Directions.DOWN

    }
}
