package pl.marekbar;

import android.content.pm.PackageManager;
import android.hardware.Camera;

public class CameraManager
{
	public static boolean CheckCameraHardware()
	{
		if (App.getContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Camera getCameraInstance()
	{
		try
		{
			if (CameraManager.CheckCameraHardware())
			{
				return Camera.open();
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
