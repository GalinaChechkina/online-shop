### Настройка проекта

* Обеспечить наличие Environment Variables в Edit Configuration
  *  `username`: `${DB_USERNAME}`, но лучше `spring.datasource.username`
  *  `password`: `${DB_PASSWORD}`, `spring.datasource.password`
  *  `url`: `${DB_URL}`, `spring.datasource.url`, тогда можно настройки для базы убрать из файла `application.yml`
  