class Lift implements ButtonPanelListener, FloorSensorListener, DoorSystemListener{
    private InteriorButtonPanel interiorButtonPanel
    private FloorSensor floorSensor
    private ArrayList<Integer> requestedFloors
    private Integer currentFloor
    public Directions direction
    DoorSystem doorSystem

    enum Directions {
        UP, DOWN, STATIONARY
    }

    Lift(FloorSensor floorSensor, InteriorButtonPanel interiorButtonPanel, DoorSystem doorSystem) {
        this.floorSensor = floorSensor
        this.currentFloor = floorSensor.getCurrentFloor()
        this.interiorButtonPanel = interiorButtonPanel
        this.doorSystem = doorSystem
        this.direction = Directions.STATIONARY
        this.requestedFloors = new ArrayList<>()
        interiorButtonPanel.setListener(this)
    }

    @Override
    def buttonPushed() {
        def floor = interiorButtonPanel.getRequestedFloor()
        changeDirection()
        requestedFloors.add(floor)
        requestedFloors.sort()
    }

    @Override
    def floorChanged() {
        currentFloor = floorSensor.getCurrentFloor()
        changeDirection()
    }

    @Override
    void doorsClosed() {
        if(requestedFloors.size() > 0){
            changeDirection()
        }
    }

    private changeDirection(){
        if(requestedFloors.size() > 0) {
            def requestedFloor = requestedFloors.first()

            if (direction == Directions.DOWN) {
                requestedFloor = requestedFloors.last()
            }

            if (requestedFloor > currentFloor) {
                direction = Directions.UP
            } else if (requestedFloor < currentFloor) {
                direction = Directions.DOWN
            } else {
                doorSystem.openDoors()
                clearFloor(requestedFloor)
                direction = Directions.STATIONARY
            }
        }
    }

    def clearFloor(Integer floor) {
        int index = requestedFloors.indexOf(floor)
        requestedFloors.remove(index)
    }
}
