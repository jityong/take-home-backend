CREATE TYPE currency_type AS ENUM('fiat', 'crypto');

CREATE TABLE exchange_rates (
    base_currency VARCHAR(10) NOT NULL,
    target_currency VARCHAR(10) NOT NULL,
    rate DOUBLE PRECISION NOT NULL,
    retrieved_at TIMESTAMP WITH TIME ZONE NOT NULL
);

SELECT create_hypertable('exchange_rates', 'retrieved_at');
CREATE INDEX ix_retrieved_at_base_currency ON exchange_rates (base_currency, retrieved_at);
