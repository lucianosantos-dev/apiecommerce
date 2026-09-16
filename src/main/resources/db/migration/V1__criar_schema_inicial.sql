CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuarios(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    fuso_horario VARCHAR(50) NOT NULL DEFAULT 'America/Sao_Paulo'
);

CREATE TABLE tokens_verificacao(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    tipo_verificacao VARCHAR(50) NOT NULL CHECK (tipo_verificacao IN ('CONFIRMACAO_EMAIL', 'REDEFINICAO_SENHA')),
    expira_em TIMESTAMP NOT NULL,
    usado_em TIMESTAMP,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),

    FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_token_verificacao_token ON tokens_verificacao(token);

CREATE TABLE categorias(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE clientes(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    nome_completo VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    data_nascimento DATE NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),

    FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE enderecos(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cliente_id UUID NOT NULL,
    identificacao VARCHAR(30) DEFAULT 'Principal',
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(80) NOT NULL,
    cidade VARCHAR(80) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    principal BOOLEAN DEFAULT FALSE,

    FOREIGN KEY(cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE secoes_home(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    ordem INTEGER NOT NULL
);

CREATE TABLE produtos(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    categoria_id UUID NOT NULL,
    nome VARCHAR(100) NOT NULL,
    detalhe_produto TEXT,
    link_foto VARCHAR(255) NOT NULL,
    preco DECIMAL(12, 2) NOT NULL CHECK (preco > 0),
    preco_promocional DECIMAL(12, 2) DEFAULT NULL CHECK(preco_promocional < preco),
    estoque INTEGER NOT NULL DEFAULT 0,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY(categoria_id) REFERENCES categorias(id)
);

CREATE TABLE pedidos(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cliente_id UUID NOT NULL,
    data_pedido TIMESTAMP NOT NULL DEFAULT NOW(),
    valor_total DECIMAL(12, 2) NOT NULL CHECK(valor_total > 0),
    forma_pagamento VARCHAR(50) NOT NULL CHECK(forma_pagamento IN ('BOLETO', 'CARTAO', 'PIX')),
    status_pedido VARCHAR(100) NOT NULL CHECK(status_pedido IN ('PENDENTE', 'PAGO', 'ENVIADO', 'CANCELADO', 'ENTREGUE')),

    cep_entrega VARCHAR(9) NOT NULL,
    logradouro_entrega VARCHAR(150) NOT NULL,
    numero_entrega VARCHAR(20) NOT NULL,
    complemento_entrega VARCHAR(50),
    bairro_entrega VARCHAR(80) NOT NULL,
    cidade_entrega VARCHAR(80) NOT NULL,
    estado_entrega VARCHAR(2) NOT NULL,

    observacoes TEXT,
    FOREIGN KEY(cliente_id) REFERENCES clientes(id)
);

CREATE TABLE itens_pedido(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    produto_id UUID NOT NULL,
    pedido_id UUID NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    preco_unitario DECIMAL(12, 2) NOT NULL DEFAULT 0,
    preco_total DECIMAL(12, 2) NOT NULL DEFAULT 0,

    FOREIGN KEY(produto_id) REFERENCES produtos(id),
    FOREIGN KEY(pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);

CREATE TABLE secoes_home_produtos(
    produto_id UUID NOT NULL,
    secao_id UUID NOT NULL,
    PRIMARY KEY(produto_id, secao_id),

    FOREIGN KEY(produto_id) REFERENCES produtos(id) ON DELETE CASCADE,
    FOREIGN KEY(secao_id) REFERENCES secoes_home(id) ON DELETE CASCADE
);

CREATE TABLE roles(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    nome VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE usuarios_roles(
    usuario_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY(usuario_id, role_id),

    FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
);

INSERT INTO roles(nome) VALUES('ROLE_ADMIN');
INSERT INTO roles(nome) VALUES('ROLE_USER');