package com.example.ezras.travelagencies.model.backend;

import com.example.ezras.travelagencies.model.datasource.LocalDBManager;
import com.example.ezras.travelagencies.model.datasource.ServerDBManager;

/**
 * Created by Ezra_Steinmetz on Aug 2016.
 *
 * a singleton class for the DB manager
 */

public class DBManagerFactory {
    enum DBtype {Local, RemoteServer};

    static DBtype type = DBtype.RemoteServer;

    private static DB_Manager manager = null;
    public static DB_Manager getManager(){
        if(manager == null){
            switch (type){
                case Local:
                    manager = new LocalDBManager();
                    break;
                case RemoteServer:
                    manager = new ServerDBManager();
                    break;
            }
        }
        return manager;
    }
}
