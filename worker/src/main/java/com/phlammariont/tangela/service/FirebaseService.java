package com.phlammariont.tangela.service;

import com.phlammariont.tangela.domain.NurseRoster;
import com.phlammariont.tangela.model.Shift;

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
        Map<String, Object> data = this.buildMessage(newBestSolution);
        daoService.saveNewBestSolution(data);
    }

    public void saveBestSolution(NurseRoster solvedNurseRoster) {
        Map<String, Object> data = this.buildMessage(solvedNurseRoster);
        daoService.saveBestSolution(data);
    }

    private Map<String, Object> buildMessage(NurseRoster nurseRoster) {
        Map<String, Object> shifts = new HashMap<>();
        Map<String, Object> shiftMap;
        for (Shift shift: nurseRoster.getShiftList()) {
            shiftMap = new HashMap<>();
            shiftMap.put("id", shift.getId());
            shiftMap.put("nurseId", shift.getNurse().getId());
            shifts.put(shift.getId(), shiftMap);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", nurseRoster.getId());
        data.put("shifts", shifts);

        return data;
    }
}
