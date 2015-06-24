package br.ufpe.cin.writejapanese;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomer Simis on 03/06/2015.
 */
public class CloudinarySingleton {

    private static Cloudinary instance;

    public static Cloudinary getInstance(){
        if(instance == null){
            Map config = new HashMap();
            config.put("cloud_name", "dfgdz578y");
            config.put("api_key", "729866734514146");
            config.put("api_secret", "eAXvYsZgLWuiMG51jXh59jsBOIM");
            instance = new Cloudinary(config);
        }
        return instance;
    }


}
