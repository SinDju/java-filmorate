# java-filmorate
Template repository for Filmorate project.
 ### Схема Базы данных filmorate
<picture>
<img src="src/main/resources/er_diagram.png">
</picture>

Похоже ты забыла добавить таблицу с рейтингами, чтобы на нее ссылалось поле rating_id в таблице film. Эта таблица будет содержать поля rating_id [pk] и name.

Не совсем понимаю зачем в таблице user поле friend_user_id. Пользователь скорее всего будет иметь больше одного друга, и чье id хранить в поле friend_user_id не понятно. Можно поле friend_id в таблице friends ссылать на поле id таблицы User, ведь чей-то друг тоже является пользователем

Поле film_id таблицы Like_film, мне кажется, должен ссылаться на поле film_id таблицы FILM. Поле user_like_id можно убрать по той же причине, что и friend_user_id.

Возможно не поняла твою идею с полями friend_user_id и user_like_id, если это так, то будет интересно узнать, что ты подразумевала.
