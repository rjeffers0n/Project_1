package com.game.service.itemservices;

import com.game.models.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAllItems ();
    Item findItems (int id);
    void addItem(Item item);
    void deleteItem(int id);
    void changeItem(Item item, int id);
}
