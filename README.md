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

