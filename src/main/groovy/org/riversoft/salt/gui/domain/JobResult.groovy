package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "jobs_results")
class JobResult {

    /**
     * Уникальный номер результата работы
     */
    @Id
    String id

    boolean isResult

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
//    @DBRef
//    SaltScript saltScript
    @DBRef(lazy = true)
    List<SaltScript> saltScripts = []

    /**
     * Список деталей результатов работы
     */
    @DBRef(lazy = true)
    List<JobResultDetail> jobResultDetails = []
}
