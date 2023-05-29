package ibf2022.batch1.csf.assessment.server.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ibf2022.batch1.csf.assessment.server.models.Toilet;

public class ToiletMapper implements RowMapper<Toilet> {

    @Override
    public Toilet mapRow(ResultSet rs, int rowNum) throws SQLException {
        Toilet toilet = new Toilet();
        toilet.setId(rs.getInt("id"));
        toilet.setName(rs.getString("name"));
        toilet.setAddress(rs.getString("address"));
        toilet.setLatitude(rs.getString("latitude"));
        toilet.setLongitude(rs.getString("longitude"));
        return toilet;
    }
    
}
