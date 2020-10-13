package com.phlammariont.tangela.score;

import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class NurseRosteringEasyScoreCalculator implements EasyScoreCalculator<NurseRoster> {
    private static final int SHIFT_REGULAR_HOURS = 8;
    private static final int MAX_WORK_LOAD = 7;
    private static final int IDEAL_WORK_LOAD = 4;
    private static final int MIN_REST_LAPSE = 3;
    private static final String NIGHT_CODE = "N";
    private static final String DAY_CODE = "D";

    private static final String VICE = "V";
    private static final String PRO = "P";
    private static final String BOSS = "B";
    private static final String ASIST = "A";

    /**
     * A very simple implementation. The double loop can easily be removed by using Maps as shown in
     * {@link NurseRosteringEasyScoreCalculator#calculateScore(NurseRoster)}.
     */
    @Override
    public HardSoftScore calculateScore(NurseRoster nurseRoster) {
        Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());

        int hardScore = 0;
        int softScore = 0;

        Set<Integer> usedBuildingsCosts = new HashSet<>();
        Set<Integer> usedFloorCosts = new HashSet<>();
        // Rules:
        for (Nurse nurse : nurseRoster.getNurseList()) {
            boolean used = false;
            nurse.myShifts = new ArrayList<>();

            // Calculate usage
            for (Shift shift : nurseRoster.getShiftList()) {
                if (nurse.equals(shift.getNurse())) {
                    used = true;
                    nurse.myShifts.add(shift);
                }
                usedBuildingsCosts.add(shift.getBuilding().getCost());
                usedFloorCosts.add(shift.getFloor().getCost());
            }
            // Hard constraints
            // nurses just can take a shift a day
            hardScore -= moreThanOneShiftTodayScore(nurse);

            //no se puede trabajar en un puesto de menor rank
            hardScore -= correctRankBalance(nurse);

            //Posturno no es descanzo
            //siempre que hago noche no trabajo el dia siguiente
            //hardScore -= afterNightShiftScore(nurse);

            // no se puede mas de 7 días sin descanzo
            hardScore -= maxWorkloadByDaysScore(nurse);

            // totalizar por el roster final por enfermera a final de mes por hora y ajustar con el saldo anterior


            // Soft constraints
            if (used) {
                softScore -= nurse.getShiftCost();
            }
        }

        softScore -= calculateBuildingCost(usedBuildingsCosts);

        softScore -= calculateFloorCost(usedFloorCosts);

        Timestamp stopTimestamp = new Timestamp(System.currentTimeMillis());
        nurseRoster.setPerfMillis(stopTimestamp.getTime() - startTimestamp.getTime());
        return HardSoftScore.valueOf(hardScore, softScore);
    }

    private int calculateFloorCost (Set<Integer> usedFloorCosts) {
        return usedFloorCosts.stream().mapToInt(Integer::intValue).sum() * 5;
    }

    private int calculateBuildingCost(Set<Integer> usedBuildingsCosts) {
        return usedBuildingsCosts.stream().mapToInt(Integer::intValue).sum() * 10;
    }

    // TODO @Leon esto se debe mejorar para que busque por fecha por tipo de shift y por repeticiones aisladas
    // [2,2,3,3,3] -> 3
    private long moreThanOneShiftTodayScore(Nurse nurse) {
        Set<String> uniques = new HashSet<>();
        long shiftsToday = nurse.myShifts
                .stream()
                .filter( p -> !uniques.add( p.getDate().toString() ) )
                .count();
        if ( shiftsToday >= 1 ) return shiftsToday * SHIFT_REGULAR_HOURS;
        return 0;
    }

    private long correctRankBalance(Nurse nurse) {
        long fault = 0;
        for (Shift shift: nurse.getMyShifts()) {
            if (incompatibleShift(shift, nurse.getRole())) {
                fault += SHIFT_REGULAR_HOURS * 5;
            }
        }
        return fault;
    }



    public long maxWorkloadByDaysScore(Nurse nurse) {
        Map<LocalDate, Shift> shiftByDate = getShiftsByDate(nurse);
        long fault = IDEAL_WORK_LOAD * SHIFT_REGULAR_HOURS;
        List<String> lapse;
        for (Shift shift: nurse.getMyShifts()) {
            lapse = new ArrayList<>();
            for (long i = -IDEAL_WORK_LOAD; i <= IDEAL_WORK_LOAD; i++) {
                lapse.add(getDaysInAdvanceShiftCode(shiftByDate, shift, i));
            }

            long shiftsOnPeriod = sumByCode(lapse);
            fault -= shiftsOnPeriod == 4 ? (SHIFT_REGULAR_HOURS + 1) : 0;
            fault += Math.abs(shiftsOnPeriod - (MAX_WORK_LOAD - MIN_REST_LAPSE) ) * SHIFT_REGULAR_HOURS;
        }
        return fault;
    }

    private int sumByCode(List<String> lapse) {
        int counter = 0;
        for (String code : lapse) {
            counter += "".equals(code) ? 0 : 1;
        }
        return counter;
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
        return shift.getShiftType().getCodeLetter().equals(NIGHT_CODE);
    }

    private boolean isDailyShift(Shift shift) {
        return shift.getShiftType().getCodeLetter().equals(DAY_CODE);
    }

    private boolean incompatibleShift (Shift shift, String userRole) {
        Integer userRank = roleToRank(userRole);
        Integer shiftRank = roleToRank(shift.getShiftType().getCodeLetter());
        return userRank > shiftRank;
    }

    private long moreThanTwoDaylyShiftsInARowScore(Nurse nurse) {
        Map<LocalDate, Shift> shiftByDate = getShiftsByDate(nurse);
        long fault = 0;
        for (Shift shift: nurse.getMyShifts()) {
            if (
                    isDailyShift(shift) &&
                        getDaysInAdvanceShiftCode(shiftByDate, shift, 1L).equals(DAY_CODE) &&
                        getDaysInAdvanceShiftCode(shiftByDate, shift, 2L).equals(DAY_CODE)
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
                                (s, a) -> a
                        )
                );
    }

    private String getDaysInAdvanceShiftCode(Map<LocalDate, Shift> shiftByDate, Shift shift, long daysInAdvance) {
        LocalDate today = shift.getDate();
        Shift twoDaysInAdvanceShift = shiftByDate.get(today.plusDays(daysInAdvance));
        return twoDaysInAdvanceShift != null ? twoDaysInAdvanceShift.getShiftType().getCodeLetter() : "";
    }

    private Integer roleToRank (String role) {
        if (VICE.equals(role)) return 5;
        if (PRO.equals(role)) return 4;
        if (BOSS.equals(role)) return 3;
        if (ASIST.equals(role)) return 2;
        return 0;
    }
}
