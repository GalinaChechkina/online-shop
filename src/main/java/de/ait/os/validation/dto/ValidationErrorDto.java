package de.ait.os.validation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Schema(name = "ValidationError", description = "Description of the validation error")
public class ValidationErrorDto {

    @Schema(description = "the name of the field where the error occurred", example = "price")
    private String field;
    @Schema(description = "the value that the user entered and which was rejected by the server", example = "1000.0")
    private String rejectedValue;
    @Schema(description = "the message that we need to show to the user", example = "must be less than or equal to 200")
    private String message;
}