package com.arya.lib.model;

import java.util.Observable;

/**
 * Created by ARCHANA on 25-07-2015.
 */
public class BasicModel extends Observable {
    public BasicModel(){
    }

    public void notifyObservers(Object data)
    {
        setChanged();
        super.notifyObservers(data);
    }
}
