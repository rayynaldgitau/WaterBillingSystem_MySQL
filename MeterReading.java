package model;

public class MeterReading {
    private int currentReading;
    private int previousReading;

    public MeterReading(int currentReading, int previousReading) {
        this.currentReading = currentReading;
        this.previousReading = previousReading;
    }

    public int getConsumption() {
        if (currentReading < previousReading) {
            return 0;
        }
        return currentReading - previousReading;
    }

    public int getCurrentReading() {
        return currentReading;
    }

    public int getPreviousReading() {
        return previousReading;
    }
}

