package com.game.data;

import java.util.List;

// DAO interface can be used by any means to persist data
public interface Repository<T, ID> {
    //modified parameters as each row has a unique ID, so another object parameter is redundant

    //return object from the id
    T findById(ID id);

    //creates a list of objects from all the rows
    List<T> findAll();

    //creates a list of ids from all the rows
    List<ID> findAllID();

    //insert a new row with the object, do we really need to return anything?
    void save(T obj);

    //updated row with new attributes
    void update(T obj, ID id);

    //delete row with corresponding id
    void delete(ID id);
}

