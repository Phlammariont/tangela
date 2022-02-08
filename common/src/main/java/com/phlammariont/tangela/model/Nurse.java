package com.phlammariont.tangela.model;

import java.util.ArrayList;
import java.util.List;

public class Nurse {

    public List<Shift> myShifts = new ArrayList<>();
    private String id;
    private String role;
    private String domain;
    private int shiftCost;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Shift> getMyShifts() {
        return myShifts;
    }

    public void setMyShifts(List<Shift> myShifts) {
        this.myShifts = myShifts;
    }

    public String getLabel() {
        return "nurse: " + this.id + " role: " + this.getRole() + " area: " + this.getDomain();
    }

    public int getShiftCost() {
        return shiftCost;
    }

    public void setShiftCost(int shiftCost) {
        this.shiftCost = shiftCost;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
