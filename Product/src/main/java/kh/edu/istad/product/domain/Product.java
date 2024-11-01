package kh.edu.istad.product.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    private Boolean isAvailable;
    private String alias;
    private Long businessId;

    @OneToMany
    private List<Inventory> inventories;
}
