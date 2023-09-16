--liquibase formatted sql
--changeset marshal:20230916-07-test-data contextFilter:@test
INSERT INTO clients(id, status, first_name, last_name, email, password, address, phone)
VALUES (
           1,
           'ACTIVE',
           'John',
           'Smith',
           'john.smith@gmail.com',
           '43cc355820abaf58b3442d9a22dc71530f6f6122b29c7d58680a1b1e43c8e672',
           'Berlin, Harden str. 4',
           '493048898888'
       ),
       (
           2,
           'ACTIVE',
           'Jack',
           'Shepard',
           'jack.shepard@gmail.com',
           '43cc355820abaf58b3442d9a22dc71530f6f6122b29c7d58680a1b1e43c8e672',
           'Berlin, Harden str. 5',
           '493048898889'
       );

INSERT INTO accounts(id, client_id, name, status, balance, currency_code)
VALUES (1, 1, 'debit', 'ACTIVE', 150000, 978),
       (2, 2, 'credit', 'ACTIVE', 200000, 978);
