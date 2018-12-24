package sample;

class LineModel {

    private float spead;
    private float headvay;
    private int lineId;
    private int filename;

    LineModel() {
    }

    LineModel(float spead, float headvay, int lineId, int filename) {
        this.spead = spead;
        this.headvay = headvay;
        this.lineId = lineId;
        this.filename = filename;
    }

    double getSpead() {
        return spead;
    }

    void setSpead(float spead) {
        this.spead = spead;
    }

    double getHeadvay() {
        return headvay;
    }

    void setHeadvay(float headvay) {
        this.headvay = headvay;
    }

    int getLineId() {
        return lineId;
    }

    void setLineId(int lineId) {
        this.lineId = lineId;
    }

    int getFilename() {
        return filename;
    }

    void setFilename(int filename) {
        this.filename = filename;
    }

}
