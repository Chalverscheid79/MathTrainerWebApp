package de.cominto.praktikum.Math4Juerina_Web.service;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * defines the methodnames of the service.class
 */
public interface MathServices {
	
	Player loadPlayer(String userName);
	
	Round getRound(int exercise, Player player);
	Round saveRound(Round round);
	Round findRoundById(long id);
	
	Task saveTask(Task task);
	Collection<Task>findByLastRoundAndDay(long userId, long roundId, Date date);

	List<Double> findAllTasksFromLastFiveRoundsInFrontAcutalRound(Round round, long playerId);
	
	
	double getCorrectPercent(long rundId);
	
	int getNumberOfErrors(long roundId);

	Double getPercentCorrectFromDateToLocalDate(long playerId, int priviousDays);
	List<Double> getCountAllTaskFromDateToDateGroupByDay(long playerId, int priviousDays);

}
