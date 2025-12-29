package rs.spai.demoQL;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idC;

    private String categoryName;

    // Parent category (relation auto-référentielle)
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    
    // Sub categories (sous-catégories)
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories;

    // Books in this category
    @OneToMany(mappedBy = "category")
    private List<Book> books;
}