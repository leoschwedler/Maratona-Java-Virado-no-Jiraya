# Exercícios — Bloco 04: Orientação a Objetos, Classes e Métodos

## Como fazer

Resolva os exercícios na ordem. Eles começam simples e vão juntando os conceitos.

**Não use recursos que ainda não foram estudados neste bloco só para facilitar.**

---

# Parte 1 — Classes e objetos

### 01 — Classe Pessoa
Crie uma classe `Pessoa` com:
- `nome`
- `idade`
- `sexo`

No `main`, crie um objeto e preencha os atributos. Imprima os valores.

### 02 — Dois objetos
Usando a classe `Pessoa`, crie duas pessoas diferentes e mostre os dados das duas.

### 03 — Classe Carro
Crie uma classe `Carro` com:
- `nome`
- `modelo`
- `ano`

Crie dois objetos com valores diferentes e imprima os dados.

### 04 — Alteração de atributo
Crie um objeto `Carro`, preencha seus atributos, imprima e depois altere o `ano`. Imprima novamente.

### 05 — Vários objetos
Crie três objetos `Carro`. Cada um deve possuir valores diferentes.

### 06 — Referência
Crie dois objetos `Carro`:

```java
Carro carro1 = new Carro();
Carro carro2 = new Carro();
```

Preencha valores diferentes. Depois faça:

```java
carro1 = carro2;
```

Imprima os dados usando as duas referências e explique o que aconteceu.

---

# Parte 2 — Coesão

### 07 — Classe Professor
Crie uma classe `Professor` contendo somente informações relacionadas ao professor:
- nome
- idade
- sexo

### 08 — Separando responsabilidades
Imagine uma classe `Pessoa` contendo dados de estudante e professor ao mesmo tempo.

Separe essa ideia em duas classes:
- `Estudante`
- `Professor`

Escreva quais atributos pertencem a cada uma.

### 09 — Identifique o problema
Analise:

```java
public class Carro {
    String nome;
    int ano;

    String nomeAluno;
    double notaAluno;
}
```

Explique por que essa classe possui um problema de coesão e como você separaria as responsabilidades.

---

# Parte 3 — Métodos básicos

### 10 — Primeiro método
Crie uma classe `Calculadora` com um método:

```java
public void soma()
```

O método deve imprimir `10 + 10`.

### 11 — Dois métodos
Na `Calculadora`, crie:
- `soma()`
- `subtracao()`

Cada método deve executar sua operação e imprimir o resultado.

### 12 — Multiplicação
Crie:

```java
public void multiplica()
```

Faça o método imprimir o resultado de `5 * 8`.

### 13 — Método com parâmetros
Crie:

```java
public void multiplica(int numero1, int numero2)
```

O método deve imprimir a multiplicação dos dois valores.

Teste com pelo menos cinco chamadas diferentes.

### 14 — Parâmetro x argumento
Faça uma chamada para o método anterior e escreva em um comentário:
- quais são os parâmetros;
- quais são os argumentos.

### 15 — Calculadora
Crie métodos com parâmetros para:
- soma
- subtração
- multiplicação
- divisão

Por enquanto, faça os métodos imprimirem os resultados.

---

# Parte 4 — Retorno

### 16 — Retorno simples
Crie:

```java
public int soma(int a, int b)
```

O método deve retornar a soma.

No `main`, armazene o resultado em uma variável e imprima.

### 17 — Retorno de multiplicação
Crie um método que receba dois inteiros e retorne a multiplicação.

### 18 — Retorno de divisão
Crie um método que receba dois `double` e retorne a divisão.

### 19 — Retorno x impressão
Crie dois métodos:

```text
um retorna um valor
outro apenas imprime
```

Teste os dois e explique a diferença entre **retornar** e **imprimir**.

### 20 — Calculadora completa
Transforme sua calculadora para que os métodos retornem os resultados em vez de imprimi-los.

---

# Parte 5 — Parâmetros primitivos

### 21 — Dobro
Crie um método que receba um `int` e retorne o dobro.

### 22 — Triplo
Crie um método que receba um `double` e retorne o triplo.

### 23 — Alteração local
Crie:

```java
public void altera(int numero)
```

Dentro do método, atribua outro valor ao parâmetro.

No `main`, passe uma variável para o método e depois imprima a variável original.

**Objetivo:** observar o comportamento de parâmetros de tipos primitivos.

---

# Parte 6 — Objetos como parâmetros

### 24 — Imprimir estudante
Crie:

```java
class Estudante
```

com:
- nome
- idade
- sexo

Depois crie uma classe `Impressora` com um método que receba um `Estudante` e imprima seus dados.

### 25 — Alterar estudante
Crie um método que receba um `Estudante` e altere o nome do estudante.

No `main`, imprima o nome antes e depois da chamada.

### 26 — Alterar vários atributos
Crie um método que receba um `Estudante` e altere:
- nome
- idade
- sexo

Observe o objeto depois da chamada.

### 27 — Dois estudantes
Crie dois estudantes e passe cada um separadamente para o método de impressão.

### 28 — Objeto compartilhado
Faça duas variáveis de referência apontarem para o mesmo objeto.

Passe uma delas para um método que altere o nome.

Depois observe o que acontece ao acessar o objeto através da outra referência.

---

# Parte 7 — `this`

### 29 — Primeiro contato com `this`
Crie uma classe `Estudante` com:
- nome
- idade

Crie um método `imprime()` que use `this` para acessar os atributos.

### 30 — Dois objetos e `this`
Crie dois estudantes.

Chame `imprime()` em cada um.

Tente explicar por que o mesmo método imprime informações diferentes.

### 31 — `this` e alteração
Crie um método que altere um atributo usando `this`.

Exemplo de ideia:

```text
this.nome = ...
```

Teste com dois objetos diferentes.

---

# Parte 8 — Varargs

### 32 — Soma com varargs
Crie:

```java
public int soma(int... numeros)
```

Some todos os valores recebidos e retorne o resultado.

Teste com:
- nenhum número
- um número
- dois números
- cinco números

### 33 — Média com varargs
Crie um método que receba vários `double` usando varargs e retorne a média.

### 34 — Maior valor
Crie um método com varargs de `int` que encontre o maior valor.

Não use métodos prontos de máximo.

### 35 — Contador
Crie um método com varargs que conte quantos números pares foram recebidos.

---

# Parte 9 — Exercícios integradores

### 36 — Calculadora
Crie uma classe `Calculadora` com métodos para:

- somar
- subtrair
- multiplicar
- dividir
- calcular média de vários números usando varargs

Os métodos devem retornar os resultados.

### 37 — Estudante
Crie uma classe `Estudante` com:

```text
nome
idade
sexo
notas
```

Crie métodos para:
- imprimir dados;
- calcular a média;
- retornar a maior nota.

### 38 — Funcionário
Crie uma classe `Funcionario` com:

```text
nome
idade
salarios
```

Crie métodos para:
- imprimir os dados;
- calcular a média dos salários;
- retornar o maior salário.

### 39 — Método que recebe objeto
Crie uma classe `Impressora` com métodos que recebam:
- `Estudante`
- `Funcionario`

Cada método deve imprimir os dados do objeto recebido.

### 40 — Referência + método
Crie dois objetos `Funcionario`.

Faça um método receber um deles e alterar seu salário.

Depois imprima os dois objetos e explique por que somente um deles foi alterado.

---

# Parte 10 — Desafio final

## 41 — Sistema de Funcionário

Crie uma classe:

```text
Funcionario
```

com:

```text
nome
idade
salarios
```

O funcionário deve possuir **três salários**.

Crie métodos para:

1. imprimir todos os dados do funcionário;
2. calcular e retornar a média dos salários;
3. retornar o maior salário;
4. retornar o menor salário;
5. aumentar todos os salários por uma porcentagem recebida como parâmetro;
6. receber outro `Funcionario` como parâmetro e imprimir seus dados;
7. receber vários valores usando varargs e calcular a média deles.

No `main`:

- crie pelo menos dois funcionários;
- preencha os dados;
- chame todos os métodos;
- mostre os resultados.

### Regras

- Use classes e objetos.
- Use atributos.
- Use métodos.
- Use parâmetros.
- Use retorno.
- Use `this` em pelo menos um método.
- Use varargs em pelo menos um método.
- Não coloque toda a lógica dentro do `main`.
- Mantenha as classes com responsabilidades coerentes.

---

## Objetivo do bloco

Ao terminar, você deve conseguir olhar para um problema simples e pensar:

```text
Qual classe representa isso?
        ↓
Quais atributos ela possui?
        ↓
Quais comportamentos viram métodos?
        ↓
Quais dados o método precisa receber?
        ↓
O método precisa retornar alguma coisa?
        ↓
Estou trabalhando com um valor primitivo ou com um objeto?
```

Quando terminar os exercícios, envie seu código aqui. Na correção, primeiro vou apontar o problema e dar uma dica — sem entregar a solução diretamente.
