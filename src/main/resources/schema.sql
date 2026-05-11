CREATE TYPE user_role AS ENUM ('USER', 'MODERATOR', 'ADMIN');
CREATE TYPE privacy_type AS ENUM ('PUBLIC', 'FRIENDS', 'PRIVATE');
CREATE TYPE friendship_status AS ENUM ('PENDING', 'ACCEPTED', 'DECLINED');

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role user_role NOT NULL DEFAULT 'USER',
                       avatar_path VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tags (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE photos (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    file_path VARCHAR(255) NOT NULL,
    thumbnail_path VARCHAR(255),
    privacy privacy_type NOT NULL DEFAULT 'PRIVATE', 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE albums (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        privacy privacy_type NOT NULL DEFAULT 'PRIVATE',
                        cover_photo_id INT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE album_photos (
                              album_id INT NOT NULL REFERENCES albums(id) ON DELETE CASCADE,
                              photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
                              added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (album_id, photo_id)
);

CREATE TABLE photo_tags (
                            photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
                            tag_id INT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
                            PRIMARY KEY (photo_id, tag_id)
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
    value SMALLINT NOT NULL CHECK (value >= 1 AND value <= 10), //у нас от 1 до 10 а не дизы
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, photo_id)
);

CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendships (
                             id SERIAL PRIMARY KEY,
                             user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             friend_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             status friendship_status NOT NULL DEFAULT 'PENDING',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             UNIQUE (user_id, friend_id)
);

CREATE TABLE photo_copies (
                              id SERIAL PRIMARY KEY,
                              original_photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
                              copied_by_user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              new_photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
                              copied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
                               id SERIAL PRIMARY KEY,
                               user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               message TEXT NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reports (
                         id SERIAL PRIMARY KEY,
                         reporter_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         photo_id INT NOT NULL REFERENCES photos(id) ON DELETE CASCADE,
                         reason TEXT NOT NULL,
                         status VARCHAR(20) DEFAULT 'PENDING',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);