package projects.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import projects.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDao {
 private JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> findAll() {
        String sql = "select * from person";
        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(Person.class));
    }

    public  Person findById(int id) {
       return jdbcTemplate.query("select * from person where id = ?",
               new Object[]{id},new BeanPropertyRowMapper<>(Person.class))
               .stream().findAny().orElse(null);
    }
    public void save(Person person) {
        String sql = "insert into person(name, year) values(?, ?)";
        jdbcTemplate.update(sql,person.getName(),person.getYear());
    }

    public void update(Person person, int id) {
        String sql = "update person set name = ?, year = ? where id = ?";
        jdbcTemplate.update(sql,person.getName(),person.getYear(),id);
    }

    public void delete(int id) {
        String sql = "delete from person where id = ?";
        jdbcTemplate.update(sql,id);
    }
}
