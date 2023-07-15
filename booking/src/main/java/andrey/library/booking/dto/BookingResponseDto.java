package andrey.library.booking.dto;

import andrey.library.booking.model.BookingStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDto {
    Long id;
    String title;
    Integer borrowedQty;
    BookingStatus bookingStatus;
    ClientDto clientDto;
}
