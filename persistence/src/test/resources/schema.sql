
CREATE TABLE gift_certificate (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  duration INT NOT NULL,
  create_date DATETIME NOT NULL,
  last_update_date DATETIME NOT NULL,
  deleted BOOLEAN DEFAULT FALSE
  );

CREATE TABLE tag (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN DEFAULT FALSE
  );

CREATE TABLE gift_certificate_tag_connection (
  gift_certificate_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  PRIMARY KEY (`gift_certificate_id`, `tag_id`)
  );