package de.cominto.praktikum.Math4Juerina_Web.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import de.cominto.praktikum.Math4Juerina_Web.database.*;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneId.systemDefault;
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

    @Test
    public void TaskRepositoryTes(){
        List<Task> tasks = new ArrayList<>();

    }

//    private void updateDatabase(){
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1525730400000L),systemDefault());
//        LocalDate localDate = LocalDate.now(clock);
//
//        long startDate = 1525730400000L;
//        long endDate = System.currentTimeMillis();
//        int dif = (int)TimeUnit.MILLISECONDS.toDays(Math.abs(endDate - startDate));
//
//        LOG.info("###############clock: {}", localDate);
//        LOG.info("###############Diference: {}", dif);
//        LOG.info("###############Local add Days: {}", localDate.plusDays(dif));
//
//        Calendar cal = Calendar.getInstance();
//
//        for (Round oneRound : roundRepository.findAll()){
//            cal.setTime(oneRound.getDay());
//            cal.add(Calendar.DAY_OF_MONTH,dif);
//            oneRound.setDay(cal.getTime());
//            roundRepository.save(oneRound);
//            LOG.info("###############Changed Date in entity: {}, RoundId {}}", oneRound.getDay(), oneRound.getRoundId());
//            for(Task oneTask : taskRepository.findByRoundRoundId(oneRound.getRoundId())){
//                cal.setTime(oneTask.getPracticeDay());
//                cal.add(Calendar.DAY_OF_MONTH,dif);
//                oneTask.setPracticeDay(cal.getTime());
//                LOG.info("###############Changed Date in entity: {}, TaskId {}", oneTask.getPracticeDay(), oneTask.getTaskId());
//                taskRepository.save(oneTask);
//            }
//        }
//        int i = 0;
//        for (Task t : taskRepository.findAll()) i++;
//        LOG.info("############### datasize {}",i);
//    }
}

