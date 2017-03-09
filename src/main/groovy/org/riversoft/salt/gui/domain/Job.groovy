package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "jobs")
class Job extends Base {

    /**
     * Уникальный номер работы
     */
    @Id
    String jid

    /**
     * Название задачи
     */
    String name

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
