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
  created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
  publisher_id   INT NULL,
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
('spider-man-2', 'Spider-Man 2', 'Be Greater. Together. The incredible power of the symbiote forces Peter Parker and Miles Morales into a desperate fight as they balance their lives, friendships, and their duty to protect in an exciting chapter of the critically acclaimed Spider-Man franchise on PC.', 'T', '2023-10-20', 'Released', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2651280/header.jpg?t=1763569811', '', NOW(), NOW()),
('cyberpunk-2077', 'Cyberpunk 2077', 'Cyberpunk 2077 is an open-world, action-adventure RPG set in the dark future of Night City — a dangerous megalopolis obsessed with power, glamor, and ceaseless body modification.', 'M', '2020-12-10', 'Released', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1091500/e9047d8ec47ae3d94bb8b464fb0fc9e9972b4ac7/header.jpg?t=1765554351', '', NOW(), NOW()),
('the-witcher-4', 'The Witcher 4', 'More information coming soon', 'M', '2027-12-01', 'Coming Soon', 'https://press-start.com.au/wp-content/uploads/2024/12/The-Witcher-4-1.jpg', '', NOW(), NOW()),
('pokemon-legends-ZA', 'Pokemon Legends ZA', 'Catch Pokemon and battle trainers to level up in the ZA Royale.', 'E', '2025-10-16', 'Released', 'https://assets.nintendo.com/image/upload/ar_16:9,c_lpad,w_1240/b_white/f_auto/q_auto/store/software/switch2/70010000099365/80e1e26cdb1dd973909009e8c5bd457b01c9a57236ba6161d5e158f1357526f9', '', NOW(), NOW()),
('halo-infinite', 'Halo Infinite', 'From one of gamings most iconic sagas, Halo is bigger than ever. Featuring an expansive open-world campaign and a dynamic free to play multiplayer experience. ', 'T', '2021-12-08', 'Released', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1240440/2262ffb4f1659df1c1cbf2c70af40560b4fa12e4/header.jpg?t=1763576590', '', NOW(), NOW()),
('god-of-war-ragnarok', 'God of War Ragnarök', 'Kratos and Atreus embark on a mythic journey for answers before Ragnarök arrives – now on PC.', 'M', '2022-11-09', 'Released', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2322010/header.jpg?t=1750909504', '', NOW(), NOW()),
('starfield', 'Starfield', 'Starfield is the first new universe in 25 years from Bethesda Game Studios, the award-winning creators of The Elder Scrolls V: Skyrim and Fallout 4.', 'T', '2023-09-06', 'Released', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1716740/header.jpg?t=1749757928', '', NOW(), NOW()),
('zelda-tears-kingdom', 'The Legend of Zelda: Tears of the Kingdom', 'n this sequel to the Legend of Zelda: Breath of the Wild game, you’ll decide your own path through the sprawling landscapes of Hyrule and the mysterious islands floating in the vast skies above. Can you harness the power of Link’s new abilities to fight back against the malevolent forces that threaten the kingdom?', 'E10+', '2023-05-12', 'Released', 'https://assets.nintendo.com/image/upload/ar_16:9,c_lpad,w_1240/b_white/f_auto/q_auto/store/software/switch/70010000063714/fb30eab428df3fc993b41c76e20f72e4d76d49734d17d31996b5ab61c414b117', '', NOW(), NOW()),
('final-fantasy-vii-rebirth', 'Final Fantasy VII Rebirth', 'The Unknown Journey Continues... After escaping the city of Midgar, Cloud and his friends set out on a journey across the planet. New adventures await in a vibrant, expansive world in this standalone entry of the FFVII remake trilogy.', 'T', '2025-01-23', 'Released', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2909400/header.jpg?t=1762274642', '', NOW(), NOW()),
('resident-evil-requiem', 'Resident Evil Requiem', 'Requiem for the dead. Nightmare for the living. Prepare to escape death in a heart-stopping experience that will chill you to your core.', 'TBD', '2026-02-27', 'Coming Soon', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/3764200/ce5437442768e38eb575f205ab9397d0264017b0/header.jpg?t=1765524069', '', NOW(), NOW()),
('forza-horizon-6', 'Forza Horizon 6', 'Discover the breathtaking landscapes of Japan and become a racing Legend at the Horizon Festival. Add Forza Horizon 6 to your Wishlist today!', 'TBD', '2026-12-31', 'Coming Soon', 'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2483190/1acaf078d452ab11660b31c4716ac818a2e02680/header.jpg?t=1759247866', '', NOW(), NOW());

-- Insert test users
INSERT INTO users (email, password_hash, display_name, is_email_verified, is_admin, created_at, updated_at) VALUES
('zcf5026@psu.edu', '$2a$12$OF2ApqbWXAMSNyfBjTg2HOW9QhRzpJb99mZnbh2eAdszuVo8fWjqu', 'Zach Finley', 1, 1, NOW(), NOW()),
('exw2345@psu.edu', '$2a$12$DkWrW6Q6YmngvV4a1O8/VOq6ZrDtDOlhpwXnXP4.yFWendd7yBSRq', 'Elmer Winwood', 1, 0, NOW(), NOW()),
('jxa2349@psu.edu', '$2a$12$KcN8HhwaqBnBKcQ/5nNlJ.YZdbGvDLrI.W5qjDOK4BEGTbtujFatq', 'Joan Averal', 0, 0, NOW(), NOW()),
('nxn5012@psu.edu', '$2a$12$usIhWgnbjYsj/Jl2vMJktO7U.r3SI6f3udGhDceVqGArV6jW/O5MO', 'Niki Neil', 1, 0, NOW(), NOW()),
('sxb2753@psu.edu', '$2a$12$KJsm6rJ63LFIrRZKvcLvjeADug5FaSIpa.YVkcIcWvkiZ90BOEFAS', 'Stacy Bush', 1, 0, NOW(), NOW()),
('oxs1759@psu.edu', '$2a$12$zP3XgJJcAOqy0WZveSjef.vfocvvLiEXZyfdSLAqhG6EEDIYWTLL.', 'Oliver Symonds', false, 0, NOW(), NOW()),
('cxc628@psu.edu', '$2a$12$isQC1MjoOxY6K7AVZzNvbe1xFzaXS.1rZ8aHngv9W.dU6yuq3Sfjm', 'Chas Christonson', 0, 0, NOW(), NOW()),
('fxf1344@psu.edu', '$2a$12$wy3tm3xpTVGjxJEIc678kOD9DMpS4s9qZW8JtMgG.KI5y.NMXb.Cq', 'Fae Farley', 0, 0, NOW(), NOW());

-- Insert Test libary entries
INSERT INTO libraries (user_id, game_id, added_at, source) VALUES
(1, 1, NOW(), 'Browse'),
(1, 2, NOW(), 'Search'),
(1, 5, NOW(), 'Recommended')
