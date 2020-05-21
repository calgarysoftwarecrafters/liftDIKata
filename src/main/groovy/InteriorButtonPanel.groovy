class InteriorButtonPanel {
    private ButtonPanelListener listener

    void setListener(ButtonPanelListener listener) {
        this.listener = listener
    }

    def requestFloor(int floor){
        listener.buttonPushed()
    }
}
