package org.riversoft.salt.gui.enums

enum Permission {

    /**
     * Супер пользователь которому все разрешено
     */
    ROLE_ROOT,

    //region MAIN PAGE PERMISSIONS

    /**
     * Разрешение просматривать главную страницу
     */
     ROLE_PAGE_MAIN,

    /**
     * Разрешает просматривать блок с количествами
     * миньонов сгрупированных по статусам на главной странице
     */
     ROLE_SHOW_MINIONS_COUNTS_STATUS,

    /**
     * Разрешает просматривать блок с количествами
     * миньонов сгрупированных по групам на главной странице
     */
     ROLE_SHOW_MINIONS_COUNTS_GROUP,

    /**
     * Разрешает просматривать вкладку
     * с принятми миньонами на главной странице
     */
     ROLE_SHOW_ACCEPTED_MINIONS,

    /**
     * Разрешает просматривать вкладку
     * с отказанными миньонами на главной странице
     */
     ROLE_SHOW_DENIED_MINIONS,

    /**
     * Разрешает просматривать вкладку
     * с отклененными миньонами на главной странице
     */
     ROLE_SHOW_REJECTED_MINIONS,

    /**
     * Разрешает просматривать вкладку
     * с не принятми миньонами на главной странице
     */
     ROLE_SHOW_UNACCEPTED_MINIONS,

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
     ROLE_SHOW_GROUPED_MINIONS,

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

    /**
     * Разрешает выполнять скрипты на миньоне
     */
    ROLE_EXECUTE_SCRIPTS_ON_MINION,

    //endregion

     //region SCRIPTS PAGE PERMISSIONS

    /**
    * Разрешение просматривать страницу скриптов
    */
    ROLE_PAGE_SCRIPTS,

    /**
     * Разрешение просматривать блок сгрупированных скриптов
     * по групам на странице скрипты
     */
     ROLE_SHOW_GROUPED_SCRIPTS,

    /**
     * Разрешает выполнять скрипт на миньонах
     */
     ROLE_EXECUTE_SCRIPT_ON_MINIONS,

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
     * Разрешение редактировать название групы скриптов
     */
    ROLE_EDIT_SCRIPTS_GROUP,

    /**
     * Разрешение удалить название групы скриптов
     */
     ROLE_DELETE_SCRIPTS_GROUP,

    //endregion

    //region JOB RESULTS PAGE PERMISSIONS

      /**
      * Разрешение просматривать страницу работ
      */
      ROLE_PAGE_JOB_RESULTS,

    /**
     * Разрешение фильтровать результаты выполнения скриптов
     */
     ROLE_FILTER_JOB_RESULTS,

    /**
     * Разрешает просматривать результаты выполнения работы
     * с количеством результатов по статусам
     */
     ROLE_SHOW_JOB_RESULTS_COUNTERS,

    /**
     * Разрешает просматривать детальный список результатов работы
     */
     ROLE_SHOW_RESULTS_BY_JOB,

    /**
     * Разрешает просматривать детали конкретного результата работы
     */
    ROLE_SHOW_RESULT_DETAILS,

    /**
     * Разрешает перезапускать скрипты на миньоне
     */
    ROLE_RE_EXECUTE_SCRIPTS_ON_MINIONS

    //endregion
}