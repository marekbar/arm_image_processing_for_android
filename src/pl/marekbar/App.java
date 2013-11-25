/*
 * Marek Bar 33808
 * marekbar1985@gmail.com
 * WSiZ Informatyka
 * */
package pl.marekbar;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class App extends Application{

    private static Context context;
    public static String TAG = "Przetwarzanie obrazu";
    public static String NAME = "Przetwarzanie obrazu ARM";

    public void onCreate(){
        super.onCreate();
        App.context = getApplicationContext();
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
}
