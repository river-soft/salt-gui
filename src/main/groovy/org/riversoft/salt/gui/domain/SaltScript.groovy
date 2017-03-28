package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "salt_scripts")
class SaltScript extends Base {

    /**
     * Уникальный номер скрипта
     */
    @Id
    String id

    /**
     * Название скрипта
     */
    @Indexed(unique = true)
    String name

    /**
     * Содержимое скрипта
     */
    String content

    /**
     * Полный путь к файлу скрипта
     */
//    String filePath

    /**
     * Группа скрипта
     */
    @DBRef
    SaltScriptGroup group
}
