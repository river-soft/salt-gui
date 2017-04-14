package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.Grains
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.MinionNotFoundException
import org.riversoft.salt.gui.exception.SaltException
import org.riversoft.salt.gui.exception.SaltGuiException
import org.riversoft.salt.gui.model.view.MinionGroupViewModel
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.riversoft.salt.gui.results.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.method.P
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Slf4j
@Service
class MinionDetailsService {

    //region

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Value('${salt.minion.details.properties}')
    private String[] properties

    @Autowired
    private SaltClient saltClient

    @Autowired
    private MinionRepository minionRepository

    @Autowired
    private MinionsSaltService minionsSaltService

    @Autowired
    private MinionGroupRepository minionGroupRepository

    //endregion

    /**
     * Получение списка миньонов сгрупированных по группам
     * @return список объектов MinionGroupViewModel
     * @see MinionGroupViewModel
     */
    List<MinionGroupViewModel> findAllGroupedMinions() {

        log.debug("Start searching grouped minions.")

        List<MinionGroup> minionsGroups = minionGroupRepository.findAll()

        log.debug("Found [${minionsGroups.size()}] groups and " +
                "[${minionsGroups.size() ? minionsGroups.sum { it.minions.size() } : 0}] minions.")

        minionsGroups.collect { new MinionGroupViewModel(it) }
    }

    /**
     * Получение деталей о миньоне
     * @param minionName - именя миньона
     * @return объект данных ключ-значение
     */
    def findMinionDetails(String minionName) {

        log.debug("Start getting details for minion with name [${minionName}].")

        Minion minion = minionRepository.findByName(minionName)
        if (!minion) {
            log.error("Minion with name [${minionName}] not found.")
            throw new MinionNotFoundException("Minion with name [${minionName}] not found.")
        }

        try {

            def acceptedMinions = minionsSaltService.getAllAcceptedMinions()

            if (!acceptedMinions.contains(minionName)) {
                log.error("Minion with name [${minionName}] not found in list of accepted minions on salt server.")
                throw new MinionNotFoundException("Minion with name [${minionName}] not found in list of accepted minions on salt server.")
            }

            // Set targets
            Target<List<String>> minionList = new MinionList(minionName);

            // call Grains.item
            Map<String, Result<Map<String, Object>>> grainResults = Grains.item(false, properties)
                    .callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

            // get result of Grains.item
            def result = grainResults.collect { ["${it.key}": it.value?.xor?.right()?.value] }

            return result

        } catch (MinionNotFoundException e) {

            log.error("Minion with name [${minionName}] not found in list of accepted minions on salt server.", e)
            throw new MinionNotFoundException("Minion with name [${minionName}] not found in list of accepted minions on salt server.",
                    'error.minions.server.not_found', [minionName])
        } catch (SaltException e) {

            if (e.cause instanceof SocketTimeoutException) {

                log.error("Minion with name [${minionName}] don't answer", e)
                throw new SaltGuiException("Minion with name [${minionName}] don't answer", e, 'error.minion.read.time.out', [minionName])
            } else {

                log.error("Error of getting minion [${minionName}] details from salt server.")
                throw new SaltGuiException("Error of getting minion [${minionName}] details from salt server.", e,
                        "error.minion.get.details", [minionName])
            }
        } catch (Exception e) {

            log.error("Error of getting minion [${minionName}] details from salt server.")
            throw new SaltGuiException("Error of getting minion [${minionName}] details from salt server.", e,
                    "error.minion.get.details", [minionName])
        }
    }
}