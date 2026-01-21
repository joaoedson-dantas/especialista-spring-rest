insert into cozinha (id, nome) values (1, 'Tailandesa');
insert into cozinha (id, nome) values (2, 'Indiana');

insert into restaurante (nome, taxa_frete, cozinha_id) values ('Divina Comidas', 0, 1);
insert into restaurante (nome, taxa_frete, cozinha_id) values ('Grand Cheff', 15, 1);
insert into restaurante (nome, taxa_frete, cozinha_id) values ('Carneiro do Ordones', 25, 2);

insert into forma_pagamento (descricao) values ('DINHEIRO');
insert into forma_pagamento (descricao) values ('CARTÃO DE CRÉDITO');
insert into forma_pagamento (descricao) values ('CARTÃO DE DÉBITO');
insert into forma_pagamento (descricao) values ('PIX');

insert into permissao (nome, descricao) values ('CONSULTAR_COZINHAS', 'Permite consultar cozinhas');
insert into permissao (nome, descricao) values ('CRIAR_COZINHAS', 'Permite criar cozinhas');
insert into permissao (nome, descricao) values ('EDITAR_COZINHAS', 'Permite editar cozinhas');

insert into estado (id, nome) values (1, 'Minas Gerais');
insert into estado (id, nome) values (2, 'São Paulo');
insert into estado (id, nome) values (3, 'Ceará');

insert into cidade (id, nome, estado_id) values (1, 'Uberlândia', 1);
insert into cidade (id, nome, estado_id) values (2, 'Belo Horizonte', 1);
insert into cidade (id, nome, estado_id) values (3, 'São Paulo', 2);
insert into cidade (id, nome, estado_id) values (4, 'Campinas', 2);
insert into cidade (id, nome, estado_id) values (5, 'Fortaleza', 3);