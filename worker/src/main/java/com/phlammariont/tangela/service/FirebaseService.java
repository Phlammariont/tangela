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
            if(shift.getNurse() != null) {
                shiftMap = new HashMap<>();
                shiftMap.put("id", shift.getId());
                shiftMap.put("userId", shift.getNurse().getId());
                shiftMap.put("date", shift.getDate().toString());
                shiftMap.put("type", shift.getShiftType().getCodeLetter());
                shiftMap.put("userRole", shift.getNurse().getRole());
                shiftMap.put("userDomain", shift.getNurse().getDomain());
                shiftMap.put("florName", shift.getFloor().getName());
                shiftMap.put("buildingName", shift.getBuilding().getName());
                shiftMap.put("buildingId", shift.getBuilding().getId());
                shiftMap.put("floorId", shift.getFloor().getId());
                shifts.put(shift.getId(), shiftMap);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", nurseRoster.getId());
        data.put("performance", "" + nurseRoster.getPerfMillis());
        data.put("shifts", shifts);

        return data;
    }
}
