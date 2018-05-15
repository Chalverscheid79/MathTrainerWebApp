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
 * controls everything in the active exercise session in connection with the exercises
 */
/**
 * @author halverscheid
 *
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
	private MathSession session;
	
	@Autowired
	private MathServices mathServices;
	
	
	
	
	
/**
 * Die ModelAndView Klasse ist EINE Möglichkeit die Objekte in der View
 * zu füllen bzw zu beschriften oder damit zu arbeiten
 * @return ModelAndView
 */
	
	
	/**
	 * create the new tasks
	 * @return the task in an ModelAndView objekt for template
	 */
	@RequestMapping("/play")
	public ModelAndView task() {
		
		

		
		Round round = session.getRound();
		

		final ModelAndView view = new ModelAndView();
		
		if (round.getExercise() != 0) {
			
/**
 * 			In die Session wird nur ein neues Task Objekt gelegt wenn es "null" ist
 *			also bei Programm Start oder richtig gelöster aufgabe.
 *			Damit wird auch verhindert, dass beim Reload der website eine neue Aufgabe erstellt wird
 */
			
			if (!session.hasTask()) {
				
/**
 * 				ein Objekt der Klasse wird erzeugt und der HttpSession
 * 				zugeordnet.
 */
				session.setTask( round.createTask());
			}

//			Das in session abgelegte Objekt wird wieder zu "Task" gecastet
			Task task = session.getTask();
			
//			Die VIEW (html || jsp) wird ausgewählt
			view.setViewName(VIEW_PLAY);
			
			view.addObject("userName", round.getUserName());
			view.addObject("page", "play");
			view.addObject("page_fragment","play-form");
			
//			Das session Objekt der Klasse Task wird an die Methode "stringbuilder" übergeben
			view.addObject(TASK,  task);
//			Das Attribute "richtig" der session wird an das Lbl der View übergeben
			
//			Infoausgabe der "Logger"-Klasse... Achtung weiter Informationen einholen
			LOG.info("Aufgabe Nr: {}",round.getStartExercise() - round.getExercise());
			
//			Aktuelle Aufgabenmenge wird um ein reduziert
			round.countDownExercise();
			
			view.addObject(SOLUTION_CORRECT, session.isCorrect());
			
		}else {
			
//			Die VIEW (html || jsp) wird ausgewählt
			view.setViewName(VIEW_SUCCESS);
			round.restartRound(round.getStartExercise());

		}
		session.removeSolution();
		
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
		if (! session.hasTask()) {
			view.setViewName("redirect:/index/welcome");
		}else {
		view.setViewName(VIEW_PLAY);
		view.addObject("page", "play");
		view.addObject("page_fragment","play-form");
		view.addObject(TASK,  session.getTask());
		view.addObject(ERROR, param);
		view.addObject(SOLUTION_CORRECT, session.isCorrect());
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
		if (! session.hasTask()) {
			return "redirect:/index/welcome";
		}
		
		Round round = session.getRound();
		Task task = (session.getTask());
		
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
		session.setCorrect(task.isCorrect());
		
		
		task.setTaskId(null);
		task.setRound(round);
		task.setPracticeDay(new Date());
		
		
		
		if (task.isCorrect()) {
			mathServices.saveTask(task);
			session.removeTask();
			round.resetWrongSolution();
			return "redirect:/ui/play";
		}
		else {
			round.addWrongSolution(); // Fehlversuch um 1 erhoehen
			
			if (round.getWrongSolution()== 3) {
				mathServices.saveTask(task);
				session.removeTask();
				return "redirect:/ui/play";
			}
			return "redirect:/ui/wrong";
		}
	}
	

}
