package de.cominto.praktikum.Math4Juerina_Web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;

class RoundTest {

	Player player = new Player( "developer");
	
	@Test
	public void getTaskTest() {
		Round r = new Round(1, player);
		Task t = r.createTask();
		assertEquals("+", t.getEnumOperator().getOperator());
		r.setFactoryDecision(1);
		t=r.createTask();
		assertEquals("-", t.getEnumOperator().getOperator());
		r.setFactoryDecision(2);
		t=r.createTask();
		assertEquals("-", t.getEnumOperator().getOperator());
		r.setFactoryDecision(5);
		t=r.createTask();
		assertEquals("+", t.getEnumOperator().getOperator());
	}
	
	@Test
	public void checkResultTest() {
		Round r = new Round(2, player);
		Task t = r.createTask();
		t.setX(5);
		t.setY(5);
		assertFalse(t.checkResult());
		t.setSuggestedSolution(10);
		assertTrue(t.checkResult());
		assertTrue(t.checkResult(10));
		assertFalse(t.checkResult(9));
		assertEquals("+", t.getEnumOperator().getOperator());
		r.setFactoryDecision(2);
		t=r.createTask();
		t.setX(5);
		t.setY(4);
		assertEquals("-", t.getEnumOperator().getOperator());
		assertTrue(t.checkResult(1));
		
	}
	
	@Test
	public void getYTest() {
		Round r = new Round(1, player);
		Task t = r.createTask();
		t.setY(5);
		assertEquals(5, t.getY());
		
	}
	@Test
	public void getWrongSolutionTest() {
		Round r = new Round(1, player);
		r.addWrongSolution();
		assertEquals(1, r.getWrongSolution());
		
	}
	
	@Test
	public void idRoundOffTest() {
		Round r = new Round(1, player);
		Task t = r.createTask();
		r.setFactoryDecision(1);
		assertFalse(r.isRoundOff());
		r.setFactoryDecision(2);
		assertTrue(r.isRoundOff());
		
	}
	
	@Test
	public void getStopMillisTest() {
		Round r = new Round(1, player);
		Task t = r.createTask();
		assertEquals(((r.getExercise()*10000)+r.getStartMillis()), r.getStopMillis());
	}

}
