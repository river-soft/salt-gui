package org.riversoft.salt.gui.enums

enum Permission {

    /**
     * Супер пользователь которому все разрешено
     */
    ROLE_ROOT,



    /**
     * Разрешение просматривать страницу работ
     */
     ROLE_PAGE_JOB_RESULTS,


    //region MAIN PAGE PERMISSIONS

    /**
     * Разрешение просматривать главную страницу
     */
     ROLE_PAGE_MAIN,

    /**
     * Разрешает просматривать блок с количествами
     * миньонов сгрупированных по статусам на главной странице
     */
    ROLE_MAIN_MINIONS_COUNTS_STATUS,

    /**
     * Разрешает просматривать блок с количествами
     * миньонов сгрупированных по групам на главной странице
     */
    ROLE_MAIN_MINIONS_COUNTS_GROUP,

    /**
     * Разрешает просматривать вкладку
     * с принятми миньонами на главной странице
     */
     ROLE_MAIN_MINIONS_ACCEPTED_TAB,

    /**
     * Разрешает просматривать вкладку
     * с отказанными миньонами на главной странице
     */
    ROLE_MAIN_MINIONS_DENIED_TAB,

    /**
     * Разрешает просматривать вкладку
     * с отклененными миньонами на главной странице
     */
    ROLE_MAIN_MINIONS_REJECTED_TAB,

    /**
     * Разрешает просматривать вкладку
     * с не принятми миньонами на главной странице
     */
     ROLE_MAIN_MINIONS_UNACCEPTED_TAB,

    /**
     * Разрешение принять миньон
     */
    ROLE_ACCEPT_MINION,

    /**
     * Разрешение удалить миньон
     */
     ROLE_DELETE_MINION,

    /**
     * Разрешение отклонить миньон
     */
     ROLE_REJECT_MINION,

    //endregion

     //region GROUPS AND MINIONS PAGE PERMISSIONS

     /**
     * Разрешение просматривать страницу миньонов
     */
     ROLE_PAGE_GROUPS_AND_MINIONS,

    /**
     * Разрешение просматривать блок сгрупированных миньонов
     * по групам на странице миньонов
     */
    ROLE_GROUPED_MINIONS,

    /**
     * Разрешение создавать группу миньонов
     */
    ROLE_CREATE_MINIONS_GROUP,

    /**
     * Разрешение просматривать детальную информацию по миньону
     */
     ROLE_SHOW_MINION_DETAILS,

    /**
     * Разрешает редактировать название групы миньона
     */
     ROLE_EDIT_MINIONS_GROUP,

    /**
     * Разрешает удалять групу миньона
     */
     ROLE_DELETE_MINIONS_GROUP,

    /**
     * Разрешает редактировать группы к которым принадлежит миньон
     */
     ROLE_EDIT_GROUPS_OF_MINION,

    //endregion

    /**
     * Разрешение просматривать блок сгрупированных скриптов
     * по групам на странице скрипты
     */
    ROLE_GROUPED_SCRIPTS,


    /**
     * Разрешает выполнять скрипты на миньоне
     */
     ROLE_EXECUTE_SCRIPTS_ON_MINION,

    /**
     * Разрешает перезапускать скрипты на миньоне
     */
    ROLE_RE_EXECUTE_SCRIPTS_ON_MINION,


    //region SCRIPTS PAGE PERMISSIONS

    /**
     * Разрешение просматривать страницу скриптов
     */
    ROLE_PAGE_SCRIPTS,

    /**
     * Разрешение создавать скрипт и его групу
     */
    ROLE_CREATE_SCRIPT_AND_GROUP,

    /**
     * Разрешение просматривать детальную информацию по скрипту
     */
    ROLE_SHOW_SCRIPT_DETAILS,

    /**
     * Разрешает редактировать название групы миньона
     */
    ROLE_EDIT_SCRIPT,

    /**
     * Разрешает удалять групу миньона
     */
    ROLE_DELETE_SCRIPT,

    /**
     * Разрешает выполнять скрипт на миньонах
     */
    ROLE_EXECUTE_SCRIPT_ON_MINIONS,

    /**
     * Разрешение редактировать название групы скриптов
     */
    ROLE_EDIT_SCRIPTS_GROUP,

    /**
     * Разрешение удалить название групы скриптов
     */
     ROLE_DELETE_SCRIPTS_GROUP,

    /**
     * Разрешает перезапускать скрипты на миньоне
     */
     ROLE_RE_EXECUTE_SCRIPT_ON_MINIONS,


    //endregion


//    List<Permission> findAll(){
//        this.findAll() - ROLE_ROOT
//    }



}