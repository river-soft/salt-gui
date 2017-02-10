package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "minions_group")
class MinionGroup {

    /**
     * Уникальный номер группы миньна
     */
    @Id
    String id

    /**
     * Название группы миньонов
     */
    @Indexed(unique = true)
    String name

    /**
     * Список миньонов группы
     */
    @DBRef(lazy = true)
    List<Minion> minions = []
}
