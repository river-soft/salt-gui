package org.riversoft.salt.gui.domain

import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "jobs_results")
@ToString(includePackage = false, includeNames = true)
class JobResult extends Base {

    /**
     * Уникальный номер результата работы
     */
    @Id
    String id

    /**
     * Флаг указывающий получены ли результаты
     */
    Boolean isResult

    /**
     * Флаг указывающий на перезапуск скрипта
     */
    boolean reExecuted

    /**
     * Работа по которой береться результат
     */
    @DBRef
    Job job

    /**
     * Миньон на котором выполняется работа
     */
    @DBRef
    Minion minion

    /**
     * Скрипты по которому выполняется работа
     */
    @DBRef(lazy = true)
    List<SaltScript> saltScripts = []

    /**
     * Список деталей результатов работы
     */
    @DBRef(lazy = true)
    List<JobResultDetail> jobResultDetails = []
}
