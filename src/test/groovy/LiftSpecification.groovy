import spock.lang.Specification

class LiftSystemSpecification extends Specification{
    def "Lift is stationary if no buttons are pressed"() {
        given:
            def interiorButtonPanel = new InteriorButtonPanel()
            def lift = new Lift(0, interiorButtonPanel)
        expect:
            lift.direction == Lift.Directions.STATIONARY
    }

    def "Lift moves towards the requested floor"() {
        given:
            def interiorButtonPanel = Mock(InteriorButtonPanel)
            def lift = new Lift(currentFloor, interiorButtonPanel)
            interiorButtonPanel.getRequestedFloor() >> requestedFloor
            lift.buttonPushed()
        expect:
            lift.direction == expectedDirection
        where:
            currentFloor | requestedFloor | expectedDirection
            0            | 10             | Lift.Directions.UP
            10           | 0              | Lift.Directions.DOWN
            10           | 10             | Lift.Directions.STATIONARY
    }
}
