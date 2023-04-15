package com.example.lobby;

public class roomCard {
    private String userName;
    private String playerTwo;
    private int roomID;
    private String gameType;

    public roomCard(String userName, String gameType, String playerTwo, int roomID) {
        this.userName = userName;
        this.gameType = gameType;
        this.playerTwo = playerTwo;
        this.roomID = roomID;
    }
    public String getUserName()
    {
        return userName;
    }
    public int getRoomID()
    {
        return roomID;
    }
}
