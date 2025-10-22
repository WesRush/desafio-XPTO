# Desafio XPTO: API de Controle Financeiro

Este projeto é uma API REST desenvolvida como parte de um desafio técnico, com foco na célula de Financeiro e Controladoria.

## 1. Objetivo Principal

Desenvolver uma API REST utilizando Java 8 com Spring Boot para o controle de receitas e despesas dos clientes (Pessoa Física e Pessoa Jurídica) da empresa fictícia XPTO. O projeto também deve ser versionado no GitHub.

O sistema deve permitir o gerenciamento (CRUD) dos clientes, suas contas e endereços, e registrar suas movimentações financeiras. Além disso, a API deve calcular a receita que a XPTO obtém com base nas movimentações de seus clientes.

## 2. Requisitos de Negócio

### 2.1. Gerenciamento de Clientes (CRUD)
* O sistema deve permitir manter (CRUD) os clientes.
* **Distinção PF/PJ:** O cadastro deve diferenciar Pessoa Física (PF) e Pessoa Jurídica (PJ), lidando com os dados específicos de cada um (ex: CPF para PF, CNPJ para PJ). A normalização dessa estrutura é um requisito do desafio.
* **Movimentação Inicial:** Ao cadastrar um novo cliente, o sistema deve registrar uma "movimentação inicial", que servirá como ponto de partida para o controle financeiro.
* **Manutenção:** Deve ser possível atualizar os dados do cliente, mas com atenção para não alterar dados que comprometam o histórico de movimentações.

### 2.2. Contas e Movimentações
* Um cliente pode ter múltiplas contas bancárias cadastradas (C1, C2, C3).
* Todas as movimentações (pagamentos e recebimentos) são feitas através dessas contas.
* **CRUD de Contas:** O sistema deve ter um CRUD para as contas.
* **Exclusão Lógica:** Caso uma conta possua movimentações associadas, ela não poderá ser alterada; a exclusão deve ser apenas lógica.

### 2.3. Cálculo de Receita da XPTO
A receita da XPTO é calculada com base no volume de operações (entradas e saídas) de cada cliente, em um período de 30 dias a partir da data de cadastro do cliente.

A regra de cobrança por movimentação é:
* Até 10 movimentações: R$ 1,00 por movimentação.
* De 10 a 20 movimentações: R$ 0,75 por movimentação.
* Acima de 20 movimentações: R$ 0,50 por movimentação.

### 2.4. Endereços
* O sistema deve possuir um CRUD de endereços.
* Deve ser possível efetuar a manutenção (atualização) dos endereços dos clientes.
* **Manutenção:** O sistema permite a manutenção dos endereços dos clientes. A criação e atualização do endereço são realizadas através dos endpoints de criação (`POST /api/clientes`) e atualização (`PUT /api/clientes/{id}`) do próprio cliente, garantindo a associação correta.
* **Leitura Independente:** Foi implementado um endpoint (`GET /api/enderecos/{id}`) para permitir a consulta direta de um endereço pelo seu ID, caso necessário. A exclusão independente foi omitida para manter a integridade referencial com o cliente.

## 3. Relatórios e Saídas de Dados

O sistema deve fornecer as seguintes funções de relatório:

1.  **Relatório de saldo do cliente X:**
    * Dados do cliente, endereço e data de cadastro.
    * Totais de movimentações (crédito, débito, total).
    * Valor pago pelas movimentações (taxas XPTO).
    * Saldo inicial e Saldo atual.

2.  **Relatório de saldo do cliente X por período:**
    * Mesmas informações do relatório anterior, mas filtradas por um período (data início e fim).

3.  **Relatório de saldo de todos os clientes:**
    * Listagem de todos os clientes, data de cadastro e saldo atual.

4.  **Relatório de receita da empresa (XPTO) por período:**
    * Listagem de clientes, quantidade de movimentações e o valor pago por elas (receita).
    * Total geral de receitas da XPTO no período.

---
## 5. Boas Práticas e Padrões de Projeto

Esta seção detalha as boas práticas de desenvolvimento e os padrões de projeto utilizados na implementação atual.

### 5.1. Boas Práticas Adotadas
* **Separação de Responsabilidades:** O projeto foi estruturado em camadas lógicas distintas para facilitar a manutenção e organização:
    * `controller`: Responsável por expor a API REST e receber/responder requisições HTTP.
    * `service`: Contém a lógica de negócio principal (regras de cadastro, cálculo do relatório).
    * `repository`: Abstrai o acesso aos dados, interagindo com o banco de dados via Spring Data JPA.
    * `entity`: Define as entidades (tabelas) do banco de dados.
    * `dto`: Utiliza Data Transfer Objects para transportar dados entre as camadas `controller` e `service`, evitando a exposição direta das entidades na API.
* **Injeção de Dependência (DI):** Utilizada extensivamente pelo Spring Boot para gerenciar as instâncias das classes (ex: injetar o `ClienteService` no `ClienteController`, injetar os `Repositories` e o `EntityManager` no `ClienteService`). Foi adotada a **injeção via construtor** no `ClienteService`, que é considerada uma melhor prática.
* **Tratamento de Exceções:** Implementado um handler básico no `ClienteController` (`@ExceptionHandler`) para capturar `RuntimeException` (como "Cliente não encontrado") e retornar um status HTTP 404 Not Found apropriado, em vez de um erro 500 genérico.
* **Uso de `Optional`:** O método `buscarClientePorId` no `ClienteService` utiliza `Optional<Cliente>` retornado pelo `findById` do repositório para tratar caso de um cliente não ser encontrado.
* **Transacionalidade (`@Transactional`):** A anotação `@Transactional` foi utilizada nos métodos do `ClienteService` que modificam dados (`criarNovoCliente`) para garantir a atomicidade das operações no banco de dados. A opção `readOnly = true` foi usada nos métodos de consulta (`buscarClientePorId`, `listarTodosClientes`, `gerarRelatorioSaldoCliente`) para otimização de performance.
* **Uso de `BigDecimal`:** O tipo `BigDecimal` foi utilizado para representar valores monetários (`saldoInicial`, `valor` da movimentação), evitando problemas de arredondamento comuns com `float` ou `double`.
* **API RESTful:** Os endpoints foram projetados seguindo princípios REST, utilizando os verbos HTTP apropriados (`POST` para criar, `GET` para ler) e URLs baseadas em recursos (`/api/clientes`).
* **Documentação com Swagger (OpenAPI):** A dependência `springfox-boot-starter` foi adicionada e configurada (`SwaggerConfig.java`) para gerar documentação interativa da API, facilitando testes e o entendimento dos endpoints.
* **Testes Unitários (JUnit + Mockito):** Foi criada uma classe de testes (`ClienteServiceTest`) utilizando JUnit 5 e Mockito para validar a lógica de negócio do `ClienteService` de forma isolada, começando pelo método `criarNovoCliente`.
* **Saída Formatada no Console:** O endpoint do Relatório de Saldo (`GET /api/clientes/{id}/relatorio-saldo`), além de retornar o JSON, imprime uma versão formatada e legível do relatório no console da IDE, facilitando a visualização e depuração, conforme flexibilidade permitida.
### 5.2. Padrões de Projeto (Design Patterns)
* **Repository Pattern:** Implementado automaticamente pelo Spring Data JPA através das interfaces que estendem `JpaRepository` (`ClienteRepository`, `MovimentacaoRepository`, etc.). Abstrai os detalhes de acesso aos dados.
* **Service Layer Pattern:** A classe `ClienteService` atua como uma camada de serviço, encapsulando a lógica de negócio e orquestrando as interações entre os `Controllers` e os `Repositories`.
* **Data Transfer Object (DTO) Pattern:** Utilizado com as classes no pacote `dto` (`ClienteRequestDTO`, `EnderecoDTO`, `ContaInicialDTO`, `RelatorioSaldoClienteDTO`) para transferir dados entre as camadas sem expor as entidades JPA.
* **Builder Pattern:** Utilizado nas entidades (`Movimentacao`) e DTOs (`RelatorioSaldoClienteDTO`) através da anotação `@Builder` do Lombok, facilitando a criação de objetos complexos com muitos campos.
* **JPA Inheritance (Single Table Strategy):** A estratégia de Herança de Tabela Única (`@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`) foi utilizada na entidade `Cliente` para mapear as classes `Cliente` (abstrata), `PessoaFisica` e `PessoaJuridica` em uma única tabela no banco de dados, resolvendo o requisito de normalização PF/PJ.

---
## 6. Observações Adicionais


* **Abordagem do Stored Procedure/Function:** A intenção original era implementar o cálculo das taxas da XPTO utilizando um Stored Procedure com parâmetro `OUT`, conforme exigência do desafio . No entanto, devido a problemas persistentes, a chamada a procedures/functions com parâmetros `OUT` ou mesmo via `@NamedStoredProcedureQuery`/`@Query` nativa falhava consistentemente. Para garantir a funcionalidade do relatório e cumprir o prazo, optou-se por mover o cálculo da taxa para a camada de serviço Java (`ClienteService`). Para cumprir o requisito *técnico* de chamar um objeto de banco, foi criada e chamada com sucesso uma Stored Function (`fn_calcular_taxa_xpto`) que realiza o mesmo cálculo, utilizando `EntityManager.createNativeQuery` para execução direta, contornando as abstrações que apresentavam problemas.
* **Tecnologias Utilizadas:** Java 8, Spring Boot 2.7.18, Spring Data JPA, Hibernate, MySQL, Lombok, SpringFox (Swagger).
* **Banco de Dados:** MySQL, conforme flexibilidade dada no e-mail de instrução.
* **Escopo:** 
  - Foram implementados os requisitos mínimos obrigatórios: CRUD de Clientes (Create e Read) e o Relatório de Saldo do Cliente. 
  - O tratamento de exceções foi aprimorado. 
  - **Gerenciamento de Endereços** (vinculado ao cliente, com leitura independente)