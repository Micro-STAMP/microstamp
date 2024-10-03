FROM node:20-alpine AS build

WORKDIR /app
COPY package.json package-lock.json ./
COPY package-lock.json ./

RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
WORKDIR /app
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
