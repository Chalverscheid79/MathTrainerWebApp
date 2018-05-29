package de.cominto.praktikum.Math4Juerina_Web.web;

import de.cominto.praktikum.Math4Juerina_Web.database.*;
import de.cominto.praktikum.Math4Juerina_Web.service.EnumOperatorImpl;
import de.cominto.praktikum.Math4Juerina_Web.service.Factory;
import de.cominto.praktikum.Math4Juerina_Web.service.impl.MathServicesImpl;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"integration"})
@AutoConfigureMockMvc
public class ControllerMockTest {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerMockTest.class);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    RoundRepository roundRepository;
    @Autowired
    TaskRepository taskRepository;


    @Test
    public void welcomeControllerTest() throws Exception {

        this.mockMvc.perform(get("/index/welcome").accept(MediaType.TEXT_HTML)).andExpect(status().isOk()).andExpect(xpath("//input[@id='userName']").exists())
                .andExpect(xpath("//input[@id='exercise']").exists());


    }

    @Test
    public void loginControllerTest() throws Exception {
        this.mockMvc
                .perform(
                        post("/index/userConfig")
                                .param("userName", "ahoi")
                                .param("exercise", "30")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ui/play"));


        List<Player> playerList = new ArrayList<>();
        playerRepository.findAll().forEach(playerList::add);
        assertThat(playerList, is(Matchers.notNullValue()));
        assertThat(playerList.size(), is(1));

    }

    @Test
    public void playControllerTest() throws Exception {
        int labelIndexReflection = 2;
        Player player = new Player("ahoi");

        Round round = Mockito.mock(Round.class);
        Mockito.when(round.getPlayer()).thenReturn(player);
        Mockito.when(round.getUserName()).thenReturn(player.getUserName());
        Mockito.when(round.getExercise()).thenReturn(2);
        Task task = Factory.buildTask(15);
        Mockito.when(round.createTask()).thenReturn(task);
        this.mockMvc
                .perform(
                        get("/ui/play").sessionAttr(MathSession.ROUND, round)
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", task))
                .andExpect(xpath("//form[@class='play-form']/h1/span").string("ahoi"))
                .andExpect(xpath("//label[@for='result']").string(String.format("%d %s %d =",
                        task.getX(), task.getEnumOperator().getOperator(), task.getY())))
                .andExpect(xpath("//input[@id='result']").exists())
                .andExpect(xpath("//form[@class='play-form']/div[@class='form-group play-form']/label").nodeCount(2))
                .andExpect(xpath("//form[@class='play-form']/div[@class='form-group play-form']/label[" + labelIndexReflection + "]").exists())
                .andExpect(xpath("//form[@class='play-form']/div[@class='form-group play-form']/label[" + labelIndexReflection + "]").string(""));


    }

    @Test
    public void wrongFromPlayWithoutSessionControllerTest() throws Exception {

        this.mockMvc
                .perform(
                        get("/ui/wrong")
                ).andExpect(status().is(302))
                .andExpect(redirectedUrl("/index/welcome"));
    }

    @Test
    public void wrongFromPlayWithSessionControllerTest() throws Exception {
        int labelIndexReflection = 2;
        MathSession mathSession = Mockito.mock(MathSession.class);
        Task task;
        task = Factory.buildTask(10);
        mathSession.setTask(task);
        Mockito.when(mathSession.getTask()).thenReturn(task);
        Mockito.when(mathSession.isCorrect()).thenReturn(true);

        this.mockMvc
                .perform(
                        get("/ui/wrong").sessionAttr(MathSession.TASK,task).sessionAttr(MathSession.SOLUTION,false)
                ).andExpect(status().isOk())
                .andExpect(model().attribute("task",task))
                .andExpect(model().attribute("reflection",false))
                .andExpect(xpath("//form[@class='play-form']/div[@class='form-group play-form']/label").nodeCount(2))
                .andExpect(xpath("//form[@class='play-form']/div[@class='form-group play-form']/label[" + labelIndexReflection + "]").exists())
                .andExpect(xpath("//form[@class='play-form']/div[@class='form-group play-form']/label[" + labelIndexReflection + "]").string("falsch"))
                .andExpect(view().name("index"));
    }

    @Test
    public void checkTaskIfHasNotTASK() throws Exception {



        this.mockMvc
                .perform(
                        get("/ui/checkTask").param("result", "")
                ).andExpect(status().is(302))
                .andExpect(redirectedUrl("/index/welcome"));

    }

    @Test
    public void checkTaskHasTaskWithoutPram() throws Exception {
        MathSession mathSession = Mockito.mock(MathSession.class);
        Task task;
        task = Factory.buildTask(10);
        mathSession.setTask(task);
        Mockito.when(mathSession.getTask()).thenReturn(task);

        this.mockMvc
                .perform(
                        get("/ui/checkTask").sessionAttr(MathSession.TASK,task).param("result","")
                ).andExpect(model().attribute("error","Bitte Zahlen eingeben"));
    }


    @Test
    public void checkTaskIsCorrect() throws Exception {
        Task task;
        task = Factory.buildTask(10);
        task.setX(5);
        task.setEnumOperator(EnumOperatorImpl.ADD);
        task.setY(5);


        HttpSession session = this.mockMvc
                .perform(
                        get("/ui/checkTask").sessionAttr(MathSession.TASK, task).param("result", "10")
                                .sessionAttr(MathSession.ROUND,roundRepository.save(new Round(1,playerRepository.save(new Player("ahoi")))))
                ).andExpect(redirectedUrl("/ui/play"))
                .andReturn().getRequest().getSession();
        assertNull(session.getAttribute(MathSession.TASK));
        Iterator<Task> iterator = taskRepository.findAll().iterator();
        assertTrue(iterator.hasNext());
        Task next = iterator.next();
        assertEquals(next.getX(),task.getX());
        assertEquals(next.getEnumOperator(),task.getEnumOperator());
        assertEquals(next.getY(),task.getY());
        assertFalse(iterator.hasNext());

    }

    @Test
    public void checkTaskIsNotCorrect() throws Exception{
        Round round =roundRepository.save(new Round(1,playerRepository.save(new Player("ahoi"))));
        round.setWrongSolution(2);
        Task task;
        task = Factory.buildTask(10);
        task.setX(5);
        task.setEnumOperator(EnumOperatorImpl.ADD);
        task.setY(5);


       HttpSession session = this.mockMvc
                .perform(
                        get("/ui/checkTask").sessionAttr(MathSession.TASK, task).param("result", "9")
                                .sessionAttr(MathSession.ROUND,round)
                ).andExpect(redirectedUrl("/ui/play"))
               .andReturn().getRequest().getSession();
        assertNull(session.getAttribute(MathSession.TASK));
        Iterator<Task> iterator = taskRepository.findAll().iterator();
        assertTrue(iterator.hasNext());
        Task next = iterator.next();
        assertEquals(next.getX(),task.getX());
        assertEquals(next.getEnumOperator(),task.getEnumOperator());
        assertEquals(next.getY(),task.getY());
        assertFalse(iterator.hasNext());

       round.setWrongSolution(0);

       this.mockMvc
               .perform(
                       get("/ui/checkTask").sessionAttr(MathSession.TASK, task).param("result", "9")
                               .sessionAttr(MathSession.ROUND,round)
               ).andExpect(redirectedUrl("/ui/wrong"));
    }





}
