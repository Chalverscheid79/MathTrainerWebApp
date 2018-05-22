package de.cominto.praktikum.Math4Juerina_Web.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.TaskRepository;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * controls the dynamic content of the view
 *
 * @author halverscheid
 */
@Controller
@RequestMapping("index")
public class IndexController {
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
	private static final String WELCOME = "redirect:/index/welcome";
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
		

		Round round = mathServices.getRound(exercise, player);
		session.setRound(round);

		return PLAY;
	}

	@RequestMapping("signout")
	public String signOut(){

		session.clear();

		return WELCOME;
	}

}
