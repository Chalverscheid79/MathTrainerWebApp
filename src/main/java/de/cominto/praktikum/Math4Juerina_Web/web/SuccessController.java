package de.cominto.praktikum.Math4Juerina_Web.web;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;

@Controller
@RequestMapping("success")
public class SuccessController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SuccessController.class);
	
	private static final String SUCCESS = "success";
	
	@Autowired
	private MathSession session;
	
	@Autowired
	private MathServices mathServices;

	
	@RequestMapping("/praise") // loben
	public ModelAndView getpraise() {
		ModelAndView view = new ModelAndView();
		Round round = session.getRound();
		view.setViewName(SUCCESS);
		view.addObject("exercise",round.getStartExercise());
		
		mathServices.saveRound(round);
		
		view.addObject("avg", mathServices.getCorrectPercent(round.getRoundId()));
		
		view.addObject("error",mathServices.getNumberOfErrors(round.getRoundId()));
		view.addObject("actualRoundId", round.getRoundId());

		/**
		 * Die Entity Objekte werden in eine "List" geladen gemaess der Abfrage aus der Repository
		 */
		Collection<Task> list = mathServices.findByLastRoundAndDay(round.getPlayer().getPlayerId(), round.getRoundId(),new Date());
		
		LOG.info("LISTE: {}",list.size());
		
		view.addObject("listOfTasks", list);
		
		return view;
	}
	
	@RequestMapping("/restart")
	public String restart() {
		if (! session.hasRound()) {
			
			return "redirect:/index/welcome";
		}
		//TODO Nullpointer behandeln
		Round oldRound = session.removeRound();
		
		Round round = mathServices.getRound(oldRound.getStartExercise(), oldRound.getPlayer());
		
		session.setRound(round);
		
		return "redirect:/ui/play";
	}
	

}
