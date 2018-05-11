package de.cominto.praktikum.Math4Juerina_Web.web;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;

 

/**
 * Wrapper class for HttpSession
 * 
 * @author halverscheid
 *
 */
@Component
public class MathSession {
	
	private static final String PLAYER = "player";
	private static final String TASK = "task";
	private static final String ROUND = "round";
	private static final String SOLUTION = "solution";
	
	@Autowired
	private HttpSession session;
	
	/**
	 * Get the Player object from session
	 * @return object. Can return null
	 */
	public Player getPlayer() {
		
		return (Player)this.session.getAttribute(PLAYER);
	}
	
	/**
	 * Get the Task object from session
	 * @return object. Can return null.
	 */
	public Task getTask() {
		
		return (Task)this.session.getAttribute(TASK);
	}
	
	/**
	 * Get the round object from the session.
	 * 
	 * @return The round object. Can return null.
	 */
	public Round getRound() {
	
		return (Round)this.session.getAttribute(ROUND);
	}
	
	/**
	 * set a object of Player in session
	 * 
	 * @param player
	 */
	public void setPlayer (Player player) {
		session.setAttribute(PLAYER, player);
	}
	
	/**
	 * set a object of Round in session
	 * 
	 * @param round
	 */
	public void setRound (Round round) {
		session.setAttribute(ROUND, round);
	}
	
	/**
	 * set a object of Task in session
	 * 
	 * @param task
	 */
	public void setTask (Task task) {
		session.setAttribute(TASK, task);
	}
	
	/**
	 * remove the Player object from session
	 * 
	 * @return the last player object. Can return null
	 */
	public Player removePlayer() {
		Player player = getPlayer();
		session.removeAttribute(PLAYER);
		return player;
	}
	
	/**
	 * remove the Round object from Session
	 * 
	 * @return the last round object. Can return null
	 */
	public Round removeRound() {
		Round round = getRound();
		session.removeAttribute(ROUND);
		return round;
	}
	
	/**
	 * remove the Task object from session
	 * 
	 * @return the last Task object. Can return null.
	 */
	public Task removeTask() {
		Task task = getTask();
		session.removeAttribute(TASK);
		return task;
	}
	
	/**
	 * remove the solution Boolean from session
	 * 
	 * @return the last solution boolean
	 */
	public Boolean removeSolution() {
		Boolean isCorrect = isCorrect();
		session.removeAttribute(SOLUTION);
		return isCorrect;
	}
	
	/**
	 * removes all object (attributes) from session
	 */
	public void clear() {
		Enumeration<String> enumeration = session.getAttributeNames(); 
		while (enumeration.hasMoreElements()) {
			session.removeAttribute(enumeration.nextElement());
		}
	}
	
	/**
	 * Check if a round is in the object
	 * 
	 * @return boolean (true or false)
	 */
	public boolean hasRound() {
		return session.getAttribute(ROUND) != null;
	}
	
	/**
	 * Get the Boolean from session
	 * 
	 * @return Boolean. Can return null
	 */
	public Boolean isCorrect() {
		
		return (Boolean) session.getAttribute(SOLUTION);
	}
	
	/**
	 * 
	 * @param solution
	 */
	public void setCorrect(boolean solution) {
		
		session.setAttribute(SOLUTION ,solution);
	}
	

	/**
	 * Check if a round is in the object
	 * 
	 * @return boolean
	 */
	public boolean hasTask() {
		return session.getAttribute(TASK) != null;
	}

}
