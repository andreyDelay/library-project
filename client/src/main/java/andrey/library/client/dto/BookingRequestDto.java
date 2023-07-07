package andrey.library.client.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {
    String bookTitle;
    Integer desiredQuantity;
    String clientAccount;
}
