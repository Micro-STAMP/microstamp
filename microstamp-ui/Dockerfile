FROM node:20-alpine AS build

WORKDIR /app
COPY package.json package-lock.json ./
COPY package-lock.json ./

RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN rm -rf /usr/share/nginx/html/*

WORKDIR /app
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
