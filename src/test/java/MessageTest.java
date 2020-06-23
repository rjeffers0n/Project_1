import com.game.data.AccountSQLRepo;
import com.game.data.MessageSQLRepo;
import com.game.service.accountservices.AccountDetailService;
import com.game.service.accountservices.AccountDetailServiceImp;
import com.game.service.messageservices.MessageService;
import com.game.service.messageservices.MessageServiceImp;
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

public class MessageTest {

    @Mock MessageSQLRepo mockMessage;
    @Mock AccountSQLRepo mockAccount;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    AccountDetailService accountDetailService;
    MessageService messageService;

    @Before
    public void init(){
        List<String> accountList = new ArrayList<>();
        accountList.add("admin");
        accountList.add("king");
        accountList.add("queen");
        accountList.add("jack");
        when(mockAccount.findAllID()).thenReturn(accountList);
        accountDetailService = new AccountDetailServiceImp(mockAccount);
        messageService = new MessageServiceImp(mockMessage, accountDetailService);
        accountDetailService.addAccount("test","password","email");
    }

    @Test
    public void sendTest(){
        Assert.assertFalse("Message was sent somehow",
                messageService.send("test","baduser","I need help"));
        Assert.assertTrue("Message was not sent",
                messageService.send("test","admin","I need help"));
    }

}
