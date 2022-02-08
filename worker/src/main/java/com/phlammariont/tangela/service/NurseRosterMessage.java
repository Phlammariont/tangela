package com.phlammariont.tangela.service;

import com.phlammariont.tangela.PlannerMessage;
import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NurseRosterMessage {
    private static final String VICE = "V";
    private static final String PRO = "P";
    private static final String BOSS = "B";
    private static final String ASIST = "A";

    public NurseRoster createRoster(PlannerMessage data) {
        NurseRoster result = new NurseRoster();
        // set Id:
        result.setId(data.getId());

        List<Nurse> nurses = new ArrayList<>();
        Nurse nurse;
        // create the nurses on roster:

        for (MessageNurse user : data.getUsers()) {
            nurse = new Nurse();
            nurse.setId(user.getId());
            nurse.setRole(user.getRole().getCode());
            nurse.setDomain(user.getServices().get(0).getId());
            nurse.setShiftCost(roleToRank(user.getRole().getCode()) * 5);

            nurses.add(nurse);
        }
        result.setNurseList(nurses);

        List<Shift> shifts = new ArrayList<>();
        Shift shift;
        ShiftType shiftType;
        // create the shifts on roster:
        for (MessageShift messageShift : data.getShifts()) {
            shift = new Shift();
            shift.setId(messageShift.getId());
            shift.setDate(LocalDate.parse(messageShift.getDate()));
            shift.setBuilding(messageShift.getBuilding());
            shift.setFloor(messageShift.getFloor());

            shiftType = new ShiftType();
            shiftType.setCodeLetter(messageShift.getShiftType().getCode());
            shift.setShiftType(shiftType);

            shifts.add(shift);
        }
        result.setShiftList(shifts);


        return result;
    }

    private Integer roleToRank (String role) {
        if (VICE.equals(role)) return 1;
        if (PRO.equals(role)) return 2;
        if (BOSS.equals(role)) return 3;
        if (ASIST.equals(role)) return 4;
        return 0;
    }
}
