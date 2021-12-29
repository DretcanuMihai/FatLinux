package com.map_toysocialnetworkgui.model.entities_dto;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.entities.User;

import java.time.LocalDate;

/**
 * DTO class for friendship entities
 */
public class FriendshipDTO {
    /**
     * begin date info
     */
    private final LocalDate beginDate;

    /**
     * users info
     */
    private final UserUIDTO user1, user2;

    /**
     * creates a Friendship DTO for a friendship between two users
     * user1 and user2 must be the friendship's users - this isn't verified
     *
     * @param friendship - said friendship
     * @param user1      - first user
     * @param user2      - second user
     */
    public FriendshipDTO(Friendship friendship, User user1, User user2) {
        this.beginDate = friendship.getBeginDate();
        this.user1 = new UserUIDTO(user1);
        this.user2 = new UserUIDTO(user2);
    }

    /**
     * gets the date the friendship begun
     *
     * @return said date
     */
    public LocalDate getBeginDate() {
        return beginDate;
    }

    /**
     * gets the first user's DTO
     *
     * @return said DTO
     */
    public UserUIDTO getUser1() {
        return user1;
    }

    /**
     * gets the second user's DTO
     *
     * @return said DTO
     */
    public UserUIDTO getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "FriendshipDTO{" +
                "beginDate=" + beginDate +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }
}
