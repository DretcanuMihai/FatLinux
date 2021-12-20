package com.map_toysocialnetworkgui.repository.with_db;


import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.repository.skeletons.AbstractDBRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.CreateOperationRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.DeleteOperationRepository;
import com.map_toysocialnetworkgui.repository.skeletons.operation_based.ReadOperationRepository;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDBRepository extends AbstractDBRepository
        implements CreateOperationRepository<UnorderedPair<String>,Friendship>,
        DeleteOperationRepository<UnorderedPair<String>,Friendship>,
        ReadOperationRepository<UnorderedPair<String>,Friendship> {
    public FriendshipDBRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public void save(Friendship friendship) {
        String sqlSave = "INSERT INTO friendships(first_user_email, second_user_email, begin_date) values (?,?,?)";
        try (Connection connection = getConnection();
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
    public Friendship tryGet(UnorderedPair<String> id) {
        String sqlFind = "SELECT * from friendships where (first_user_email=(?) and second_user_email=(?))";
        Friendship toReturn=null;

        try (Connection connection = getConnection();
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
    public void delete(UnorderedPair<String> id) {
        String sqlDelete = "DELETE FROM friendships where (first_user_email=(?) and second_user_email=(?))";
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
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
