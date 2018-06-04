package de.cominto.praktikum.Math4Juerina_Web.web;

import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Date;

/**
 * controls the dynamic content of the view
 *
 * @author halverscheid
 */
@Controller
@RequestMapping("success")
public class SuccessController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SuccessController.class);
	
	private static final String VIEW_INDEX = "index";
	
	private MathSession session;
	private MathServices mathServices;

	public SuccessController(MathSession mathSession, MathServices mathServices) {
		this.session = mathSession;
		this.mathServices = mathServices;
	}

	@RequestMapping("/praise") // loben
	public ModelAndView getpraise() {

		ModelAndView view = new ModelAndView();
		Round round = session.getRound();
		mathServices.saveRound(round);
		view.setViewName(VIEW_INDEX);
		view.addObject("userName", round.getUserName());
		view.addObject("page", "success");
		view.addObject("pageleft","left");
		view.addObject("page_fragment","success-form");
		view.addObject("pageright","right");
		view.addObject("exercise",round.getStartExercise());
		view.addObject("correctPercent",mathServices.getPercentCorrectFromDateToLocalDate(session.getRound().getPlayer().getPlayerId(),30));
		view.addObject("avg", mathServices.getCorrectPercent(round.getRoundId()));
		view.addObject("error",mathServices.getNumberOfErrors(round.getRoundId()));
		view.addObject("actualRoundId", round.getRoundId());
		view.addObject("fiveDayRefletkionLeftList",mathServices.getCountAllTaskFromDateToDateGroupByDay(session.getRound().getPlayer().getPlayerId(),5));
		view.addObject("fiveRoundRefletkionRightList",mathServices.findAllTasksFromLastFiveRoundsInFrontAcutalRound(session.getRound(), session.getRound().getPlayer().getPlayerId()));

		/*
		 * Die Entity Objekte werden in eine "List" geladen gemaess der Abfrage aus der Repository
		 */
		Collection<Task> list = mathServices.findByLastRoundAndDay(round.getPlayer().getPlayerId(), round.getRoundId(),new Date());
		

		view.addObject("listOfTasks", list);
		
		return view;
	}
	
	@RequestMapping("/restart")
	public String restart() {
		if (! session.hasRound()) {
			
			return "redirect:/index/welcome";
		}
		Round oldRound = session.removeRound();
		
		Round round = mathServices.getRound(oldRound.getStartExercise(), oldRound.getPlayer());
		
		session.setRound(round);
		
		return "redirect:/ui/play";
	}
	

}
