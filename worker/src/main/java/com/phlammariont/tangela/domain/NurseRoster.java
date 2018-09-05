package com.phlammariont.tangela.domain;

import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class NurseRoster {
    private List<Shift> shiftList;
    private String id;
    private List<Nurse> nurseList;
    private HardSoftScore score;


    @PlanningEntityCollectionProperty
    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNurseList(List<Nurse> nurseList) {
        this.nurseList = nurseList;
    }

    @ValueRangeProvider(id = "nurseRange")
    @ProblemFactCollectionProperty
    public List<Nurse> getNurseList() {
        return nurseList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
