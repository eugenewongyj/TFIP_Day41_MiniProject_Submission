package ibf2022.batch1.csf.assessment.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch1.csf.assessment.server.models.Toilet;

@Repository
public class ToiletRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Toilet> getAllToilets() {
        String query = "select * from toilet";
        List<Toilet> toiletList = jdbcTemplate.query(query, new ToiletMapper());
        return toiletList;
    }

    public Optional<Toilet> getToiletByName(String name) {
        String query = "select * from toilet where name = ?";
        Toilet toilet = jdbcTemplate.queryForObject(query, new ToiletMapper(), name);
        return Optional.ofNullable(toilet);
    }
    
}
