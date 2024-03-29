DELETE FROM USER_LIKES;
DELETE FROM FILMS_GENRES;
DELETE FROM FRIENDS;
DELETE FROM USERS;
DELETE FROM FILM;

ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILM ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FRIENDS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS_GENRES ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USER_LIKES ALTER COLUMN ID RESTART WITH 1;

MERGE INTO MPARATING (ID, RATING_NAME)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO GENRES(ID, GENRE_NAME)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');