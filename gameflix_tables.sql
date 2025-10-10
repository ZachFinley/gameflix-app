CREATE DATABASE IF NOT EXISTS gameflix_db;
USE gameflix_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS wishlists;
DROP TABLE IF EXISTS libraries;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS user_subscriptions;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS platforms;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS publishers;
DROP TABLE IF EXISTS subscription_plans;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ========= Base / Lookup =========

CREATE TABLE users (
  id               INT PRIMARY KEY AUTO_INCREMENT,
  email            VARCHAR(255) NOT NULL UNIQUE,
  password_hash    VARCHAR(255) NULL,
  display_name     VARCHAR(100) NOT NULL,
  is_email_verified TINYINT(1) NOT NULL DEFAULT 0,
  is_admin         TINYINT(1) NOT NULL DEFAULT 0,
  created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE roles (
  id     INT PRIMARY KEY AUTO_INCREMENT,
  name   VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE publishers (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  name  VARCHAR(150) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE platforms (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  name  VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE genres (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  name  VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE subscription_plans (
  id             INT PRIMARY KEY AUTO_INCREMENT,
  name           VARCHAR(100) NOT NULL,
  price_monthly  DECIMAL(10,2) NOT NULL,
  currency       CHAR(3) NOT NULL DEFAULT 'USD',
  is_active      TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ========= Join / Auth =========

CREATE TABLE user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id) REFERENCES roles(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ========= Catalog =========

CREATE TABLE games (
  id             INT PRIMARY KEY AUTO_INCREMENT,
  slug           VARCHAR(150) NOT NULL UNIQUE,
  title          VARCHAR(200) NOT NULL,
  description    TEXT NULL,
  esrb_rating    VARCHAR(10) NULL,
  publisher_id   BIGINT NULL,
  release_date   DATE NULL,
  status         ENUM('Coming Soon','Released') NOT NULL,
  box_art_url    VARCHAR(500) NULL,
  trailer_url    VARCHAR(500) NULL,
  created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_games_status_releasedate (status, release_date),
  INDEX idx_games_title (title),
  CONSTRAINT fk_games_publisher
    FOREIGN KEY (publisher_id) REFERENCES publishers(id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ========= Subscriptions & Billing =========

CREATE TABLE user_subscriptions (
  id                       INT PRIMARY KEY AUTO_INCREMENT,
  user_id                  INT NOT NULL,
  plan_id                  INT NOT NULL,
  status                   VARCHAR(100) NOT NULL,
  start_date               DATE NOT NULL,
  renewal_date             DATE NULL,
  cancel_at_period_end     TINYINT(1) NOT NULL DEFAULT 0,
  created_at               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_subs_user_status (user_id, status),
  CONSTRAINT fk_user_subs_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_user_subs_plan
    FOREIGN KEY (plan_id) REFERENCES subscription_plans(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE payments (
  id                    INT PRIMARY KEY AUTO_INCREMENT,
  user_id               INT NOT NULL,
  subscription_id       INT NOT NULL,
  amount                DECIMAL(10,2) NOT NULL,
  currency              CHAR(3) NOT NULL DEFAULT 'USD',
  status                ENUM('Succeeded','Failed','Refunded','Pending') NOT NULL,
  paid_at               DATETIME NULL,
  created_at            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_payments_user_status (user_id, status, paid_at),
  CONSTRAINT fk_payments_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_payments_subscription
    FOREIGN KEY (subscription_id) REFERENCES user_subscriptions(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ========= Library, Wishlist, Reviews =========

CREATE TABLE libraries (
  user_id  INT NOT NULL,
  game_id  INT NOT NULL,
  added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  source   ENUM('Browse','Search','Recommended') NOT NULL DEFAULT 'Browse',
  PRIMARY KEY (user_id, game_id),
  INDEX idx_libraries_game_user (game_id, user_id),
  CONSTRAINT fk_libraries_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_libraries_game
    FOREIGN KEY (game_id) REFERENCES games(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE wishlists (
  user_id  INT NOT NULL,
  game_id  INT NOT NULL,
  added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, game_id),
  CONSTRAINT fk_wishlists_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_wishlists_game
    FOREIGN KEY (game_id) REFERENCES games(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE reviews (
  id        INT PRIMARY KEY AUTO_INCREMENT,
  user_id   INT NOT NULL,
  game_id   INT NOT NULL,
  rating    INT NOT NULL,
  title     VARCHAR(200) NULL,
  body      TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_reviews_user_game (user_id, game_id),
  CONSTRAINT chk_reviews_rating CHECK (rating BETWEEN 1 AND 5),
  CONSTRAINT fk_reviews_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_reviews_game
    FOREIGN KEY (game_id) REFERENCES games(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Insert test games
INSERT INTO games (slug, title, description, esrb_rating, release_date, status, box_art_url, trailer_url, created_at, updated_at) VALUES
('spider-man-2', 'Spider-Man 2', 'Web-slinging action adventure game featuring Spider-Man in an open-world New York City.', 'T', '2023-10-20', 'Released', 'https://example.com/spiderman2.jpg', 'https://example.com/spiderman2-trailer.mp4', NOW(), NOW()),
('cyberpunk-2077', 'Cyberpunk 2077', 'Open-world RPG set in the dystopian Night City where you play as a cyberpunk mercenary.', 'M', '2020-12-10', 'Released', 'https://example.com/cyberpunk.jpg', 'https://example.com/cyberpunk-trailer.mp4', NOW(), NOW()),
('the-witcher-4', 'The Witcher 4', 'The next installment in the legendary Witcher series with a new protagonist.', 'M', '2025-12-01', 'Coming Soon', 'https://example.com/witcher4.jpg', 'https://example.com/witcher4-trailer.mp4', NOW(), NOW()),
('pokemon-legends-ZA', 'Pokemon Legends ZA', 'Catch Pokemon and battle trainers to level up in the ZA Royale.', 'E', '2024-10-16', 'Coming Soon', 'https://example.com/mariokart9.jpg', 'https://example.com/mariokart9-trailer.mp4', NOW(), NOW()),
('halo-infinite', 'Halo Infinite', 'Master Chief returns in this epic sci-fi first-person shooter.', 'T', '2021-12-08', 'Released', 'https://example.com/halo.jpg', 'https://example.com/halo-trailer.mp4', NOW(), NOW()),
('god-of-war-ragnarok', 'God of War Ragnarök', 'Kratos and Atreus embark on a mythic journey for answers before Ragnarök arrives.', 'M', '2022-11-09', 'Released', 'https://example.com/gow.jpg', 'https://example.com/gow-trailer.mp4', NOW(), NOW()),
('starfield', 'Starfield', 'Bethesdas first new universe in over 25 years - a space exploration RPG.', 'T', '2023-09-06', 'Released', 'https://example.com/starfield.jpg', 'https://example.com/starfield-trailer.mp4', NOW(), NOW()),
('zelda-tears-kingdom', 'The Legend of Zelda: Tears of the Kingdom', 'The sequel to Breath of the Wild with new abilities and expanded Hyrule.', 'E10+', '2023-05-12', 'Released', 'https://example.com/zelda.jpg', 'https://example.com/zelda-trailer.mp4', NOW(), NOW()),
('final-fantasy-vii-rebirth', 'Final Fantasy VII Rebirth', 'The second installment in the Final Fantasy VII Remake trilogy.', 6, '2024-02-29', 'Released', 'https://example.com/ff7.jpg', 'https://example.com/ff7-trailer.mp4', NOW(), NOW()),
('grand-theft-auto-vi', 'Grand Theft Auto VI', 'The highly anticipated next entry in the GTA series.', 'M', '2025-03-15', 'Coming Soon', 'https://example.com/gta6.jpg', 'https://example.com/gta6-trailer.mp4', NOW(), NOW());

-- Insert test users
INSERT INTO users (email, password_hash, display_name, is_email_verified, is_admin, created_at, updated_at) VALUES
('zcf5026@psu.edu', 'hash1', 'Zach Finley', 1, 1, NOW(), NOW()),
('exw2345@psu.edu', 'hash2', 'Elmer Winwood', 1, 0, NOW(), NOW()),
('jxa2349@psu.edu', 'hash3', 'Joan Averal', 0, 0, NOW(), NOW()),
('nxn5012@psu.edu', 'hash4', 'Niki Neil', 1, 0, NOW(), NOW()),
('sxb2753@psu.edu', 'hash5', 'Stacy Bush', 1, 0, NOW(), NOW()),
('oxs1759@psu.edu', 'hash6', 'Oliver Symonds', false, 0, NOW(), NOW()),
('cxc628@psu.edu', 'hash7', 'Chas Christonson', 0, 0, NOW(), NOW()),
('fxf1344@psu.edu', 'hash8', 'Fae Farley', 0, 0, NOW(), NOW());
