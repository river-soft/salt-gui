package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "salt_scripts_group")
class SaltScriptGroup extends Base/*implements Serializable*/ {

    /**
     * Уникальный номер группы скриптов
     */
    @Id
    String id

    /**
     * Название группы скриптов
     */
    @Indexed(unique = true)
    String name

    /**
     * Список скриптов группы
     */
    @DBRef
    List<SaltScript> scriptList = []
}
