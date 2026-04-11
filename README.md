# Zenon Fraud Detector

Sistema de detecção de fraudes em transações financeiras.

## Sobre o Projeto

O Zenon Fraud Detector é uma aplicação Java que lê arquivos de transações financeiras (CSV) e identifica possíveis fraudes. O projeto utiliza datasets no formato PaySim, que simula transações de dinheiro móvel.

**Status:** 🚧 Em desenvolvimento

## Tecnologias

- Java 21+
- Gradle 9.2

## Estrutura do Projeto

```
src/main/java/br/com/zenon/
├── Main.java                 # Ponto de entrada da aplicação
├── Transaction.java          # Modelo de dados da transação
├── TransactionCustomer.java  # Dados do cliente (origem/destino)
├── TransactionIngestor.java  # Leitor de arquivos CSV
└── TransactionType.java      # Tipos de transação
```

## Modelo de Dados

### Transaction

| Campo | Tipo | Descrição |
|-------|------|-----------|
| step | int | Unidade de tempo (1 step = 1 hora) |
| type | TransactionType | Tipo da transação |
| amount | BigDecimal | Valor da transação |
| origin | TransactionCustomer | Cliente de origem |
| recipient | TransactionCustomer | Cliente de destino |
| isFraud | boolean | Indica se é fraude |
| isFlaggedFraud | boolean | Indica se foi marcada como fraude |

### Tipos de Transação

- `CASH_IN` - Depósito
- `CASH_OUT` - Saque
- `DEBIT` - Débito
- `PAYMENT` - Pagamento
- `TRANSFER` - Transferência

## Como Executar

### Pré-requisitos

- JDK 21 ou superior
- Gradle 9.2 (ou use o wrapper incluído)

### Dataset

Baixe o dataset PaySim do Kaggle em [Synthetic Financial Datasets For Fraud Detection](https://www.kaggle.com/datasets/ealaxi/paysim1). Clique em **Download** e depois em **Download dataset as zip** (login necessário).

Este dataset é baseado em logs financeiros de uma empresa de pagamentos móveis presente em 14 países e contém aproximadamente 6 milhões de transações (~493MB).

### Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/zenon-fraud-detector.git
cd zenon-fraud-detector
```

2. Baixe o dataset e extraia o arquivo CSV na pasta `data/`:
```bash
mkdir -p data
# Extraia o arquivo PS_20174392719_1491204439457_log.csv para data/
```

3. Compile o projeto:
```bash
./gradlew build
```

4. Execute:
```bash
./gradlew run
```

## Formato do CSV

O arquivo CSV deve seguir o formato PaySim com as seguintes colunas:

```
step,type,amount,nameOrig,oldbalanceOrg,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud
```

Exemplo:
```csv
step,type,amount,nameOrig,oldbalanceOrg,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud
1,PAYMENT,9839.64,C1231006815,170136.0,160296.36,M1979787155,0.0,0.0,0,0
1,CASH_OUT,850002.52,C1280323807,850002.52,0.0,C873221189,6510099.11,7360101.63,1,0
```

## Roadmap

- [x] Modelo de dados
- [x] Ingestão de arquivos CSV
- [ ] Lógica de detecção de fraudes
- [ ] Testes unitários
- [ ] Relatórios de análise

## Licença

Este projeto está sob a licença MIT.
