package com.phlammariont.tangela.model;

public class MessageShift {
    private String id;
    private String date;
    private Service service;
    private Building building;
    private MessageShiftType shiftType;
    private Floor floor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public MessageShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(MessageShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
