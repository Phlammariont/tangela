package com.phlammariont.tangela.service;

import com.phlammariont.tangela.domain.NurseRoster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseService {
    DaoService daoService;
    public FirebaseService() throws IOException {
        daoService = new DaoService();
        daoService.init();
    }

    public void saveNewBestSolution(NurseRoster newBestSolution) {
        Map<String, Object> shifts = new HashMap<>();
        shifts.put("first", "nuevo ");
        shifts.put("last", "shift");
        Map<String, Object> data = new HashMap<>();
        data.put("id", newBestSolution.getId());
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
        data.put("test", shifts);
        daoService.saveNewBestSolution(data);
    }
}
