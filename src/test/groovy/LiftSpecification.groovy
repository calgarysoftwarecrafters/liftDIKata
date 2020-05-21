import spock.lang.Specification

class LiftSystemSpecification extends Specification{
    def "Dinger event fires when elevator is already on requested floor"() {
        given:
            def interiorButtonPanel = new InteriorButtonPanel();
            def dingerMock =  Mock(Dinger);
            def lift = new Lift(0, interiorButtonPanel, dingerMock)
        when:
            interiorButtonPanel.requestFloor(0)
        then:
            1 * dingerMock.ding()
    }

    def "Lift is stationary if no buttons are pressed"() {
        given:
            def interiorButtonPanel = new InteriorButtonPanel();
            def lift = new Lift(0, interiorButtonPanel)
        expect:
            lift.direction == Lift.Directions.STATIONARY
    }

    
}
