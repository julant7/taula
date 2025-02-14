package com.julant7.boardservice.dto;

public class CreateBoardRequest {
    private String name;

    private String description;

    public CreateBoardRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
