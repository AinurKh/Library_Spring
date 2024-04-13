package projects.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import projects.models.Book;

import java.util.List;

@Component
public class BookDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBooks() {
       String sql = "select * from book";
       return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBookById(int id) {
        String sql = "select * from book where id = ?";
        return jdbcTemplate.query(sql,new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book,int id) {
        PersonDao personDao = new PersonDao(jdbcTemplate);
        String sql = "insert into book values(?,?,?,?)";
        jdbcTemplate.update(sql,id,book.getTitle(),book.getAuthor(),book.getYear());
    }

    public void update(Book book, int id) {
        String sql = "update book set title = ?, author = ?, year = ? where id = ?";
        jdbcTemplate.update(sql,book.getTitle(),book.getAuthor(),
                book.getYear(),id);
    }

    public void delete(int id) {
        String sql = "delete from book where id = ?";
        jdbcTemplate.update(sql,id);
    }

}
