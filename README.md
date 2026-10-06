# Trabalho_LPOO_Bianca_Luis_Paola# Motor de Física 3D (T1 - LPOO)

Trabalho prático desenvolvido para a disciplina de Linguagem de Programação Orientada a Objetos (LPOO), consistindo na implementação de um motor de física modular em Java para o cálculo de propriedades geométricas, mássicas e inerciais de corpos rígidos estruturados hierarquicamente.

## Autores
* **Bianca Tofanelli**
* **Luis Cardoso**
* **Vendruscolo**

---

## 🏗️ Arquitetura do Projeto

O código-fonte está organizado em três pacotes principais para garantir o desacoplamento e a coesão arquitetural:

1. **`lpoo.geom`**: Concentra todas as definições geométricas e analíticas.
   * `Shape` (Classe abstrata base para formas tridimensionais).
   * `Primitive` (Classe base para formas analíticas primitivas com densidade).
   * `Box`, `Sphere`, `Cylinder`, `Capsule` (Geometrias primitivas).
   * `Mesh` (Malha de triângulos baseada no formato `.obj`, com integração poliédrica de volume, massa e tensor de inércia — Atividade A5).
   * `CompositeShape` e `CompositeShapeInstance` (Formas compostas e instâncias para reutilização de geometrias e aplicação do Teorema de Steiner).
   * `Pose` (Gerenciamento de posições espaciais via translação e orientações por quaternions).

2. **`lpoo.phyx`**: Gerencia a simulação física macroscópica.
   * `RigidBody` (Associa uma forma geométrica a uma pose global no espaço, calculando propriedades físicas acumuladas).
   * `Scene` (Representa o ambiente de simulação contendo uma lista de atores/corpos rígidos).

3. **`lpoo.util` e Raiz (`lpoo`)**: Utilitários de leitura e relatórios.
   * `SceneReader` (Interpretador de arquivos de texto de cena `.txt`).
   * `SceneReport` (Gerador de relatórios textuais detalhados da cena).
   * `Main` (Ponto de entrada do sistema via linha de comando).

---

## 🚀 Como Compilar e Executar

Certifique-se de ter o **Java JDK** instalado em sua máquina.

### 1. Compilação
Abra o terminal na raiz do projeto onde se encontram as pastas dos pacotes (`lpoo/`) e execute o comando de compilação para todos os arquivos `.java`:

```bash
javac lpoo/*.java

java lpoo.Main SceneTest.txt