<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/css/main.css">
    <title>salt-gui</title>
</head>
<body>
    <div id="root"></div>
    <#if security??>
    <#else>
    <script>
        window.unAuthorized = true;
    </script>
    </#if>
    <script src="/js/bundle.js"></script>
</body>
</html>