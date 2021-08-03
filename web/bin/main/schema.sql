
CREATE TABLE IF NOT EXISTS gift_certificate (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  duration INT NOT NULL,
  create_date DATETIME NOT NULL,
  last_update_date DATETIME NOT NULL,
  deleted BOOLEAN DEFAULT FALSE
  );

CREATE TABLE IF NOT EXISTS tag (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN DEFAULT FALSE
  );

CREATE TABLE IF NOT EXISTS gift_certificate_tag_connection (
  gift_certificate_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  PRIMARY KEY (`gift_certificate_id`, `tag_id`)
  );
  
  CREATE TABLE IF NOT EXISTS user (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS item_order (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  cost DECIMAL(10,2) NOT NULL,
  user_id BIGINT NOT NULL,
  create_date DATETIME NOT NULL,
  deleted BOOLEAN DEFAULT FALSE
 );
 
 CREATE TABLE IF NOT EXISTS ordered_gift_certificate (
  order_id BIGINT NOT NULL,
  gift_certificate_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  duration INT NOT NULL,
  create_date DATETIME NOT NULL,
  last_update_date DATETIME NOT NULL,
  number INT NOT NULL,
  PRIMARY KEY (`order_id`, `gift_certificate_id`)
  )