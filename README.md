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

