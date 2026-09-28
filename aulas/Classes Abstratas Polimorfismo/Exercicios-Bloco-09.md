# Exercícios — Bloco 09

> **Regra:** faça uma aula por vez. Não avance enquanto a aula atual não
> estiver corrigida.

> Os exercícios não devem copiar os exemplos da aula. A dificuldade
> aumenta progressivamente e o desafio final exige modelagem própria.

---

# Aula 84 — Classes abstratas

## 1. 🟢 Funcionários

Crie uma classe abstrata `Funcionario` contendo:

- nome;
- salário.

Crie duas subclasses concretas:

- `Desenvolvedor`;
- `Gerente`.

Crie objetos somente das subclasses.

**Objetivo:** perceber que `Funcionario` serve como base e não deve ser
instanciada diretamente.

## 2. 🟡 Veículos

Modele:

```text
Veiculo
├── Carro
└── Moto
```

Faça `Veiculo` ser abstrata.

Coloque nela pelo menos duas características comuns às subclasses e crie
os objetos concretos.

## 3. 🔴 Sistema de documentos

Crie uma classe abstrata `Documento` com informações comuns a documentos.

Crie pelo menos três subclasses diferentes.

O sistema deve conseguir armazenar objetos das subclasses em uma estrutura
de `Documento`, sem criar diretamente um `Documento`.

---

# Aula 85 — Métodos abstratos

## 4. 🟢 Cálculo

Crie uma classe abstrata `Operacao` com:

```java
public abstract double calcular();
```

Crie duas subclasses com cálculos diferentes.

## 5. 🟡 Funcionários e bônus

Crie uma classe abstrata `Funcionario` com:

```java
public abstract double calcularBonus();
```

Faça `Desenvolvedor` e `Gerente` implementarem regras diferentes.

Não coloque a regra de bônus dentro de `main`.

## 6. 🔴 Sistema de preços

Crie uma classe abstrata `Produto` com um método abstrato que calcule o
preço final.

Crie pelo menos três produtos com regras diferentes.

O programa deve conseguir percorrer uma coleção/array de `Produto` e
calcular o preço de cada item.

---

# Aula 86 — Regras dos métodos abstratos

## 7. 🟢 Implementação obrigatória

Crie uma classe abstrata com dois métodos abstratos.

Crie uma subclasse concreta e implemente os dois.

Depois crie outra subclasse que implemente apenas um e observe o erro.
Corrija transformando a segunda classe em abstrata ou implementando o
método restante.

## 8. 🟡 Abstrato + concreto

Crie uma classe abstrata `Relatorio` contendo:

- um método concreto que imprime uma informação comum;
- um método abstrato que define uma parte específica do relatório.

Crie duas subclasses.

## 9. 🔴 Hierarquia em três níveis

Crie:

```text
Pessoa
  ↓
Funcionario
  ↓
Gerente
```

Faça `Pessoa` possuir comportamento concreto, `Funcionario` possuir uma
obrigação abstrata e `Gerente` fornecer a implementação final.

O objetivo é entender que uma classe abstrata pode participar de uma
hierarquia maior e não precisa conter somente métodos abstratos.

---

# Aula 87 — Interfaces

## 10. 🟢 Contrato simples

Crie uma interface `Autenticavel` com uma operação de autenticação.

Crie duas classes diferentes que implementem o contrato.

## 11. 🟡 Capacidade

Crie uma interface `Exportavel`.

Faça duas classes que não possuem relação de herança entre si
implementarem essa interface.

Crie um código que trabalhe com a interface, e não diretamente com uma
das classes.

## 12. 🔴 Sistema de notificações

Crie uma interface `Notificavel`.

Implemente-a em pelo menos três classes diferentes.

Crie um método que receba `Notificavel` como parâmetro e execute a operação
do contrato.

---

# Aula 88 — Múltiplas interfaces

## 13. 🟢 Duas capacidades

Crie:

```text
Imprimivel
Digitalizavel
```

Faça uma classe implementar as duas.

## 14. 🟡 Dispositivo

Crie três interfaces representando capacidades diferentes.

Faça um dispositivo implementar duas delas.

Depois crie outro dispositivo implementando uma combinação diferente.

## 15. 🔴 Sistema de arquivos

Crie interfaces:

```text
Compactavel
Criptografavel
Exportavel
```

Crie classes diferentes implementando combinações diferentes dessas
interfaces.

Crie métodos que recebam cada interface como parâmetro.

---

# Aula 89 — `static` em interfaces

## 16. 🟢 Constantes

Crie uma interface contendo pelo menos três valores constantes relacionados
a uma configuração.

Utilize esses valores em outra classe.

## 17. 🟡 Método `static`

Crie uma interface `Conversor` com um método `static` para realizar uma
conversão simples.

Chame o método pela própria interface.

## 18. 🔴 Biblioteca de utilidades

Crie uma interface contendo constantes e pelo menos dois métodos
`static` relacionados ao mesmo domínio.

Crie uma classe que utilize essa interface, mas sem transformar os métodos
`static` em métodos de instância.

---

# Aula 90 — Polimorfismo: introdução

## 19. 🟢 Referência genérica

Crie:

```text
Animal
├── Cachorro
└── Gato
```

Faça:

```java
Animal animal = new Cachorro();
```

e depois uma segunda referência para `Gato`.

Observe quais métodos podem ser acessados pela referência.

## 20. 🟡 Produtos

Crie:

```text
Produto
├── Computador
├── Livro
└── Alimento
```

Faça cada classe possuir uma implementação diferente de uma operação
comum.

Utilize referências do tipo `Produto`.

## 21. 🔴 Processamento genérico

Crie uma classe que receba objetos `Produto` e execute uma operação comum.

A classe não deve possuir `if` para descobrir se recebeu computador, livro
ou alimento.

O comportamento específico deve ficar nas subclasses.

---

# Aula 91 — Polimorfismo em operação

## 22. 🟢 Sobrescrita

Crie uma hierarquia com um método sobrescrito em duas subclasses.

Use referências da superclasse e observe qual implementação é executada.

## 23. 🟡 Relatório

Crie um método que receba uma referência genérica e produza uma informação
usando um método sobrescrito.

Teste com pelo menos três objetos diferentes.

## 24. 🔴 Processador de operações

Crie uma classe que processe diferentes tipos de operações através de uma
referência comum.

Não coloque `if` ou `switch` para escolher qual cálculo executar.

Cada objeto deve ser responsável pelo seu próprio comportamento.

---

# Aula 92 — Parâmetros polimórficos

## 25. 🟢 Parâmetro genérico

Crie um método que receba uma superclasse como parâmetro e teste com duas
subclasses.

## 26. 🟡 Serviço genérico

Crie um serviço:

```java
processar(Produto produto)
```

Faça-o funcionar com pelo menos três subclasses de `Produto`.

## 27. 🔴 Relatório desacoplado

Crie uma classe `Relatorio` que receba uma abstração como parâmetro.

Adicione novas subclasses depois sem precisar alterar o método do
relatório.

O objetivo é demonstrar que o método depende da abstração e não dos tipos
concretos.

---

# Aula 93 — Cast e `instanceof`

## 28. 🟢 Cast simples

Crie uma superclasse e uma subclasse com um método exclusivo.

Crie uma referência da superclasse apontando para a subclasse.

Faça o cast e acesse o método específico.

## 29. 🟡 Cast seguro

Crie pelo menos três subclasses.

Receba objetos através da superclasse.

Use `instanceof` para descobrir quando um cast para uma subclasse
específica é seguro.

## 30. 🔴 Processamento especializado

Crie um método que receba a superclasse.

Para o comportamento comum, use polimorfismo.

Quando houver uma funcionalidade realmente exclusiva de uma subclasse,
use `instanceof` e cast somente quando necessário.

Evite transformar o método inteiro em uma sequência de `if`.

---

# Aula 94 — Programação orientada a interfaces

## 31. 🟢 Repositório

Crie:

```text
Repositorio
├── RepositorioMemoria
└── RepositorioArquivo
```

`Repositorio` deve ser uma interface.

Crie um serviço que receba `Repositorio` como parâmetro.

## 32. 🟡 Serviço desacoplado

Crie uma interface para representar um serviço de pagamento.

Implemente pelo menos três formas de pagamento.

O serviço principal deve depender somente da interface.

## 33. 🔴 Troca de implementação

Crie uma interface `Notificacao` e pelo menos três implementações.

Crie uma classe que utilize somente `Notificacao`.

Depois troque a implementação utilizada sem modificar a classe que envia
a notificação.

---

# DESAFIO INTEGRADOR — Sistema de Processamento de Pedidos

## Objetivo

Crie um pequeno sistema de processamento de pedidos que reúna **todo o
conteúdo das aulas 84–94**.

O desafio deve exigir decisões de modelagem. Não copie os exemplos das
aulas.

---

## Requisitos mínimos

### 1. Classe abstrata

Crie uma classe abstrata relacionada ao domínio.

Ela deve possuir:

- atributos comuns;
- pelo menos um comportamento concreto;
- pelo menos um método abstrato.

### 2. Hierarquia

Crie pelo menos três subclasses concretas.

Cada uma deve possuir alguma regra própria.

### 3. Interfaces

Crie pelo menos duas interfaces representando capacidades diferentes.

Pelo menos uma classe deve implementar mais de uma interface.

### 4. `static`

Utilize pelo menos:

- uma constante da interface;
- um método `static` de interface.

### 5. Polimorfismo

Tenha uma estrutura que armazene objetos através de uma abstração:

```java
Produto
```

ou:

```java
Interface
```

sem precisar conhecer os tipos concretos.

### 6. Parâmetro polimórfico

Crie pelo menos um método que receba uma abstração como parâmetro e aceite
objetos de diferentes implementações.

### 7. Cast e `instanceof`

Inclua uma situação em que exista uma funcionalidade realmente específica
de uma implementação.

Use:

```java
instanceof
```

antes do cast.

Não use cast apenas para demonstrar a sintaxe.

### 8. Programação orientada a interfaces

Pelo menos um serviço principal deve depender de uma interface, e não de
uma implementação concreta.

---

## Restrições

Não faça:

```text
um único main gigante
```

Não faça:

```text
if (objeto instanceof A) ...
else if (objeto instanceof B) ...
else if (objeto instanceof C) ...
```

para substituir todo o polimorfismo.

Não crie interfaces sem responsabilidade apenas para cumprir requisito.

Não use herança somente para reaproveitar código quando não existir uma
relação real de especialização.

Não coloque todas as regras de negócio em uma única classe.

---

## O que você deverá conseguir explicar depois

Ao terminar o desafio, você deverá conseguir apontar no seu próprio código:

```text
onde está a classe abstrata?
onde está o método abstrato?
onde uma subclasse implementa a obrigação?
onde estão as interfaces?
onde uma classe implementa múltiplas interfaces?
onde está o static da interface?
onde está o polimorfismo?
onde está o parâmetro polimórfico?
onde foi necessário fazer cast?
por que instanceof foi usado?
onde o código depende de uma interface?
```

Se você não conseguir explicar cada uma dessas decisões, o desafio ainda
não terminou.

---

# Progressão do bloco

```text
84  Classes abstratas
 ↓
85  Métodos abstratos
 ↓
86  Regras dos métodos abstratos
 ↓
87  Interfaces
 ↓
88  Múltiplas interfaces
 ↓
89  static em interfaces
 ↓
90  Polimorfismo
 ↓
91  Polimorfismo em execução
 ↓
92  Parâmetros polimórficos
 ↓
93  Cast + instanceof
 ↓
94  Programação orientada a interfaces
 ↓
🏆 DESAFIO INTEGRADOR
```
