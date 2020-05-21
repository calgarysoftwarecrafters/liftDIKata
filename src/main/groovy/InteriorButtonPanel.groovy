class InteriorButtonPanel {
    private ButtonPanelListener listener
    private int requestedFloor

    void setListener(ButtonPanelListener listener) {
        this.listener = listener
    }

    def requestFloor(int floor){
        listener.buttonPushed()
    }

    int getRequestedFloor() {
        return requestedFloor
    }
}
