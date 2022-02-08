package com.phlammariont.tangela.score;

import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import com.phlammariont.tangela.model.ShiftType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NurseRosteringEasyScoreCalculatorTest {
    NurseRosteringEasyScoreCalculator calculator = new NurseRosteringEasyScoreCalculator();

    @org.junit.jupiter.api.Test
    void maxWorkloadByDaysScore() {
        Nurse nurse = new Nurse();
        ShiftType shiftType = new ShiftType();
        shiftType.setCodeLetter("V");

        Shift shift1 = new Shift();
        shift1.setDate(LocalDate.parse("2020-11-01"));
        shift1.setShiftType(shiftType);
        nurse.myShifts.add(shift1);

        Shift shift2 = new Shift();
        shift2.setDate(LocalDate.parse("2020-11-02"));
        shift2.setShiftType(shiftType);
        nurse.myShifts.add(shift2);

        Shift shift3 = new Shift();
        shift3.setDate(LocalDate.parse("2020-11-03"));
        shift3.setShiftType(shiftType);
        nurse.myShifts.add(shift3);

        Shift shift4 = new Shift();
        shift4.setDate(LocalDate.parse("2020-11-04"));
        shift4.setShiftType(shiftType);
        nurse.myShifts.add(shift4);

        long fail = calculator.maxWorkloadByDaysScore(nurse);
        System.out.println("desde la prueba " + fail);
    }

    @org.junit.jupiter.api.Test
    void maxWorkloadByDaysScorePositive() {
        Nurse nurse = new Nurse();
        ShiftType shiftType = new ShiftType();
        shiftType.setCodeLetter("V");

        Shift shift2 = new Shift();
        shift2.setDate(LocalDate.parse("2020-11-02"));
        shift2.setShiftType(shiftType);
        nurse.myShifts.add(shift2);

        Shift shift3 = new Shift();
        shift3.setDate(LocalDate.parse("2020-11-03"));
        shift3.setShiftType(shiftType);
        nurse.myShifts.add(shift3);

        Shift shift4 = new Shift();
        shift4.setDate(LocalDate.parse("2020-11-04"));
        shift4.setShiftType(shiftType);
        nurse.myShifts.add(shift4);

        long fail = calculator.maxWorkloadByDaysScore(nurse);
        System.out.println("desde la prueba " + fail);
    }

    @org.junit.jupiter.api.Test
    void maxDomainsByDaysScorePositive() {
        Set<String> domains = new HashSet<>();

        domains.add("Area 1");
        domains.add("Area 2");
        domains.add("Area 3");
        long fail = calculator.uniqueConcurrentDomains(domains);
        System.out.println("desde la prueba " + fail);
    }
}