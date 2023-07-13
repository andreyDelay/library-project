package andrey.library.client;

import andrey.library.client.dto.*;
import andrey.library.client.feign.BookingClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@SpringBootTest
public class BookingClientApplicationTests {
    @Autowired
    BookingClient client;

    final long elements = 10;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    Random random = new Random();

    @Test
    void testBookService() throws InterruptedException {
        long start = System.currentTimeMillis();
        List<BookingRequestDto> bookingRequests = generateBookings();
        List<Long> bookings = createBookings(bookingRequests);
//        getBookings(bookings);
        System.out.println(System.currentTimeMillis() - start + " millis to all operations of TEST.");
    }

    List<Long> createBookings(List<BookingRequestDto> bookingRequests) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<Long> resultList = new CopyOnWriteArrayList<>();
        List<CreateBooking> tasks = bookingRequests.stream().map(CreateBooking::new).toList();
        List<Future<ResponseEntity<BookingResponseDto>>> futureBookings = executorService.invokeAll(tasks);
        futureBookings.parallelStream().forEach(booking -> {
            try {
                System.out.println(booking.get().getStatusCode());
//                Long id = Objects.requireNonNull(booking.get().getBody()).getId();
//                resultList.add(id);
            } catch (InterruptedException e) {
                System.out.println("createBooks() Err: InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("createBooks() Err: ExecutionException");
                throw new RuntimeException(e);
            }
        });
        System.out.println(System.currentTimeMillis() - start + " millis to all operations");
        return resultList;
    }

    void getBookings(List<Long> bookings) throws InterruptedException {
        long start = System.currentTimeMillis();

        List<GetBooking> tasks = bookings.stream().map(GetBooking::new).collect(Collectors.toList());
        List<Future<ResponseEntity<BookingResponseDto>>> futureBooks = executorService.invokeAll(tasks);
        futureBooks.parallelStream().forEach(existingBooking -> {
            try {
                System.out.println(existingBooking.get().getStatusCode());
            } catch (InterruptedException e) {
                System.out.println("getBooks() Err: InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("getBooks() Err: ExecutionException");
                throw new RuntimeException(e);
            }
        });

        System.out.println(System.currentTimeMillis() - start + " millis to all operations");
    }

    private List<BookingRequestDto> generateBookings() {
        List<BookingRequestDto> bookingRequests = new ArrayList<>();
        for (int i = 0; i < elements; i++) {
            BookingRequestDto bookingRequest = BookingRequestDto.builder()
                    .bookTitle("Book#" + i)
                    .desiredQuantity(1)
                    .clientAccount("Account:0" + i)
                    .build();
            bookingRequests.add(bookingRequest);
        }
        return bookingRequests;
    }

    public class CreateBooking implements Callable<ResponseEntity<BookingResponseDto>> {

        private BookingRequestDto bookingRequestDto;

        public CreateBooking(BookingRequestDto bookingRequestDto) {
            this.bookingRequestDto = bookingRequestDto;
        }

        @Override
        public ResponseEntity<BookingResponseDto> call() throws Exception {
            return client.createBooking(bookingRequestDto);
        }
    }

    public class GetBooking implements Callable<ResponseEntity<BookingResponseDto>> {

        private Long id;

        public GetBooking(Long id) {
            this.id = id;
        }

        @Override
        public ResponseEntity<BookingResponseDto> call() throws Exception {
            return client.getBooking(id);
        }
    }

}
