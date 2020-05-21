class Lift implements ButtonPanelListener{
    private int currentFloor
    private InteriorButtonPanel interiorButtonPanel
    public Directions direction

    enum Directions {
        UP, DOWN, STATIONARY
    }

    Lift(int currentFloor, InteriorButtonPanel interiorButtonPanel) {
        this.currentFloor = currentFloor
        this.interiorButtonPanel = interiorButtonPanel
        this.direction = Directions.STATIONARY
        interiorButtonPanel.setListener(this)
    }

    @Override
    def buttonPushed() {

    }
}
