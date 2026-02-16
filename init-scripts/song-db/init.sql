CREATE TABLE songs(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100),
    artist   VARCHAR(100),
    album    VARCHAR(100),
    duration CHAR(5),
    year     CHAR(4)
);