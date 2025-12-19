# Beacon Navigator API

## 📋 Sobre o Projeto

O **Beacon Navigator** é uma API RESTful desenvolvida em Spring Boot para gerenciamento e navegação baseada em beacons. O sistema oferece funcionalidades completas de autenticação, autorização e operações CRUD, utilizando as melhores práticas de desenvolvimento e segurança.

## Sumário

- [Tecnologias Utilizadas](#Tecnologias-Utilizadas)
- [Estrutura do Projeto](#-Estrutura-do-Projeto)
- [Pré-requesitos](#pré-requesitos)
- [Documentação da API](#Documentação-da-API)
- [Desenvolvimento](#Desenvolvimento)
- [Docker](#Docker)
- [Padrões e boas práticas](#padrões-e-boas-práticas)
- [Contribuição](#contribuição)
- [Licença](#licença)

## 🚀 Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 4.0.0** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT (JJWT 0.12.5)** - Tokens de autenticação
- **MySQL** - Banco de dados relacional
- **Swagger/OpenAPI 2.5.0** - Documentação da API
- **Lombok** - Redução de código boilerplate
- **Bean Validation** - Validação de dados
- **Maven** - Gerenciamento de dependências

## 📁 Estrutura do Projeto

```
beacon-navigator/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/beaconnavigator/
│   │   │       ├── config/          # Configurações (Security, Swagger)
│   │   │       ├── controller/      # Controllers REST
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── entity/          # Entidades JPA
│   │   │       ├── repository/      # Repositórios
│   │   │       ├── service/         # Serviços de negócio
│   │   │       ├── security/        # Filtros e utilitários JWT
│   │   │       └── exception/       # Tratamento de exceções
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## ⚙️ Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- Java JDK 21 ou superior
- Maven 3.9.x ou superior
- MySQL 8.0 ou superior
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## 📚 Documentação da API

Após iniciar a aplicação, acesse a documentação interativa do Swagger:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html

## 🔐 Autenticação e Segurança

O projeto utiliza **JWT (JSON Web Tokens)** para autenticação stateless.

### Fluxo de Autenticação

1. **Registro/Login:** O cliente envia credenciais para `/api/auth/login`
2. **Token JWT:** A API retorna um token JWT válido
3. **Requisições Autenticadas:** Incluir o token no header:
   ```
   Authorization: Bearer {seu_token_jwt}
   ```

### Endpoints Públicos vs Protegidos

- ✅ **Públicos:** `/api/auth/**`, `/swagger-ui/**`, `/api-docs/**`
- 🔒 **Protegidos:** Todos os demais endpoints requerem autenticação

_Consulte a documentação Swagger para a lista completa de endpoints._

## 🛠️ Desenvolvimento

### Hot Reload

O projeto inclui Spring Boot DevTools para reload automático durante o desenvolvimento. Basta salvar as alterações e a aplicação será recarregada automaticamente.

### Lombok

O projeto usa Lombok para reduzir código boilerplate. Certifique-se de habilitar o processamento de anotações na sua IDE:

- **IntelliJ IDEA:** Settings → Build → Compiler → Annotation Processors → Enable
- **Eclipse:** Instalar o plugin Lombok via marketplace

## 🐳 Docker

```dockerfile
# Dockerfile exemplo
docker run --name beacon-navigator-api \
  --env-file .env \
  -p 8080:8080 \
  -d beacon-navigator-api:latest

```

```bash
# Build e execução
docker build -t beacon-navigator .
docker run -p 8080:8080 beacon-navigator
```

Endpoints disponíveis: `/actuator/health`, `/actuator/metrics`, etc.

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Convenções de Código

- Seguir padrões Java (CamelCase, nomenclatura clara)
- Usar anotações Lombok quando apropriado
- Documentar código complexo
- Escrever testes unitários para novas features
- Manter commits pequenos e descritivos

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

## 👥 Autores

- **Sua Equipe** - [Jorge Figueredo](https://github.com/Jorgefigueredoo) |
  [Vitor Santos](https://github.com/VitorrSantoss) |
  [Jairo Marinho](https://github.com/JairoMarinho)

## 📞 Contato

Para dúvidas ou sugestões:

- Email: marinho.tecnologias@gmail.com
- LinkedIn: [Vitor Santos](https://www.linkedin.com/in/vitorsantosll/) |
  [Jorge Figueredo](https://www.linkedin.com/in/jorge-antonio-282874303/)

---

**Desenvolvido pela equipe Beacon Navigator ❤️**
