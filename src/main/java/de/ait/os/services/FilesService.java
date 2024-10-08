package de.ait.os.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.ait.os.dto.StandardResponseDto;
import de.ait.os.models.FileInfo;
import de.ait.os.repositories.FilesInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import de.ait.os.models.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * 8/28/2024
 * OnlineShop
 *
 * @author Chechkina (AIT TR)
 */
@Service
@RequiredArgsConstructor
public class FilesService {

    private final AmazonS3 amazonS3;

    private final FilesInfoRepository filesInfoRepository;

    @Transactional
    @SneakyThrows
    public StandardResponseDto upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename(); // получаем имя файла оригинальное

        String extension;

        if (originalFileName != null) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1); // забираем расширение файла
        } else {
            throw new IllegalArgumentException("null original file name");
        }

        String uuid = UUID.randomUUID().toString(); // генерируем случайную строку в формате UUID
        String newFileName = uuid + "." + extension; // создаем новое имя файла, которое состоит из случайной строки и расширения

        InputStream inputStream = file.getInputStream(); // получили поток для чтения

        // загружаем в DigitalOcean

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType()); // будем сообщать хранилищу какого типа у нас файл
        metadata.setContentLength(file.getSize()); // надо сообщить амазону размер файла

        // создали запрос на отправку файла
        PutObjectRequest request =
                new PutObjectRequest("online-shop-files", "images/" + newFileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead); // разрешаем доступ к файлу

        amazonS3.putObject(request); // отправили файл на DigitalOcean

        String link = amazonS3.getUrl("online-shop-files", "images/" + newFileName).toString(); // получили ссылку на файл

        FileInfo fileInfo = FileInfo.builder()
                .link(link)
                .build();

        filesInfoRepository.save(fileInfo); // сохранили файл к себе в базу

        return StandardResponseDto.builder()
                .message(link)
                .build();
    }
}
