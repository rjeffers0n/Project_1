package com.game.system;

import com.game.data.AccountSQLRepo;
import com.game.data.MessageSQLRepo;
import com.game.data.Repository;
import com.game.models.Account;
import com.game.service.accountservices.*;
import com.game.service.messageservices.MessageService;
import com.game.service.messageservices.MessageServiceImp;
import com.game.utils.ConnectionUtils;
import com.game.utils.PostgresConnectionUtil;
import org.apache.log4j.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class PortalContextListener  implements ServletContextListener {
    static final Logger logger = Logger.getLogger(PortalContextListener.class);

    /**
     * Sets up database connections and services for web app
     * Passes them into context
     * @param servletContextEvent servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        // initialize log4j
        String log4jConfigFile = context.getInitParameter("log4j-config-location");
        String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;

        PropertyConfigurator.configure(fullPath);

        // rest of the context configuration
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.debug("db.properties not found");
        }
        ConnectionUtils connection = new PostgresConnectionUtil(prop.getProperty("url"),
                prop.getProperty("username"),prop.getProperty("password"));

        Repository<Account,String> accountSQLRepo = new AccountSQLRepo(connection);
        //ItemSQLRepo itemSQLRepo = new ItemSQLRepo(connection);
        MessageSQLRepo messageSQLRepo = new MessageSQLRepo(connection);
        AccountDetailServiceImp accountDetailService = new AccountDetailServiceImp(accountSQLRepo);
        //ItemService itemService = new ItemServiceImp(itemSQLRepo);
        MessageService messageService = new MessageServiceImp(messageSQLRepo, accountDetailService);
        CreationService creationService = new CreationServiceImp(accountDetailService,messageService);
        ModificationService modificationService = new ModificationServiceImp(accountDetailService);

        //context.setAttribute("itemService", itemService);
        context.setAttribute("accountDetailService",accountDetailService);
        context.setAttribute("messageService", messageService);
        context.setAttribute("creationService", creationService);
        context.setAttribute("modificationService", modificationService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
