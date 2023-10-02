--liquibase formatted sql
--changeset marshal:20231001-01-prod-data

INSERT INTO products(id, name, status, currency_code, min_interest_rate, max_offer_limit)
VALUES (0, 'Default', 'ACTIVE', 978, 0, 0);