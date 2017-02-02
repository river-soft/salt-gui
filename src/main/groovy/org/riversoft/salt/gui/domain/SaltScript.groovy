package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "salt_scripts")
class SaltScript implements Serializable {

    /**
     * Название скрипта
     */
    @Id
    @Indexed(unique = true)
    String name

    /**
     * Содержимое/текст скрипта
     */
    String content

    /**
     * Группа скрипта
     */
    @DBRef
    SaltScriptGroup group

}
