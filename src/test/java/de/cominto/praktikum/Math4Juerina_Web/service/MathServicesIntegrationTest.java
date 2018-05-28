package de.cominto.praktikum.Math4Juerina_Web.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.PlayerRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.Task;
import de.cominto.praktikum.Math4Juerina_Web.database.TaskRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"integration"})
@DatabaseSetup(value={"/dbunit/testData.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MathServicesIntegrationTest {



    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TaskRepository taskRepository;


    @Test
    public void playerRepositoryTest() {

        List<Player> playerList = new ArrayList<>();
        playerRepository.findAll().forEach(playerList::add);
        assertThat(playerList,is(Matchers.notNullValue()));
        assertThat(playerList.size(), is(3));
        assertThat(playerList.get(0).getUserName(), is("chris"));
        assertThat(playerList.get(1).getUserName(), is("kai"));
        assertThat(playerList.get(2).getUserName(), is("ahoi"));
    }

    @Test
    public void TaskRepositoryTes(){
        List<Task> tasks = new ArrayList<>();

    }
}

