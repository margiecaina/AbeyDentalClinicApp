package com.example.loginexperiment;

public class GlobalVariables {
    private static GlobalVariables instance;
    private boolean globalVariable;

    private GlobalVariables() {
        // Private constructor to prevent instantiation
    }

    public static synchronized GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public boolean getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(boolean globalVariable) {
        this.globalVariable = globalVariable;
    }
}

