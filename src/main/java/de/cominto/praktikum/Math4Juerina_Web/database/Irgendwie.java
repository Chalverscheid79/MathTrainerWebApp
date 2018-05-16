package de.cominto.praktikum.Math4Juerina_Web.database;


public class Irgendwie {
    private boolean correct;
    private long tasks;

    public Irgendwie( boolean correct, long task){
        this.correct = correct;
        this.tasks = task;
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
        return "Irgendwie{" +
                "correct=" + correct +
                ", tasks=" + tasks +
                '}';
    }
}
