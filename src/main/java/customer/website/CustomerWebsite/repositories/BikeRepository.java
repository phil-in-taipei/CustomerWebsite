package customer.website.CustomerWebsite.repositories;

import customer.website.CustomerWebsite.models.RentalBike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<RentalBike, Long> {
    RentalBike findByCustomerId(Long id);
}
