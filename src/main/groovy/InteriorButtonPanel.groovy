class InteriorButtonPanel {
    private ButtonPanelListener listener
    private Integer requestedFloor

    void setListener(ButtonPanelListener listener) {
        this.listener = listener
    }

    def requestFloor(int floor){
        listener.buttonPushed()
    }

    Integer getRequestedFloor() {
        return requestedFloor
    }
}
