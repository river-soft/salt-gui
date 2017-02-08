package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "minions")
class Minion {

    /**
     * Уникальный номер миньона
     */
    @Id
    String id

    /**
     * Название миньона
     */
    @Indexed(unique = true)
    String name

    /**
     * Спикок групп миньона
     */
    @DBRef
//    @Transient
    List<MinionGroup> groups = []
}
