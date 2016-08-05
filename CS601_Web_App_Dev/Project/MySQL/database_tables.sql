CREATE TABLE vendor_info
(
    vendor_id INT NOT NULL AUTO_INCREMENT,
    vendor_name VARCHAR(50),
    address VARCHAR(100),
    phone VARCHAR(50),
    email VARCHAR(60),
    PRIMARY KEY(vendor_id)
);

CREATE TABLE prod_info
(
    prodcode VARCHAR(5) NOT NULL,
    prodname VARCHAR(50),
    approx_size INT,
    date_range VARCHAR(50),
    PRIMARY KEY(prodcode)
);

CREATE TABLE vendor_prod
(
    vendor_id INT NOT NULL REFERENCES vendor_info(vendor_id),
    prodcode VARCHAR(5) NOT NULL REFERENCES prod_info(prodcode),
    PRIMARY KEY(vendor_id, prodcode)
);

CREATE TABLE feeds
(
    feed_id INT NOT NULL AUTO_INCREMENT,
    vendor_id INT NOT NULL REFERENCES vendor_info(vendor_id),
    description VARCHAR(100),
    delivery_type VARCHAR(20),
    delivery_freq VARCHAR(15),
    build_freq VARCHAR(15),
    approx_size INT,
    path VARCHAR(200),
    filename_pattern VARCHAR(50),
    PRIMARY KEY(feed_id, vendor_id)
);

CREATE TABLE feed_prod
(
    feed_id INT NOT NULL REFERENCES feeds(feed_id),
    prodcode VARCHAR(5) NOT NULL REFERENCES prod_info(prodcode),
    PRIMARY KEY(feed_id, prodcode)
);

CREATE TABLE ftp_info
(
    feed_id INT NOT NULL REFERENCES feeds(feed_id),
    host VARCHAR(50),
    username VARCHAR(25),
    password VARCHAR(25),
    PRIMARY KEY(feed_id)
);

CREATE TABLE files
(
    file_id INT NOT NULL AUTO_INCREMENT,
    feed_id INT NOT NULL REFERENCES feeds(feed_id),
    filename VARCHAR(50),
    date_posted DATE,
    PRIMARY KEY(file_id, feed_id)
);

CREATE TABLE user_info
(
    username VARCHAR(25) NOT NULL,
    password VARCHAR(25) NOT NULL,
    PRIMARY KEY(username)
);