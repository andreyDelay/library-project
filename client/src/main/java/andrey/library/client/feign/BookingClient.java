package andrey.library.client.feign;

import andrey.library.client.config.FeignConfig;
import andrey.library.client.dto.BookingRequestDto;
import andrey.library.client.dto.BookingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "booking", url = "${api.url.booking}", configuration = FeignConfig.class)
public interface BookingClient {

    @PostMapping
    ResponseEntity<BookingResponseDto> createBooking(BookingRequestDto bookingRequestDto);

    @GetMapping("/{bookingId}")
    ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long bookingId);

}
