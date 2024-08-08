package com.taehun.springframe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.taehun.springframe.domain.User;

public abstract class UserDao {
    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
            "insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();
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

    abstract protected Connection getConnection() throws ClassNotFoundException, SQLException;

    // USERS 테이블을 생성하는 메서드 추가
    public void createTable() throws ClassNotFoundException, SQLException {
        Connection c = getConnection();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), password VARCHAR(255))";
        PreparedStatement ps = c.prepareStatement(createTableSQL);
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new HUserDao();

        // USERS 테이블 생성
        dao.createTable();

        User user = new User();
        user.setId("son");
        user.setName("손흥민");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}

