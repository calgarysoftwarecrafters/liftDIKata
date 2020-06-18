class Lift implements ButtonPanelListener, FloorSensorListener, DoorSystemListener {
    private InteriorButtonPanel interiorButtonPanel
    private FloorSensor floorSensor
    def requestedFloorsForDirection
    private Integer currentFloor
    public Directions direction
    public Boolean isStopped
    DoorSystem doorSystem
    boolean doorsOpen

    enum Directions {
        UP, DOWN, STATIONARY
    }

    Lift(FloorSensor floorSensor, InteriorButtonPanel interiorButtonPanel, DoorSystem doorSystem) {
        this.floorSensor = floorSensor
        this.currentFloor = floorSensor.getCurrentFloor()
        this.interiorButtonPanel = interiorButtonPanel
        this.doorSystem = doorSystem

        this.direction = Directions.STATIONARY
        this.isStopped = true
        this.doorsOpen = false

        this.requestedFloorsForDirection = new HashMap<>()
        this.requestedFloorsForDirection.put(Directions.UP, new ArrayList<Integer>())
        this.requestedFloorsForDirection.put(Directions.DOWN, new ArrayList<Integer>())
        interiorButtonPanel.setListener(this)
    }

    @Override
    def buttonPushed() {
        def floor = interiorButtonPanel.getRequestedFloor()

        if (floor > currentFloor) {
            requestedFloorsForDirection.get(Directions.UP).add(floor)
            requestedFloorsForDirection.get(Directions.UP).sort()
        } else if (floor < currentFloor) {
            requestedFloorsForDirection.get(Directions.DOWN).add(floor)
            requestedFloorsForDirection.get(Directions.DOWN).sort()
        }

        stopIfArrivedAtRequestedFloor([floor])
        evaluateDirectionOfTravel()
    }

    @Override
    def floorChanged() {
        currentFloor = floorSensor.getCurrentFloor()
        stopIfArrivedAtRequestedFloor(requestedFloorsForDirection.get(direction))
        evaluateDirectionOfTravel()
    }

    @Override
    void doorsClosed() {
        evaluateDirectionOfTravel()
    }

    private evaluateDirectionOfTravel() {
        def proposedDirection = direction
        if(proposedDirection == Directions.STATIONARY) {
            proposedDirection = Directions.UP
        }

        if (requestedFloorsForDirection.get(proposedDirection).isEmpty()) {
            if (requestedFloorsForDirection.get(calculateOppositeDirection(proposedDirection)).isEmpty()) {
                direction = Directions.STATIONARY
                isStopped = true
            } else if (!doorsOpen) {
                direction = calculateOppositeDirection(proposedDirection)
                isStopped = false
            }
        } else if (!doorsOpen){
            direction = proposedDirection
            isStopped = false
        }
    }

    private Directions calculateOppositeDirection(Directions initial){
        if(initial == Directions.UP) {
            return Directions.DOWN
        } else if(initial == Directions.DOWN) {
            return Directions.UP
        }
        return Directions.STATIONARY
    }

    private stopIfArrivedAtRequestedFloor(ArrayList<Integer> requestedFloors) {
        def requestedFloor = nextRequestedFloor(requestedFloors)

        if (requestedFloor == currentFloor) {
            doorSystem.openDoors()
            doorsOpen = true
            clearFloor(requestedFloor, requestedFloors)
            isStopped = true
        }
    }

    private Integer nextRequestedFloor(ArrayList<Integer> requestedFloors) {
        def requestedFloor = requestedFloors.first()

        if (direction == Directions.DOWN) {
            requestedFloor = requestedFloors.last()
        }

        return requestedFloor
    }

    def clearFloor(Integer floor, ArrayList<Integer> requestedFloors) {
        int index = requestedFloors.indexOf(floor)
        requestedFloors.remove(index)
    }
}
