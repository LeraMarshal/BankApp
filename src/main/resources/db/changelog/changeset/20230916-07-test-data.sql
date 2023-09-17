--liquibase formatted sql
--changeset marshal:20230916-07-test-data contextFilter:@test
INSERT INTO clients(id, status, first_name, last_name, email, password, address, phone)
VALUES (
           nextval('clients_id_seq'),
           'ACTIVE',
           'John',
           'Smith',
           'john.smith@gmail.com',
           '43cc355820abaf58b3442d9a22dc71530f6f6122b29c7d58680a1b1e43c8e672',
           'Berlin, Harden str. 4',
           '493048898888'
       ),
       (
           nextval('clients_id_seq'),
           'ACTIVE',
           'Jack',
           'Shepard',
           'jack.shepard@gmail.com',
           '43cc355820abaf58b3442d9a22dc71530f6f6122b29c7d58680a1b1e43c8e672',
           'Berlin, Harden str. 5',
           '493048898889'
       );

INSERT INTO accounts(id, client_id, name, status, balance, currency_code)
VALUES (nextval('accounts_id_seq'), 1, 'debit', 'ACTIVE', 150000, 978),
       (nextval('accounts_id_seq'), 2, 'credit', 'ACTIVE', 200000, 978);

INSERT INTO products(id, name, status, currency_code, min_interest_rate, max_offer_limit)
VALUES (nextval('products_id_seq'), 'Deposit', 'ACTIVE', 978, -5, 0),
       (nextval('products_id_seq'), 'Credit', 'ACTIVE', 978, 10, 1000000);

INSERT INTO agreements(id, account_id, product_id, status, interest_rate, debt)
VALUES (nextval('agreements_id_seq'), 1, 1, 'ACTIVE', -5, 0),
       (nextval('agreements_id_seq'), 2, 2, 'ACTIVE', 13, 1000000);

INSERT INTO transactions(id, debit_account_id, credit_account_id, status, amount, description)
VALUES (nextval('transactions_id_seq'), 1, 2, 'COMPLETED', 50000, 'lend'),
       (nextval('transactions_id_seq'), 2, 1, 'COMPLETED', 50000, 'repayment');
