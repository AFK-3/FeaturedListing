package id.ac.ui.cs.advprog.afk3.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Listing {
    private String id;
    private String sellerUsername;
    private String name;
    private String description;
    private int quantity;
    private boolean isFeatured = false;
}