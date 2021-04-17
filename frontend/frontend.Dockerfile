FROM node:14
# Create directory for the 
WORKDIR /usr/src/frontend
# copy package.json and package-lock.json
COPY package*.json ./

RUN npm ci --only=production
# copy all source doe
COPY . .
# Install serve for the production build
RUN npm install -g serve
# create production build
RUN npm run build
EXPOSE 5000
CMD [ "serve", "-s",  "build"]