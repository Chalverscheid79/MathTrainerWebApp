package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.Date;
import java.util.List;

public interface JpaTaskRepository {
    List<Task> findAllTasks ();
    List<WrapperCount>findAllTasksFromLastFiveRoundsInfrintAcutalRound(long roundId, Date fromDate, Date toDate);
}
