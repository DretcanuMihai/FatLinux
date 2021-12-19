package com.map_toysocialnetworkgui.repository;

import com.map_toysocialnetworkgui.model.entities.Entity;
import com.map_toysocialnetworkgui.model.entities.User;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * an Abstract Database Repository class
 */
public abstract class AbstractDBRepository<ID, E extends Entity<ID>> {
    /**
     * the database's URL
     */
    private final String url;
    /**
     * the database's username
     */
    private final String username;
    /**
     * the database's password
     */
    private final String password;

    /**
     * constructs a Repository with a Database described by an url
     * authentication is made with the username and password given
     * @param url - said url
     * @param username - said username
     * @param password - said password
     */
    public AbstractDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * gets a Connection to the database
     * @return said connection
     * @throws SQLException if any error occurs
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
