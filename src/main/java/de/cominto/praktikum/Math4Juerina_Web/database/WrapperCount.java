package de.cominto.praktikum.Math4Juerina_Web.database;


import java.time.ZoneId;
import java.util.Date;

/**
 * auxiliary class for datanbank query
 */
public class WrapperCount {
    private boolean correct;
    private long tasks;
    private Long roundId;
    private Date day;

    public WrapperCount(boolean correct, long task){
        this(correct, task, 0, null);
    }

    public WrapperCount(boolean correct, long task, Date day){
        this(correct, task, 0, day);
    }
    public WrapperCount(boolean correct, long task, long roundId){
            this(correct, task, roundId,null);
        }

    public WrapperCount(boolean correct, long task, long roundId, Date day){
        this.correct = correct;
        this.tasks = task;
        this.roundId = roundId;
        if(day != null){
            this.day = Date.from(day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
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
                ", roundId=" + roundId +
                ", day=" + day +
                '}';
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }
}
