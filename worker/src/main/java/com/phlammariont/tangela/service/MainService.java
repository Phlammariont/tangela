package com.phlammariont.tangela.service;

import com.phlammariont.tangela.BigOperation;

public class MainService {
    public void startPlanner(BigOperation data) {
        System.out.println("starting planner for: ");
        System.out.println(data.getName());
    }
}
