package de.cominto.praktikum.Math4Juerina_Web.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.PlayerRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.RoundRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.TaskRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MathServicesIntegrationTest {



    Logger LOG = LoggerFactory.getLogger(MathServicesIntegrationTest.class);
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    RoundRepository roundRepository;


    @Test
    @DatabaseSetup(value={"/dbunit/testData.xml"})
//    clean inmemory Database after Test
    @DatabaseTearDown("/dbunit/testData_clean.xml")
    public void playerRepositoryTest() {

        List<Player> playerList = new ArrayList<>();
        playerRepository.findAll().forEach(playerList::add);
        assertThat(playerList,is(Matchers.notNullValue()));
        assertThat(playerList.size(), is(7));
        assertThat(playerList.get(0).getUserName(), is("unnamed user"));
        assertThat(playerList.get(1).getUserName(), is("Chris"));
        assertThat(playerList.get(2).getUserName(), is("hjkhjk"));
        assertThat(playerList.get(3).getUserName(), is("Kai"));
        assertThat(playerList.get(4).getUserName(), is("Papa"));
        assertThat(playerList.get(5).getUserName(), is("test"));
        assertThat(playerList.get(6).getUserName(), is("ahoi"));


    }
}

