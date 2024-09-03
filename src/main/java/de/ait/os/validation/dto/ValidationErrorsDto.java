package de.ait.os.validation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 6/29/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ValidationErrors", description = "information about validation errors")
public class ValidationErrorsDto {

    @Schema(description = "list of validation errors")
    private List<ValidationErrorDto> errors;
}
