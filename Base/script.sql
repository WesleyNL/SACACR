CREATE DATABASE TG;
USE TG;

-- --------------------------------------------------------

CREATE TABLE ACESSO(
	CODIGO int auto_increment primary key,
    NOME varchar(50) not null,
    LOGIN varchar(20) not null,
    SENHA varchar(20) not null,
    DATA_INCLUSAO datetime not null
);

SELECT * FROM ACESSO;
INSERT INTO ACESSO VALUES (1, 'Wesley NL', 'Teste', 'RPRMFL', '2016-09-11 14:30:00'); -- Senha 12345 (Algoritmo próprio) 

-- --------------------------------------------------------

CREATE TABLE LOG_ACESSO(
	CODIGO int auto_increment primary key,
    CODIGO_USUARIO int references ACESSO(codigo),
    DATA_ACESSO datetime not null
);

SELECT * FROM LOG_ACESSO;

-- --------------------------------------------------------

CREATE TABLE LOG_UTILIZACAO(
	CODIGO int auto_increment primary key,
    QUANTIDADE decimal not null,
	TIPO_RESERVATORIO tinyint not null,
    DATA_HORA datetime not null
);

SELECT * FROM LOG_UTILIZACAO;

SELECT SUM(CASE WHEN TIPO_RESERVATORIO = 0 OR TIPO_RESERVATORIO IS NULL THEN QUANTIDADE ELSE 0 END) AS QTD_CONCESSIONARIA,
SUM(CASE WHEN TIPO_RESERVATORIO = 1 THEN QUANTIDADE ELSE 0 END) AS QTD_CHUVA,
MIN(PORCENTAGEM) AS NIVEL_CHUVA
FROM LOG_UTILIZACAO u 
LEFT JOIN LOG_NIVEL n
ON u.CODIGO = n.CODIGO_UTILIZACAO
WHERE u.DATA_HORA >= '2016-09-01' AND u.DATA_HORA <= '2016-09-30';

-- --------------------------------------------------------

CREATE TABLE LOG_NIVEL(
	CODIGO int auto_increment primary key,
    CODIGO_UTILIZACAO int references LOG_UTILIZACAO (codigo),
    PORCENTAGEM decimal not null,
    DATA_HORA datetime not null
);

SELECT * FROM LOG_NIVEL ORDER BY CODIGO DESC LIMIT 1;

-- --------------------------------------------------------

CREATE TABLE PARAMETROS(
	CODIGO int auto_increment primary key,
    RESPONSAVEL int references ACESSO(codigo),
    PREFERENCIA tinyint not null,
    DATA_ALTERACAO datetime not null
);

SELECT * FROM PARAMETROS;

-- --------------------------------------------------------
