--liquibase formatted sql
--changeset marshal:20230916-06-test-data contextFilter:@test
INSERT INTO clients(status, first_name, last_name, email, address, phone)
VALUES (
           'ACTIVE',
           'John',
           'Smith',
           'john.smith@gmail.com',
           'Berlin, Harden str. 4',
           '493048898888'
       ), -- 1
       (
           'ACTIVE',
           'Jack',
           'Shepard',
           'jack.shepard@gmail.com',
           'Berlin, Harden str. 5',
           '493048898889'
       ) -- 2
;

INSERT INTO accounts(client_id, name, status, balance, currency_code)
VALUES (1, 'debit', 'ACTIVE', 150000, 978),                            -- 1
       (2, 'credit', 'ACTIVE', 200000, 978),                           -- 2
       (2, 'test account with no agreements', 'ACTIVE', 0, 978),       -- 3
       (2, 'test account with blocked status', 'BLOCKED', 0, 978),     -- 4
       (2, 'test account with non-zero balance', 'ACTIVE', 1000, 978), -- 5
       (2, 'test account with agreements', 'ACTIVE', 0, 978),          -- 6
       (2, 'test account in usd', 'ACTIVE', 1000000, 840) -- 7
;

INSERT INTO products(name, status, currency_code, min_interest_rate, max_offer_limit)
VALUES ('Deposit', 'ACTIVE', 978, -5, 5000000), -- 1
       ('Credit', 'ACTIVE', 978, 10, 1000000),  -- 2
       ('Inactive product', 'INACTIVE', 978, -5, 500000) -- 3
;

INSERT INTO agreements(account_id, product_id, status, interest_rate, debt)
VALUES (1, 1, 'ACTIVE', -5, 0),       -- 1
       (2, 2, 'ACTIVE', 13, 1000000), -- 2
       (6, -1, 'ACTIVE', 0, 0),       -- 3
       (2, -1, 'ACTIVE', 0, 0),       -- 4
       (2, -1, 'INACTIVE', 0, 0),     -- 5
       (2, 2, 'ACTIVE', 12, 1000000), -- 6
       (2, 2, 'PENDING', 0, -100000), -- 7
       (2, 2, 'PENDING', 5, 100000),  -- 8
       (2, 2, 'PENDING', 0, -1000000) -- 9
;

INSERT INTO transactions(debit_account_id, credit_account_id, status, amount, description)
VALUES (1, 2, 'COMPLETED', 50000, 'lend'), -- 1
       (2, 1, 'COMPLETED', 50000, 'repayment') -- 2
;