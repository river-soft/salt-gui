package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "jobs")
class Job {

    /**
     * Уникальный номер работы
     */
    @Id
    String jid

    /**
     * Время начала выполнения работы
     */
    Date startTime

    /**
     * Флаг указывающий на то что задача выполнена
     */
    boolean done

    /**
     * Имя пользователя
     */
    String user

    /**
     * Спикок результатов работы
     */
    @DBRef(lazy = true)
    List<JobResult> results = []
}
