package de.cominto.praktikum.Math4Juerina_Web.database;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.cominto.praktikum.Math4Juerina_Web.service.Factory;

@Entity
public class Round {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // AutoIncrement (1,1)
	private long roundId;
	

	private int wrongSolution;			// falsche Lösung
	@Transient							// ignoriert Feldnamen bei der Tabellenerstellung
	private int factoryDecision;		// Übergabeparameter an Factory
	private int exercise;				// Übungsaufgabe
	@Transient
	private int startExecise;
	private long startMillis;			// Startzeit in currentTimeMillis
	private long stopMillis;			// Stopzeit in currentTimeMillis
	@Temporal(TemporalType.DATE)
	private Date day;
	
	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="playerId",nullable=false, foreignKey=@ForeignKey(name="fk_Player"))
	private Player player;
	
	@OneToMany(mappedBy="round")
	private List <Task> tasks;
	

	

	public Round() {}
	
	public Round(Integer exercise, Player player) {
		
		this.exercise = exercise;
		this.startExecise = exercise;
		this.player = player;
		startMillis = System.currentTimeMillis();
		stopMillis = this.startMillis + (exercise * 10000);
	}
	
	//getter
	// Round fordert aufgaben
	public Task createTask() {
		addFactoryDecision();
		return  Factory.buildTask(this.factoryDecision);
	}
	
	// prozentsatz der richtigen aufgaben errechen
	public double getAvg() {
		double avg ;
		avg = 100 - ( 100 / this.exercise * this.wrongSolution);
		
		return avg;
	}
	public String getUserName () {
		return this.player.getUserName();
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public int getStartExercise() {
		return this.startExecise;
	}
	
	//falsche User Lösungen
	public int getWrongSolution() {
		return this.wrongSolution;
	}
	
	public void resetWrongSolution() {
		this.wrongSolution = 0;
	}
	
	public int getFactoryDecision() {
		return this.factoryDecision;
	}
	
	public boolean isRoundOff() {
		return getExercise() == getFactoryDecision()-1;
	}
	
	public int getExercise() {
		return this.exercise;
	}
	
	public void countDownExercise() {
		this.exercise--;
	}
	
	public long getStopMillis() {
		return this.stopMillis;
	}
	public long getRoundId() {
		return roundId;
	}
	//setter
	public void setRoundId(long round_id) {
		this.roundId = round_id;
	}
	private void addFactoryDecision() {
		factoryDecision++;
	}
	
	public void addWrongSolution() {
		wrongSolution++;

	}
	public void restartRound(Integer i) {
		this.exercise  = i;
		this.player = getPlayer();
		this.factoryDecision = 0;
	}
	public void setPlayer(Player user) {
		this.player = user;
	}

	// JUnit
	public void setFactoryDecision(int fd) {
		this.factoryDecision = fd;
	}
	
	public long getStartMillis() {
		return this.startMillis;
	}

	public List<Task> getTasks() {
		//TODO
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + exercise;
		result = prime * result + factoryDecision;
		result = prime * result + (int) (roundId ^ (roundId >>> 32));
		result = prime * result + startExecise;
		result = prime * result + (int) (startMillis ^ (startMillis >>> 32));
		result = prime * result + (int) (stopMillis ^ (stopMillis >>> 32));
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + wrongSolution;
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
		Round other = (Round) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (exercise != other.exercise)
			return false;
		if (factoryDecision != other.factoryDecision)
			return false;
		if (roundId != other.roundId)
			return false;
		if (startExecise != other.startExecise)
			return false;
		if (startMillis != other.startMillis)
			return false;
		if (stopMillis != other.stopMillis)
			return false;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (wrongSolution != other.wrongSolution)
			return false;
		return true;
	}

	
}
