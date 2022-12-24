package customer.website.CustomerWebsite.models;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bikes")
@Builder
@Getter
@Setter
public class RentalBike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String color;
    private String brand;
    private String model;


    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Override
    public String toString() {
        return "RentalBike: " +
                ", color='" + color + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" ;
    }
}
