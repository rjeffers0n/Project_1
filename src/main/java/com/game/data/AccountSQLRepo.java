package com.game.data;

import com.game.models.Account;
import com.game.utils.ConnectionUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Accesses the account_info in the postgres data base
 */
public class AccountSQLRepo implements Repository<Account, String> {
    private ConnectionUtils connectionUtils;
    static final Logger logger = Logger.getLogger(AccountSQLRepo.class);
    static final String PEL = "prepared statement not closed";
    static final String REL = "prepared statement not closed";
    static final String CEL = "Connection did not close";
    static final String TABLE = ".Account_Info ";

    /**
     * Contructor that passes in the connection utils
     * @param connectionUtils connection utilities
     */
    public AccountSQLRepo(ConnectionUtils connectionUtils) {
        BasicConfigurator.configure();
        if (connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    /**
     * Finds a account by the username
     * @param s username
     * @return account
     */
    @Override
    public Account findById(String s) {
        Account temp = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "select password, email, friends, credits, bankaccount from " + schemaName + TABLE + "where username = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1,s);
            rs = ps.executeQuery();
            while(rs.next()) {
                temp = new Account(s,rs.getString("password").trim(),
                        rs.getString("email").trim(),rs.getString("friends").trim(),rs.getInt("credits"),
                        rs.getString("bankaccount"));
            }
        } catch (SQLException e) {
            logger.info("SQL find by id failed", e);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { logger.info(REL, e); }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { logger.info(PEL, e); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { logger.info(CEL, e); }
            }
        }
        return temp;
    }

    /**
     * No longer used for project as is would take up too much memory instantiating all the account
     * @return returns a list of the accounts
     */
    @Override
    public List<Account> findAll() {
        //reuse code from flashcard project
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Account> Account_Info = new ArrayList<>();
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "Select username, password, credits, email, friends, bankaccount from " + schemaName + TABLE;
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            Account temp;
            while(rs.next()) {
                temp = new Account(rs.getString("username").trim(),rs.getString("password").trim(),
                        rs.getString("email").trim(),rs.getString("friends"),rs.getInt("credits"),
                        rs.getString("bankaccount").trim());

                Account_Info.add(temp);
            }
        } catch (SQLException e) {
            logger.info("SQL find all failed", e);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { logger.info(REL, e); }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { logger.info(PEL, e); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { logger.info(CEL, e); }
            }
        }
        return Account_Info;
    }

    /**
     * Gives list of IDs to check against if an account exist
     * @return ArrayList of IDs
     */
    @Override
    public List<String> findAllID() {
        //reuse code from flashcard project
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> idList = new ArrayList<>();

        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "Select username from " + schemaName + TABLE;
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                idList.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            logger.info("SQL find all by ID failed", e);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { logger.info(REL, e); }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { logger.info(PEL, e); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { logger.info(CEL, e); }
            }
        }
        return idList;
    }

    /**
     * Insert a new record with corresponding username, password, and credits
     * as the account object that is passed in
     * @param obj account object
     */
    @Override
    public void save(Account obj) {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "insert into " + schemaName + TABLE +
                    "(username,password,email) values (?,?,?);";
            ps = connection.prepareStatement(sql);
            ps.setString(1,obj.getName());
            ps.setString(2,obj.getPassword());
            ps.setString(3,obj.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.info("SQL save failed", e);
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { logger.info(PEL, e); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { logger.info(CEL, e); }
            }
        }
    }

    /**
     * Finds the record with the same username as the account object.
     * Updates the record with the changed field(s) in the account object.
     * @param obj Account object
     */
    @Override
    public void update(Account obj, String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            List<String> temp = obj.getFriends();
            StringBuilder sline = new StringBuilder();
            for (int i=0;i<temp.size();i++) {
                if(i!=0){
                    sline.append(",");
                }
                sline.append(temp.get(i));
            }
            String sql = "update " + schemaName + TABLE +
                    "set password = ?,"+
                    "credits = ?," +
                    "friends = ?," +
                    "bankaccount = ? where " +
                    "username = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1,obj.getPassword());
            ps.setInt(2,obj.getBalance());
            ps.setString(3,sline.toString());
            ps.setString(4,obj.getCardNumber());
            ps.setString(5,obj.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.info("SQL update failed", e);
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { logger.info(PEL, e); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { logger.info(CEL, e); }
            }
        }
    }

    /**
     * Deletes record that has the inputted username
     * @param s the username
     */
    @Override
    public void delete(String s) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "delete from " + schemaName + TABLE +
                    "where username = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1,s);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.info("SQL delete failed", e);
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { logger.info(PEL, e); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { logger.info(CEL, e); }
            }
        }
    }
}
