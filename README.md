### **Параметры конфигурации**

**application.properties:**

- **salt.api.url** - url к api сервера salt (salt_master)
- **salt.user** - имя пользователя для логина на сервер salt (salt_master)
- **salt.password** - пароль для логина на сервер salt (salt_master)

- **salt.scripts.default_group** - название группы для скриптов по умолчанию
- **salt.scripts.directory** - путь к папке куда создавать sls файлы скриптов

- **salt.minion.details.properties** - параметры которые необходимо получить для страницы детализации миньона

- **salt.minions.update_counts_interval** - интервал с которым будет запрашиваться информация о количестве миньонов 
  (сгруппированных по статусам и по группам)
- **salt.minions.update_list_by_status** - интервал с которым будет запрашиваться информация о списках миньонов (по статусам)

### **Сборка проекта**

Для того чтобы собрать проект нужно утановить npm, nodejs и webpack.

NodeJs и npm

```sh
apt-get install nodejs
```

```sh
apt-get install npm
```

Webpack

```sh
npm i -g webpack
```
Зависимости для проекта

```sh
npm i
```

Собрать проект

```sh
webpack
```