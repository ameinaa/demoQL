package rs.spai.demoQL;

import java.util.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

    private final AuthorRepository authorRepo;
    private final BookRepository bookRepo;
    private final CategoryRepository categoryRepo;

    public MyController(
        AuthorRepository ar,
        BookRepository br,
        CategoryRepository cr
    ) {
        this.authorRepo = ar;
        this.bookRepo = br;
        this.categoryRepo = cr;
    }

    

    private PageInfo makePageInfo(int page, int size, int total) {
        int from = page * size;
        int to = Math.min(from + size, total);
        int itemsLeft = Math.max(total - to, 0);
        boolean hasMore = itemsLeft > 0;
        return new PageInfo(total, itemsLeft, hasMore, page, size);
    }

    private boolean isInCategory(Book b, int catId, boolean recursive) {
        if (b.getCategory() == null) return false;

        Category c = b.getCategory();
        if (c.getIdC() == catId) return true;

        if (!recursive) return false;

        Category parent = c.getParentCategory();
        while (parent != null) {
            if (parent.getIdC() == catId) return true;
            parent = parent.getParentCategory();
        }
        return false;
    }

    // ---------- PART 1 : QUERIES PUBLIQUES ----------

    @QueryMapping
    public BookPage books(
        @Argument Integer page,
        @Argument Integer size,
        @Argument Integer publicationYear,
        @Argument String language,
        @Argument Integer idCategory,
        @Argument Boolean recursive
    ) {
        int p = page != null ? page : 0;
        int s = (size != null && size > 0) ? size : 10;
        boolean rec = recursive != null && recursive;

        List<Book> filtered = bookRepo.findAll().stream()
            .filter(b -> publicationYear == null || b.getPublicationYear() == publicationYear)
            .filter(b -> language == null || b.getLanguage().equalsIgnoreCase(language))
            .filter(b -> idCategory == null || isInCategory(b, idCategory, rec))
            .toList();

        int total = filtered.size();
        List<Book> pageList = filtered.stream()
            .skip(p * s)
            .limit(s)
            .toList();

        return new BookPage(pageList, makePageInfo(p, s, total));
    }

    @QueryMapping
    public List<Book> booksByAuthor(@Argument int idAuthor) {
        return bookRepo.findAll().stream()
            .filter(b -> b.getAuthor() != null && b.getAuthor().getIdAuthor() == idAuthor)
            .toList();
    }

    @QueryMapping
    public SearchPage search(
        @Argument String keyword,
        @Argument String type,
        @Argument Integer page,
        @Argument Integer size
    ) {
        String kw = keyword.toLowerCase();
        String t = type == null ? "ALL" : type.toUpperCase();

        int p = page != null ? page : 0;
        int s = (size != null && size > 0) ? size : 10;

        List<Object> results = new ArrayList<>();

        if (t.equals("ALL") || t.equals("BOOK")) {
            results.addAll(
                bookRepo.findAll().stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(kw))
                    .toList()
            );
        }

        if (t.equals("ALL") || t.equals("AUTHOR")) {
            results.addAll(
                authorRepo.findAll().stream()
                    .filter(a -> a.getName().toLowerCase().contains(kw))
                    .toList()
            );
        }

        if (t.equals("ALL") || t.equals("CATEGORY")) {
            results.addAll(
                categoryRepo.findAll().stream()
                    .filter(c -> c.getCategoryName().toLowerCase().contains(kw))
                    .toList()
            );
        }

        int total = results.size();
        List<Object> pageList =
            results.stream().skip(p * s).limit(s).toList();

        return new SearchPage(pageList, makePageInfo(p, s, total));
    }

    @QueryMapping
    public Author author(@Argument int idA) {
        return authorRepo.findById(idA).orElse(null);
    }

    @QueryMapping
    public Book book(@Argument int idBook) {
        return bookRepo.findById(idBook).orElse(null);
    }

    @QueryMapping
    public Category category(@Argument int idC) {
        return categoryRepo.findById(idC).orElse(null);
    }

    // ---------- PART 2 : ADMIN ONLY ----------

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Book addBook(@Argument BookInput book) {

        Author a = authorRepo.findById(book.getIdAuthor()).orElseThrow();
        Category c = categoryRepo.findById(book.getIdC()).orElseThrow();

        return bookRepo.save(
            Book.builder()
                .title(book.getTitle())
                .publicationYear(book.getPublicationYear())
                .language(book.getLanguage())
                .nbPages(book.getNbPages())
                .author(a)
                .category(c)
                .build()
        );
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteAuthor(@Argument int idA) {

        if (!authorRepo.existsById(idA)) return false;
        authorRepo.deleteById(idA);
        return true;
    }
}
