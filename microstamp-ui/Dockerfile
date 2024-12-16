FROM node:20-alpine AS build

WORKDIR /app
COPY package.json /app/package.json
COPY package-lock.json /app/package-lock.json
COPY .env /app/.env

RUN npm install
COPY . /app

ARG VITE_UI_URL
ARG VITE_GATEWAY_URL
ARG VITE_AUTH_URL
ARG VITE_TOKEN_AUTH_URL
ARG VITE_AUTH_CLIENT_ID
ARG VITE_AUTH_CLIENT_SECRET

RUN echo "VITE_UI_URL=$VITE_UI_URL" > .env.local && \
    echo "VITE_GATEWAY_URL=$VITE_GATEWAY_URL" >> .env.local && \
    echo "VITE_AUTH_URL=$VITE_AUTH_URL" >> .env.local && \
    echo "VITE_TOKEN_AUTH_URL=$VITE_TOKEN_AUTH_URL" >> .env.local && \
    echo "VITE_AUTH_CLIENT_ID=$VITE_AUTH_CLIENT_ID" >> .env.local && \
    echo "VITE_AUTH_CLIENT_SECRET=$VITE_AUTH_CLIENT_SECRET" >> .env.local


RUN npm run build

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN rm -rf /usr/share/nginx/html/*

WORKDIR /app
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
