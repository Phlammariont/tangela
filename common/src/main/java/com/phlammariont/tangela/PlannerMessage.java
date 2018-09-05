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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MessageNurse> getUsers() {
        return users;
    }

    public void setUsers(List<MessageNurse> users) {
        this.users = users;
    }

    public MessageTimeLapse getTimeLapse() {
        return timeLapse;
    }

    public void setTimeLapse(MessageTimeLapse timeLapse) {
        this.timeLapse = timeLapse;
    }

    public List<MessageShift> getShifts() {
        return shifts;
    }

    public void setShifts(List<MessageShift> shifts) {
        this.shifts = shifts;
    }

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