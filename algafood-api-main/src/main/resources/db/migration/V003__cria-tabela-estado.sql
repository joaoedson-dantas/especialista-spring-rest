-- 1 - Criar a nova tabela

CREATE TABLE estado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,

    PRIMARY KEY (id)
) engine=InnoDB default charset=utf8mb4;

-- 2 - Transferir dados da tabela cidade para tabela estado - O resultado do SELECT iremos jogar para dentro do insert
INSERT INTO estado (nome)
SELECT DISTINCT c.nome_estado
FROM cidade c;

-- 3 - Alterar a tabela de cidade para adicionar a coluna estado_id (Aqui vai ser a chave estrangeira) - Para fazer referência a Estado
ALTER TABLE cidade ADD COLUMN estado_id BIGINT NOT NULL;

-- Obs: Como está not null, vai ficar o valor 0 em todos campo de estado_id, por conta disso, não é possível criar a foreign key
-- foreign key: A chave estrangeira, a referência de estado_id com a tabela estado. Não tem como fazer referência com id 0;

-- 4 - Realizar uma atualização na tabela de cidade, atribuindo o valor correto para id_cidade

UPDATE cidade c SET c.estado_id = (SELECT e.id FROM estado e WHERE e.nome = c.nome_estado);

-- 5 - Adicionar o foreign key - Agora conseguimos adicionar a foreign key apontando para tabela estado.
ALTER TABLE cidade ADD CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estado (id);

-- 6 - Apagar a coluna nome_estado
ALTER TABLE cidade DROP COLUMN nome_estado;

-- 7 - Renomear a coluna nome_cidade
ALTER TABLE cidade CHANGE nome_cidade nome VARCHAR(80) NOT NULL;