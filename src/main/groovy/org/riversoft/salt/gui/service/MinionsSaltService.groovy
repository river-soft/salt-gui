package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionsSaltService {

    //region injection

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    private SaltClient saltClient

    //endregion

    /**
     * Принятие синьона на сервере salt
     * @param minionName - название миньона
     */
    def acceptMinion(String minionName) {

        log.debug("Start accepting minion [${minionName}] on salt server.")

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        if (!keys.getUnacceptedMinions().contains(minionName)) {
            log.error("Minion with name [${minionName}] don't found in list of unaccepted minions.")
            throw new Exception("Minion with name [${minionName}] don't found in list of unaccepted minions.")
        }

        WheelResult<Object> acceptKeyResults = Key.accept(minionName).callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);

        log.debug("Finish accepting minion [${minionName}] on salt server.")

        def test = 1
    }

    /**
     * Отклонение миньона на сервере salt
     * @param minionName - название миньона
     */
    def rejectMinion(String minionName) {

        log.debug("Start rejecting minion [${minionName}] on salt server.")

        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        if (!keys.getUnacceptedMinions().contains(minionName)) {
            log.error("Minion with name [${minionName}] don't found in list of unaccepted minions.")
            throw new Exception("Minion with name [${minionName}] don't found in list of unaccepted minions.")
        }

        WheelResult<Object> rejectResults = Key.reject(minionName).callSync(
                saltClient, USER, PASSWORD, AuthModule.PAM);

        log.debug("Finish rejecting minion [${minionName}] on salt server.")
    }

}
