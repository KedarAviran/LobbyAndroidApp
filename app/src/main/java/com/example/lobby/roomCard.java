package com.example.lobby;

public class roomCard {
    private String userName;
    private String gameType;
    private boolean isFull;

    public roomCard(String userName, String gameType, boolean isFull) {
        this.userName = userName;
        this.gameType = gameType;
        this.isFull = isFull;
    }
    public String getUserName()
    {
        return userName;
    }
}
