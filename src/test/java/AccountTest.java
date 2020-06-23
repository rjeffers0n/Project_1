import com.game.data.AccountSQLRepo;
import com.game.models.Account;
import com.game.service.accountservices.*;
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

public class AccountTest {
    @Mock AccountSQLRepo mockAccount;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    AccountDetailService accountDetailService;

    @Before
    public void init(){
        List<String> accountList = new ArrayList<>();
        accountList.add("admin");
        when(mockAccount.findAllID()).thenReturn(accountList);
        Account temp = new Account("admin","password","email");
        when(mockAccount.findById("admin")).thenReturn(temp);
        accountDetailService = new AccountDetailServiceImp(mockAccount);
    }

    @Test
    public void checkExistTest(){
        Assert.assertTrue("Account does not exist",accountDetailService.checkExist("admin"));
        Assert.assertFalse("Account somehow exists",accountDetailService.checkExist("test"));
    }

    @Test
    public void credentialsTest(){
        Assert.assertTrue("Credentials was not correct",
                accountDetailService.checkCredentials("admin","password"));
    }

    @Test
    public void getFindByID(){
        Account temp = accountDetailService.findByID("admin");
        Assert.assertEquals("Username does not match", "admin", temp.getName());
        Assert.assertEquals("Username does not match", "email", temp.getEmail());
        Assert.assertEquals("Username does not match", "password", temp.getPassword());
        temp = accountDetailService.findByID("test");
        Assert.assertNull("Null object was not returned",temp);
    }

    @Test
    public void getAccountTest(){
        accountDetailService.checkCredentials("admin","password");
        Account temp = accountDetailService.getAccount("test");
        Assert.assertNull("Null object was not returned",temp);
        temp = accountDetailService.getAccount("admin");
        Assert.assertEquals("Username does not match", "admin", temp.getName());
        Assert.assertEquals("Username does not match", "email", temp.getEmail());
        Assert.assertEquals("Username does not match", "password", temp.getPassword());
    }

    @Test
    public void getAccountListTest(){
        accountDetailService.addAccount("test","Password1", "email");
        accountDetailService.addAccount("test2","Password1", "email");
        accountDetailService.addAccount("test3","Password1", "email");
        List<String> temp = accountDetailService.getAccountList();
        Assert.assertEquals("Size is incorrect", 4, temp.size());
        Assert.assertTrue("test not found", temp.contains("test"));
        Assert.assertTrue("test2 not found", temp.contains("test2"));
        Assert.assertTrue("test3 not found", temp.contains("test3"));
        Assert.assertTrue("admin not found", temp.contains("admin"));
    }
}
