package com.game.data;

import com.game.models.Message;
import com.game.utils.ConnectionUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository to implement CRUD methods of the DAO interface.
 * Build to manages the messageList table in Postgresql.
 * Not all CRUD methods are necessary so some will be left
 * unimplemented.
 */
public class MessageSQLRepo implements Repository<Message, Timestamp> {
    static final Logger logger = Logger.getLogger(MessageSQLRepo.class);
    private ConnectionUtils connectionUtils;
    static final String PEL = "prepared statement not closed";
    static final String REL = "prepared statement not closed";
    static final String CEL = "Connection did not close";
    static final String TABLE = ".Message_List ";

    public MessageSQLRepo(ConnectionUtils connectionUtils) {
        BasicConfigurator.configure();
        if (connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    /**
     * will not use this method; set up messages so that their id is arbitrary
     * @param time would be the timestamp of the message
     */
    @Override
    public Message findById(Timestamp time) {
        Message temp = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "select touser, fromuser, messagecontent, messagetime from " + schemaName + TABLE +" where messagetime = ?;";
            ps = connection.prepareStatement(sql);
            ps.setTimestamp(1,time);
            rs = ps.executeQuery();
            while(rs.next()) {
                temp = new Message(rs.getString("messagecontent").trim(),rs.getString("fromuser").trim(),
                        rs.getTimestamp("messagetime"),rs.getString("touser").trim());
            }
        } catch (SQLException e) {
            logger.info("SQL message find by id failed", e);
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
        return temp;    }

    /**
     * finds all messages to the current account that is signed in
     * @return list of messages that are sent to the user
     */
    @Override
    public List<Message> findAll() {
        return null;
    }

    public List<Message> findAllbyName(String name){
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Message> messageList = new ArrayList<>();
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "Select messagecontent, fromuser, touser, messagetime from " + schemaName + TABLE +
                    "where touser = ? or fromuser = ? ORDER BY messagetime ASC;";
            ps = connection.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,name);
            rs = ps.executeQuery();
            Message temp;
            while(rs.next()) {
                temp = new Message(rs.getString("messagecontent").trim(),
                        rs.getString("fromuser").trim(),rs.getTimestamp("messagetime"),rs.getString("touser").trim());
                messageList.add(temp);
            }
        } catch (SQLException e) {
            logger.info("SQL find all by name failed", e);
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
        return messageList;
    }

    @Override
    public List<Timestamp> findAllID() {
        return null;
    }

    /**
     * Will not use this method; no changes could be made to messages once created.
     * Made it so that those values are final
     * @param obj is the message object
     */
    @Override
    public void update(Message obj, Timestamp id) {
        logger.debug("Method not implemented");
    }

    /**
     * Adds new record to the messageList.
     * @param obj - the message object with a from, to, and message strings
     */
    @Override
    public void save(Message obj) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "insert into " + schemaName + TABLE +
                    "(fromuser,touser,messagecontent,messagetime) values (?,?,?,?);";
            ps = connection.prepareStatement(sql);
            ps.setString(1,obj.getFromuser());
            ps.setString(2,obj.getTouser());
            ps.setString(3,obj.getMessage());
            ps.setTimestamp(4,obj.getMessageTime());
            ps.executeUpdate();
            connection.close();
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
     * Id, aka the primary key, allows for quicker access to the correct
     * record in message list. This method will then delete it.
     * @param time, an arbitrary value assigned once the record is created
     */
    @Override
    public void delete(Timestamp time) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "delete from " + schemaName + TABLE +
                    "where messagetime = ?;";
            ps = connection.prepareStatement(sql);
            ps.setTimestamp(1,time);
            ps.executeUpdate();
            connection.close();
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

    public List findAll(String name) {
        return null;
    }

    /**
     * clears all messages that references that user
     * @param name username of who sent message
     */
    public void clear(String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectionUtils.getConnection();
            String schemaName = connectionUtils.getDefaultSchema();
            String sql = "delete from " + schemaName + TABLE +
                    "where touser = ? or fromuser = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,name);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.info("SQL message delete failed", e);
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
