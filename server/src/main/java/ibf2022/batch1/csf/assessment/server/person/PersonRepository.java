package ibf2022.batch1.csf.assessment.server.person;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Optional<Person> getPersonByEmail(String email) {
        String query = "select * from person where email = ?";
        Person person = jdbcTemplate.queryForObject(query, new PersonMapper(), email);
        return Optional.ofNullable(person);
    }

    public boolean addPerson(Person person) {
        String query = "insert into person (email, password, role) values (?, ?, ?)";
        int added = jdbcTemplate.update(query, person.getEmail(), person.getPassword(), person.getRole().name());
        return added > 0;
    }
}
