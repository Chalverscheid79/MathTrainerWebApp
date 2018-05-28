package de.cominto.praktikum.Math4Juerina_Web.web;

import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.PlayerRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;
import de.cominto.praktikum.Math4Juerina_Web.service.Factory;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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


    @Test
    public void welcomeControllerTest() throws Exception {

        this.mockMvc.perform(get("/index/welcome").accept(MediaType.TEXT_HTML)).andExpect(status().isOk()).andExpect(xpath("//input[@id='userName']").exists())
            .andExpect(xpath("//input[@id='exercise']").exists());


    }

    @Test
    public void loginControllerTest() throws Exception{
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
        assertThat(playerList,is(Matchers.notNullValue()));
        assertThat(playerList.size(), is(1));

    }

    @Test
    public void playControllerTest()throws Exception{
        Player player = new Player("ahoi");

//        Round round = new Round(2, player);
        Round round = Mockito.mock(Round.class);
        Mockito.when(round.getPlayer()).thenReturn(player);
        Mockito.when(round.getUserName()).thenReturn(player.getUserName());
        Mockito.when(round.getExercise()).thenReturn(2);
        Task task = Factory.buildTask(15);
        Mockito.when(round.createTask()).thenReturn(task);

        this.mockMvc
                .perform(
                        get("/ui/play").sessionAttr("round", round)
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", task))
                .andExpect(xpath("//form[@class='play-form']/h1/span").string("ahoi"))
                .andExpect(xpath("//label[@for='result']").string(String.format("%d %s %d =", task.getX(), task.getEnumOperator().getOperator(), task.getY())));

    }
}
