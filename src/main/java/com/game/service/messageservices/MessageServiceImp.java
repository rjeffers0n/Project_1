package com.game.service.messageservices;

import com.game.data.MessageSQLRepo;
import com.game.models.Message;
import com.game.service.accountservices.AccountDetailService;
import org.apache.log4j.Logger;
import java.sql.Timestamp;
import java.util.List;
import org.apache.log4j.BasicConfigurator;

/**
 * Class that manages message repository
 */
public class MessageServiceImp implements MessageService{
    private final MessageSQLRepo mrepo;
    private final AccountDetailService accountDetailService;
    private final Logger logger = Logger.getLogger(MessageServiceImp.class);

    /**
     * Requires message repo and account detail service
     * @param mrepo message repository
     * @param accountDetailService accountDetailService
     */
    public MessageServiceImp(MessageSQLRepo mrepo, AccountDetailService accountDetailService) {
        BasicConfigurator.configure();
        this.mrepo = mrepo;
        this.accountDetailService = accountDetailService;
    }

    /**
     * Creates and add message to the message list and call the repo's save method to
     * add the record to the database.
     * @return true if send succeeds
     */
    public boolean send(String from, String to, String content) {
        if(accountDetailService.checkExist(to)&&to!=null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Message temp = new Message(content, from, timestamp, to);
            mrepo.save(temp);
            logger.debug("Message sent");
            return true;
        }
        return false;
    }

    /**
     * deletes all messages to and from that user
     */
    @Override
    public void clear(String name) {
        mrepo.clear(name);
        logger.debug("All messages deleted");
    }

    /**
     * Retrieves all the messages in list form
     * @param username session user
     * @return ArrayList of messages
     */
    @Override
    public List<Message> getMessageList(String username) {
        return mrepo.findAllbyName(username);
    }
}
