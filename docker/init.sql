CREATE TABLE IF NOT EXISTS funcionarios (
                                            id UUID PRIMARY KEY,
                                            nome VARCHAR(100) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    salario FLOAT NOT NULL,
    data_admissao DATE NOT NULL,
    data_demissao DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    duracao_contrato INT NOT NULL,
    tipo VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS jogadores (
                                         id UUID PRIMARY KEY REFERENCES funcionarios(id),
    numero_camisa INT NOT NULL,
    liberado_pelo_dm BOOLEAN NOT NULL DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS comissao_tecnica (
                                                id UUID PRIMARY KEY REFERENCES funcionarios(id)
    );

CREATE TABLE IF NOT EXISTS funcionarios_clube (
                                                  id UUID PRIMARY KEY REFERENCES funcionarios(id)
    );

CREATE TABLE IF NOT EXISTS usuarios (
                                        id UUID PRIMARY KEY,
                                        email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
    );