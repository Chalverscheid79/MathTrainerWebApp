package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.Date;
import java.util.List;

/**
 * definition of the methods in
 *
 * @author halverscheid
 */
public interface JpaTaskRepository {
    List<Task> findAllTasks ();
    List<WrapperCount> findAllTasksFromLastFiveRoundsWithoutAcutalRound(Round round, Date fromDate, Date toDate);
}
