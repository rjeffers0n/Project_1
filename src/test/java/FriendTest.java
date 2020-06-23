import com.game.data.AccountSQLRepo;
import com.game.models.Account;
import com.game.service.accountservices.*;
import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

public class FriendTest {
    @Mock AccountSQLRepo mockAccount;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    AccountDetailService accountDetailService;
    FriendService friendService;

    @Before
    public void init(){
        BasicConfigurator.configure();
        List<String> accountList = new ArrayList<>();
        accountList.add("admin");
        accountList.add("king");
        accountList.add("queen");
        accountList.add("jack");
        Account temp = new Account("test","Password1","email");
        when(mockAccount.findAllID()).thenReturn(accountList);
        when(mockAccount.findById("test")).thenReturn(temp);
        accountDetailService = new AccountDetailServiceImp(mockAccount);
        friendService = new FriendServiceImp(accountDetailService);
        accountDetailService.checkCredentials("test","Password1");
    }


    @Test
    public void addFriendTest(){
        //Add an account that doesn't exist
        Assert.assertFalse("Non existent friend was added",
                friendService.addFriend("test", "ten"));
        //Add an account that exists
        Assert.assertTrue("Friend was not added",friendService.addFriend("test", "jack"));
    }

    @Test
    public void removeFriendTest(){
        friendService.addFriend("test", "jack");
        friendService.addFriend("test", "queen");
        friendService.addFriend("test", "king");

        //remove non-existing friend
        Assert.assertFalse("Friend was not added",friendService.removeFriend("test", "ten"));
        //remove friend on friend list
        Assert.assertTrue("Friend was not added",friendService.removeFriend("test", "jack"));
    }

    @Test
    public void getFriendsTest(){
        //friend list should be empty at the beginning
        Assert.assertTrue("FriendList os not empty somehow",friendService.getFriends("test").isEmpty());
        friendService.addFriend("test", "jack");
        //Friend list should contain 1 friend
        Assert.assertFalse("FriendList is not empty somehow",friendService.getFriends("test").isEmpty());
        Assert.assertEquals("FriendList is not empty somehow",1,friendService.getFriends("test").size());
        friendService.addFriend("test", "queen");
        friendService.addFriend("test", "king");
        Assert.assertEquals("FriendList is not empty somehow",3,friendService.getFriends("test").size());
    }
}
