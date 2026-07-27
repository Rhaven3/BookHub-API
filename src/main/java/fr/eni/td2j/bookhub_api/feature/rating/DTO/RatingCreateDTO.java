package fr.eni.td2j.bookhub_api.feature.rating.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingCreateDTO {
    @Min(1)
    @Max(5)
    private int note;

    private String commentary;

    @NotNull
    private Long bookId;
}
