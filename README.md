### **Параметры конфигурации**

**application.properties:**

- **salt.api.url** - url к api сервера salt (salt_master)
- **salt.user** - имя пользователя для логина на сервер salt (salt_master)
- **salt.password** - пароль для логина на сервер salt (salt_master)

- **salt.scripts.default_group** - название группы для скриптов по умолчанию
- **salt.scripts.directory** - путь к папке куда создавать sls файлы скриптов

- **minion.details.properties** - параметры которые необходимо получить для страницы детализации миньона

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