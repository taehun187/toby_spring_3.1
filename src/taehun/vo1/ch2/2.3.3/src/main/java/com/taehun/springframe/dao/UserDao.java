package com.taehun.springframe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.taehun.springframe.domain.User;

public class UserDao {
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws SQLException {
		Connection c = this.dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement(
			"insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public User get(String id) throws SQLException {
		Connection c = this.dataSource.getConnection();
		PreparedStatement ps = c
				.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));

		rs.close();
		ps.close();
		c.close();

		return user;
	}

	public void deleteAll() throws SQLException {
		Connection c = dataSource.getConnection();
	
		// drop table users -> 테이블 자체를 삭제 
		// 이건 테이블은 그냥 두고 내용을 다 삭제 
		PreparedStatement ps = c.prepareStatement("delete from users");
		ps.executeUpdate();

		ps.close();
		c.close();
	}	

	public int getCount() throws SQLException  {
		Connection c = dataSource.getConnection();
		// select count (*) from users 
		// users 테이블의 총 row 개수 -> 정수 값 
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		
		// 한 개 이상의 result row를 가진다 ... row는 한개이상의 column으로 구성  
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1); // 첫번째 컬럼[1]의 값을 가져온다 

		rs.close();
		ps.close();
		c.close();
	
		return count;
	}
}
