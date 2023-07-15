package andrey.library.booking.mapper;

import andrey.library.booking.dto.BookingRequestDto;
import andrey.library.booking.dto.BookingResponseDto;
import andrey.library.booking.kafka.BookingEvent;
import andrey.library.booking.model.Booking;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(uses = { ClientMapper.class }, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(target = "title", source = "bookTitle")
    @Mapping(target = "borrowedQty", source = "desiredQuantity")
    @Mapping(target = "client", source = "clientAccount")
    Booking toBooking(BookingRequestDto bookingRequestDto);

    @InheritInverseConfiguration
    @Mapping(target = "clientDto", source = "client")
    BookingResponseDto toBookingResponse(Booking booking);

    @Mapping(target = "desiredQuantity", source = "borrowedQty")
    BookingEvent toBookingRequestMessage(Booking booking);
}
