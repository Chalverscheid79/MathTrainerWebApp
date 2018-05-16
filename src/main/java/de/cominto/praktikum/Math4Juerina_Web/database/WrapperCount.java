package de.cominto.praktikum.Math4Juerina_Web.database;


import java.util.Date;

public class WrapperCount {
    private boolean correct;
    private long tasks;
    private Date day;

    public WrapperCount(boolean correct, long task){
        this(correct,task,null);
    }

    public WrapperCount(boolean correct, long task, Date day){
        this.correct = correct;
        this.tasks = task;
        this.day = day;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public long getTasks() {
        return tasks;
    }

    public void setTasks(long tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "WrapperCount{" +
                "correct=" + correct +
                ", tasks=" + tasks +
                '}';
    }
}
