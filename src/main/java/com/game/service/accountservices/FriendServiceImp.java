package com.game.service.accountservices;

import com.game.data.MessageSQLRepo;
import com.game.models.Account;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.List;

public class FriendServiceImp implements FriendService {
    private final AccountDetailService accountDetailService;
    static final Logger logger = Logger.getLogger(MessageSQLRepo.class);

    public FriendServiceImp(AccountDetailService accountDetailService){
        BasicConfigurator.configure();
        this.accountDetailService = accountDetailService;
    }

    @Override
    public boolean addFriend(String user, String friend) {
        if(accountDetailService.checkExist(friend)){
            Account temp = accountDetailService.getAccount(user);
            temp.getFriends().add(friend);
            accountDetailService.update(temp);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getFriends(String user) {
        return accountDetailService.getAccount(user).getFriends();
    }

    @Override
    public boolean removeFriend(String user, String friend) {
        Account temp = accountDetailService.getAccount(user);
        if (temp.getFriends().contains(friend)) {
            temp.getFriends().remove(friend);
            accountDetailService.update(temp);
            return true;
        }
        logger.debug("This user is not your friend");
        return false;
    }
}
