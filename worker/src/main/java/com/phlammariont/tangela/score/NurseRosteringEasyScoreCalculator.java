package com.phlammariont.tangela.score;

import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
                    // System.out.println("nurse Id: " + nurse.getId() + " shift date: " + shift.getDate());
                }
            }
            // Hard constraints
            // nurses just can take a shift a day
            long shiftsToday =  moreThanOneShiftToday(nurse);
            if ( shiftsToday >= 1 ) hardScore += -1 * shiftsToday * SHIFT_REGULAR_HOURS;


            // Soft constraints
            if (used) {
                softScore -= nurse.getShiftCost();
            }
        }
        return HardSoftScore.valueOf(hardScore, softScore);
    }

    // TODO @Leon esto se debe mejorar para que busque por fecha por tipo de shift y por repeticiones aisladas
    // [2,2,3,3,3] -> 3
    private long moreThanOneShiftToday(Nurse nurse) {
        Set<String> uniques = new HashSet<String>();
        return nurse.myShifts
                .stream()
                .filter( p -> !uniques.add( p.getDate().toString() ) )
                .count();
    }
}
