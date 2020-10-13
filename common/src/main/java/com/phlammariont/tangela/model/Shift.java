package com.phlammariont.tangela.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.time.LocalDate;

@PlanningEntity
public class Shift {
    private static final int SHIFT_REGULAR_HOURS = 12;
    private Nurse nurse;
    private String id;
    private LocalDate date;
    private ShiftType shiftType;
    private int requiredHours = SHIFT_REGULAR_HOURS;
    private Building building;
    private Floor floor;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PlanningVariable(valueRangeProviderRefs = {"nurseRange"})
    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public String getLabel() {
        return "shift: " +
            this.id +
            " - " +
            this.date +
            " - " +
            this.shiftType.getCodeLetter();
    }

    public int getRequiredHours() {
        return requiredHours;
    }

    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
