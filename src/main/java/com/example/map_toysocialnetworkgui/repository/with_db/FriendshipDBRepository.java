package com.example.map_toysocialnetworkgui.repository.with_db;


import com.example.map_toysocialnetworkgui.model.entities.Friendship;
import com.example.map_toysocialnetworkgui.repository.Repository;
import com.example.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDBRepository implements Repository<UnorderedPair<String>, Friendship> {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void save(Friendship friendship) {
        String sqlSave = "INSERT INTO friendships(first_user_email, second_user_email, begin_date) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementSave= connection.prepareStatement(sqlSave)) {

                statementSave.setString(1,friendship.getEmails().getFirst());
                statementSave.setString(2,friendship.getEmails().getSecond());
                statementSave.setDate(3,Date.valueOf(friendship.getBeginDate()));
                statementSave.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Friendship get(UnorderedPair<String> id) {
        String sqlFind = "SELECT * from friendships where (first_user_email=(?) and second_user_email=(?))";
        Friendship toReturn=null;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementFind = connection.prepareStatement(sqlFind)) {

            statementFind.setString(1,id.getFirst());
            statementFind.setString(2,id.getSecond());
            ResultSet result=statementFind.executeQuery();
            if(result.next()){
                String email1=result.getString("first_user_email");
                String email2=result.getString("second_user_email");
                LocalDate joinDate=result.getDate("begin_date").toLocalDate();
                toReturn=new Friendship(email1,email2,joinDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public void update(Friendship friendship) {
        String sqlUpdate = "UPDATE friendships SET begin_date=(?) where (first_user_email=(?) and second_user_email=(?))";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate)) {

            statementUpdate.setDate(1,Date.valueOf(friendship.getBeginDate()));
            statementUpdate.setString(2,friendship.getEmails().getFirst());
            statementUpdate.setString(3,friendship.getEmails().getSecond());
            statementUpdate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UnorderedPair<String> id) {
        String sqlDelete = "DELETE FROM friendships where (first_user_email=(?) and second_user_email=(?))";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDelete = connection.prepareStatement(sqlDelete)) {

            statementDelete.setString(1,id.getFirst());
            statementDelete.setString(2,id.getSecond());
            statementDelete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Friendship> getAll() {
        Set<Friendship> friendships = new HashSet<>();
        String sql="SELECT * from friendships";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String email1 = resultSet.getString("first_user_email");
                String email2= resultSet.getString("second_user_email");
                LocalDate beginDate=resultSet.getDate("begin_date").toLocalDate();
                Friendship friendship=new Friendship(email1,email2,beginDate);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
}
