package andrey.library.books.kafka;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingStatusEvent {
    Long id;
    Integer borrowedQty;
    BookingStatus bookingStatus;
}
