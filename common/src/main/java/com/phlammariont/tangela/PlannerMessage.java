package com.phlammariont.tangela;

import com.phlammariont.tangela.model.MessageNurse;
import com.phlammariont.tangela.model.MessageShift;
import com.phlammariont.tangela.model.MessageTimeLapse;

import java.io.Serializable;
import java.util.List;

/**
 * A model class for a big, imaginary, expensive operation
 * that a user submits via the web, but is processed async
 * by a worker.
 */
public class PlannerMessage implements Serializable {

    private String id;
    private String name;
    private List<MessageNurse> users;
    private MessageTimeLapse timeLapse;
    private List<MessageShift> shifts;

    public PlannerMessage() {
    }

    public PlannerMessage(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PlannerMessage{" +
                "name='" + name + '\'' +
                "users=" + toStringNurseList(users) +
                "timeLapse=" + timeLapse.toString() +
                "shifts=" + toStringShiftList(shifts) +
                '}';
    }

    public String toStringNurseList(List<MessageNurse> list) {
        String result = "";
        for (MessageNurse item:list) {
            result += item.getId() + ", ";
        }
        return result + " | ";
    }

    public String toStringShiftList(List<MessageShift> list) {
        String result = "";
        for (MessageShift item:list) {
            result += item.getId() + ", ";
        }
        return result + " | ";
    }
}