package de.cominto.praktikum.Math4Juerina_Web.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * definition of spring data queries from the entity Task
 */
public interface TaskRepository extends CrudRepository<Task, Long>, JpaTaskRepository {
	List <Task>findByRoundPlayerPlayerIdAndRoundDayOrderByTaskId (long player_id, Date date );
	List <Task> findByRoundRoundId(long roundId);
	

	/**
	 * The select from database starts with an construktor call, the used qurey here Springdata query language
	 * @param playerId long from Player entity
	 * @param fromDate date from java.util.Date
	 * @param toDate date from java.util.Date
	 * @return list from WrapperCount, can be null
	 */
	@Query("select new de.cominto.praktikum.Math4Juerina_Web.database.WrapperCount(t.correct, count(t.taskId)) from Task t" +
			" join t.round r" +
			" join r.player p" +
			" where p.playerId = :playerId" +
			" and t.practiceDay >= :fromDate and t.practiceDay   < :toDate " +
			" group by t.correct")
	List <WrapperCount> countAllTaskFromDateToDate (@Param("playerId") long playerId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	/**
	 * The select from database starts with an construktor call, the used qurey here Springdata query language
	 * @param playerId long from Player entity
	 * @param fromDate date from java.util.Date
	 * @param toDate date from java.util.Date
	 * @return list from WrapperCount, can be null
	 */
	@Query("select new de.cominto.praktikum.Math4Juerina_Web.database.WrapperCount(t.correct, count(t.taskId),min(t.practiceDay)) from Task t" +
				" join t.round r" +
				" join r.player p" +
				" where p.playerId = :playerId" +
				" and t.practiceDay >= :fromDate and t.practiceDay   < :toDate" +
				" group by substring(t.practiceDay,1,10),t.correct" +
				" order by substring(t.practiceDay,1,10) asc")
		List <WrapperCount> countAllTaskFromDateToDateGroupByDay(@Param("playerId") long playerId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	/**
	 * The select from database starts with an construktor call, the used qurey here Springdata query language
	 * @param playerId long from Player entity
	 * @param roundId long from RoundId
	 * @param fromDate date from java.util.Date
	 * @param toDate date from java.util.Date
	 * @return list from WrapperCount, can be null
	 */
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
