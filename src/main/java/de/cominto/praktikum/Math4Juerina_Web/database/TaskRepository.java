package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends CrudRepository<Task, Long> {
	List <Task>findByRoundPlayerPlayerIdAndRoundDayOrderByTaskId (long player_id, Date date );
	List <Task> findByRoundRoundId(long roundId);
	
	//TODO Query im Template einbauen.
	@Query("select t from Task t"
			+ " inner join t.round r"
			+ " inner join r.player p"
			+ " where p.userName = :userName")
	List<Task> allRoundOfPlayerOnTable (@Param("userName") String userName);
	

}
