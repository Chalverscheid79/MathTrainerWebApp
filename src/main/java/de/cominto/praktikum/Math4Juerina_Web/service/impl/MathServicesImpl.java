package de.cominto.praktikum.Math4Juerina_Web.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.persistence.EntityManager;

import de.cominto.praktikum.Math4Juerina_Web.MathProperties;
import de.cominto.praktikum.Math4Juerina_Web.database.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;

/**
 *
 */
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

//	@Value("${math.userName}")
//	private String defaultName;
//	@Value("${math.num_tasks}")
//	private int defaultExercise;

	@Autowired
	MathProperties mathProperties;

	@Autowired
	private EntityManager em;

	public MathServicesImpl() {

//		this.playerRepository = playerRepository;
	}

    /**
     * load player from database or create new player object
     * @param userName String from userConfig
     * @return Player, can#t be null
     */
	@Override
	public Player loadPlayer(String userName) {

		Player player;

		// eventuelle whitspaceses entfernen und auf Leerstring prüfen
		if (userName.trim().equals("")) {
			userName = mathProperties.getUserName();
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

    /**
     * Create Round Object
     * @param exercise must be min 10
     * @param player needs object
     * @return object from Round, cannot null
     */
	@Override
	public Round getRound(int exercise, Player player) {

		if (exercise < 10) {
			exercise = mathProperties.getNumTasks();
		}

		Round round = new Round(exercise, player);
		round.setDay(new Date());
		saveRound(round);
		return round;
	}

    /**
     * Save entity in repository
     * @param round entity
     * @return saved entity
     */
	@Override
	public Round saveRound(Round round) {

		roundRepository.save(round);
		return round;
	}

	/**
	 * JPQL Query
	 * @param id roundId
	 * @return List, can be null
	 */
	@Override
	public Round findRoundById(long id) {

		return roundRepository.findById(id).orElse(null);
	}

    /**
     * Save entity in repository
     * @param task entity
     * @return saved entity
     */
	@Override
	public Task saveTask(Task task) {

		taskRepository.save(task);
		return task;
	}




    /**
     * Search by roundId all wrong solutions from task and counts them
     * @param id Round rounId
     * @return amount of the right solutions in percent, can be -1
     */
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
		double correctPercent;
		int errors = 0;

		for (Task task : tasks) {
			if (! task.isCorrect()) {
				errors++;
				
			}
		}

		correctPercent = 100 - (100 / round.get().getExercise() * errors);
		

		LOG.info("******* Prozent {} *********",correctPercent);
		
//		getNumberOfErrors(round.get().getRoundId(), new Date());
		
		return correctPercent;
	}

    /**
     * Search all task from day
     * @param userId from player
     * @param roundId from round
     * @param date actual date
     * @return list, can be null
     */
	@Override
	public Collection<Task> findByLastRoundAndDay(long userId, long roundId, Date date) {
		//TODO
		List<Task> list = taskRepository.findByRoundPlayerPlayerIdAndRoundDayOrderByTaskId(userId, date);
		List<Task> listLastRound = taskRepository.findByRoundRoundId(roundId);

		
		Set<Task> tasks = new LinkedHashSet<>();
		tasks.addAll(listLastRound);
		tasks.addAll(list);
		
		return tasks;
	}

    /**
     * coutns errors from task etentity
     * @param id from round etentity
     * @return if optioanal list not present -1 / or if
     * present number of errors as in
     */
	@Override
	public int getNumberOfErrors(long id) {

		this.em.flush();
		this.em.clear();
		
		Optional<Round> round = roundRepository.findById(id);
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

	/**
	 *
	 * @param playerId
	 * @param priviousDays
	 * @return
	 */
	@Override
	public long getPercentCorrectFromDateToLocalDate(long playerId, int priviousDays) {

		LocalDate localDate = LocalDate.now();
		LocalDate pastLocalDate = localDate.minusDays(priviousDays);
		Date toDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fromDate = Date.from(pastLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<WrapperCount> list = taskRepository.countAllTaskFromDateToDate(playerId,fromDate,toDate);

		return  getPercentCorrect(list);
	}

	@Override
	public List<Long> getCountAllTaskFromDateToDateGroupByDay(long playerId, int priviousDays) {

		LocalDate localDate = LocalDate.now();
		LocalDate pastLocalDate = localDate.minusDays(priviousDays);
		Date toDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fromDate = Date.from(pastLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<WrapperCount> list = taskRepository.countAllTaskFromDateToDateGroupByDay(playerId, fromDate, toDate);
		List <Long> percentCorrect = new ArrayList<>();
		/*
			SQL Result:
			2018-05-04	0	2
			2018-05-04	1	26
			2018-05-07	0	9
			2018-05-07	1	27
			2018-05-08	0	10
			2018-05-08	1	80
			2018-05-09	0	146
			2018-05-09	1	80
			2018-05-11	0	2
			2018-05-11	1	12
			2018-05-14	1	61
			2018-05-15	0	1
			2018-05-15	1	34
			2018-05-16	1	48
		 */
		List<WrapperCount> oneDay = new ArrayList<>();
		Date currentDay = null;
		for(WrapperCount wrapperCount:list){
			if (currentDay == null || !currentDay.equals(wrapperCount.getDay())){
				if(currentDay != null){
					percentCorrect.add(getPercentCorrect(oneDay));
				}
				oneDay.clear();
				currentDay=wrapperCount.getDay();
			}
			oneDay.add(wrapperCount);
		}
		if(!oneDay.isEmpty()) {
			percentCorrect.add(getPercentCorrect(oneDay));
		}

//		percentCorrect.add(45L);
//		percentCorrect.add(95L);
//		percentCorrect.add(55L);
//		percentCorrect.add(65L);
//		percentCorrect.add(0L);
//		percentCorrect.add(10L);

		LOG.info("*******##### LocalDate: {}", new Date());
		return  percentCorrect;
	}

	private long getPercentCorrect(List<WrapperCount> wrapperCount){
		long correctAnswers = 0;
		long wrongAnswers = 0;
		for(WrapperCount i : wrapperCount){
			if(i.isCorrect()){
				correctAnswers = i.getTasks();
			}
			if (! i.isCorrect()){
				wrongAnswers = i.getTasks();
			}

		}
		long totalTasks = correctAnswers + wrongAnswers;
		long percentCorrect = correctAnswers * 100 / totalTasks;
		LOG.info("##### CORRECT: {} +++++ WRONG: {}",correctAnswers,wrongAnswers);
		return  percentCorrect;
	}
}
