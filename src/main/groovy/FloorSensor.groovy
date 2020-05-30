class FloorSensor {
    private Integer currentFloor
    private FloorSensorListener listener

    Integer getCurrentFloor() {return currentFloor}

    def floorChanged(Integer floor) {
        this.currentFloor = floor
        listener.floorChanged()
    }

    void setListener(FloorSensorListener listener) {
        this.listener = listener
    }


}
