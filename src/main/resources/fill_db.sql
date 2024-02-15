use client;

DROP TABLE IF EXISTS brand;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS company_details;
DROP TABLE IF EXISTS client;

CREATE TABLE client (
client_id bigint(20) NOT NULL AUTO_INCREMENT,
name varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
address varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
email varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
phone_number varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
is_active TINYINT NOT NULL,
PRIMARY KEY (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE brand (
brand_id bigint(20) NOT NULL AUTO_INCREMENT,
name varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
PRIMARY KEY (brand_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE car (
car_id bigint(20) NOT NULL AUTO_INCREMENT,
model varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
number varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
brand_id bigint(20) NOT NULL,
client_id bigint(20) NOT NULL,
PRIMARY KEY (car_id),
CONSTRAINT FK_brand_id FOREIGN KEY (brand_id)
REFERENCES brand(brand_id),
CONSTRAINT FK_client_id FOREIGN KEY (client_id)
REFERENCES client(client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE company_details (
company_details_id bigint(20) NOT NULL AUTO_INCREMENT,
code varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
pvm varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
representative_person varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
client_id bigint(20) NOT NULL,
PRIMARY KEY (company_details_id),
CONSTRAINT FK_client_entity_id FOREIGN KEY (client_id)
REFERENCES client(client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;INE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO brand (brand_id, name) VALUES (1, 'Volkswagen');
INSERT INTO brand (brand_id, name) VALUES (2, 'BMW');
INSERT INTO brand (brand_id, name) VALUES (3, 'TOYOTA');
INSERT INTO brand (brand_id, name) VALUES (4, 'FORD');
INSERT INTO brand (brand_id, name) VALUES (5, 'AUDI');
INSERT INTO brand (brand_id, name) VALUES (6, 'HONDA');
INSERT INTO brand (brand_id, name) VALUES (7, 'Porsche');
INSERT INTO brand (brand_id, name) VALUES (8, 'Subaru');
INSERT INTO brand (brand_id, name) VALUES (9, 'Chrysler');
INSERT INTO brand (brand_id, name) VALUES (10, 'Nissan');

INSERT INTO client (client_id, name, address, email, phone_number, is_active) VALUES (1, 'Client1', 'Vilnius, Vilniaus gatve, 1, Lietuva', 'client1@gmail.com', '+37012345678', 1);
INSERT INTO client (client_id, name, address, email, phone_number, is_active) VALUES (2, 'Client2', 'Vilnius, Vilniaus gatve, 2, Lietuva', 'client2@gmail.com', '+37012345678', 1);
INSERT INTO client (client_id, name, address, email, phone_number, is_active) VALUES (3, 'Client3', 'Vilnius, Vilniaus gatve, 3, Lietuva', 'client3@gmail.com', '+37012345678', 1);
INSERT INTO client (client_id, name, address, email, phone_number, is_active) VALUES (4, 'Client4', 'Vilnius, Vilniaus gatve, 4, Lietuva', 'client4@gmail.com', '+37012345678', 1);
INSERT INTO client (client_id, name, address, email, phone_number, is_active) VALUES (5, 'Client5', 'Vilnius, Vilniaus gatve, 5, Lietuva', 'client5@gmail.com', '+37012345678', 1);

INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (1, 'Corolla', 'ABC1234', 3, 1);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (2, 'Focus', 'ABC2234', 4, 1);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (3, 'Phaeton', 'ABC3234', 1, 2);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (4, 'Passat', 'ABC4234', 1, 2);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (5, 'A6', 'ABC5234', 5, 2);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (6, 'Forester', 'ABC6234', 8, 3);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (7, 'Pacifica', 'ABC7234', 9, 4);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (8, 'Juke', 'ABC8234', 10, 5);
INSERT INTO car (car_id, model, number, brand_id, client_id) VALUES (9, null, null, 2, 5);

INSERT INTO company_details (company_details_id, code, pvm, representative_person, client_id) VALUES (1, '123451234', 'LT10000011234', 'Freddie Mercury', 1);
INSERT INTO company_details (company_details_id, code, pvm, representative_person, client_id) VALUES (2, '223451234', 'LT10000021234', 'Brian May', 4);
