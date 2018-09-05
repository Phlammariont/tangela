package com.phlammariont.tangela.service;

import com.phlammariont.tangela.PlannerMessage;
import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

public class NurseRosterService {
    public void resolve(PlannerMessage data) {

        System.out.println("setting the planner");
        // Build the Solver
        SolverFactory<NurseRoster> solverFactory = SolverFactory.createFromXmlResource(
                "com.phlammariont.tangela/NurseRostering/solver/nurseRosteringSolverConfig.xml");
        Solver<NurseRoster> solver = solverFactory.buildSolver();

        System.out.println("building the problem for the planner");
        NurseRoster unsolvedNurseRoster = new NurseRosterMessage().createRoster(data);

        System.out.println("problem for the planner: ");
        System.out.println(toDisplayString(unsolvedNurseRoster));

        // Solve the problem
        System.out.println("solving");
        NurseRoster solvedNurseRoster = solver.solve(unsolvedNurseRoster);

        // Display the result
        System.out.println("solving finish " +
            "\nSolved roster with nurses and shifts:\n"
            + toDisplayString(solvedNurseRoster));
    }

    public static String toDisplayString(NurseRoster nurseRoster) {
        StringBuilder displayString = new StringBuilder();
        for (Shift shift : nurseRoster.getShiftList()) {
            Nurse nurse = shift.getNurse();
            displayString.append("  ")
                    .append(shift.getLabel())
                    .append(" -> ")
                    .append(nurse == null ? null : nurse.getLabel()).append("\n");
        }
        return displayString.toString();
    }
}
