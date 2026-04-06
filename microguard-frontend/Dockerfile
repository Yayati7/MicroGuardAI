FROM node:20-alpine AS builder

WORKDIR /app

COPY package*.json ./

# install dependencies properly
RUN npm install

COPY . .

# 🔥 IMPORTANT FIX
RUN chmod +x node_modules/.bin/ng

# 🔥 USE NODE DIRECTLY (bypass sh issue)
RUN node node_modules/@angular/cli/bin/ng build