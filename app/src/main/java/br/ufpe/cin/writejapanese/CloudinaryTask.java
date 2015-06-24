package br.ufpe.cin.writejapanese;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Tomer Simis on 03/06/2015.
 */
public class CloudinaryTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    private SignaturePad pad;

    private CheckerResult result;

    private KanjiDrawActivity.PlaceholderFragment fragment;

    public CloudinaryTask(SignaturePad pad, CheckerResult result, KanjiDrawActivity.PlaceholderFragment fragment){
        this.pad = pad;
        this.result = result;
        this.fragment = fragment;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory() + "/"+albumName);
        if(file.exists()){
            Log.v("SignaturePad", "Folder already exists");
        }
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public byte[] toByteArray(File file){
        FileInputStream fileInputStream=null;

        byte[] bFile = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return bFile;
    }

    protected String doInBackground(String... files) {
        try {
            Cloudinary cloudinary = CloudinarySingleton.getInstance();
            Bitmap signatureBitmap = pad.getSignatureBitmap();
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));

            try {
                saveBitmapToJPG(signatureBitmap, photo);

                Map uploadResult = cloudinary.uploader().upload( toByteArray(photo), ObjectUtils.asMap("phash", true) );

                Log.v("Result",(String) uploadResult.get("public_id") );

                this.result.setKanjiMap(uploadResult);

                Log.v("pHash", (String) uploadResult.get("phash"));

                return (String) uploadResult.get("phash");

            }catch(Exception e){
                Log.v("[KenjiDrawActivity]", "Exception occurred");
                //Log.v("Exception", e.getMessage());
                e.printStackTrace();
                return null;
            }


        } catch (Exception e) {
            this.exception = e;
            this.result = null;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        fragment.onPostCheck(result);
    }

}
