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

import java.util.concurrent.ConcurrentHashMap

@Slf4j
@Service
class MinionsService {

    @Value('${salt.user}') private String USER
    @Value('${salt.password}') private String PASSWORD

    @Autowired private ObjectMapper mapper
    @Autowired private SaltClient saltClient
    @Autowired private MinionRepository minionRepository
    @Autowired private SimpMessagingTemplate messagingTemplate
    @Autowired private MinionGroupRepository minionGroupRepository

    ConcurrentHashMap<String, String[]> saltMinions = new ConcurrentHashMap()

    /**
     * Поиск принятых миньонов (из бд)
     * @return список названий миньонов
     */
    List<MinionViewModel> findAllAcceptedMinions() {

        log.trace("Start searching accepted minions.")

        List<Minion> minions = minionRepository.findAll()

        log.trace("Found [${minions.size()}] accepted minions.")

        minions.collect { new MinionViewModel(it) }
    }

    @Scheduled(fixedDelayString = '${salt.get-minions.time:5000}')
    void getMinionsFromSalt() {
        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM)
        Key.Names keys = keyResults.getData().getResult()

        saltMinions.putAll([
                accepted  : keys.getMinions() as String[],
                denied    : keys.getDeniedMinions() as String[],
                rejected  : keys.getRejectedMinions() as String[],
                unaccepted: keys.getUnacceptedMinions() as String[]
        ])

        log.trace("""
    Update minions:
        accepted: ${saltMinions.get('accepted')}
        denied: ${saltMinions.get('denied')}
        rejected: ${saltMinions.get('rejected')}
        unaccepted: ${saltMinions.get('unaccepted')}
""")
    }

    /**
     * Поиск и отправка принятых миньонов (из бд)
     */
    @Scheduled(fixedDelayString = '${salt.minions.update_list_by_status:5000}', initialDelay = 1000L)
    def findAndSendAllAcceptedMinions() {

        sendAllMinionsByStatuses("/queue/minions/update-accepted-minions", "accepted", findAllAcceptedMinions())
    }

    @Scheduled(fixedDelayString = '${salt.minions.update_list_by_status:30000}', initialDelay = 1000L)
    def findAndSendAllMinionsByStatuses() {

        sendAllMinionsByStatuses("/queue/minions/update-denied-minions", "denied", saltMinions.get('denied'))
        sendAllMinionsByStatuses("/queue/minions/update-rejected-minions", "rejected", saltMinions.get('rejected'))
        sendAllMinionsByStatuses("/queue/minions/update-unaccepted-minions", "unaccepted", saltMinions.get('unaccepted'))
    }

    /**
     * Получение количества миньонов сгруппированное по статусу
     * @return map содержащий ключи - статус миньона, количество миньонов этого статуса
     */
    @Scheduled(fixedDelayString = '${salt.minions.update_counts_interval:10000}', initialDelay = 1000L)
    def getAndSendCountsOfMinionsByStatus() {
        sendCountsOfMinionsByStatus([
                "client.minions.state.accepted"  : saltMinions.get('accepted')?.size() ?: 0,
                "client.minions.state.unaccepted": saltMinions.get('unaccepted')?.size() ?: 0,
                "client.minions.state.rejected"  : saltMinions.get('rejected')?.size() ?: 0,
                "client.minions.state.denied"    : saltMinions.get('denied')?.size() ?: 0
        ])
    }

    /**
     * Получение количества миньонов сгруппированное по группе
     * @return map содержащий ключи - группа миньона, количество миньонов в этой группе
     */
    @Scheduled(fixedDelayString = '${salt.minions.update_counts_interval:10000}', initialDelay = 1000L)
    def getAndSendCountsOfMinionsByGroup() {

        log.trace("Start getting minions counts by groups from DB.")

        def counts = [:]

        List<MinionGroup> minionGroups = minionGroupRepository.findAll()

        for (MinionGroup minionGroup : minionGroups) {

            counts.put(minionGroup.name, minionRepository.countByGroupsId(minionGroup.id))
        }

        log.trace("Start getting minions counts by groups from DB.")

        sendCountsOfMinionsByGroup(counts)
    }

    /**
     * Отправка данных миньонов
     * @param signal - сигнал для отправки результатов
     * @param status - статус миньона
     * @param map - объект/мапа с данными
     */
    void sendAllMinionsByStatuses(String signal, String status, def map) {

        log.trace("Update ${status} minions list to [${mapper.writeValueAsString(map)}].")

        messagingTemplate.convertAndSend(signal, mapper.writeValueAsString(map))
    }

    /**
     * Отправка количества миньонов по статусам
     * @param map - объект/мапа с данными
     */
    void sendCountsOfMinionsByStatus(def map) {

        log.trace("Update counts of minions by statuses [${mapper.writeValueAsString(map)}]")

        messagingTemplate.convertAndSend('/queue/minions/update-counts-status', mapper.writeValueAsString(map))
    }

    /**
     * Отправка количества миньонов по группам
     * @param map - объект/мапа с данными
     */
    void sendCountsOfMinionsByGroup(def map) {

        log.trace("Update counts of minions by group [${mapper.writeValueAsString(map)}]")

        messagingTemplate.convertAndSend('/queue/minions/update-counts-group', mapper.writeValueAsString(map))
    }
}
