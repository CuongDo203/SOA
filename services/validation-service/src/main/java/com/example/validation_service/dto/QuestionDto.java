package com.example.validation_service.dto;

public class QuestionDto{

    private String id;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answerKey;

    public String getConTent(){
        return this.content;
    }
    public String getOptA(){
        return this.optionA;
    }
    public String getOptB(){
        return this.optionB;
    }
    public String getOptC(){
        return this.optionC;
    }
    public String getOptD(){
        return this.optionD;
    }
    public String getAnswerKey(){
        return this.answerKey;
    }
}
