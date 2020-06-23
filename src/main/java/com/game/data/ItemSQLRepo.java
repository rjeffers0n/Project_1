package com.game.data;

import com.game.models.Item;
import com.game.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Not being used for this version
 */
public class ItemSQLRepo implements Repository<Item, Integer> {
    private ConnectionUtils connectionUtils;
    static final Logger logger = Logger.getLogger(ItemSQLRepo.class);

    public ItemSQLRepo(ConnectionUtils connectionUtils) {
        if (connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    @Override
    public Item findById(Integer i) {
        return null;
    }

    @Override
    public List<Item> findAll() {
        return null;
    }

    @Override
    public List<Integer> findAllID() {
        return null;
    }

    @Override
    public void save(Item obj) {

    }

    @Override
    public void update(Item obj, Integer id) {

    }

    @Override
    public void delete(Integer i) {

    }
}
