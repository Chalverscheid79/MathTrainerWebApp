package de.cominto.praktikum.Math4Juerina_Web.service.impl;


import de.cominto.praktikum.Math4Juerina_Web.MathProperties;
import de.cominto.praktikum.Math4Juerina_Web.database.*;
import de.cominto.praktikum.Math4Juerina_Web.service.MathServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MathServicesImplTest {

    private  static final Logger LOG = LoggerFactory.getLogger(MathServicesImplTest.class);
    private Random random = new Random();

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MathProperties mathProperties;

    private MathServices mathServices;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        this.mathServices = new MathServicesImpl(this.taskRepository, this.playerRepository, this.roundRepository,mathProperties,null);
    }

    @Test
    public void loadPlayer() {
        String defaultUserConfig = "default";
        Mockito.when(this.mathProperties.getUserName()).thenReturn(defaultUserConfig);
        Mockito.when(this.playerRepository.findFirstByUserName(Mockito.anyString())).thenReturn(new Player());
        Player player = this.mathServices.loadPlayer("");

        assertThat(player, is(notNullValue()));

        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(playerRepository).findFirstByUserName(arg.capture());

        String defaultUsername = arg.getValue();
        assertThat(defaultUsername, is(notNullValue()));
        assertThat(defaultUsername,is(defaultUserConfig));
    }

    @Test
    public void loadPlayerCheckString(){
        String defaultUserConfig = "default";
        String defaultUserConfigNull = "byNull";
        String userName = "abc";
        List<String> defaultUsername;

        Mockito.when(this.mathProperties.getUserName()).thenReturn(defaultUserConfig);
        this.mathServices.loadPlayer("");
        Mockito.when(this.mathProperties.getUserName()).thenReturn(defaultUserConfigNull);
        this.mathServices.loadPlayer(null);
        Mockito.when(this.mathProperties.getUserName()).thenReturn(defaultUserConfigNull);
        this.mathServices.loadPlayer(userName);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.playerRepository, times(3)).findFirstByUserName(argumentCaptor.capture());
        defaultUsername = argumentCaptor.getAllValues();
        assertThat(defaultUsername.size(),is(3));
        assertThat(defaultUsername.get(0),is(defaultUserConfig));
        assertThat(defaultUsername.get(1),is(defaultUserConfigNull));
        assertThat(defaultUsername.get(2),is(userName));

    }

    @Test
    public void loadPlayerCheckPlayerIsNotNull(){
        String userName = "test";

        Mockito.when(this.playerRepository.findFirstByUserName(Mockito.anyString())).thenReturn(new Player());
        this.mathServices.loadPlayer(userName);
        Player acceptedPlayer = this.playerRepository.findFirstByUserName(userName);
        assertThat(acceptedPlayer,is(notNullValue()));

    }

    @Test
    public void loadPlayerCheckPlayerIsNull(){
        String userName = "test";

        Mockito.when(this.playerRepository.findFirstByUserName(Mockito.anyString())).thenReturn(null);

        LOG.info("Player {}",this.playerRepository.findFirstByUserName(userName));
        Player acceptedPlayer = this.mathServices.loadPlayer(userName);
        assertThat(acceptedPlayer,is(notNullValue()));
        assertThat(acceptedPlayer.getUserName(),is(userName));
        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(this.playerRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(),is(acceptedPlayer));

    }

    @Test
    public void getRound() {
        Round round;
        int exercise = 9;
        when(this.mathProperties.getNumTasks()).thenReturn(10);
        round = this.mathServices.getRound(exercise,new Player());
        assertThat(round.getExercise(),is(10));



    }

    @Test
    public void saveRound() {
    }

    @Test
    public void findRoundById() {
    }

    @Test
    public void saveTask() {
    }

    @Test
    public void getCorrectPercent() {
    }

    @Test
    public void findByLastRoundAndDay() {
    }

    @Test
    public void findAllTasksFromLastFiveRoundsInfrintAcutalRound() {

        LOG.info("Wert 1 {}",random.nextInt());
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Collections.emptyList());
        MathServices mathServices = new MathServicesImpl(taskRepository,null,null,null ,null);
        Round round = new Round();

        List<Double> tasks = mathServices.findAllTasksFromLastFiveRoundsInfrintAcutalRound(round, 1);
        assertThat(tasks, is(notNullValue()));
        assertThat(tasks,is(emptyCollectionOf(Double.class)));

        verify(taskRepository).findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any());

        LOG.info("Wert 1 {}",random.nextInt());
        Mockito.when(taskRepository.findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(Collections.emptyList());
        assertThat(tasks, is(notNullValue()));
        assertThat(tasks,is(emptyCollectionOf(Double.class)));

        verify(taskRepository).findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any());
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
        MathServices mathServices = new MathServicesImpl(taskRepository,null,null,null,null);
        Round round = new Round();


        List<Double> tasks = mathServices.findAllTasksFromLastFiveRoundsInfrintAcutalRound(round, 1);
        assertThat(tasks, is(notNullValue()));
        assertThat(tasks.size(),is(numRoundIds));
        for (int i = 0; i < expectedValues.size();i++){
            assertThat(Math.round(expectedValues.get(i)*100)/100.0,is(tasks.get(i)));
        }

        verify(taskRepository).findAllTasksFromLastFiveRoundsWithoutAcutalRound(Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void getNumberOfErrors() {
    }

    @Test
    public void getPercentCorrectFromDateToLocalDate() {
    }

    @Test
    public void getCountAllTaskFromDateToDateGroupByDay() {
    }
}