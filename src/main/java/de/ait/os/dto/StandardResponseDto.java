package de.ait.os.dto;

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
@Schema(name = "Message", description = "Any error message")
public class StandardResponseDto {

    @Schema(description = "Possible: error message, status change, etc.", example = "the course was not found")
    private String message;

}
