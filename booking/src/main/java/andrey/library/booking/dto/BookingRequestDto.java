package andrey.library.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {

    @NotBlank
    String bookTitle;

    @Positive
    Integer desiredQuantity;

    @NotBlank
    String clientAccount;
}
