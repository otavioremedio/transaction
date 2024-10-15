# Caju transaction
Fiz o desenvolvimento mais orientado em testes, então pra executar os cenários use /.gradlew test

Se quiser testar via postman, será necessário o uso do docker.<br>

A partir da pasta do projeto executar docker-compose -f wiremock/docker-compose-wiremock.yml up e depois subir a aplicação. <br> 

Usar o curl de exempo abaixo:

```json
curl --location 'http://localhost:8093/transactions' \
--header 'Content-Type: application/json' \
--data '{
"account": "124",
"mcc": "5412",
"merchant": "UBER EATS                   SAO PAULO BR",
"totalAmount": 500.00
} '
```

Para o ponto 4, penso que iria utilizar um cache pra armazenar a conta da transação até o momento em que se encerra o fluxo, essa verificação poderia ficar num método recursivo por um limite de tempo, verificando se a conta ja foi removida do cache, para então adicionar novamente e prosseguir com a nova cobrança.

![image](https://github.com/user-attachments/assets/6990bfba-5cd6-4e4c-8041-0f1837c556be)

