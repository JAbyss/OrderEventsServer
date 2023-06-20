# Order Event Server

Order Event Server - это сервер, который получает заказы из внешних источников, регистрирует их и принимает события, связанные с этими заказами. Каждое событие записывается в базу данных PostgreSQL в таблицу `order_events`. Сервер также предоставляет возможность получить информацию о заказе и все его события по определенному запросу.

## Обработка и проверка событий

Сервер реализует логику обработки и проверки событий для обеспечения целостности данных. Он применяет следующие правила:

- Однажды зарегистрированный заказ не может быть зарегистрирован повторно.
- Если заказ отменен или помечен как доставленный, для этого заказа больше нельзя принимать события.
- События должны следовать определенной последовательности, например Заказ зарегистрирован -> Заказ принят -> Заказ готов и т.д.

- Существуют следующие EventType:
    - ORDER_REGISTERED
    - ORDER_IN_PROGRESS
    - ORDER_READY
    - ORDER_DELIVERED
    - ORDER_CANCELLED

## Тестирование в контейнере

В проекте предусмотрены тесты в контейнере, которые проверяют вышеуказанную функциональность. Эти тесты предназначены для последовательного выполнения, так как они охватывают различные этапы обработки событий.

## Интеграция с ActiveMQ Artemis (закомментированный код)

Сервер имеет возможность подключаться к ActiveMQ Artemis в качестве брокера сообщений, хотя код для этой функции в настоящее время закомментирован.

## Развертывание с использованием Docker

Весь проект может быть развернут с использованием Docker Compose. Предоставляется Dockerfile для сборки JAR-файла и создания Docker-образа (находятся в build/libs). В проекте также присутствует задача Gradle - `bootBuildImage`, которая собирает образ с указанным именем, указанным в файле docker-compose. Также присутствует задача `bootJar`, которая собирает необходимый JAR-файл для Dockerfile.

Пожалуйста, убедитесь, что на целевой машине установлены Docker и Docker Compose, чтобы успешно запустить проект.

## Обработка ошибок

Проект обрабатывает различные исключения, которые могут возникнуть в процессе обработки заказа. Для каждой ошибки реализованы отдельные классы ошибок для улучшения обработки ошибок и удобства использования.

## Доступ

В настоящее время проект работает на моей локальной машине и доступен через следующие порты:

- 8899 - OrderEventServer:
    - Добавить заказ: `/orders/add`
    - Отправить событие: `/orders/events`
    - Получить информацию о заказе и его событиях: `/orders/{id}`

- 1221 - PostgreSQL (открыт для просмотра базы данных при необходимости)

## Postman

В проекте присутствует файл `OrderEventsServer.postman_collection.json`, который можно импортировать в Postman и воспользоваться запросами к моему серверу.