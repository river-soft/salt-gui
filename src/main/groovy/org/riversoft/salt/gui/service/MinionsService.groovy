package org.riversoft.salt.gui.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.model.view.MinionViewModel
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionsService {

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    private SaltClient saltClient

    @Autowired
    private MinionRepository minionRepository

    @Autowired
    private MinionGroupRepository minionGroupRepository

    @Autowired
    SimpMessagingTemplate messagingTemplate

    @Autowired
    ObjectMapper mapper

    void sendSignalToUpdateMinionsCount(def map) {

        messagingTemplate.convertAndSend('/queue/update-minions-count', mapper.writeValueAsString(map))
    }

    /**
     * Поиск принятых миньонов (из бд)
     * @return список названий миньонов
     */
    def findAllAcceptedMinions() {

        log.debug("Start searching accepted minions.")
//
        List<Minion> minions = minionRepository.findAll()

        log.debug("Found [${minions.size()}] accepted minions.")
//
        minions.collect { new MinionViewModel(it) }

//        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
//                saltClient, USER, PASSWORD, AuthModule.PAM);
//        Key.Names keys = keyResults.getData().getResult();
//
//        keys.getMinions()
    }

    /**
     * Поиск миньонов по статусу
     * @param state - статус миньона
     * @return массив содержащий названия миньонов
     */
    def findAllByState(String state) {

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        def result = []

        switch (state) {

            case "unaccepted":

                result = keys.getUnacceptedMinions()

                break

            case "rejected":

                result = keys.getRejectedMinions()

                break

            case "denied":

                result = keys.getDeniedMinions()

                break
        }

        return result
    }

    /**
     * Поиск не принятых миньонов
     * @return список названий миньонов
     */
    def findAllUnaccepted() {

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        keys.getUnacceptedMinions()
    }

    /**
     * Поиск отклоненных миньонов
     * @return список названий миньонов
     */
    def findAllRejected() {

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        keys.getRejectedMinions()
    }

    /**
     * Поиск миньонов которым было отказано
     * @return список названий миньонов
     */
    def findAllDenied() {

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        keys.getDeniedMinions()
    }

    /**
     * Получение количества миньонов сгруппированное по статусу
     * @return map содержащий ключи - статус миньона, количество миньонов этого статуса
     */
    @Scheduled(fixedDelayString = '${salt:5000}', initialDelay = 10000l)
    def getCountsOfMinionsByStatus() {

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        def counts = ["Accepted"  : keys.getMinions().size(),
                      "Unaccepted": keys.getUnacceptedMinions().size(),
                      "Rejected"  : keys.getRejectedMinions().size(),
                      "Denied"    : keys.getDeniedMinions().size()]

        sendSignalToUpdateMinionsCount(counts)

//        return counts
    }

    /**
     * Получение количества миньонов сгруппированное по группе
     * @return map содержащий ключи - группа миньона, количество миньонов в этой группе
     */
    def getCountsOfMinionsByGroup() {

        def counts = [:]

        List<MinionGroup> minionGroups = minionGroupRepository.findAll()

        for (MinionGroup minionGroup : minionGroups) {

            counts.put(minionGroup.name, minionRepository.countByGroupsId(minionGroup.id))
        }

        return counts
    }
}
