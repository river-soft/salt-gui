package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Job
import org.riversoft.salt.gui.domain.JobResult
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.MinionNotFoundException
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.model.EditMinion
import org.riversoft.salt.gui.model.view.MinionViewModel
import org.riversoft.salt.gui.repository.JobRepository
import org.riversoft.salt.gui.repository.JobResultDetailRepository
import org.riversoft.salt.gui.repository.JobResultRepository
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionCRUDService {

    //region injection

    @Autowired
    private MinionRepository minionRepository

    @Autowired
    private MinionGroupRepository minionGroupRepository

    @Autowired
    MinionGroupService minionGroupService

    @Autowired
    JobResultRepository jobResultRepository

    @Autowired
    JobRepository jobRepository

    @Autowired
    JobResultDetailRepository jobResultDetailRepository

    //endregion

    /**
     * Получение миньона по имени
     * @param minionName - имя меньона
     * @return объект Minion
     * @see Minion
     */
    Minion getMinionByName(String minionName) {

        Minion minion = minionRepository.findByName(minionName)

        if (!minion) {
            log.error("Minion with name [${minionName}] not found.")
            throw new MinionNotFoundException("Minion with name [${minionName}] not found.")
        }

        minion
    }

    /**
     * Поиск миньона по имени
     * @param minionName - имя меньона
     * @return объект Minion
     * @see Minion
     */
    Minion findMinionByName(String minionName) {

        minionRepository.findByName(minionName)
    }

    /**
     * Создание миньона
     * @param createMinion - модель создания миньона
     * @param minionGroups - перечень объектов групп миньонов
     * @return объект Minion
     * @see Minion
     */
    Minion createMinion(CreateMinion createMinion, List<MinionGroup> minionGroups) {

        Minion minion = minionRepository.findByName(createMinion.name)

        if (minion) {
            log.error("Minion with name [${createMinion.name}] already exist.")
            throw new SaltScriptAlreadyExistException("Minion with name [${createMinion.name}] already exist.",
                    412, "Minion with name [${createMinion.name}] already exist.")
        }

        log.debug("Start creating minion with name [${createMinion.name}].")

        minion = new Minion(
                name: createMinion.name.trim(),
                groups: minionGroups,
                createDate: new Date(),
                lastModifiedDate: new Date()
        )

        minionRepository.save(minion)

        log.debug("Successfully created minion with name [${minion.name}].")

        for (MinionGroup minionGroup : minionGroups) {

            log.debug("Adding minion [${minion.name}] to group [${minionGroup.name}].")

            minionGroup.minions.add(minion)
            minionGroup.lastModifiedDate = new Date()
            minionGroupRepository.save(minionGroup)

            log.debug("Successfully added minion [${minion.name}] to group [${minionGroup.name}].")
        }

        return minion
    }

    /**
     * Обновление миньона т.е. его групп
     * @param editMinion - объект EditMinion
     * @return
     */
    def updateMinion(EditMinion editMinion) {

        Minion minion = minionRepository.findByName(editMinion.name)

        List<String> minionsGroups = minion.groups.collect { it.name }

        List<String> newMinionsGroups = editMinion.groups.collect { it.name }

        //добавление не достающей группы
        for (String newGroup : newMinionsGroups) {

            if (!minionsGroups.contains(newGroup)) {

                MinionGroup minionGroup = minionGroupRepository.findByName(newGroup)

                log.debug("Start adding group [${minionGroup.name}] to minion [${minion.name}].")

                minion.groups.add(minionGroup)

                minion.lastModifiedDate = new Date()
                minionRepository.save(minion)

                minionGroup.minions.add(minion)
                minionGroup.lastModifiedDate = new Date()
                minionGroupRepository.save(minionGroup)

                log.debug("Finish adding group [${minionGroup.name}] to minion [${minion.name}].")
            }
        }

        //удаление не нужной группы
        for (String group : minionsGroups) {

            if (!newMinionsGroups.contains(group)) {

                MinionGroup minionGroup = minionGroupRepository.findByName(group)

                log.debug("Start deleting group [${minionGroup.name}] from minion [${minion.name}].")

                minion.groups.removeAll { it.name == group }

                minion.lastModifiedDate = new Date()
                minionRepository.save(minion)

                minionGroup.minions.removeAll { it.name == minion.name }
                minionGroup.lastModifiedDate = new Date()
                minionGroupRepository.save(minionGroup)

                log.debug("Finish deleting group [${minionGroup.name}] from minion [${minion.name}].")
            }
        }

        new MinionViewModel(minion)
    }

    /**
     * Удаление миньона
     * @param объект Minion
     */
    def deleteMinion(Minion minion) {

        log.debug("Start deleting minion with name [${minion.name}].")

        String deletedMinionMessage = "Finish deleting minion with name [${minion.name}] and id [${minion.id}]."

        //удаление миньона из групп где он есть
        for (MinionGroup group : minion.groups) {

            group.minions.removeAll { it.id == minion.id }

            minionGroupRepository.save(group)
        }

        //удаление миньона из результатов где он есть и деталей результатов?
        List<JobResult> jobResults = jobResultRepository.findAllByMinionId(minion.id)

        for (JobResult jobResult : jobResults) {

            log.debug("Start deleting Job Results by minion with name [${minion.name}].")

            //удаление результата из работы где он есть
            Job job = jobResult.job
            job.results.removeAll{it.id == jobResult.id}

            jobRepository.save(job)

            //удаление деталей
            jobResult.jobResultDetails.each {
                jobResultDetailRepository.delete(it.id)
            }

            jobResultRepository.delete(jobResult.id)

            log.debug("Finish deleting Job Results by minion with name [${minion.name}].")
        }

        //удаление самого миньога
        minionRepository.delete(minion.id)

        log.debug(deletedMinionMessage)
    }

}
