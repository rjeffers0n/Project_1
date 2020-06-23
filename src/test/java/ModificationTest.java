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

public class ModificationTest {
    @Mock AccountSQLRepo mockAccount;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    AccountDetailService accountDetailService;
    ModificationService modificationService;

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
        modificationService = new ModificationServiceImp(accountDetailService);
        accountDetailService.checkCredentials("test","Password1");
    }

    @Test
    public void depositTest(){
        accountDetailService.addAccount("test2","Password1","email");
        modificationService.changeBankAccount("379354508162306","test");
        Assert.assertFalse(modificationService.deposit(-100,"test"));
        //user does not exist, should return false
        Assert.assertFalse(modificationService.deposit(100,"test2"));
        Assert.assertTrue(modificationService.deposit(100,"test"));
    }

    @Test
    public void withdrawtest(){
        modificationService.changeBankAccount("379354508162306","test");
        modificationService.deposit(1000,"test");
        Assert.assertFalse("negative amount was withdrawn",
                modificationService.withdraw(-100,"test"));
        Assert.assertTrue("Amount was not withdrawn",
                modificationService.withdraw(100,"test"));
    }

    @Test
    public void changePasswordTest(){

    }

    @Test
    public void changeBankAccountTest(){
        //valid
        Assert.assertTrue("Bank account was not added",
                modificationService.changeBankAccount("379354508162306","test"));
        //invalid
        Assert.assertFalse("Bank account was added",
                modificationService.changeBankAccount("4388576018402626","test"));
    }

}
