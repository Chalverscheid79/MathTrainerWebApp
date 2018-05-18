package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends CrudRepository<Task, Long>, JpaTaskRepository {
	List <Task>findByRoundPlayerPlayerIdAndRoundDayOrderByTaskId (long player_id, Date date );
	List <Task> findByRoundRoundId(long roundId);
	
	//TODO Query im Template einbauen.
	@Query("select t from Task t"
			+ " inner join t.round r"
			+ " inner join r.player p"
			+ " where p.userName = :userName")
	List<Task> allRoundOfPlayerOnTable (@Param("userName") String userName);

	/**
	 *
	 * @param playerId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@Query("select new de.cominto.praktikum.Math4Juerina_Web.database.WrapperCount(t.correct, count(t.taskId)) from Task t" +
			" join t.round r" +
			" join r.player p" +
			" where p.playerId = :playerId" +
			" and t.practiceDay >= :fromDate and t.practiceDay   < :toDate " +
			" group by t.correct")
	List <WrapperCount> countAllTaskFromDateToDate (@Param("playerId") long playerId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("select new de.cominto.praktikum.Math4Juerina_Web.database.WrapperCount(t.correct, count(t.taskId),min(t.practiceDay)) from Task t" +
				" join t.round r" +
				" join r.player p" +
				" where p.playerId = :playerId" +
				" and t.practiceDay >= :fromDate and t.practiceDay   < :toDate" +
				" group by substring(t.practiceDay,1,10),t.correct")
		List <WrapperCount> countAllTaskFromDateToDateGroupByDay(@Param("playerId") long playerId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

@Query("select new de.cominto.praktikum.Math4Juerina_Web.database.WrapperCount(t.correct, count(t.taskId),min(t.practiceDay)) from Task t" +
				" join t.round r" +
				" join r.player p" +
				" where p.playerId = :playerId" +
				" and t.practiceDay > :fromDate and t.practiceDay   < :toDate" +
				" and t.round.roundId < :roundId"+
				" group by t.round" +
				" order by t.round desc")
		List <WrapperCount> countAllTaskFromDayAndGroupBOrderByRoundOrderByRoundDescLimitFive(@Param("playerId") long playerId,@Param("roundId") long roundId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);




}
