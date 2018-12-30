FROM nginx:1.15-alpine

# set the working directory to /app
WORKDIR /app

# copy the current directory contents into the container at /app
COPY . /app

ENV NGINX_DIR /usr/share/nginx/html

# copy the built files to nginx folder
RUN rm -r ${NGINX_DIR}/*
RUN ln -s /app/index.html ${NGINX_DIR}
RUN ln -s /app/build ${NGINX_DIR}/build

# confirm the result
RUN ls -alhr ${NGINX_DIR}

RUN echo "Please visit the site at http://localhost:3000"
