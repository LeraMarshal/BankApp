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
       ),
       (
           'ACTIVE',
           'Jack',
           'Shepard',
           'jack.shepard@gmail.com',
           'Berlin, Harden str. 5',
           '493048898889'
       );

INSERT INTO accounts(client_id, name, status, balance, currency_code)
VALUES (1, 'debit', 'ACTIVE', 150000, 978),
       (2, 'credit', 'ACTIVE', 200000, 978);

INSERT INTO products(name, status, currency_code, min_interest_rate, max_offer_limit)
VALUES ('Deposit', 'ACTIVE', 978, -5, 0),
       ('Credit', 'ACTIVE', 978, 10, 1000000);

INSERT INTO agreements(account_id, product_id, status, interest_rate, debt)
VALUES (1, 1, 'ACTIVE', -5, 0),
       (2, 2, 'ACTIVE', 13, 1000000);

INSERT INTO transactions(debit_account_id, credit_account_id, status, amount, description)
VALUES (1, 2, 'COMPLETED', 50000, 'lend'),
       (2, 1, 'COMPLETED', 50000, 'repayment');
