package com.phlammariont.tangela.score;

import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class NurseRosteringEasyScoreCalculator implements EasyScoreCalculator<NurseRoster> {
    private static final int SHIFT_REGULAR_HOURS = 12;

    /**
     * A very simple implementation. The double loop can easily be removed by using Maps as shown in
     * {@link NurseRosteringEasyScoreCalculator#calculateScore(NurseRoster)}.
     */
    @Override
    public HardSoftScore calculateScore(NurseRoster nurseRoster) {
        int hardScore = 0;
        int softScore = 0;

        // Rules:
        for (Nurse nurse : nurseRoster.getNurseList()) {
            int hoursLabor = 0;
            boolean used = false;
            nurse.myShifts = new ArrayList<>();

            // Calculate usage
            for (Shift shift : nurseRoster.getShiftList()) {
                if (nurse.equals(shift.getNurse())) {
                    hoursLabor += shift.getRequiredHours();
                    used = true;
                    nurse.myShifts.add(shift);
                }
            }
            // Hard constraints
            // nurses just can take a shift a day
            hardScore -= moreThanOneShiftTodayScore(nurse);

            //no se puede mas de dos diarios seguidos
            hardScore -= moreThanTwoDaylyShiftsInARowScore(nurse);

            //Posturno no es descanzo
            //siempre que hago noche no trabajo el dia siguiente
            hardScore -= afterNightShiftScore(nurse);

            // no se puede mas de 10 días sin descanzo

            // totalizar por el roster final por enfermera a final de mes por hora y ajustar con el saldo anterior


            // Soft constraints
            if (used) {
                softScore -= nurse.getShiftCost();
            }
        }
        return HardSoftScore.valueOf(hardScore, softScore);
    }

    private long afterNightShiftScore(Nurse nurse) {
        Map<LocalDate, Shift> shiftByDate = getShiftsByDate(nurse);
        long fault = 0;
        for (Shift shift: nurse.getMyShifts()) {
            if ( hasAfterNightShift(shift, shiftByDate) ) {
                fault += SHIFT_REGULAR_HOURS;
            }
        }
        return fault;
    }

    private boolean hasAfterNightShift(Shift shift, Map<LocalDate, Shift> shiftByDate) {
        return isNightShift(shift)  && !getDaysInAdvanceShiftCode(shiftByDate, shift, 1L).equals("");
    }

    private boolean isNightShift(Shift shift) {
        return shift.getShiftType().getCodeLetter().equals("N");
    }

    private boolean isDailyShift(Shift shift) {
        return shift.getShiftType().getCodeLetter().equals("D");
    }

    private long moreThanTwoDaylyShiftsInARowScore(Nurse nurse) {
        Map<LocalDate, Shift> shiftByDate = getShiftsByDate(nurse);
        long fault = 0;
        for (Shift shift: nurse.getMyShifts()) {
            if (
                    isDailyShift(shift) &&
                        getDaysInAdvanceShiftCode(shiftByDate, shift, 1L).equals("D") &&
                        getDaysInAdvanceShiftCode(shiftByDate, shift, 2L).equals("D")
            ) {
                fault += SHIFT_REGULAR_HOURS;
            }
        }
        return fault;
    }

    private Map<LocalDate, Shift> getShiftsByDate(Nurse nurse) {
        return nurse.getMyShifts()
                .stream()
                .collect(
                        Collectors.toMap(
                                Shift::getDate,
                                shift -> shift,
                                (s, a) -> isDailyShift(s) ? s : a
                        )
                );
    }

    private String getDaysInAdvanceShiftCode(Map<LocalDate, Shift> shiftByDate, Shift shift, long daysInAdvance) {
        LocalDate today = shift.getDate();
        Shift twoDaysInAdvanceShift = shiftByDate.get(today.plusDays(daysInAdvance));
        return twoDaysInAdvanceShift != null ? twoDaysInAdvanceShift.getShiftType().getCodeLetter() : "";
    }

    // TODO @Leon esto se debe mejorar para que busque por fecha por tipo de shift y por repeticiones aisladas
    // [2,2,3,3,3] -> 3
    private long moreThanOneShiftTodayScore(Nurse nurse) {
        Set<String> uniques = new HashSet<String>();
        long shiftsToday = nurse.myShifts
                .stream()
                .filter( p -> !uniques.add( p.getDate().toString() ) )
                .count();
        if ( shiftsToday >= 1 ) return shiftsToday * SHIFT_REGULAR_HOURS;
        return 0;
    }
}
