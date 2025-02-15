Cборка:
1. Вызвать "gradle build"
2. Вызвать "docker build -t nyura-task-service:0.0.1"

Деплой:
1. Настроить необходимое окружение для запуска(postgres, kafka, redis)
2. Скорректировать env-переменные в "docker-compose.yaml"
3. Вызвать "docker-compose up -d"

Локальный запуск
1. Запустить приложение используя дефолтный профиль и application.yaml

Работа с сервисом:
1. Сервис не имеет собственного механизма авторизации
2. Вместо этого используется JWT токен сгенерированный и свалидированный user-service, приходящие в хедер Authorization
3. См. Readme user-service