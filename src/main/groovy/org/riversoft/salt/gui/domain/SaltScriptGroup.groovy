package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "salt_scripts_group")
class SaltScriptGroup implements Serializable {

    /**
     * Название группы
     */
    @Id
    String name

    @DBRef
    List<SaltScript> scriptList = []
}
