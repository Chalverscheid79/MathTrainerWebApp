package de.cominto.praktikum.Math4Juerina_Web.service.impl;

import de.cominto.praktikum.Math4Juerina_Web.database.Round;
import de.cominto.praktikum.Math4Juerina_Web.database.TaskRepository;
import de.cominto.praktikum.Math4Juerina_Web.database.WrapperCount;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.MatcherGenericTypeExtractor;
import org.mockito.verification.VerificationMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class MathServicesImplTest {
    private  static final Logger LOG = LoggerFactory.getLogger(MathServicesImplTest.class);
    private Random random = new Random();

    @Test
    public void findAllTasksFromLastFiveRoundsInfrintAcutalRound() {
        LOG.info("Wert 1 {}",random.nextInt());
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Collections.emptyList());
        MathServices mathServices = new MathServicesImpl(taskRepository);
        Round round = new Round();


        List<Double> tasks = mathServices.findAllTasksFromLastFiveRoundsInfrintAcutalRound(round, 1);
        assertThat(tasks, is(notNullValue()));
        assertThat(tasks,is(emptyCollectionOf(Double.class)));

        Mockito.verify(taskRepository).findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void findAllTasksFromLastFiveRoundsInfrintAcutalRound2() {
        final int numRoundIds = random.nextInt(5)+1;
        Set<Long> roundIds = new TreeSet<>();
        List<WrapperCount> wrapperCounts = new ArrayList();
        List <Double> expectedValues = new ArrayList<>();

        while ( roundIds.size() < numRoundIds){
            long roundId = random.nextInt(100000)+1;
            roundIds.add(roundId);
        }

        for (final long id : roundIds ){

            WrapperCount wrapperCount1 = new WrapperCount(random.nextBoolean(),random.nextInt(30)+1,id);
            wrapperCounts.add(wrapperCount1);

            long correctTaks = (wrapperCount1.isCorrect())?  wrapperCount1.getTasks(): 0;
            long totalTasks = wrapperCount1.getTasks();


            if(random.nextBoolean()){
                WrapperCount wrapperCount2 = new WrapperCount((!wrapperCount1.isCorrect()), random.nextInt(30) + 1, id);
                wrapperCounts.add(wrapperCount2);
                totalTasks += wrapperCount2.getTasks();
                correctTaks += (wrapperCount2.isCorrect())?  wrapperCount2.getTasks(): 0;
            }

            expectedValues.add(correctTaks * 100.0 / totalTasks);
        }
//        WrapperCount wrapperCount = new WrapperCount(true,0,);
        LOG.info("Wert 2 {} {}",numRoundIds, roundIds);
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(wrapperCounts);
        MathServices mathServices = new MathServicesImpl(taskRepository);
        Round round = new Round();


        List<Double> tasks = mathServices.findAllTasksFromLastFiveRoundsInfrintAcutalRound(round, 1);
        assertThat(tasks, is(notNullValue()));
        assertThat(tasks.size(),is(numRoundIds));
        for (int i = 0; i < expectedValues.size();i++){
            assertThat(expectedValues.get(i),is(tasks.get(i)));
        }

        Mockito.verify(taskRepository).findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void test() {

        LOG.info("A: {}", (100 - (100 / 3 * 1)));
        LOG.info("B: {}", Math.round((100 - (100.0 / 3 * 1))*100)/100.0);
    }
}

