package org.riversoft.salt.gui.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

class User extends Base {

    /**
     * имя/логин пользователя
     */
    @Id
    @Indexed(unique = true)
    String name

    /**
     * Пароль пользователя
     */
    String password

    /**
     * Имя
     */
    //TODO нужно или нет ?
    String firstName

    /**
     * Фамилия
     */
    //TODO нужно или нет ?
    String lastName

    /**
     * Разрешения
     */
    List<String> permissions = []

    /**
     * Статус пользователя
     */
    //TODO нужно или нет ?
    String status
}
