# 1 шаг - build
# maven- образ Linux + Maven
FROM maven as build
# создаем папку app внутри этого linux
WORKDIR /workspace/app

# копируем в эту папку pom.xml
COPY pom.xml .
# копируем в эту папку наш исходный код
COPY src src

# запускаем на сборку maven, можно запускать и из IJ
RUN mvn clean package
# создаем папку, кот. в докере наз. dependency, и в нее копируем все, что находится внутри нашего jar-file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# 2 шаг
# берем linux, в кот. нет maven и прочего мусора, а есть  только JRE (виртуальная машина)
FROM eclipse-temurin:17-jre-alpine
# назвали нашу папку с зависимотями DEPENDENCY
ARG DEPENDENCY=/workspace/app/target/dependency
# из предыдущего шага забираем все зависимости и копируем их в новый linux
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# запускаем приложение вместе со всеми библиотеками
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "-Dispring.profiles.active=prod", "de.ait.os.OnlineShopApplication"]
