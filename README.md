# Organiza Aí — Central do Estudante

Aplicação acadêmica simples desenvolvida com Java e Spring Boot, usando arquitetura MVC, Spring Data JPA, Thymeleaf e banco H2.

## Funcionalidades

- **Início:** resumo das atividades e dos flashcards cadastrados.
- **Rotina:** calendário por data, cadastro de matéria, descrição e horário; opção de marcar atividades como concluídas ou excluí-las.
- **Gerenciar tempo:** abre um cronômetro online externo em outra aba do navegador.
- **Flashcards:** formulário simples para cadastrar pergunta e resposta; os cartões ficam listados na página e salvos no banco de dados.

## Tecnologias

- Java 17 ou superior
- Spring Boot 3.5.7
- Spring Web, Thymeleaf, Spring Data JPA e H2
- Maven

## Como executar no Visual Studio Code

1. Extraia o ZIP.
2. Abra a pasta `organiza-ai` no VS Code.
3. Verifique se o Java 17 ou superior está instalado.
4. Aguarde a importação das dependências Maven.
5. Abra `src/main/java/br/com/organizaai/OrganizaAiApplication.java` e clique em **Run** acima do método `main`.

Também é possível executar pelo terminal integrado, se o Maven estiver instalado:

```bash
mvn spring-boot:run
```

Depois que a aplicação iniciar, acesse `http://localhost:8080`.

## Páginas

- Início: `http://localhost:8080/`
- Rotina e calendário: `http://localhost:8080/rotina`
- Gerenciar tempo: `http://localhost:8080/tempo`
- Flashcards: `http://localhost:8080/flashcards`

## Banco de dados

O H2 salva os dados em `./data/organiza-ai-db`. O console pode ser acessado em `http://localhost:8080/h2-console`, usando:
- JDBC URL: `jdbc:h2:file:./data/organiza-ai-db`
- User Name: `sa`
- Password: deixe em branco

## Estrutura principal

```text
src/main/java/br/com/organizaai/
├── OrganizaAiApplication.java
├── controller/PaginaController.java
├── model/AtividadeEstudo.java
├── model/Flashcard.java
├── repository/AtividadeEstudoRepository.java
├── repository/FlashcardRepository.java
├── service/AtividadeEstudoService.java
└── service/FlashcardService.java

src/main/resources/
├── application.properties
├── templates/
│   ├── fragments.html
│   ├── index.html
│   ├── rotina.html
│   ├── tempo.html
│   └── flashcards.html
└── static/css/style.css
```

## Arquitetura em camadas

- `model`: classes que representam os dados salvos no banco.
- `repository`: interfaces que estendem `JpaRepository` e fazem o acesso aos dados.
- `service`: contém as operações e validações simples da aplicação.
- `controller`: recebe as requisições das páginas e chama os Services.

O projeto mantém a separação entre Controller, Service, Repository e Model, conforme a revisão de Spring Boot.

## Flashcards

Cada cartão possui uma pergunta e uma resposta. Ao clicar em **Salvar**, o Controller chama `FlashcardService`, que valida os campos e utiliza `FlashcardRepository` para gravar o cartão no H2. Os cartões cadastrados aparecem abaixo do formulário. Eles permanecem salvos quando a aplicação é fechada e aberta novamente, desde que a pasta `data` seja mantida.
