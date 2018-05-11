package de.cominto.praktikum.Math4Juerina_Web.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.PlayerRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.RoundRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;
import de.cominto.praktikum.Math4Juerina_Web.database.TaskRepository;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;

@Service
@Transactional
public class MathServicesImpl implements MathServices {

	private static final Logger LOG = LoggerFactory.getLogger(MathServicesImpl.class);

	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private RoundRepository roundRepository;
	@Autowired
	private TaskRepository taskRepository;

	@Value("${math.userName}")
	private String defaultName;
	@Value("${math.num_tasks}")
	private int defaultExercise;

	@Autowired
	private EntityManager em;
	
	@Override
	public Player loadPlayer(String userName) {

		Player player = null;

		// eventuelle whitspaceses entfernen und auf Leerstring prüfen
		if (userName.trim().equals("")) {
			userName = defaultName;
		}

		// Player wird nur in die Datenbank geschrieben wenn er noch nicht da war
		if (playerRepository.findByUserName(userName).isEmpty()) {
			player = new Player(userName);
			playerRepository.save(player);
		} else {
			player = playerRepository.findFirstByUserName(userName);
		}
		return player;
	}

	@Override
	public Round getRound(int exercise, Player player) {

		if (exercise < 10) {
			exercise = defaultExercise;
		}

		Round round = new Round(exercise, player);
		round.setDay(new Date());
		saveRound(round);
		return round;
	}

	@Override
	public Round saveRound(Round round) {

		roundRepository.save(round);
		return round;
	}

	@Override
	public Round findRoundById(long id) {
		Round round = roundRepository.findById(id).get();
		return round;
	}

	@Override
	public Task saveTask(Task task) {

		taskRepository.save(task);
		return task;
	}

	@Override
	public List<Task> findByDay(long userId, Date date) {

		List<Task> list = taskRepository.findByRoundPlayerPlayerIdAndRoundDayOrderByTaskId(userId, date);		
		
		return list;
	}

	@Override
	public double getCorrectPercent(long id) {

		LOG.info("*** getCorrectPercent({}) ***", id);
	
		this.em.flush();
		this.em.clear();
		
		Optional<Round> round = roundRepository.findById(id);
		if (!round.isPresent()) {

			return -1;
		}

		LOG.info("Player: {}", round.get().getPlayer());

		List<Task> tasks = round.get().getTasks();
		double correctPercent = 0;
		int errors = 0;

		for (Task task : tasks) {
			if (task.isCorrect()) {
				errors++;
				
			}
		}

		correctPercent = 100 - (100 / round.get().getExercise() * errors);
		
		LOG.info("******* Prozent {} *********",correctPercent*2);
		LOG.info("******* Prozent {} *********",correctPercent);
		
//		getNumberOfErrors(round.get().getRoundId(), new Date());
		
		return correctPercent;
	}

	@Override
	public Collection<Task> findByLastRoundAndDay(long userId, long roundId, Date date) {
		//TODO
		List<Task> list = findByDay(userId, date);
		List<Task> listLastRound = taskRepository.findByRoundRoundIdAndRoundDay(roundId, date);
//		listLastRound.addAll(list);
		
		Set<Task> tasks = new LinkedHashSet<>();
		tasks.addAll(listLastRound);
		tasks.addAll(list);
		
		return tasks;
	}

	@Override
	public int getNumberOfErrors(long id) {
		// TODO Auto-generated method stub

		this.em.flush();
		this.em.clear();
		
		Optional<Round> round = roundRepository.findById(id);
//		LOG.info("#################round {} #####################", round);
		if (!round.isPresent()) {

			return -1;
		}

		LOG.info("Player: {}", round.get().getPlayer());

		List<Task> tasks = round.get().getTasks();
		int errors = 0;

		for (Task task : tasks) {
			if (!task.isCorrect()) {
				errors++;
				
			}
			
			LOG.info("######################## ERRORS {} ####################",errors);
		}
		return errors;
	}

}
