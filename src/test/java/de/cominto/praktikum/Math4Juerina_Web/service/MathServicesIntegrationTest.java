package de.cominto.praktikum.Math4Juerina_Web.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import de.cominto.praktikum.Math4Juerina_Web.database.Player;
import de.cominto.praktikum.Math4Juerina_Web.database.PlayerRepository;
import de.cominto.praktikum.Math4Juerina_Web.web.MathSession;
import  org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"integration"})
@DatabaseSetup("/dbunit/testData.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class MathServicesIntegrationTest {

    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void test() {
        Player player = new Player();
        player.setUserName("test");
        playerRepository.save(player);
        assertThat(player.getPlayerId(),is(Matchers.notNullValue()));
        List<Player> playerList = new ArrayList<>();
        playerRepository.findAll().forEach(playerList::add);
        assertThat(playerList.size(), is(1));
        assertThat(playerList.get(0).getUserName(), is(player.getUserName()));
    }
}

