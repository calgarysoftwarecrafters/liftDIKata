import spock.lang.Specification

class LiftSystemSpecification extends Specification{
    def "Lift is stationary if no buttons are pressed"() {
        given:
            def interiorButtonPanel = new InteriorButtonPanel()
            def lift = new Lift(0, interiorButtonPanel)
        expect:
            lift.direction == Lift.Directions.STATIONARY
    }

    
}
