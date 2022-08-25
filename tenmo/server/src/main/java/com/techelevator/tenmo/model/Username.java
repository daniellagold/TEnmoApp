package com.techelevator.tenmo.model;

public class Username {

    private int id;
    private String username;

    public Username(){

    }
    public  Username (String username){
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
