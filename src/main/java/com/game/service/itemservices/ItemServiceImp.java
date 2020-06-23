package com.game.service.itemservices;

import com.game.data.ItemSQLRepo;
import com.game.models.Item;

import java.util.List;

/**
 * Not used for this version of the application
 */
public class ItemServiceImp implements ItemService{
    ItemSQLRepo irepo;

    public ItemServiceImp(ItemSQLRepo irepo){
        this.irepo=irepo;
    }

    @Override
    public List<Item> findAllItems() {
        return irepo.findAll();
    }

    @Override
    public Item findItems(int id) {
        return irepo.findById(id);
    }

    @Override
    public void addItem(Item item) {
        irepo.save(item);
    }

    @Override
    public void deleteItem(int id) {
        irepo.delete(id);
    }

    @Override
    public void changeItem(Item item, int id) {
        irepo.update(item, id);
    }
}
