package id.ac.ui.cs.advprog.afk3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter @Setter
public class FeaturedListing {
    private String id;
    private String sellerUsername;
    private String name;
    private String description;
    private int quantity;
    private Calendar featuredExpiryTime;

    public void setFeatured(boolean b) {
    }
}
