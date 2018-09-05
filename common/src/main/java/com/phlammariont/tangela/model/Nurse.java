package com.phlammariont.tangela.model;

import java.util.ArrayList;
import java.util.List;

public class Nurse {

    public List<Shift> myShifts = new ArrayList<>();
    private String id;
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
        return "nurse: " + this.id;
    }

    public int getShiftCost() {
        return shiftCost;
    }

    public void setShiftCost(int shiftCost) {
        this.shiftCost = shiftCost;
    }
}
