package andrey.library.booking.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_sequence")
    @SequenceGenerator(name = "bookings_sequence", sequenceName = "bookings_sequence", allocationSize = 1)
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "borrowed_qty")
    Integer borrowedQty;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    Client client;

}
