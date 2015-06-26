package br.ufpe.cin.writejapanese;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * Created by Tomer Simis on 26/06/2015.
 */
public class Database {

    private static DB instance;

    public static void init(Context context) throws SnappydbException{
        instance = DBFactory.open(context);
    }

    public static DB getInstance(){
        return instance;
    }

    public static void close() throws SnappydbException {
        instance.close();
    }


}
