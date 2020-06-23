package com.game.service.accountservices;

import java.util.List;

public interface FriendService {

    boolean addFriend(String user, String friend);
    List<String> getFriends(String user);
    boolean removeFriend(String user, String friend);

}
