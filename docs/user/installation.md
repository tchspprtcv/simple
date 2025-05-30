# Guia de Instalação do Sistema Simple

Este guia fornece instruções detalhadas para instalar e configurar o sistema Simple em ambiente Windows. O sistema é composto por três componentes principais: banco de dados PostgreSQL, backend Spring Boot e frontend Next.js, todos empacotados em containers Docker para facilitar a implantação.

## Pré-requisitos

Antes de iniciar a instalação, certifique-se de que seu sistema atende aos seguintes requisitos:

### Requisitos de Hardware
- **Processador**: Intel Core i5 (ou equivalente) ou superior
- **Memória RAM**: Mínimo de 8GB (recomendado 16GB)
- **Espaço em Disco**: Mínimo de 10GB livres
- **Conexão de Internet**: Para download dos componentes

### Requisitos de Software
- **Sistema Operacional**: Windows 10 64-bit (Pro, Enterprise ou Education, Build 16299 ou posterior)
- **Docker Desktop para Windows**: Versão mais recente
  - Com recursos Hyper-V e Containers do Windows habilitados
- **Git** (opcional): Para clonar o repositório

## Etapa 1: Instalação do Docker Desktop

1. Baixe o Docker Desktop para Windows no [site oficial](https://www.docker.com/products/docker-desktop)
2. Execute o instalador e siga as instruções na tela
3. Durante a instalação, certifique-se de que a opção "Use WSL 2 instead of Hyper-V" está selecionada (recomendado)
4. Após a instalação, reinicie o computador
5. Inicie o Docker Desktop a partir do menu Iniciar
6. Aguarde até que o Docker Desktop esteja em execução (ícone na barra de tarefas ficará estável)

### Verificação da Instalação do Docker

Para verificar se o Docker foi instalado corretamente, abra o Prompt de Comando ou PowerShell e execute:

```
docker --version
docker-compose --version
```

Você deve ver a versão do Docker e do Docker Compose instalados.

## Etapa 2: Obtenção dos Arquivos do Sistema

### Opção 1: Download do Pacote

1. Baixe o arquivo ZIP do sistema Simple
2. Extraia o conteúdo para um diretório de sua preferência (ex: `C:\Simple`)

### Opção 2: Clonagem do Repositório Git

Se você optou por instalar o Git, pode clonar o repositório:

1. Abra o Prompt de Comando ou PowerShell
2. Navegue até o diretório onde deseja instalar o sistema
3. Execute o comando:
   ```
   git clone <URL_DO_REPOSITÓRIO> Simple
   cd Simple
   ```

## Etapa 3: Configuração do Sistema

Antes de iniciar os serviços, você pode personalizar algumas configurações:

### Configuração de Portas

Por padrão, o sistema utiliza as seguintes portas:
- **PostgreSQL**: 5432
- **Backend**: 8080
- **Frontend**: 3000

Se precisar alterar estas portas (por exemplo, se já estiverem em uso), edite o arquivo `docker-compose.yml`:

1. Abra o arquivo `docker-compose.yml` em um editor de texto
2. Localize as seções `ports` de cada serviço
3. Altere o primeiro número de cada par (que representa a porta no host)
   ```yaml
   ports:
     - "NOVA_PORTA_HOST:PORTA_CONTAINER"
   ```

### Configuração de Credenciais

Para alterar as credenciais padrão do banco de dados, edite as seguintes variáveis no arquivo `docker-compose.yml`:

```yaml
environment:
  POSTGRES_DB: simple
  POSTGRES_USER: simple_user
  POSTGRES_PASSWORD: simple_password
```

> **Importante**: Se alterar as credenciais do banco de dados, certifique-se de atualizar também as variáveis correspondentes no serviço do backend:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/simple
  SPRING_DATASOURCE_USERNAME: simple_user
  SPRING_DATASOURCE_PASSWORD: simple_password
```

## Etapa 4: Instalação e Execução

1. Abra o Prompt de Comando ou PowerShell como administrador
2. Navegue até o diretório onde os arquivos foram extraídos ou clonados:
   ```
   cd C:\Simple
   ```
3. Execute o comando para construir e iniciar todos os containers:
   ```
   docker-compose up -d
   ```
   
   Este comando:
   - Constrói as imagens Docker para o backend e frontend
   - Baixa a imagem do PostgreSQL
   - Cria e inicia todos os containers
   - Configura a rede para comunicação entre os containers

4. Aguarde até que todos os containers sejam iniciados. Isso pode levar alguns minutos na primeira execução.

5. Verifique se todos os containers estão em execução:
   ```
   docker-compose ps
   ```
   
   Você deve ver três containers em execução: `simple-postgres`, `simple-backend` e `simple-frontend`.

## Etapa 5: Acesso ao Sistema

Após a inicialização bem-sucedida de todos os containers, você pode acessar o sistema:

1. Abra um navegador web (Chrome, Firefox, Edge, etc.)
2. Acesse o endereço: `http://localhost:3000`
3. Você será redirecionado para a página de login do sistema

### Credenciais Iniciais

O sistema é pré-configurado com um utilizador administrador:

- **Utilizador**: admin@simple.municipio.gov
- **Senha**: admin123

> **Importante**: Por segurança, altere a senha do administrador após o primeiro login.

## Gerenciamento do Sistema

### Parar o Sistema

Para parar todos os containers sem removê-los:

```
docker-compose stop
```

### Iniciar o Sistema Novamente

Para iniciar containers que foram parados:

```
docker-compose start
```

### Reiniciar o Sistema

Para reiniciar todos os containers:

```
docker-compose restart
```

### Parar e Remover Containers

Para parar e remover todos os containers (os dados do banco serão preservados):

```
docker-compose down
```

### Remover Tudo (Incluindo Dados)

Para remover todos os containers e volumes (isso apagará todos os dados):

```
docker-compose down -v
```

## Backup e Restauração

### Backup do Banco de Dados

Para criar um backup do banco de dados:

```
docker exec simple-postgres pg_dump -U simple_user simple > backup.sql
```

### Restauração do Banco de Dados

Para restaurar um backup:

```
docker exec -i simple-postgres psql -U simple_user simple < backup.sql
```

## Atualização do Sistema

Para atualizar o sistema para uma nova versão:

1. Faça backup do banco de dados
2. Obtenha os novos arquivos (download ou git pull)
3. Pare e remova os containers atuais:
   ```
   docker-compose down
   ```
4. Reconstrua e inicie os containers com os novos arquivos:
   ```
   docker-compose up -d --build
   ```

## Solução de Problemas

### Verificando Logs

Para visualizar os logs de um container específico:

```
docker-compose logs [serviço]
```

Exemplo:
```
docker-compose logs backend
```

Para acompanhar os logs em tempo real:

```
docker-compose logs -f [serviço]
```

### Problemas Comuns

#### O Sistema Não Inicia

- Verifique se o Docker Desktop está em execução
- Verifique se as portas necessárias não estão sendo usadas por outros aplicativos
- Verifique os logs para identificar erros específicos

#### Erro de Conexão com o Banco de Dados

- Verifique se o container do PostgreSQL está em execução
- Verifique as credenciais no arquivo `docker-compose.yml`
- Aguarde alguns segundos após iniciar os containers para que o PostgreSQL esteja pronto

#### Página Não Carrega

- Verifique se o container do frontend está em execução
- Verifique se a porta 3000 está acessível
- Tente limpar o cache do navegador

#### API Não Responde

- Verifique se o container do backend está em execução
- Verifique os logs do backend para identificar erros
- Verifique se a porta 8080 está acessível

## Requisitos de Rede

Se o sistema for acessado por outros computadores na rede:

1. Certifique-se de que o firewall do Windows permite conexões nas portas utilizadas
2. Use o endereço IP do servidor em vez de localhost
3. Considere configurar um nome de host ou DNS para facilitar o acesso

## Próximos Passos

Após a instalação bem-sucedida, consulte os seguintes guias:

- [Guia do Atendente](./attendant_guide.md): Para funcionários de atendimento
- [Guia do Utente](./citizen_guide.md): Para cidadãos que utilizam o sistema
- [Processos](./processes.md): Descrição detalhada dos fluxos de processos

---

Se você encontrar dificuldades durante a instalação, entre em contato com a equipe de suporte.
