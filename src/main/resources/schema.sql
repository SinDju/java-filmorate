CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    varchar,
    login    varchar,
    name     varchar NULL,
    birthday Date
);
CREATE TABLE IF NOT EXISTS genres
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    genre_name varchar
);
CREATE TABLE IF NOT EXISTS MPARating
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating_name varchar
);
CREATE TABLE IF NOT EXISTS film
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         varchar,
    description  varchar,
    releaseDate  Date,
    duration     int,
    MPARating_id int REFERENCES MPARating (id)
);
CREATE TABLE IF NOT EXISTS FILMS_GENRES
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    film_id  int REFERENCES film (id),
    genre_id int REFERENCES genres (id)
);
CREATE TABLE IF NOT EXISTS user_likes
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id int REFERENCES users (id),
    film_id int REFERENCES film (id)
);
CREATE TABLE IF NOT EXISTS friends
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   int REFERENCES users (id),
    friend_id int REFERENCES users (id),
    accepted  Boolean
);

