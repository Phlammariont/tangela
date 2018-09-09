package com.phlammariont.tangela.service;

import com.phlammariont.tangela.PlannerMessage;

import java.io.IOException;

public class MainService {
    NurseRosterService nurseRoster;

    public MainService() throws IOException {
        nurseRoster = new NurseRosterService();;
    }

    public void startPlanner(PlannerMessage data) throws IOException {
        System.out.println("starting planner for: ");
        System.out.println(data.getName());

        nurseRoster.resolve(data);
    }
}
