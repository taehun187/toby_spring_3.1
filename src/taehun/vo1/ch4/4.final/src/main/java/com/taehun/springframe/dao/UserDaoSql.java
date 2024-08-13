package com.taehun.springframe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.taehun.springframe.domain.User;

public class UserDaoSql {
    
    public UserDaoSql() {
        
    }
    
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        Connection c = DriverManager.getConnection(
                        "jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8",
                        "root",
                        "12341234"
                        );
        
        return c;
                
    }
    
    public void add(User user) throws ClassNotFoundException, SQLException  {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)");
                    
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());        
        ps.setString(3, user.getPassword());
           
        ps.executeUpdate();
        
        // 자원 해제 (중요)
        ps.close();
        c.close();
    }
}