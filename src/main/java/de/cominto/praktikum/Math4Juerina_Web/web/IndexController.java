package de.cominto.praktikum.Math4Juerina_Web.web;


import de.cominto.praktikum.Math4Juerina_Web.MathProperties;
import de.cominto.praktikum.Math4Juerina_Web.database.Irgendwie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.TaskRepository;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("index")
public class IndexController {
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
	private static final String INDEX = "index";
	private static final String PLAY = "redirect:/ui/play";
	
	@Autowired
	private MathServices mathServices;
	
	@Autowired
	TaskRepository taskre;
	
	//TODO auskommentieren
	@Autowired
	private MathSession session;

	
	/**
	 * Go to the home page of the mathetrainer
	 * 
	 * @return final String
	 */
	@RequestMapping("welcome")
	public String welcomeUser(final Model model) {

		model.addAttribute("page", "login");
		model.addAttribute("page_fragment", "login-form");

		LocalDate localDate = LocalDate.now();
		LocalDate yesterday = localDate.minusDays(2);
		Date toDate = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date fromDate = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
//		LOG.info("SQL:{} toDate: {} fromDate: {}", taskre.countAllTaskFromDateToDate(1,Date.from(LocalDate.of(2018,05,14).atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(LocalDate.of(2018,05,16).atStartOfDay(ZoneId.systemDefault()).toInstant())),toDate,fromDate);

		LOG.info("**##### Query: {} ####fromDate: {} ####toDate: {} **####", taskre.countAllTaskFromDateToDate(1,fromDate,toDate),fromDate,toDate);

		List<Irgendwie> list = taskre.countAllTaskFromDateToDate(1,fromDate,toDate);
		LOG.info("ListIndex 1: {} ListIndex 2: {}", list.get(0),list.get(1));

		long correctAnswers = 0;
		long wrongAnswers = 0;
		for(Irgendwie i : list){
			if(i.isCorrect()){
				correctAnswers = i.getTasks();
			}
			if (! i.isCorrect()){
				wrongAnswers = i.getTasks();
			}
		}
		LOG.info("##### CORRECT: {} +++++ WRONG: {}",correctAnswers,wrongAnswers);
		return INDEX;
	}
	
	/**
	 * Invites the player and opens the training round.
	 * 
	 * @param userName from request.post gets the username as String
	 * @param exercise has a default "0"
	 * @return final String to call the play controller
	 */
	@RequestMapping(path = "userConfig", method = RequestMethod.POST)
	public String setUserConfig(@RequestParam(name="userName",required=false) String userName,@RequestParam (name="exercise",required=false,defaultValue="0") int exercise) {

		Player player = mathServices.loadPlayer(userName);
		
		LOG.info("**** exercise : {} ****",exercise);
		
		Round round = mathServices.getRound(exercise, player);
		
		session.setRound(round);
		
//		LOG.info("########### QueryList: {}###############",taskre.allRoundOfPlayerOnTable(player.getUserName()));
		return PLAY;
	}

}
