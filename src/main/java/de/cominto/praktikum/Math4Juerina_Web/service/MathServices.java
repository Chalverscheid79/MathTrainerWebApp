package de.cominto.praktikum.Math4Juerina_Web.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;

public interface MathServices {
	
	Player loadPlayer(String userName);
	
	Round getRound(int exercise, Player player);
	Round saveRound(Round round);
	Round findRoundById(long id);
	
	Task saveTask(Task task);
	Collection<Task>findByLastRoundAndDay(long userId, long roundId, Date date);
	
	
	double getCorrectPercent(long rundId);
	
	int getNumberOfErrors(long roundId);
}
