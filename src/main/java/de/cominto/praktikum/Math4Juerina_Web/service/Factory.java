package de.cominto.praktikum.Math4Juerina_Web.service;

import de.cominto.praktikum.Math4Juerina_Web.database.Task;

public class Factory {

	/**
	 * creates a task
	 */
	private Factory() {
	}
	
	public static Task buildTask(int i) {
		Task task = new Task();
		java.util.Random rand = new java.util.Random();
		
		
		
		if (i % 3 == 0) {
			if (i % 2 == 0) {
				task.setEnumOperator(EnumOperatorImpl.ADD);
				task.setX((rand.nextInt(9) + 1) * 10);
				task.setY((rand.nextInt((10 - task.getX() / 10)) + 1) * 10);
			} else {
				task.setEnumOperator(EnumOperatorImpl.SUB);
				task.setX((rand.nextInt(9) + 1) * 10);
				task.setY((rand.nextInt((task.getX() / 10) + 1)) * 10);
			}
		} else if (i % 2 == 0) {
			task.setEnumOperator(EnumOperatorImpl.SUB);
			task.setX(rand.nextInt(20) + 1);
			task.setY(rand.nextInt(task.getX()) + 1);
		} else {
			task.setEnumOperator(EnumOperatorImpl.ADD);
			task.setX(rand.nextInt(19) + 1);
			task.setY(rand.nextInt(20 - task.getX()) + 1);
		}
		
		return task;
	}

}
