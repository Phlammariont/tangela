package com.phlammariont.tangela.service;

import com.phlammariont.tangela.PlannerMessage;
import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NurseRosterMessage {
    public NurseRoster createRoster(PlannerMessage data) {
        NurseRoster result = new NurseRoster();
        // set Id:
        result.setId(data.getId());

        List<Nurse> nurses = new ArrayList<Nurse>();
        Nurse nurse;
        // create the nurses on roster:
        for (MessageNurse user : data.getUsers()) {
            nurse = new Nurse();
            nurse.setId(user.getId());

            nurses.add(nurse);
        }
        result.setNurseList(nurses);

        List<Shift> shifts = new ArrayList<Shift>();
        Shift shift;
        ShiftType shiftType;
        // create the shifts on roster:
        for (MessageShift messageShift : data.getShifts()) {
            shift = new Shift();
            shift.setId(messageShift.getId());
            shift.setDate(LocalDate.parse(messageShift.getDate()));

            shiftType = new ShiftType();
            shiftType.setCodeLetter(messageShift.getShiftType().getCode());
            shift.setShiftType(shiftType);

            shifts.add(shift);
        }
        result.setShiftList(shifts);


        return result;
    }
}
