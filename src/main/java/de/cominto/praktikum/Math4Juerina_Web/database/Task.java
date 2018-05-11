package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.cominto.praktikum.Math4Juerina_Web.service.EnumOperatorImpl;


@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // AutoIncrement (1,1)
	private Long taskId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date practiceDay;
	
	private int x;
	private int y;
	private Integer suggestedSolution;
	
//	@Anno Enum Namen in Datanbank schreiben 
	@Enumerated(EnumType.STRING)
	private EnumOperatorImpl enumOperator;
	private boolean correct;
	
//	FK wird an den Pk der Klasse Round verwiesen
	@ManyToOne
	@JoinColumn(name="round_id",nullable=false, foreignKey=@ForeignKey(name="fk_task_to_Round"))
	private Round round;
	
	
	
	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	//constructor
	public Task() {}
	
	//getter
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public Integer getResult() {
		Integer res = null;
		switch (this.enumOperator) {
		case ADD:
			res = this.x + this.y;
			break;
		case SUB:
			res = this.x - this.y;
			break;

		}
		return res;
	}
	
	public EnumOperatorImpl getEnumOperator() {
		return this.enumOperator;
	}
	
	public Integer getSuggestedSolution() {
		return this.suggestedSolution;
	}
	
	//setter
	public void setEnumOperator(EnumOperatorImpl of) {
		this.enumOperator = of;
	}
	
	public void setSuggestedSolution(int userInput) {
		this.suggestedSolution = userInput;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long task_id) {
		this.taskId = task_id;
	}
	
	// funktions
	public boolean checkResult() {
		if (this.suggestedSolution == null)
			return false;
		else
			return checkResult(getSuggestedSolution());
	}
	
	public boolean checkResult(int userInput) {
		setSuggestedSolution(userInput);
		setCorrect(userInput == getResult());
		return isCorrect();
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public Date getPracticeDay() {
		return practiceDay;
	}

	public void setPracticeDay(Date practiceDay) {
		this.practiceDay = practiceDay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (correct ? 1231 : 1237);
		result = prime * result + ((enumOperator == null) ? 0 : enumOperator.hashCode());
		result = prime * result + ((practiceDay == null) ? 0 : practiceDay.hashCode());
		result = prime * result + ((round == null) ? 0 : round.hashCode());
		result = prime * result + ((suggestedSolution == null) ? 0 : suggestedSolution.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (correct != other.correct)
			return false;
		if (enumOperator != other.enumOperator)
			return false;
		if (practiceDay == null) {
			if (other.practiceDay != null)
				return false;
		} else if (!practiceDay.equals(other.practiceDay))
			return false;
		if (round == null) {
			if (other.round != null)
				return false;
		} else if (!round.equals(other.round))
			return false;
		if (suggestedSolution == null) {
			if (other.suggestedSolution != null)
				return false;
		} else if (!suggestedSolution.equals(other.suggestedSolution))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	
}
