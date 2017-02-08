package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.model.view.MinionViewModel
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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

    /**
     * Поиск принятых миньонов (из бд)
     * @return список названий миньонов
     */
    def /*List<SaltScriptGroupViewModel>*/ findAllAcceptedMinions() {

//        log.debug("Start searching grouped minions.")
//
        List<Minion> minions = minionRepository.findAll()

//        log.debug("Found [${saltScriptGroups.size()}] groups and " +
//                "[${saltScriptGroups.size() ? saltScriptGroups.sum { it.scriptList.size() } : 0}] script.")
//
        minions.collect { new MinionViewModel(it) }

//        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
//                saltClient, USER, PASSWORD, AuthModule.PAM);
//        Key.Names keys = keyResults.getData().getResult();
//
//        keys.getMinions()
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
     * Количество миньонов сгруппированное по статусу
     * @return map содержащий ключи - статус миньона, количество миньонов этого статуса
     */
    def getCountsOfMinionsByStatus() {

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        def counts = ["Accepted"  : keys.getMinions().size(),
                      "Unaccepted": keys.getUnacceptedMinions().size(),
                      "Rejected"  : keys.getRejectedMinions().size(),
                      "Denied"    : keys.getDeniedMinions().size()]

        return counts
    }

    /**
     * Количество миньонов сгруппированное по группе
     * @return map содержащий ключи - группа миньона, количество миньонов в этой группе
     */
    def getCountsOfMinionsByGroup() {
        //TODO
    }
}
