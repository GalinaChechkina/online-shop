package de.ait.os.controllers;

import de.ait.os.controllers.api.FilesApi;
import de.ait.os.dto.StandardResponseDto;
import de.ait.os.services.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * 8/28/2024
 * OnlineShop
 *
 * @author Chechkina (AIT TR)
 */
@RequiredArgsConstructor
@RestController
public class FilesController implements FilesApi {

    private final FilesService filesService;

    public StandardResponseDto upload(MultipartFile file) {
        return filesService.upload(file);
    }
}