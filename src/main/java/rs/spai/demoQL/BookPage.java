package rs.spai.demoQL;

import lombok.*;

import java.util.List;

@Data @AllArgsConstructor
public class BookPage {
    private List<Book> list;
    private PageInfo pageInfo;
}