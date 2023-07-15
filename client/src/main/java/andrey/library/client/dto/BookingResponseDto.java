package andrey.library.client.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDto {
    Long id;
    String title;
    Integer borrowedQty;
    BookingStatus bookingStatus;
    ClientDto clientDto;
}
