package andrey.library.booking.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "clients")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_sequence")
    @SequenceGenerator(name = "clients_sequence", sequenceName = "clients_sequence", allocationSize = 1)
    Long id;

    @Column(name = "account")
    String account;
}
