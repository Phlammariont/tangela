package com.phlammariont.tangela.service;

import com.phlammariont.tangela.PlannerMessage;

import java.io.IOException;

public class MainService {
    public void startPlanner(PlannerMessage data) {
        System.out.println("starting planner for: ");
        System.out.println(data.getName());
    }

    public void logLogin(String message) {
        DaoService daoService = new DaoService();
        try {
            daoService.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
