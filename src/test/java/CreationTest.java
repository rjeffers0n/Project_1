import com.game.data.AccountSQLRepo;
import com.game.data.MessageSQLRepo;
import com.game.service.accountservices.*;
import com.game.service.messageservices.MessageService;
import com.game.service.messageservices.MessageServiceImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import java.util.ArrayList;
import java.util.List;

public class CreationTest {
    @Mock AccountSQLRepo mockAccount;
    @Mock
    MessageSQLRepo mockMessage;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    AccountDetailService accountDetailService;
    CreationService creationService;
    MessageService messageService;

    @Before
    public void init(){
        List<String> accountList = new ArrayList<>();
        accountList.add("admin");
        when(mockAccount.findAllID()).thenReturn(accountList);
        accountDetailService = new AccountDetailServiceImp(mockAccount);
        messageService = new MessageServiceImp(mockMessage,accountDetailService);
        creationService = new CreationServiceImp(accountDetailService, messageService);
    }

    @Test
    public void createTest(){
        Assert.assertFalse("Cannot create existing account",creationService.signUp("admin","password", "email"));
        Assert.assertFalse("Created account with invalid username",creationService.signUp("","password", "email"));
        Assert.assertFalse("Create an account with invalid password",creationService.signUp("test","password", "email"));
        Assert.assertTrue("Did not create an account",creationService.signUp("test","Password1", "email"));
    }

    @Test
    public void closeTest(){
        creationService.signUp("test","Password1", "email");
        Assert.assertFalse("Account that does not exist is deleted", creationService.delete("test1"));
        Assert.assertFalse("Admin account was deleted", creationService.delete("admin"));
        Assert.assertTrue("Account was not deleted",creationService.delete("test"));
    }


}
