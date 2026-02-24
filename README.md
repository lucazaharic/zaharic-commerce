# 🛒 Zaharic Commerce

O **Zaharic Commerce** é um ecossistema de gestão comercial completo, focado em performance, usabilidade e código limpo. O projeto foi desenvolvido para gerenciar fluxos de vendas, estoque de produtos e cadastros administrativos, contando com uma interface moderna e transições fluidas.

[Image of a software architecture diagram showing Spring Boot, Thymeleaf, and PostgreSQL interaction]

## ✨ Funcionalidades Principais

- **Módulo de Vendas:** Sistema dinâmico para adição de itens, cálculo automático de totais e integração direta com o estoque.
- **Gestão de Estoque:** Controle de entrada e saída de produtos com cálculos de lucro e margem baseados em regras de negócio.
- **Interface Premium:** Experiência de usuário aprimorada com animações de "Landing Page" (Fade-in e Slide) em todas as rotas.
- **Segurança e Login:** Fluxo de autenticação customizado e proteção de rotas administrativas.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.x (Spring Data JPA, Spring Web)
- **Engine de Template:** Thymeleaf (com fragmentos dinâmicos)
- **Banco de Dados:** PostgreSQL
- **Front-end:** Bootstrap 5, Bi-Icons, JQuery (DataTables e Masks)
- **Padrões de Projeto:** SOLID, Clean Code e Service Pattern

## 📐 Arquitetura e Boas Práticas

O projeto foi refatorado para seguir os princípios **SOLID**, garantindo que a lógica de negócio (como o cálculo de lucro) esteja isolada das camadas de controle. A persistência é gerenciada via **Spring Data JPA**, otimizando as consultas e transações no banco de dados.

[Image of a MVC architecture diagram]

## 🚀 Como Executar o Projeto

1. Clone o repositório: 
   `git clone https://github.com/lucazaharic/zaharic-commerce.git`
2. Configure o banco de dados no `application.properties`.
3. Execute o comando Maven:
   `mvn spring-boot:run`
4. Acesse em seu navegador: `http://localhost:8080`

---
Desenvolvido por **Luca Zaharic** - Graduado em Sistemas de Informação pela FIAP.
