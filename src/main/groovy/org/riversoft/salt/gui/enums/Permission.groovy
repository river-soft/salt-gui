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
     * Разрешение просматривать блок с количествами
     * миньонов сгрупированных по статусам на главной странице
     */
     ROLE_SHOW_MINIONS_COUNTS_STATUS,

    /**
     * Разрешение просматривать блок с количествами
     * миньонов сгрупированных по групам на главной странице
     */
     ROLE_SHOW_MINIONS_COUNTS_GROUP,

    /**
     * Разрешение просматривать принятые миньоны на главной странице
     */
     ROLE_SHOW_ACCEPTED_MINIONS,

    /**
     * Разрешение просматривать отказанные миньоны на главной странице
     */
     ROLE_SHOW_DENIED_MINIONS,

    /**
     * Разрешение просматривать отклененные миньоны на главной странице
     */
     ROLE_SHOW_REJECTED_MINIONS,

    /**
     * Разрешение просматривать не принятые миньоны на главной странице
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
     * Разрешение просматривать сгрупированные миньоны по групам на странице миньонов
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
     * Разрешение редактировать название групы миньона
     */
     ROLE_EDIT_MINIONS_GROUP,

    /**
     * Разрешение удалять групу миньона
     */
     ROLE_DELETE_MINIONS_GROUP,

    /**
     * Разрешение редактировать группы к которым принадлежит миньон
     */
     ROLE_EDIT_GROUPS_OF_MINION,

    /**
     * Разрешение выполнять скрипты на миньоне
     */
    ROLE_EXECUTE_SCRIPTS_ON_MINION,

    //endregion

     //region SCRIPTS PAGE PERMISSIONS

    /**
    * Разрешение просматривать страницу скриптов
    */
    ROLE_PAGE_SCRIPTS,

    /**
     * Разрешение просматривать сгрупированные скрипты по групам на странице Скрипты
     */
     ROLE_SHOW_GROUPED_SCRIPTS,

    /**
     * Разрешение выполнять скрипт на миньонах
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
     * Разрешение редактировать скрипт
     */
    ROLE_EDIT_SCRIPT,

    /**
     * Разрешает удалять скрипт
     */
    ROLE_DELETE_SCRIPT,

    /**
     * Разрешение редактировать название групы скриптов
     */
    ROLE_EDIT_SCRIPTS_GROUP,

    /**
     * Разрешение удалять название групы скриптов
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
     * Разрешение просматривать результаты выполнения работы
     * с количеством результатов по статусам
     */
     ROLE_SHOW_JOB_RESULTS_COUNTERS,

    /**
     * Разрешение просматривать детальный список результатов работы
     */
     ROLE_SHOW_RESULTS_BY_JOB,

    /**
     * Разрешение просматривать детали конкретного результата работы
     */
    ROLE_SHOW_RESULT_DETAILS,

    /**
     * Разрешение перезапускать скрипты на миньоне
     */
    ROLE_RE_EXECUTE_SCRIPTS_ON_MINIONS

    //endregion
}