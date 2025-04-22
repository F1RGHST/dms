# web/Dockerfile
FROM node:20

WORKDIR /app

COPY package*.json ./
RUN npm install --silent \
 && npm install --silent react-router-dom lucide-react

COPY . .

EXPOSE 3000
CMD ["npm", "run", "start"]