package com.bcsa.planningpokeradmin;

public class classQuestion {

    private String Question;
    private boolean State;
    private String Group;

    public classQuestion(String question,boolean state) {
        this.Question = question;
        this.State = state;
    }
    public classQuestion() {}

    public void setState(boolean state) {
        State = state;
    }

    public boolean getState(){
        return State;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getQuestion() {
        return Question;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getGroup() {
        return Group;
    }


}