CREATE TABLE IF NOT EXISTS anime (
                                     id INT PRIMARY KEY,
                                     format VARCHAR(255),
    episodes INT,
    episodes_duration INT,
    status VARCHAR(255),
    start_date DATE,
    end_date DATE,
    average_score INT,
    popularity INT,
    favourites INT,
    source VARCHAR(255),
    romaji VARCHAR(255),
    english VARCHAR(255),
    original VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS studio (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    );

CREATE TABLE IF NOT EXISTS anime_studio (
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
    anime_id INT,
    studio_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (anime_id) REFERENCES anime(id),
    FOREIGN KEY (studio_id) REFERENCES studio(id)
    );

CREATE TABLE IF NOT EXISTS anime_genres (
                                            anime_id INT,
                                            genre VARCHAR(255),
    PRIMARY KEY (anime_id, genre),
    FOREIGN KEY (anime_id) REFERENCES anime(id)
    );
CREATE TABLE IF NOT EXISTS manga (
    id INT PRIMARY KEY,
    format VARCHAR(255),
    status VARCHAR(255),
    start_date DATE,
    end_date DATE,
    chapters INT,
    volumes INT,
    average_score INT,
    popularity INT,
    favourites INT,
    romaji VARCHAR(255),
    english VARCHAR(255),
    original VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS manga_genres (
                                            manga_id INT,
                                            genre VARCHAR(255),
    PRIMARY KEY (manga_id, genre),
    FOREIGN KEY (manga_id) REFERENCES manga(id)
    );