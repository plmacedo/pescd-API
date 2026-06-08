##  Como Compilar e Executar o Projeto


### Opção 1: Execução via Linha de Comando (Terminal)

Abra o terminal de comandos diretamente na pasta raiz do projeto e execute os passos seguintes:

#### 1. Limpar e Compilar o Projeto (*Build*)
Para descarregar todas as dependências necessárias, executar as validações e compilar o projeto gerando o artefato empacotado (`.jar`/`.war`), utilize:
* **Linux / macOS:**
    ```bash
    ./mvnw clean package
    ```
* **Windows (Prompt de Comando ou PowerShell):**
    ```cmd
    mvnw.cmd clean package
    ```

#### 2. Executar a Suíte de Testes Automatizados
Para rodar todos os testes unitários e de integração (incluindo o `PescdApplicationTests`) integrados ao ciclo de vida do Spring Boot:
* **Linux / macOS:**
    ```bash
    ./mvnw test
    ```
* **Windows:**
    ```cmd
    mvnw.cmd test
    ```

#### 3. Iniciar a Aplicação (`PescdApplication`)
Para levantar o servidor embutido Apache Tomcat e expor o sistema, execute:
* **Linux / macOS:**
    ```bash
    ./mvnw spring-boot:run
    ```
* **Windows:**
    ```cmd
    mvnw.cmd spring-boot:run
    ```

Após o término da inicialização com sucesso no console, o sistema estará pronto para acesso no navegador através do endereço:  
**[http://localhost:8080](http://localhost:8080)**

---

### Opção 2: Execução via IntelliJ IDEA (ou outra IDE Maven)

1.  **Importar o Projeto:** Inicie a IDE, selecione a opção **Open** (Abrir) e aponte para a pasta raiz do projeto. Aguarde a indexação e sincronização automática do modelo de objetos do Maven (`pom.xml`).
2.  **Configurar o SDK do Projeto:** Vá para `File > Project Structure` e certifique-se de que o SDK associado ao projeto é o **JDK 17** ou superior.
3.  **Executar a Aplicação:**
    * Navegue na estrutura de pastas até: `src/main/java/br/ufscar/pescd/PescdApplication.java`.
    * Clique com o botão direito em cima do arquivo (ou dentro do método `main`) e selecione **Run 'PescdApplication'**.
4.  **Executar os Testes na IDE:**
    * Navegue até: `src/test/java/br/ufscar/pescd/PescdApplicationTests.java`.
    * Clique com o botão direito no arquivo e escolha **Run 'PescdApplicationTests'** para validar visualmente a execução da suíte de testes.

---

##  Contribuições feitas por cada integrante

### Pedro Leal:
* **AL.02:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/b9a56dc20f3a82207d1881b0d359e7ebf93ecf82)**
* **AL.04:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/04d51242f74ce6bf1267cb7694cc7bac029e11ea)**
* **PR.04:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/1c355ed266e9119d8aea778ebc0eeb59bac7ab36)**
* **S.03:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/1c355ed266e9119d8aea778ebc0eeb59bac7ab36)**
* **AD.01:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/ed4f696441da3fe991e46e3faa5a7edea33a818b)**
* **PR.02** 

### Ricardo Yukio:
* **AL.03:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/8dd9c331e04707a096b21ce2473c4e2120177913)**
* **PS.01:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/88a43c5980df4c516ea6bc2704a70f15a272e257)**
* **PS.02:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/88a43c5980df4c516ea6bc2704a70f15a272e257)**
* **PS.03:** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/8f4e15f38a1007df94a1f4c42ff98662cf6e1eeb)**

### Mauricio Alonzo:
* **V.01** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/2d82f8428c4baeab3f675f955696e1126b138f30)**
* **U.01** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/290f79f36adef5b69787ddee973600d1f4ab8db7)**
* **S.04** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/f7add2a4e80d470898369ba82234331e5a0a9de3)**
* **U.SURPRESA** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/9b58160102c1011725b0a0f5880f40be00dcd8bc)**
* **S.03** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/885b10225a754a04f2d51edea0558f61e70c3576)**

### Vinicius Ferreira:
* **S.01** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/5a365a8e6fede3c8cef0c70ddbf00dba60515dc0)**
* **S.02** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/3547690b605811daa29814537c51cf4518925ec5)**
* **AL.01** **[commit](https://github.com/MauricioAlonzoBCC/projeto-pescd/commit/715880e1dee2c9b5544576f7dc62c80d76ab40b9)**
