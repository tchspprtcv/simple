FROM node:18-alpine

WORKDIR /app

# Copiar os arquivos de configuração
COPY package-lock.json ./
COPY app/package.json ./

# Instalar dependências
RUN npm install

# Copiar o código-fonte
COPY app/ .

# Gerar o cliente Prisma
RUN npx prisma generate

# Construir a aplicação Next.js
RUN npm run build

# Expor a porta que o Next.js usa
EXPOSE 3000

# Comando para iniciar a aplicação em modo de produção
CMD ["npm", "start"]
