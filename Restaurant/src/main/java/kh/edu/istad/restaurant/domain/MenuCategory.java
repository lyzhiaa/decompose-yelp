package kh.edu.istad.restaurant.domain;

import jakarta.persistence.*;
import kh.edu.istad.restaurant.config.jpa.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "menu_categories")
public class MenuCategory extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String icon;

    private Boolean isAvailable;

    @OneToMany(mappedBy = "menuCategory")
    private List<MenuItem> menuItems;

}
