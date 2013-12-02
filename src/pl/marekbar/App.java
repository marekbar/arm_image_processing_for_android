/*
 * Marek Bar 33808
 * marekbar1985@gmail.com
 * WSiZ Informatyka
 * */
package pl.marekbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class App extends Application{

    private static Context context;
    public static String TAG = "Przetwarzanie obrazu";
    public static String NAME = "Przetwarzanie obrazu ARM";
    public static String ALBUM_NAME = NAME;
	private static final String JPEG_FILE_PREFIX = "marek_bar_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static AlbumStorageDirFactory AlbumStorage = null;
    
    public void onCreate(){
        super.onCreate();
        App.context = getApplicationContext();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) 
		{
			AlbumStorage = new FroyoAlbumDirFactory();
		} 
		else 
		{
			AlbumStorage = new BaseAlbumDirFactory();
		}
    }

    public static Context getContext() {
        return App.context;
    }
    
    public static void info(String info)
    {
    	Log.i(App.TAG, info);
	}
    
    public static void error(String error)
    {
    	Log.e(App.TAG, error);
    }
    
	public static File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = AlbumStorage.getAlbumStorageDir(ALBUM_NAME);

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						App.error("Nie utworzono katalogu.");
						return null;
					}
				}
			}
			
		} else {
			App.error("Nie zamontowano karty SD do odczytu/zapisu");
		}
		
		return storageDir;
	}
	
	public static void galleryAddPic(String PhotoPath) {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(PhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    context.sendBroadcast(mediaScanIntent);
}
	
	public static File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = App.getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	
	public static Boolean SaveToGallery(Bitmap bitmap)
	{
		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
			String path = App.getAlbumDir().getAbsolutePath() + File.separator + JPEG_FILE_PREFIX 
					+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + JPEG_FILE_SUFFIX;
			File f = new File(path);
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
			App.galleryAddPic(path);
			return true;
		}
		catch(IOException ioe)
		{
			return false;
		}
	}
	
	public static void tost(String what)
	{
		Toast.makeText(context, what, Toast.LENGTH_LONG).show();
	}
}
