--liquibase formatted sql
--changeset marshal:20231001-01-prod-data

INSERT INTO clients(id, status, first_name, last_name, email, address, phone)
VALUES (
           -1,
           'ACTIVE',
           'Bank',
           'Bank',
           'support@mybank.com',
           'Frau Hollestraße 52 Bad Tölz 82501 Bayern Deutschland',
           '498031234567'
       );

INSERT INTO accounts(id, client_id, name, status, balance, currency_code)
VALUES (-1, -1, 'BANK ACCOUNT EUR', 'ACTIVE', 100000000000000, 978),
       (-2, -1, 'BANK ACCOUNT USD', 'ACTIVE', 100000000000000, 840);

INSERT INTO products(id, name, status, currency_code, min_interest_rate, max_offer_limit)
VALUES (-1, 'DEFAULT PRODUCT EUR', 'ACTIVE', 978, 0, 0),
       (-2, 'DEFAULT PRODUCT USD', 'ACTIVE', 840, 0, 0);
