package org.riversoft.salt.gui.enums

enum Permission {

    /**
     * Супер пользователь которому все разрешено
     */
    ROLE_ROOT,

    /**
     * Разрешение просматривать главную страницу
     */
     ROLE_PAGE_MAIN,

    /**
     * Разрешение просматривать страницу скриптов
     */
     ROLE_PAGE_SCRIPTS,

    /**
     * Разрешение просматривать страницу миньонов
     */
     ROLE_PAGE_GROUPS_AND_MINIONS,

    /**
     * Разрешение просматривать страницу работ
     */
     ROLE_PAGE_JOB_RESULTS

//    List<Permission> findAll(){
//        this.findAll() - ROLE_ROOT
//    }

}