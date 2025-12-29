package rs.spai.demoQL;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "author")
@Data //gere autot les geters et setters
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_author")
    private int idAuthor;

    private String name;
    private int age;
    private String nationality;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;
}