package com.phlammariont.tangela.model;

import java.util.List;

public class MessageNurse {
    private String id;
    private List<Service> services;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
