package com.pigdroid.simulation.editor;

public class TrainingRegimen {
    private float physicalIntensity;
    private boolean technicalFocus;
    private int identicalDaysCount;
    private boolean benched;

    public TrainingRegimen(float physicalIntensity, boolean technicalFocus, int identicalDaysCount, boolean benched) {
        this.physicalIntensity = physicalIntensity;
        this.technicalFocus = technicalFocus;
        this.identicalDaysCount = identicalDaysCount;
        this.benched = benched;
    }

    public float getPhysicalIntensity() {
        return physicalIntensity;
    }

    public void setPhysicalIntensity(float physicalIntensity) {
        this.physicalIntensity = physicalIntensity;
    }

    public boolean isTechnicalFocus() {
        return technicalFocus;
    }

    public void setTechnicalFocus(boolean technicalFocus) {
        this.technicalFocus = technicalFocus;
    }

    public int getIdenticalDaysCount() {
        return identicalDaysCount;
    }

    public void setIdenticalDaysCount(int identicalDaysCount) {
        this.identicalDaysCount = identicalDaysCount;
    }

    public boolean isIdenticalToPreviousDays(int count) {
        return this.identicalDaysCount >= count;
    }

    public boolean isBenched() {
        return benched;
    }

    public void setBenched(boolean benched) {
        this.benched = benched;
    }
}
