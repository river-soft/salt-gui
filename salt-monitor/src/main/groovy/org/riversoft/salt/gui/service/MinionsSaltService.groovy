package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.exception.SaltGuiException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionsSaltService {

    @Value('${salt.user}') private String USER
    @Value('${salt.password}') private String PASSWORD

    @Autowired private SaltClient saltClient
    @Autowired private MinionsService minionsService

    /**
     * Получение списка принятых миньонов с сервера salt
     * @return перечень принятых миньонов
     */
    def getAllAcceptedMinions() {
        minionsService.saltMinions.get('accepted') as String[]
    }

    /**
     * Принятие миньона на сервере salt
     * @param minionName - название миньона
     */
    def acceptMinion(String minionName) {

        log.debug("Start accepting minion [${minionName}] on salt server.")

        try {

            if (!minionsService.saltMinions.get('unaccepted').contains(minionName)) {
                log.error("Minion with name [${minionName}] not found in list of unaccepted minions.")
                throw new Exception("Minion with name [${minionName}] not found in list of unaccepted minions.")
            }

            WheelResult<Object> acceptKeyResults = Key.accept(minionName).callSync(
                    saltClient, USER, PASSWORD, AuthModule.PAM)

            log.debug("Finish accepting minion [${minionName}] on salt server.")

        } catch (Exception e) {

            log.error("Error of accepting minion on salt server.")
            throw new SaltGuiException("Error of accepting minion on salt server.", e,
                    "error.minions.accept")
        }
    }

    /**
     * Отклонение миньона на сервере salt
     * @param minionName - название миньона
     */
    def rejectMinion(String minionName) {

        log.debug("Start rejecting minion [${minionName}] on salt server.")

        try {

            if (!minionsService.saltMinions.get('unaccepted').contains(minionName)) {
                log.error("Minion with name [${minionName}] don't found in list of unaccepted minions.")
                throw new Exception("Minion with name [${minionName}] don't found in list of unaccepted minions.")
            }

            WheelResult<Object> rejectResults = Key.reject(minionName).callSync(
                    saltClient, USER, PASSWORD, AuthModule.PAM)

            log.debug("Finish rejecting minion [${minionName}] on salt server.")

        } catch (Exception e) {

            log.error("Error of rejecting minion on salt server.")
            throw new SaltGuiException("Error of rejecting minion on salt server.", e,
                    "error.minions.reject")
        }
    }

    /**
     * Удаление миньона на сервере salt
     * @param minionName - название миньона
     */
    def deleteMinion(String minionName) {

        log.debug("Start deleting minion [${minionName}] on salt server.")

        try {

            WheelResult<Object> deleteResults = Key.delete(minionName).callSync(
                    saltClient, USER, PASSWORD, AuthModule.PAM)

            log.debug("Finish deleting minion [${minionName}] on salt server.")

        } catch (Exception e) {

            log.error("Error of deleting minion on salt server.")
            throw new SaltGuiException("Error of deleting minion on salt server.", e,
                    "error.minions.delete")
        }
    }
}
