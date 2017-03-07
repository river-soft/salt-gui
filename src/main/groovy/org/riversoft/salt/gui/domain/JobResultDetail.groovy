package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "jobs_results_details")
class JobResultDetail {

    /**
     * Уникальный номер единицы результата работы
     */
    @Id
    String id

    /**
     * Выполняемая команда
     */
     String cmd

    /**
     * Комментарий результата
     */
    String comment

    /**
     * Название результата
     */
    String name

    /**
     * Время начала выполнения работы
     */
    Date startTime

    /**
     * Флаг указывающий на успешное выполнение
     */
    boolean result

    /**
     * Длительность выполнения работы
     */
    Double duration

    /**
     * Изменения
     */
    String changes

    /**
     * Описание выполняемой работы
     */
    String description

    /**
     * Работа по которой береться результат
     */
    @DBRef
    JobResult jobResult
}
