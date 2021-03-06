package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * definition of spring data queries from the entity Player
 */
public interface PlayerRepository extends CrudRepository <Player, Long> {
	
	List<Player> findByUserName(String userName);
	Player findFirstByUserName (String userName);

}
