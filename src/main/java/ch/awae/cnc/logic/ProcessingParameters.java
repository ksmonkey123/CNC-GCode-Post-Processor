package ch.awae.cnc.logic;

public class ProcessingParameters {

    private HomingMode homingMode;
    private boolean doBedLevelling;
    private int workSpeed;
    private int travelSpeed;
    private int zeroX;
    private int zeroY;


    public HomingMode getHomingMode() {
        return homingMode;
    }

    public void setHomingMode(HomingMode homingMode) {
        this.homingMode = homingMode;
    }

    public boolean isDoBedLevelling() {
        return doBedLevelling;
    }

    public void setDoBedLevelling(boolean doBedLevelling) {
        this.doBedLevelling = doBedLevelling;
    }

    public int getWorkSpeed() {
        return workSpeed;
    }

    public void setWorkSpeed(int workSpeed) {
        this.workSpeed = workSpeed;
    }

    public int getTravelSpeed() {
        return travelSpeed;
    }

    public void setTravelSpeed(int travelSpeed) {
        this.travelSpeed = travelSpeed;
    }

    public int getZeroX() {
        return zeroX;
    }

    public void setZeroX(int zeroX) {
        this.zeroX = zeroX;
    }

    public int getZeroY() {
        return zeroY;
    }

    public void setZeroY(int zeroY) {
        this.zeroY = zeroY;
    }
}
