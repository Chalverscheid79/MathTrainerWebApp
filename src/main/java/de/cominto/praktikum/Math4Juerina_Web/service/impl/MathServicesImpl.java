package de.cominto.praktikum.Math4Juerina_Web.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.persistence.EntityManager;

import de.cominto.praktikum.Math4Juerina_Web.MathProperties;
import de.cominto.praktikum.Math4Juerina_Web.database.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private PlayerRepository playerRepository;
	private RoundRepository roundRepository;
	private TaskRepository taskRepository;
	private MathProperties mathProperties;
	private EntityManager entityManager;

	public MathServicesImpl(TaskRepository taskRepository, PlayerRepository playerRepository,
							RoundRepository roundRepository, MathProperties mathProperties,
							EntityManager entityManager) {
		this.taskRepository = taskRepository;
		this.playerRepository = playerRepository;
		this.roundRepository = roundRepository;
		this.mathProperties = mathProperties;
		this.entityManager = entityManager;
	}

    /**
     * load player from database or create new player object
     * @param userNameInput String from userConfig
     * @return Player, can#t be null
     */
	@Override
	public Player loadPlayer(final String userNameInput) {
		String userName = userNameInput;
		Player player;

		// eventuelle whitspaceses entfernen und auf Leerstring prüfen
		//if (userName.trim().equals("")) {
		if (StringUtils.isBlank(userName)) {
			userName = mathProperties.getUserName();
		}

		player = playerRepository.findFirstByUserName(userName);

    // Player wird nur in die Datenbank geschrieben wenn er noch nicht da war
		if (player == null) {
			player = new Player(userName);
			playerRepository.save(player);
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
	public Round getRound(final int exercise, final Player player) {

		Round round = new Round(Math.max(exercise, this.mathProperties.getNumTasks()), player);
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
	public Round saveRound(final Round round) {

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
	public Task saveTask(final Task task) {

		taskRepository.save(task);
		return task;
	}




    /**
     * Search by roundId all wrong solutions from task and counts them
     * @param rundId
     * @return amount of the right solutions in percent, can be -1
     */
	@Override
	public double getCorrectPercent(final long rundId) {

		LOG.info("*** getCorrectPercent({}) ***", rundId);
	
		this.entityManager.flush();
		this.entityManager.clear();
		
		Optional<Round> round = roundRepository.findById(rundId);
		if (!round.isPresent()) {

			return -1;
		}

		LOG.info("Player: {}", round.get().getPlayer());

		List<Task> tasks = round.get().getTasks();
		double correctPercent;
		double errors = 0;
		double correct = 0;
		double total = 0;


		for (Task task : tasks) {
			if (! task.isCorrect()) {
				errors++;
				
			}else{
				correct++;
			}
			total = errors + correct;
		}

		correctPercent = Math.round(correct * 100.0 / total *100)/100.0;
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
	 * merge the query results
	 * @param round entity objekt
	 * @param playerId long from Player entity
	 * @return list, can be null
	 */
    @Override
    public List<Double> findAllTasksFromLastFiveRoundsInfrintAcutalRound(Round round, long playerId) {
        LocalDate localDate = LocalDate.now();
        LocalDate pastLocalDate = localDate.minusDays(0);
        Date toDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fromDate = Date.from(pastLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<WrapperCount> list = taskRepository.findAllTasksFromLastFiveRoundsWithoutAcutalRound(round, fromDate, toDate);
        List<Double> percent = new ArrayList<>();
        List<WrapperCount> oneRound = new ArrayList<>();
        Long currentRound = null;
        for(WrapperCount wrapperCount:list){
            if (currentRound == null || !currentRound.equals(wrapperCount.getRoundId())){
                if(currentRound != null){
                    percent.add(Math.round(getPercentCorrect(oneRound)*100.0)/100.0);
                }
                oneRound.clear();
                currentRound =wrapperCount.getRoundId();
            }
            oneRound.add(wrapperCount);
        }
        if(!oneRound.isEmpty()) {
            percent.add(Math.round(getPercentCorrect(oneRound)*100.0)/100.0);
        }

        return percent;
    }

    /**
     * coutns errors from task etentity
     * @param id from round etentity
     * @return if optioanal list not present -1 / or if
     * present number of errors as int
     */
	@Override
	public int getNumberOfErrors(final long id) {

		this.entityManager.flush();
		this.entityManager.clear();
		
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
			
		}
		return errors;
	}

	/**
	 * calls from source / Database all task from player of the called proid of days where result is true
	 * @param playerId long from Player entity
	 * @param priviousDays int number of days in past
	 * @return invoked a method to caculate the percentage of correct task and returns a list of values as double,
	 * 			can be null
	 */
	@Override
	public Double getPercentCorrectFromDateToLocalDate(long playerId, int priviousDays) {

		LocalDate localDate = LocalDate.now();
		LocalDate pastLocalDate = localDate.minusDays(priviousDays);
		Date toDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fromDate = Date.from(pastLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<WrapperCount> listTasks = taskRepository.countAllTaskFromDateToDate(playerId,fromDate,toDate);

		return  Math.round(getPercentCorrect(listTasks)*100.0)/100.0;
	}

	/**
	 * The query returns tw values per day. these values are collected together at a date an transferred to a method
	 * for calculatig the percentage of the correct tasks
	 * @param playerId long from Player entity
	 * @param priviousDays int number of days in past
	 * @return ArrayList <Long> values with percent from correct answers
	 */
	@Override
	public List<Double> getCountAllTaskFromDateToDateGroupByDay(long playerId, int priviousDays) {

		LocalDate localDate = LocalDate.now();
		LocalDate pastLocalDate = localDate.minusDays(priviousDays);
		Date toDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fromDate = Date.from(pastLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<WrapperCount> list = taskRepository.countAllTaskFromDateToDateGroupByDay(playerId, fromDate, toDate);
//		for (int i = 0; i< list.size();i++)
//		LOG.info("####################### WrapperDate: {} ********************",list.get(i).getDay());
		List <Double> percentCorrect = new ArrayList<>();
		List<WrapperCount> oneDay = new ArrayList<>();
		Date currentDay = null;
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
		for(WrapperCount wrapperCount:list){
			if (currentDay == null || !currentDay.equals(wrapperCount.getDay())){
				if(currentDay != null){
					percentCorrect.add( (Math.round(getPercentCorrect(oneDay)*100)/100.0));
					LOG.info("####################### WrapperDate: {} ********************",wrapperCount.getDay());
				}
				oneDay.clear();
				currentDay=wrapperCount.getDay();
			}
			oneDay.add(wrapperCount);
		}
		if(!oneDay.isEmpty()) {
			percentCorrect.add( (Math.round(getPercentCorrect(oneDay)*100)/100.0));
		LOG.info("####################### WrapperDate oneDay: {} ********************",oneDay.get(0).getDay());
		}
		LOG.info("####################### percent: {} ********************",percentCorrect.size());

		return  percentCorrect;
	}

    /**
     * calculates the percentage of the correct tasks
     * @param /List</WrapperCount> comes up with list of two values per entity objekt.
	 *                     number of correct tasks and  number of wrong tasks
     * @return double percent of the right tasks
     */
	private double getPercentCorrect(List<WrapperCount> wrapperCount){
		double correctAnswers = 0;
		double wrongAnswers = 0;
		double percentCorrect;

		for(WrapperCount i : wrapperCount){
			if(i.isCorrect()){
				correctAnswers = i.getTasks();
			}
			if (! i.isCorrect()){
				wrongAnswers = i.getTasks();
			}

		}
		double totalTasks = correctAnswers + wrongAnswers;
		percentCorrect = correctAnswers * 100.0 / totalTasks;
		return  percentCorrect;
	}


}
