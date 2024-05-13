package id.ac.ui.cs.advprog.afk3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "featured_listings")
public class FeaturedListing{
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "seller_username")
    private String sellerUsername;

    @Column(name = "name")
    private String name;

    @Column(name = "expiry_time")
    private LocalDate featuredExpiryTime = LocalDate.now().plusDays(7);
}
