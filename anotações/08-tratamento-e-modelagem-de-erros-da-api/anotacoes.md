# Tratamento e modelagem de erros da API

## 8.1. Introdução ao tratamento e modelagem de erros

Uma REST API sempre vai ter erros, e os consumidores da API podem usar ela de forma incorreta, passando dados inválidos,
inconsistentes ou tentando fazer algo que não seja possível. Como desenvolvedor precisamos pensar sempre nos consumidores,
se os consumidores estão a usar a API de forma incorreta, os consumidores precisam receber na resposta um status HTTP correto e
também uma mensagem que descreve de forma clara o motivo do erro e possivelmente até uma instrução de como resolver.
