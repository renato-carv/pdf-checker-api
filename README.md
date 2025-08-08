# PDF Checker

Uma aplicação Java Spring Boot para comparação de arquivos PDF, identificando diferenças visuais e de conteúdo entre documentos.

## 📋 Descrição

A aplicação PDF Checker permite comparar dois arquivos PDF e identificar diferenças entre eles de forma automatizada. Quando diferenças são encontradas, a aplicação gera um PDF visual destacando as alterações e o retorna codificado em Base64 para o frontend.

## 🚀 Funcionalidades

- **Comparação de Conteúdo**: Verifica se o conteúdo textual dos PDFs é idêntico
- **Comparação Visual**: Gera um PDF destacando diferenças visuais entre documentos
- **Validação de Arquivos**: Verifica se os arquivos enviados são PDFs válidos
- **Hash de Segurança**: Utiliza SHA-256 para comparação de integridade
- **API REST**: Interface simples para integração com frontend

## 🛠️ Tecnologias Utilizadas

- **Java**: Linguagem principal
- **Spring Boot**: Framework web
- **iText PDF**: Manipulação e extração de texto de PDFs
- **pdf-compare**: Biblioteca para comparação visual de PDFs
- **Maven**: Gerenciamento de dependências

## 📦 Dependências

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- iText PDF -->
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itextpdf</artifactId>
        <version>5.5.13.3</version>
    </dependency>
    
    <!-- PDF Compare -->
    <dependency>
        <groupId>de.redsix</groupId>
        <artifactId>pdfcompare</artifactId>
        <version>1.1.61</version>
    </dependency>
</dependencies>
```

## 🔧 Configuração

1. **Clone o repositório**:
```bash
git clone https://github.com/seu-usuario/pdf-checker.git
cd pdf-checker
```

2. **Compile o projeto**:
```bash
mvn clean install
```

3. **Execute a aplicação**:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## 📡 API Endpoints

### POST /api/pdf/compare

Compara dois arquivos PDF e retorna o resultado da comparação.

**Request Body**:
```json
{
    "originalBase64": "JVBERi0xLjQKJeLjz9M...",
    "modifiedBase64": "JVBERi0xLjQKJeLjz9M..."
}
```

**Response - PDFs Iguais**:
```json
{
    "isEqual": true,
    "message": "O conteúdo dos PDFs são iguais."
}
```

**Response - PDFs Diferentes**:
```json
{
    "isEqual": false,
    "message": "O conteúdo dos PDFs são diferentes.",
    "diffPdfBase64": "JVBERi0xLjQKJeLjz9M..."
}
```

**Response - Erro de Validação**:
```json
{
    "isEqual": false,
    "message": "Arquivo não é um PDF válido."
}
```

## 🏗️ Arquitetura

### Estrutura do Projeto
```
src/
├── main/java/com/renatocarvalho/pdf_checker/
│   ├── controllers/
│   │   └── PdfController.java
│   ├── services/
│   │   └── PdfService.java
│   └── dtos/
│       ├── RequestPdfDTO.java
│       └── ResponsePdfDTO.java
```

### Componentes Principais

#### PdfController
- Endpoint REST para comparação de PDFs
- Validação de entrada (verificação se é PDF válido)
- Conversão Base64 para bytes
- Tratamento de respostas

#### PdfService
- **`getTextHash()`**: Extrai texto do PDF e gera hash SHA-256
- **`getFullContentHash()`**: Gera hash do conteúdo completo do PDF
- **`generateVisualDiffPdf()`**: Cria PDF visual com diferenças destacadas
- **`isContentEqual()`**: Compara PDFs por hash de conteúdo e texto

## 🔍 Como Funciona

1. **Recepção**: A aplicação recebe dois PDFs codificados em Base64
2. **Validação**: Verifica se os arquivos são PDFs válidos (magic bytes)
3. **Comparação**:
    - Compara hash do conteúdo completo
    - Compara hash do texto extraído
4. **Geração de Diferenças**: Se diferentes, gera PDF visual com destacamento
5. **Resposta**: Retorna resultado e PDF de diferenças (se aplicável)

## 🎯 Casos de Uso

- **Controle de Versão**: Identificar alterações entre versões de documentos
- **Auditoria**: Verificar integridade de documentos importantes
- **Revisão de Contratos**: Destacar mudanças em documentos jurídicos
- **Validação de Assinaturas**: Verificar se documento foi alterado após assinatura

## ⚠️ Limitações

- Arquivos temporários são criados durante o processamento
- Comparação visual pode ser intensiva para PDFs grandes
- Dependência de bibliotecas externas para processamento

## 🐛 Tratamento de Erros

A aplicação trata os seguintes cenários de erro:
- Arquivos inválidos (não são PDFs)
- Falha na extração de texto
- Problemas de I/O durante processamento
- Erro na geração do PDF de diferenças

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request
