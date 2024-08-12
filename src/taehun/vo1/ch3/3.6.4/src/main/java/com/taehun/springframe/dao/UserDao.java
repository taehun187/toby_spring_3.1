package com.taehun.springframe.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.taehun.springframe.domain.User;

public class UserDao {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
			
		this.dataSource = dataSource;
	}	
	
	private RowMapper<User> userRowMapper() {
		return (rs, rowNum) -> {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		};
	}
	
	public void add(final User user) throws SQLException {
		this.jdbcTemplate.
		update("insert into users(id, name, password) values(?,?,?)",
				user.getId(), 
				user.getName(), 
				user.getPassword());
	}
	
	public Optional<User> get(String id) throws ClassNotFoundException, SQLException {
		
		String sql = "select * from users where id = ?";
		try (Stream<User> stream = 
				jdbcTemplate.queryForStream(
						sql, 
						userRowMapper(),
						id)) {
			return stream.findFirst();
		} catch (DataAccessException e) {
			return Optional.empty();
		}		
	}
	
	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("delete from users");
	}
	
	public int getCount() {
		List<Integer> result = jdbcTemplate.query(
				"select count(*) from users",
				(rs, rowNum) -> rs.getInt(1));
		
		return DataAccessUtils.singleResult(result);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",
				userRowMapper());
	}

}