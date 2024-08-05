# Лабораторная работа по Java "Переводчик"

Этот проект представляет собой веб-приложение для перевода текста.
Написан в качестве лабораторной работы для Т-Банка

## Функционал

- Перевод текста через веб-интерфейс
- Перевод текста через api

## Технологии

- Java
- Spring Framework
- Thymeleaf
- Lombok
- RestTemplate
- JDBC

## Запуск приложения

1. Склонируйте репозиторий на локальную машину.
```bash
git clone https://github.com/elvinaf1996/final_task_itis.git
```
2. Добавьте себе базу данных в pgAdmin (
   1. Откройте pgAdmin 4 и подключитесь к своему серверу.
   2. Создайте новую базу данных с именем translate.
   3. Щелкните правой кнопкой мыши по базе данных и выберите Restore....
   4. Укажите файл резервной копии (лежит по пути src/main/resources/database/translateDb.sql)
   5. Нажмите Restore и дождитесь завершения процесса.)
3. Запустите приложение с помощью Maven:
```bash
mvn spring-boot:run
```
4. Для просмотра веб: перейдите по адресу `http://localhost:8080/`.
5. Для отправки запросов в бек используйте запросы: 
```bash
curl --location 'http://localhost:8080/translate/api' \
--header 'Content-Type: application/json' \
--data '{
    "sourceLang": "ru",
    "targetLang": "en",
    "textToTranslate": "меня любит жизнь"
}'
```

## Автор

 Фаттахова Эльвина Нилевна - [elvinaf1996@gmail.com]