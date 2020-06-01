class Lift implements ButtonPanelListener, FloorSensorListener{
    private InteriorButtonPanel interiorButtonPanel
    private FloorSensor floorSensor
    public Directions direction
    DoorSystem doorSystem

    enum Directions {
        UP, DOWN, STATIONARY
    }

    Lift(FloorSensor floorSensor, InteriorButtonPanel interiorButtonPanel, DoorSystem doorSystem) {
        this.floorSensor = floorSensor
        this.interiorButtonPanel = interiorButtonPanel
        this.doorSystem = doorSystem
        this.direction = Directions.STATIONARY
        interiorButtonPanel.setListener(this)
    }

    @Override
    def buttonPushed() {
        changeDirection()
    }

    @Override
    def floorChanged() {
        changeDirection()
    }

    private changeDirection(){
        def requestedFloor = interiorButtonPanel.requestedFloor
        def currentFloor = floorSensor.getCurrentFloor()

        if(requestedFloor > currentFloor){
            direction = Directions.UP
        } else if(requestedFloor < currentFloor) {
            direction = Directions.DOWN
        } else {
            doorSystem.openDoors()
            direction = Directions.STATIONARY
        }
    }
}
