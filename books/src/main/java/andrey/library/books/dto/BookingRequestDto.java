package andrey.library.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {
    String bookTitle;
    Integer desiredQuantity;
    String clientAccount;
}
