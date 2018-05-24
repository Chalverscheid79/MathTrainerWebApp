package de.cominto.praktikum.Math4Juerina_Web.web;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;


/**
 * @author halverscheid
 * controls everything in the active exercise mathSession in connection with the exercises
 */
@Controller
@RequestMapping("/ui")
public class PlayController {
	
	private static final String ERROR = "error";
	private static final String SOLUTION_CORRECT = "reflection";
	
	private static final String TASK = "task";
	private static final String VIEW_PLAY = "index";
	private static final String VIEW_SUCCESS = "redirect:/success/praise";
	
	private static final Logger LOG = LoggerFactory.getLogger(PlayController.class);
	
	@Autowired
	private MathSession mathSession;
	
	@Autowired
	private MathServices mathServices;

	public PlayController(MathSession mathSession, MathServices mathServices) {
		this.mathSession = mathSession;
		this.mathServices = mathServices;
	}

	/**
 * The ModelAndView class is ONE possibility the objects in the view
 * fill or label or work with it
 * @return ModelAndView
 */
	
	
	/**
	 * create the new tasks
	 * @return the task in an ModelAndView objekt for template
	 */
	@RequestMapping("/play")
	public ModelAndView task() {
		
		

		
		Round round = mathSession.getRound();
		

		final ModelAndView view = new ModelAndView();
		
		if (round.getExercise() != 0) {
			
/**
 * 	Only a new task object is placed in the mathSession if it is "null"
 *  So at program start or correctly solved task.
 *  This also prevents the reload of the website from creating a new task
 */
			
			if (!mathSession.hasTask()) {
				
/*
 * 				ein Objekt der Klasse wird erzeugt und der HttpSession
 * 				zugeordnet.
 */
				mathSession.setTask( round.createTask());
			}

//			Das in mathSession abgelegte Objekt wird wieder zu "Task" gecastet
			Task task = mathSession.getTask();
			
//			Die VIEW (html || jsp) wird ausgewählt
			view.setViewName(VIEW_PLAY);
			
			view.addObject("userName", round.getUserName());
			view.addObject("page", "play");
			view.addObject("page_fragment","play-form");
			
//			Das mathSession Objekt der Klasse Task wird an die Methode "stringbuilder" übergeben
			view.addObject(TASK,  task);
//			Das Attribute "richtig" der mathSession wird an das Lbl der View übergeben
			
//			Infoausgabe der "Logger"-Klasse... Achtung weiter Informationen einholen
			LOG.info("Aufgabe Nr: {}",round.getStartExercise() - round.getExercise());
			
//			Aktuelle Aufgabenmenge wird um ein reduziert
			round.countDownExercise();
			
			view.addObject(SOLUTION_CORRECT, mathSession.isCorrect());
			
		}else {
			
//			Die VIEW (html || jsp) wird ausgewählt
			view.setViewName(VIEW_SUCCESS);
			round.restartRound(round.getStartExercise());

		}
		mathSession.removeSolution();
		
		return view;
	}
	
	/**
	 * is called if the task was solved incorrectly and repeats the task until it is correct
	 * @param param calls the that only allows digits
	 * @return ModelAndView for next action
	 */
	@RequestMapping("/wrong")					// "name = "error", required = false" macht den Übergabeparameter zu einer "kann"-Option
	public ModelAndView wrongTask(@RequestParam(name = ERROR, required = false) String param) {
		
		final ModelAndView view = new ModelAndView();
		if (! mathSession.hasTask()) {
			view.setViewName("redirect:/index/welcome");
		}else {
		view.setViewName(VIEW_PLAY);
		view.addObject("page", "play");
		view.addObject("page_fragment","play-form");
		view.addObject(TASK,  mathSession.getTask());
		view.addObject(ERROR, param);
		view.addObject(SOLUTION_CORRECT, mathSession.isCorrect());
		}
		
		return view;
	}
	
	
	/**
	 * checked user solution and calls the next mapping station
	 * 
	 * @param result for the suggested solution from user
	 * @param model contains data for exeption handling
	 * @return String with redirect next mapping
	 */
	@RequestMapping("/checkTask")
	public String submit(@RequestParam String result, Model model)  {
		if (! mathSession.hasTask()) {
			return "redirect:/index/welcome";
		}
		
		Round round = mathSession.getRound();
		Task task = (mathSession.getTask());
		
		LOG.info("Result: {}", result);
		int res = 0;
		
		try {
			res = Integer.valueOf(result);
		}catch(NumberFormatException e){
			e.printStackTrace();

			model.addAttribute(TASK,  task);
			model.addAttribute(ERROR, "Bitte Zahlen eingeben");
			return VIEW_PLAY;
		}
		
		task.checkResult(res);
		mathSession.setCorrect(task.isCorrect());
		
		
		task.setTaskId(null);
		task.setRound(round);
		task.setPracticeDay(new Date());
		
		
		
		if (task.isCorrect()) {
			mathServices.saveTask(task);
			mathSession.removeTask();
			round.resetWrongSolution();
			return "redirect:/ui/play";
		}
		else {
			round.addWrongSolution(); // Fehlversuch um 1 erhoehen
			
			if (round.getWrongSolution()== 3) {
				mathServices.saveTask(task);
				mathSession.removeTask();
				return "redirect:/ui/play";
			}
			return "redirect:/ui/wrong";
		}
	}
	

}
