package de.cominto.praktikum.Math4Juerina_Web.web;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;

 

/**
 * Wrapper class for HttpSession
 * is the link between the presetation-, business and dataset
 * @author halverscheid
 *
 */
@Component
public class MathSession {
	/**
	 * todo
	 */
	public static final String TASK = "task";
	public static final String ROUND = "round";
	public static final String SOLUTION = "solution";
	
	private HttpSession httpSession;

	public MathSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	/**
	 * Get the Task object from httpSession
	 * @return object. Can return null.
	 */
	public Task getTask() {
		
		return (Task)this.httpSession.getAttribute(TASK);
	}
	
	/**
	 * Get the round object from the httpSession.
	 * 
	 * @return The round object. Can return null.
	 */
	public Round getRound() {
	
		return (Round)this.httpSession.getAttribute(ROUND);
	}
	
	/**
	 * set a object of Round in httpSession
	 * 
	 * @param round
	 */
	public void setRound (Round round) {
		httpSession.setAttribute(ROUND, round);
	}
	
	/**
	 * set a object of Task in httpSession
	 * 
	 * @param task
	 */
	public void setTask (Task task) {
		httpSession.setAttribute(TASK, task);
	}
	
	/**
	 * remove the Round object from Session
	 * 
	 * @return the last round object. Can return null
	 */
	public Round removeRound() {
		Round round = getRound();
		httpSession.removeAttribute(ROUND);
		return round;
	}
	
	/**
	 * remove the Task object from httpSession
	 * 
	 * @return the last Task object. Can return null.
	 */
	public Task removeTask() {
		Task task = getTask();
		httpSession.removeAttribute(TASK);
		return task;
	}
	
	/**
	 * remove the solution Boolean from httpSession
	 * 
	 * @return the last solution boolean
	 */
	public Boolean removeSolution() {
		Boolean isCorrect = isCorrect();
		httpSession.removeAttribute(SOLUTION);
		return isCorrect;
	}
	
	/**
	 * removes all object (attributes) from httpSession
	 */
	public void clear() {
		Enumeration<String> enumeration = httpSession.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			httpSession.removeAttribute(enumeration.nextElement());
		}
	}
	
	/**
	 * Check if a round is in the object
	 * 
	 * @return boolean (true or false)
	 */
	public boolean hasRound() {
		return httpSession.getAttribute(ROUND) != null;
	}
	
	/**
	 * Get the Boolean from httpSession
	 * 
	 * @return Boolean. Can return null
	 */
	public Boolean isCorrect() {
		
		return (Boolean) httpSession.getAttribute(SOLUTION);
	}
	
	/**
	 * 
	 * @param solution
	 */
	public void setCorrect(boolean solution) {
		
		httpSession.setAttribute(SOLUTION ,solution);
	}
	

	/**
	 * Check if a task is in the object
	 * 
	 * @return boolean
	 */
	public boolean hasTask() {
		return httpSession.getAttribute(TASK) != null;
	}

}
