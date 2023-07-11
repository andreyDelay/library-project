package andrey.library.client;

import andrey.library.client.dto.AuthorDto;
import andrey.library.client.dto.BookDtoRequest;
import andrey.library.client.dto.BookDtoResponse;
import andrey.library.client.feign.BookClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.*;

@SpringBootTest
class BookClientApplicationTests {

    @Autowired
    BookClient client;

    final long elements = 2;

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    Random random = new Random();

    @Test
    void testBookService() throws InterruptedException {
        long start = System.currentTimeMillis();
        Map<String, BookDtoRequest> stringBookDtoRequestMap = generateBookAndAuthor();
//        clearDatabase();
        createBooks(stringBookDtoRequestMap);
//        getBooks(stringBookDtoRequestMap);
//        deleteBooks(stringBookDtoRequestMap);
        System.out.println(System.currentTimeMillis() - start + " millis to all operations of TEST.");
    }

    void createBooks(Map<String, BookDtoRequest> map) throws InterruptedException {
        long start = System.currentTimeMillis();

        List<CreateBook> tasks = map.values().stream().map(CreateBook::new).toList();
        List<Future<ResponseEntity<BookDtoResponse>>> futureBooks = executorService.invokeAll(tasks);
        futureBooks.parallelStream().forEach(book -> {
            try {
                System.out.println(book.get().getStatusCode());
            } catch (InterruptedException e) {
                System.out.println("createBooks() Err: InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("createBooks() Err: ExecutionException");
                throw new RuntimeException(e);
            }
        });

        System.out.println(System.currentTimeMillis() - start + " millis to all operations");
    }

    void getBooks(Map<String, BookDtoRequest> map) throws InterruptedException {
        long start = System.currentTimeMillis();

        List<GetBook> tasks = map.keySet().stream().map(GetBook::new).toList();
        List<Future<ResponseEntity<BookDtoResponse>>> futureBooks = executorService.invokeAll(tasks);
        futureBooks.parallelStream().forEach(book -> {
            try {
                System.out.println(book.get().getStatusCode());
            } catch (InterruptedException e) {
                System.out.println("getBooks() Err: InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("getBooks() Err: ExecutionException");
                throw new RuntimeException(e);
            }
        });

        System.out.println(System.currentTimeMillis() - start + " millis to all operations");
    }

    void deleteBooks(Map<String, BookDtoRequest> map) throws InterruptedException {
        long start = System.currentTimeMillis();

        List<DeleteBook> tasks = map.keySet().stream().map(DeleteBook::new).toList();
        List<Future<ResponseEntity<Void>>> futureDeletes = executorService.invokeAll(tasks);
        futureDeletes.parallelStream().forEach(response -> {
            try {
                System.out.println(response.get().getStatusCode());
            } catch (InterruptedException e) {
                System.out.println("deleteBooks() Err: InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("deleteBooks() Err: ExecutionException");
                throw new RuntimeException(e);
            }
        });
        System.out.println(System.currentTimeMillis() - start + " millis to all operations");
    }

    private Map<String, BookDtoRequest> generateBookAndAuthor() {
        Map<String, BookDtoRequest> booksAndAuthors = new HashMap<>();
        for (int i = 0; i < elements; i++) {
            BookDtoRequest bookDtoRequest = BookDtoRequest.builder()
                    .title("Book#" + i)
                    .quantityInStock(10)
                    .authors(generateAuthors())
                    .build();
            booksAndAuthors.put(bookDtoRequest.getTitle(), bookDtoRequest);
        }
        return booksAndAuthors;
    }

    private List<AuthorDto> generateAuthors() {
        List<AuthorDto> authorDtoList = new ArrayList<>();
        for (int i = 0; i < random.nextInt(1, 5); i++) {
            authorDtoList.add(
                    AuthorDto.builder()
                            .firstName("First name: #" + random.nextInt(1, 300))
                            .lastName("Last name: #" + random.nextInt(1, 600))
                            .build());
        }
        return authorDtoList;
    }

    private void clearDatabase() {
        client.clearDatabase();
    }

    public class CreateBook implements Callable<ResponseEntity<BookDtoResponse>> {

        private BookDtoRequest bookDtoRequest;

        public CreateBook(BookDtoRequest bookDtoRequest) {
            this.bookDtoRequest = bookDtoRequest;
        }

        @Override
        public ResponseEntity<BookDtoResponse> call() throws Exception {
            return client.createBook(bookDtoRequest);
        }
    }

    public class GetBook implements Callable<ResponseEntity<BookDtoResponse>> {

        private String title;

        public GetBook(String title) {
            this.title = title;
        }

        @Override
        public ResponseEntity<BookDtoResponse> call() throws Exception {
            return client.getBook(title);
        }
    }

    public class DeleteBook implements Callable<ResponseEntity<Void>> {

        private String title;

        public DeleteBook(String title) {
            this.title = title;
        }

        @Override
        public ResponseEntity<Void> call() throws Exception {
            return client.removeBook(title);
        }
    }
}
