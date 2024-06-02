package com.example.user_app;

public class servicesItemValues {
    String servicesName;
    String servicesDescription;

    public servicesItemValues(String servicesName, String servicesDescription) {
        this.servicesName = servicesName;
        this.servicesDescription = servicesDescription;
    }

    public String getServicesName() {
        return servicesName;
    }

    public String getServicesDescription() {
        return servicesDescription;
    }
}
