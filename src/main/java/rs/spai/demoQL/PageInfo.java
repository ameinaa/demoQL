package rs.spai.demoQL;

import lombok.*;

@Data @AllArgsConstructor
public class PageInfo {
    private int totalItems;
    private int itemsLeft;
    private boolean hasMore;
    private int pageNumber;
    private int pageSize;
}