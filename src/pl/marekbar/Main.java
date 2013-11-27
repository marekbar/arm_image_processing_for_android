package pl.marekbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class Main extends Activity
{
	private static final int SELECT_PHOTO = 100;
	private Bitmap image;
	private ImageView display;
	static
	{
		System.loadLibrary("armimageprocess");
	}

	public native byte[] BitmapToGrayscale(byte[] bitmap, int bytes, int bytesPerPixel);
	public native byte[] BitmapDetectEdges(byte[] bitmap, int imageWidth, int imageHeight, int bytesPerPixel);
	public native byte[] BitmapColorManipulate(byte[] bitmap, int bytes, int bytesPerPixel, int option);
	public native byte[] BitmapUpsideDown(byte[] bitmap, int bytesPerPixe, int width, int height);
	public native byte[] BitmapAddBorder(byte[] bitmap, int[] args);
	public native int Divide(int a, int b);
	public native int SizeOfJByte();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		InitializeInterface();
		App.info("Rozmiar jbyte to: " + String.valueOf(this.SizeOfJByte()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void SelectPhoto()
	{
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	private void GetLenaBack()
	{
		try
		{
			image = BitmapFactory.decodeStream(getAssets().open("Lena.png"));
		} catch (IOException e)
		{
			error("Nie za³adowano domyœlnego obrazka - Lena");
		}
		display.setImageBitmap(image);
	}

	private void ConvertImageToGrayscale()
	{
		if(image == null)
		{
			GetLenaBack();
			info("Nie byÅ‚o obrazu, ustawiono domyÅ›lny.");
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		int bytesLength = image.getByteCount();
		int pixels = image.getWidth()*image.getHeight();
		int bytesPerByte = bytesLength / pixels;
		App.info("Konwersja do skali szaroœci");
		App.info("Szerokoœæ: " + String.valueOf(image.getWidth()));
		App.info("Wysokoœæ: " + String.valueOf(image.getHeight()));
		App.info("Liczba bajtów w pikselu: " + String.valueOf(bytesPerByte));
		ByteBuffer result = ByteBuffer.wrap(this.BitmapToGrayscale(bb.array(), pixels, bytesPerByte));
		App.info("Konwersja zakoñczona");
		image.copyPixelsFromBuffer(result);
		display.setImageBitmap(image);
		
	}
	
	public void ColorManipulate(ColorManipulateOption option)
	{
		if(image == null)
		{
			GetLenaBack();
			info("Nie by³o obrazu, ustawiono domyœlny.");
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		int bytesLength = image.getByteCount();
		int pixels = image.getWidth()*image.getHeight();
		int bytesPerPixel = bytesLength / pixels;
		ByteBuffer result = ByteBuffer.wrap(this.BitmapColorManipulate(bb.array(), pixels, bytesPerPixel, option.ordinal()));
		image.copyPixelsFromBuffer(result);
		display.setImageBitmap(image);
	}
	
	public void BitmapDetecEdges()
	{
		if(image == null)
		{
			GetLenaBack();
			info("Nie by³o obrazu, ustawiono domyœlny.");
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		int bytesLength = image.getByteCount();
		int pixels = image.getWidth()*image.getHeight();
		int bytesPerPixel = bytesLength / pixels;
		ByteBuffer result = ByteBuffer.wrap(this.BitmapDetectEdges(bb.array(), image.getWidth(), image.getHeight(), bytesPerPixel));
		image.copyPixelsFromBuffer(result);
		display.setImageBitmap(image);
	}

	public void UpsideDown()
	{
		if(image == null)
		{
			GetLenaBack();
			info("Nie by³‚o obrazu, ustawiono domyÅ›lny.");
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		int bytesLength = image.getByteCount();
		int pixels = image.getWidth()*image.getHeight();
		int bytesPerPixel = bytesLength / pixels;
		App.info("Szerokoœæ: " + String.valueOf(image.getWidth()));
		App.info("Wysokoœæ: " + String.valueOf(image.getHeight()));
		App.info("Liczba bajtów w pikselu: " + String.valueOf(bytesPerPixel));
		ByteBuffer result = ByteBuffer.wrap(this.BitmapUpsideDown(bb.array(), bytesPerPixel, image.getWidth(), image.getHeight()));
		image.copyPixelsFromBuffer(result);
		display.setImageBitmap(image);
	}
	public void AddFrame()
	{
		String wynik = "\n15/3 = " + String.valueOf(Divide(15,3)) + 
				"\n15/0 = " + String.valueOf(Divide(15,0)) + 
				"\n5/3 = " + String.valueOf(Divide(5,3)) + 
				"\n0/3 = " + String.valueOf(Divide(0,3)) +
				"\n0/0 = " + String.valueOf(Divide(0,0)) +
				"\n1/1 = " + String.valueOf(Divide(1,1)) +
				"\n3/3 = " + String.valueOf(Divide(3,3));
		info(wynik);
		Log.i("App", wynik);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent)
	{
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode)
		{
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK)
			{
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream = null;
				try
				{
					imageStream = getContentResolver().openInputStream(
							selectedImage);
					image = BitmapFactory.decodeStream(imageStream);
					display.setImageBitmap(image);
				} catch (FileNotFoundException e)
				{
					error("Nie odnalzeiono pliku.");
				}
			}
		}
	}

	private void error(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	private void info(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.imageFromCamera:
			TurnCameraOn();
			return true;
		case R.id.DefaultPhoto:
			GetLenaBack();
			return true;
		case R.id.imageLeaveRed:
			ColorManipulate(ColorManipulateOption.OnlyRed);
			return true;
		case R.id.imageLeaveGreen:
			ColorManipulate(ColorManipulateOption.OnlyGreen);
			return true;
		case R.id.imageLeaveBlue:
			ColorManipulate(ColorManipulateOption.OnlyBlue);
			return true;
		case R.id.imageNoBlue:
			ColorManipulate(ColorManipulateOption.NoBlue);
			return true;
		case R.id.imageNoRed:
			ColorManipulate(ColorManipulateOption.NoRed);
			return true;
		case R.id.imageNoGreen:
			ColorManipulate(ColorManipulateOption.NoGreen);
			return true;
		case R.id.upsideDown:
			UpsideDown();
			return true;
		case R.id.imgToGray:
			ConvertImageToGrayscale();
			return true;
		case R.id.imageEdges:
			BitmapDetecEdges();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void InitializeInterface()
	{
		setContentView(R.layout.activity_main);
		display = (ImageView) findViewById(R.id.display);
		GetLenaBack();
	}
	
	private void TurnCameraOn()
	{
		Intent cam = new Intent(this, CameraActivity.class);
		startActivity(cam);
	}

}
