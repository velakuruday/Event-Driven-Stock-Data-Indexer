FROM gradle:jdk18-jammy
COPY . /usr/src/eventapi
WORKDIR /usr/src/eventapi
CMD ["gradle", "bootrun"]