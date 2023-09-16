--liquibase formatted sql
--changeset marshal:20230916-06-triggers-pgsql
--https://dba.stackexchange.com/questions/301596/postgresql-trigger-to-update-date-modified
--https://stackoverflow.com/questions/35734806/liquibase-error-postgresql-unterminated-dollar-quoted-string-at-or-near-bod
CREATE FUNCTION created_at() RETURNS TRIGGER AS '
BEGIN
    NEW.created_at = now();
    NEW.updated_at = now();
    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE FUNCTION updated_at() RETURNS TRIGGER AS '
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
' LANGUAGE plpgsql;

-- clients
CREATE TRIGGER clients_created_at BEFORE INSERT ON clients
    FOR EACH ROW EXECUTE PROCEDURE created_at();

CREATE TRIGGER clients_updated_at BEFORE UPDATE ON clients
    FOR EACH ROW EXECUTE PROCEDURE updated_at();

-- accounts
CREATE TRIGGER accounts_created_at BEFORE INSERT ON accounts
    FOR EACH ROW EXECUTE PROCEDURE created_at();

CREATE TRIGGER accounts_updated_at BEFORE UPDATE ON accounts
    FOR EACH ROW EXECUTE PROCEDURE updated_at();

-- agreements
CREATE TRIGGER agreements_created_at BEFORE INSERT ON agreements
    FOR EACH ROW EXECUTE PROCEDURE created_at();

CREATE TRIGGER agreements_updated_at BEFORE UPDATE ON agreements
    FOR EACH ROW EXECUTE PROCEDURE updated_at();

-- products
CREATE TRIGGER products_created_at BEFORE INSERT ON products
    FOR EACH ROW EXECUTE PROCEDURE created_at();

CREATE TRIGGER products_updated_at BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE PROCEDURE updated_at();

-- transactions
CREATE TRIGGER transactions_created_at BEFORE INSERT ON transactions
    FOR EACH ROW EXECUTE PROCEDURE created_at();

CREATE TRIGGER transactions_updated_at BEFORE UPDATE ON transactions
    FOR EACH ROW EXECUTE PROCEDURE updated_at();
