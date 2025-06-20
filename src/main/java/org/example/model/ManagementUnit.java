package org.example.model;

public class ManagementUnit {

    private int muId;
    private String muName;
    private String lineOfWork;

    public ManagementUnit() {
    }

    public ManagementUnit(int muId, String muName, String lineOfWork) {
        this.muId = muId;
        this.muName = muName;
        this.lineOfWork = lineOfWork;
    }


    public int getMuId() {
        return muId;
    }

    public void setMuId(int muId) {
        this.muId = muId;
    }

    public String getMuName() {
        return muName;
    }

    public void setMuName(String muName) {
        this.muName = muName;
    }

    public String getLineOfWork() {
        return lineOfWork;
    }

    public void setLineOfWork(String lineOfWork) {
        this.lineOfWork = lineOfWork;
    }

    @Override
    public String toString() {
        return this.muName;
    }
}