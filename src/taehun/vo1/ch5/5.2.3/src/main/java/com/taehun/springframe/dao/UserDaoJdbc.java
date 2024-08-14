package com.taehun.springframe.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;

public class UserDaoJdbc implements UserDao {
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userRowMapper = (rs, rowNum) -> {
	    User user = new User();
	    user.setId(rs.getString("id"));
	    user.setName(rs.getString("name"));
	    user.setPassword(rs.getString("password"));
	    user.setLevel(Level.valueOf(rs.getInt("level")));
	    user.setLogin(rs.getInt("login"));
	    user.setRecommend(rs.getInt("recommend"));
	    return user;
	};

	public void add(User user) {
	 try {
		this.jdbcTemplate.update(
				"insert into users(id, name, password, level, login, recommend) " +
				"values(?,?,?,?,?,?)", 
					user.getId(), user.getName(), user.getPassword(), 
					user.getLevel().intValue(), user.getLogin(), user.getRecommend());}
	catch (DataAccessException de) {
		System.out.println(de);
	}
	}

	public Optional<User> get(String id) {
        String sql = "select * from users where id = ?";
        try (Stream<User> stream = 
                jdbcTemplate.queryForStream(
                    sql, 
                    userRowMapper,  
                    id)) {
            return stream.findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {
	    return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",this.userRowMapper);
	}

	public void update(User user) {
		this.jdbcTemplate.update(
				"update users set name = ?, password = ?, level = ?, login = ?, " +
				"recommend = ? where id = ? ", user.getName(), user.getPassword(), 
		user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
		user.getId());
		
	}
}