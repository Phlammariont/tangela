package com.phlammariont.tangela.service;

import com.phlammariont.tangela.PlannerMessage;
import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.Nurse;
import com.phlammariont.tangela.model.Shift;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import java.io.IOException;

public class NurseRosterService {
    FirebaseService firebaseService;


    public NurseRosterService() throws IOException {
        firebaseService = new FirebaseService();
    }

    public void resolve(PlannerMessage data) throws IOException {


        System.out.println("setting the planner");
        // Build the Solver
        SolverFactory<NurseRoster> solverFactory = SolverFactory.createFromXmlResource(
                "com.phlammariont.tangela/NurseRostering/solver/nurseRosteringSolverConfig.xml");
        Solver<NurseRoster> solver = solverFactory.buildSolver();

        System.out.println("building the problem for the planner");
        NurseRoster unsolvedNurseRoster = new NurseRosterMessage().createRoster(data);

        System.out.println("problem for the planner: ");
        System.out.println(toDisplayString(unsolvedNurseRoster));

        solver.addEventListener(new SolverEventListener<NurseRoster>() {
            public void bestSolutionChanged(BestSolutionChangedEvent<NurseRoster> event) {
                // Ignore infeasible (including uninitialized) solutions
                if (event.getNewBestSolution().getScore().isFeasible()) {
                    System.out.println("Best Solution Found: \n" +
                        toDisplayString( event.getNewBestSolution() ) );
                    firebaseService.saveNewBestSolution( event.getNewBestSolution() );
                }
            }
        });

        // Solve the problem
        System.out.println("solving");
        NurseRoster solvedNurseRoster = solver.solve(unsolvedNurseRoster);

        // Display the result
        System.out.println("solving finish " +
            "\nSolved roster with nurses and shifts:\n"
            + toDisplayString(solvedNurseRoster));

        firebaseService.saveBestSolution( solvedNurseRoster );
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
