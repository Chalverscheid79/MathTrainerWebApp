package de.cominto.praktikum.Math4Juerina_Web.database.impl;

import de.cominto.praktikum.Math4Juerina_Web.database.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Query JPA defined in the interface
 */
public class JpaTaskRepositoryImpl implements JpaTaskRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JpaTaskRepositoryImpl.class);
    @Autowired
    private EntityManager entityManager;

    /**
     * Criteria an query how founds the all task in entity
     * @return the first 10 results in an list objekt, can be null
     */
    @Override
    public List<Task> findAllTasks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);
        cq.select(task).where(cb.equal(task.get("taskId"), 1));
        TypedQuery<Task> query = entityManager.createQuery(cq);
        return query.setMaxResults(10).getResultList();


    }

    /**
     *  Criteia query how search the last 5 rounds from the actual player without the last round
     * @param roundObjekt Object from round entity
     * @param fromDate Date object
     * @param toDate Date object
     * @return Object list from WrapperCount class, can be null
     */
    @Override
    public List<WrapperCount> findAllTasksFromLastFiveRoundsWithoutAcutalRound(Round roundObjekt, Date fromDate, Date toDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<WrapperCount> cq = cb.createQuery(WrapperCount.class);
        Root<Task> task = cq.from(Task.class);
        Join<Task, Round> round = task.join(Task_.round);

        CompoundSelection<WrapperCount> projection = cb.construct(
                WrapperCount.class,
                task.get(Task_.correct),
                cb.count(task.get(Task_.taskId)),
                round.get(Round_.roundId));

        cq.select(projection)
                .where(
                        cb.greaterThan(task.get(Task_.practiceDay), fromDate),
                        cb.lessThan(task.get(Task_.practiceDay), toDate),
                        cb.lessThan(round.get(Round_.roundId),roundObjekt.getRoundId()),
                        cb.equal(round.get(Round_.player).get(Player_.playerId),roundObjekt.getPlayer().getPlayerId()))
                .groupBy(round.get(Round_.roundId),task.get(Task_.correct))
                .orderBy(cb.desc(round.get(Round_.roundId)));
        int maxListLenght = 5;
        List<WrapperCount> wrapperCount = entityManager.createQuery(cq).setMaxResults(maxListLenght * 2).getResultList();
        if (wrapperCount.size() <= maxListLenght){
            return wrapperCount;
        }
        Set<Long> roundIds = new HashSet<>();
        List<WrapperCount> lastFiveWrapperCounts = new ArrayList<>();

        for (WrapperCount wc : wrapperCount ){
            if(roundIds.size() < maxListLenght || roundIds.contains(wc.getRoundId())){
                roundIds.add(wc.getRoundId());
                lastFiveWrapperCounts.add(wc);
            }else{
                break;
            }
        }
        return lastFiveWrapperCounts;
    }
}
