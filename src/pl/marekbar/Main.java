package pl.marekbar;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas.EdgeType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Main extends Activity
{
	static
	{
		System.loadLibrary("armimageprocess");
	}

	public native byte[] BitmapToGrayscale(byte[] bitmap, int bytes, int bytesPerPixel);
	public native byte[] BitmapDetectEdges(byte[] bitmap, int imageWidth, int imageHeight, byte[] gray, int option);
	public native byte[] BitmapColorManipulate(byte[] bitmap, int bytes, int bytesPerPixel, int option);
	public native int Divide(int a, int b);
	public native byte[] BitmapMirrorX(byte[] bitmap, int bytesPerPixel, int imageWidth, int imageHeight);
	public native byte[] BitmapMirrorY(byte[] bitmap, int bytesPerPixel, int imageWidth, int imageHeight);
	
	private static final int ACTION_TAKE_PHOTO_BIG = 1;
	private String PhotoPath;
	private Bitmap image;
	private ImageView display;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		InitializeInterface();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void GetLenaBack()
	{
		try
		{
			image = BitmapFactory.decodeStream(getAssets().open("Lena.png"));
		} 
		catch (IOException e)
		{
			App.error(getString(R.string.no_default_photo));
		}
		display.setImageBitmap(image);
	}

	private void ConvertImageToGrayscale()
	{
		if(image == null)
		{
			GetLenaBack();
			App.info(getString(R.string.no_default_photo));
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		
		int bytesLength = image.getByteCount();
		int pixels = image.getWidth()*image.getHeight();
		int bytesPerByte = bytesLength / pixels;

		ByteBuffer result = ByteBuffer.wrap(BitmapToGrayscale(bb.array(), pixels, bytesPerByte));

		image.copyPixelsFromBuffer(result);
		display.setImageBitmap(image);
		
	}
	
	public void ColorManipulate(ColorManipulateOption option)
	{
		if(image == null)
		{
			GetLenaBack();
			App.info(getString(R.string.no_default_photo));
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		
		int bytesLength = image.getByteCount();
		int pixels = image.getWidth()*image.getHeight();
		int bytesPerPixel = bytesLength / pixels;
		
		ByteBuffer result = ByteBuffer.wrap(BitmapColorManipulate(bb.array(), pixels, bytesPerPixel, option.ordinal()));
		
		image.copyPixelsFromBuffer(result);
		display.setImageBitmap(image);
	}

	public void BitmapDetecEdges(EdgeDetectionOption option)
	{
		try
		{
		if(image == null)
		{
			GetLenaBack();
			App.info(getString(R.string.no_default_photo));
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		
		int w = image.getWidth();
		int h = image.getHeight();
		ByteBuffer gg = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(gg);
		
		bb = ByteBuffer.wrap(BitmapDetectEdges(bb.array(), w, h, gg.array(), option.ordinal()));
		
		image.copyPixelsFromBuffer(bb);
		display.setImageBitmap(image);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void MirrorX()
	{
		MirrorXY(false);
	}
	
	public void MirrorY()
	{
		MirrorXY(true);
	}
	
	public void MirrorXY(boolean isY)
	{
		if(image == null)
		{
			GetLenaBack();
			App.info(getString(R.string.no_default_photo));
		}
		ByteBuffer bb = ByteBuffer.allocate(image.getByteCount());
		image.copyPixelsToBuffer(bb);
		int bytesPerPixel = image.getByteCount() / (image.getWidth() * image.getHeight());
		if(isY)
		{
			image.copyPixelsFromBuffer(ByteBuffer.wrap(BitmapMirrorY(bb.array(), bytesPerPixel, image.getWidth(), image.getHeight())));
		}
		else
		{
			image.copyPixelsFromBuffer(ByteBuffer.wrap(BitmapMirrorX(bb.array(), bytesPerPixel, image.getWidth(), image.getHeight())));
			App.info("Odbicie wzglêdem osi X");
		}
		display.setImageBitmap(image);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.takePhoto:
			this.TakePhoto();
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
		case R.id.imgToGray:
			ConvertImageToGrayscale();
			return true;
		case R.id.edPrevit:
			BitmapDetecEdges(EdgeDetectionOption.Previtt);
			return true;
		case R.id.edSobel:
			BitmapDetecEdges(EdgeDetectionOption.Sobel);
			return true;
		case R.id.imageMirroX:
			this.MirrorX();
			return true;
		case R.id.imageMirroY:
			this.MirrorY();
			return true;
		case R.id.upsideDown:
			this.MirrorX();this.MirrorY();return true;
		case R.id.saveWork:
			if(!App.SaveToGallery(image))
			{
				App.tost(getString(R.string.photo_saved_failed));
			}
			else
			{
				App.tost(getString(R.string.photo_saved));
			}
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
	
	private void TakePhoto()
	{
		dispatchTakePictureIntent(ACTION_TAKE_PHOTO_BIG);
	}
	
	private File setUpPhotoFile() throws IOException
	{
		
		File f = App.createImageFile();
		PhotoPath = f.getAbsolutePath();
		return f;
	}

	private void setPicture()
	{
		int targetW = display.getWidth();
		int targetH = display.getHeight();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(PhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) 
		{
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		image = BitmapFactory.decodeFile(PhotoPath, bmOptions);
		
		display.setImageBitmap(image);
		display.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case ACTION_TAKE_PHOTO_BIG:
		{
			if (resultCode == RESULT_OK)
			{
				if (PhotoPath != null) 
				{
					setPicture();
					App.galleryAddPic(PhotoPath);
					PhotoPath = null;
				}
			}
			break;
		}
		} 
	}
	
	private void dispatchTakePictureIntent(int actionCode)
	{

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode)
		{
		case ACTION_TAKE_PHOTO_BIG:
			File f = null;
			
			try
			{
				f = setUpPhotoFile();
				PhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				setPicture();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				f = null;
				PhotoPath = null;
			}
			break;

		default:
			break;			
		}

		startActivityForResult(takePictureIntent, actionCode);
	}
}
