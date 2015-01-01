package kcg.datarecorder.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PhotoHandler implements PictureCallback {

	private Context mContext;
	
	public PhotoHandler(Context context) {
		mContext = context;
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		File pictureFileDir = getDestDir();

	    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

	      Log.d("SMS:", "Can't create directory to save image.");
	      Toast.makeText(mContext, "Can't create directory to save image.",
	          Toast.LENGTH_LONG).show();
	      return;

	    }

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	    String date = dateFormat.format(new Date());
	    String photoFile = "Picture_" + date + ".jpg";

	    String filename = pictureFileDir.getPath() + File.separator + photoFile;

	    File pictureFile = new File(filename);

	    try {
	      FileOutputStream fos = new FileOutputStream(pictureFile);
	      fos.write(data);
	      fos.close();
	      Toast.makeText(mContext, "New Image saved:" + photoFile,
	          Toast.LENGTH_LONG).show();
	    } catch (Exception error) {
	      Log.d("SMS:", "File" + filename + "not saved: "
	          + error.getMessage());
	      Toast.makeText(mContext, "Image could not be saved.",
	          Toast.LENGTH_LONG).show();
	    }
	  }

	  private File getDestDir() {
	    File sdDir = Environment
	      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    return new File(sdDir, "CameraPreviewTest");
	  }

}
