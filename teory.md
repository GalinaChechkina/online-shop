## 00. Замечания и определения
    * Транзакция - последовательность операций, которая должна выполняться либо полностью, либо не выполняться вообще
    * На уровне БД реализуется с помощью команд BEGIN - начало транзакции, COMMIT - фиксация транзакции, ROLLBACK - откат изменений
    * На уровне Spring-приложения можно использовать аннотацию @Transactional

## 01. Создание поля State
    * State будет показывать текущее состояние пользователя
      * NOT_CONFIRMED - не подтвержден
      * CONFIRMED - подтвержден
      * DELETED - удалил свой аккаунт
      * BANNED - забанен администратором
    * Также нужно обработать state пользователя в AuthenticatedUser
    * Предусмотреть состояние пользователя по-умолчанию при регистрации NOT_CONFIRMED

## 02. Реализовать функционал для создания ссылки подтверждения регистрации
    * Создать модель ConfirmationCode
    * Добавить зависимость в pom.xml - spring-boot-starter-mail
    * Добавить нужные свойства в applicaiton.yml
    * Реализовать класс TemplateProjectMailSender, который является оберткой над JavaMailSender
    * На метод send класса TemplateProjectMailSender нужно повесить @Async, чтобы этот метод выполнялся в другом потоке (треде)
    * В классе с mail-методом поставить аннотацию @EnableAsync

## 03. Используем шаблоны
    * Подключение зависимости freemarker в pom.xml
    * Создание папки mails с html-шаблоном будущего письма c placeholders
    * В AppConfig настроили конфигурацию Freemarker
    * Создали класс MailTemplatesUtil который генерирует html на основе необходимых параметров
    * Получаем html и отправляем его

# 01. Загрузка файлов

  * Если загружать все файлы пользователей, то сильно возрастает нагрузка на сервер
    * Потому что все запросы на получение файлов будут проходить через нас
    * Также, файлы будут занимать очень много места и вам скоро его не хватит
  * Поэтому выбирают сторонние решения, по типу - Dropbox, AWS S3, Digital Ocean Spaces (аналогичен AWS)
  * Это облачные хранилища с возможностями быстрой доставки контента (безграничная память и высокая скорость)
  * Преимущество стороннего решения в очень высокой скорости доставки контента без нагрузки вашего сервера

# 02. Примитивная загрузка файлов

```
    @PostMapping("/api/files")
    public StandardResponseDto upload(@RequestParam("file") MultipartFile file) {
    String originalFileName = file.getOriginalFilename(); // получаем имя файла оригинальное

        String extension;

        if (originalFileName != null) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1); // забираем расширение файла
        } else {
            throw new IllegalArgumentException("null original file name");
        }

        String uuid = UUID.randomUUID().toString(); // генерируем случайную строку в формате UUID
        String newFileName = uuid + "." + extension; // создаем новое имя файла, которое состоит из случайной строки и расширения

        try (InputStream inputStream = file.getInputStream()) { // открываем у загружаемого файла поток для чтения
            Files.copy(inputStream, Path.of("C:\\Users\\marse\\Desktop\\OnlineShop\\static\\" + newFileName)); // перекидываем данные из потока для чтения
            // к нам на диск
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return StandardResponseDto.builder()
                .message(newFileName)
                .build();
    }
```

# 03. Подключение S3/DigitalOcean

```
    <dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-s3</artifactId>
    <version>1.12.572</version>
    </dependency>
```    
    * sdk3-s3 - значительно упрощает работу с сервисом
    
## Дополнительно
    `@SneakyThrows` - аннотация lombok, которая позволяет обработать проверяемое исключения через `try-catch`

## Важно - Подготовка к деплою
### Работа с properties, профилями и переменными окружения
* Цель: сделать отдельные настройки для локального запуска и запуска на сервере 
  * нужно два файла: application.yml, application-local.yml
  * в edit configuration указываем в active profiles - local
* не указывать нигде явные данные для настройки, использовать environment variables в edit configuration
* оставляем логи для локального профиля, убираем логи для продакта

* Добавить Dockerfile, чтобы упаковать все приложение в один образ

## Резюме

1. Сделать отдельный профиль для локального запуска
2. Починить все тесты
3. Редактировать файл pom.xml, чтобы приложение собиралось со всеми зависимостями
    