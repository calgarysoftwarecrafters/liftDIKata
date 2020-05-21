import spock.lang.Specification

class LiftSystemSpecification extends Specification{
    def "Lift is stationary if no buttons are pressed"() {
        given:
            def interiorButtonPanel = new InteriorButtonPanel()
            def lift = new Lift(0, interiorButtonPanel)
        expect:
            lift.direction == Lift.Directions.STATIONARY
    }

    def "Lift moves up if the floor requested from the interior panel is greater than the current floor"() {
        given:
            def interiorButtonPanel = Mock(InteriorButtonPanel)
            def lift = new Lift(0, interiorButtonPanel)
            interiorButtonPanel.getRequestedFloor() >> 10
            lift.buttonPushed()
        expect:
            lift.direction == Lift.Directions.UP
    }
}
