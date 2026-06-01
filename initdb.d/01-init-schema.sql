-- 1. Criação da Tabela de Clientes
CREATE TABLE IF NOT EXISTS clients (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Criação da Tabela de Contas
CREATE TABLE IF NOT EXISTS accounts (
    id VARCHAR(255) PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL,
    balance NUMERIC(19, 4) NOT NULL DEFAULT 0.0000,
    CONSTRAINT fk_client_account FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- 3. Criação da Tabela de Transações (Histórico do Ledger)
CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(255) PRIMARY KEY,
    origin_account_id VARCHAR(255) NOT NULL,
    destination_account_id VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 4) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_origin_account FOREIGN KEY (origin_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_destination_account FOREIGN KEY (destination_account_id) REFERENCES accounts(id)
);

-- Carga inicial para os testes (Clients e Accounts)
INSERT INTO clients (id, name, email) VALUES
('cli-teste', 'Cliente Teste', 'teste@cliente.com'),
('cli-maria', 'Maria Silva', 'maria@exemplo.com'),
('cli-joao', 'João Souza', 'joao@exemplo.com')
ON CONFLICT (id) DO NOTHING;

INSERT INTO accounts (id, client_id, balance) VALUES
('acc-teste', 'cli-teste', 1000.0000),
('acc-maria', 'cli-maria', 500.0000),
('acc-joao', 'cli-joao', 750.0000)
ON CONFLICT (id) DO NOTHING;