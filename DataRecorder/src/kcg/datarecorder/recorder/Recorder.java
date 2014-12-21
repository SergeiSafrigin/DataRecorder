package kcg.datarecorder.recorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import kcg.datarecorder.location.Point3d;
import kcg.datarecorder.main.Config;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Recorder implements RecorderDataUpdater {

	private static final String TAG = "Recorder";
	private Context context;
	private Data data;
	private int finishSoundResource;

	public Recorder(Context context1, Config config, int finishSoundResource) {
		context = context1;
		this.finishSoundResource = finishSoundResource;
		data = new Data(config);
	}

	public void addData(long time, byte[] frame, Point3d location, ArrayList<WifiData> wifiDataList, float yaw,
			float pitch, float roll, float pressure) {
		
		int width = data.getConfig().getPreviewWidth();
		int height = data.getConfig().getPreviewHeight();
		YuvImage yuvImage= new YuvImage(frame, ImageFormat.NV21, width, height, null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		yuvImage.compressToJpeg(new Rect(0, 0, width, height),80, baos);
		byte jpegFrame[] = baos.toByteArray();
		
		data.addFrame(new FrameData(time, jpegFrame, location, wifiDataList, yaw, pitch, roll, pressure));
	}

	public void start() {
		data.setStartTime(System.currentTimeMillis());
	}

	public void stop() {
		if (data.size() <= 0) {
			return;
		}

		File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+"DataRecorder");
		
		if(!mediaStorageDir.exists() && !mediaStorageDir.isDirectory()) {
	        // create empty directory
	        if (mediaStorageDir.mkdirs()){
	            Log.i("CreateDir","App dir created");
	        }else {
	            Log.e("CreateDir","Unable to create app dir!");
	        }
	    }
		
		File file = new File(mediaStorageDir.getPath() + File.separator + data.getStartTime());
		
		ObjectOutputStream out;
		
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(data);
			out.close();
			
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {	
				@Override
				public void run() {
					Toast.makeText(context, "File Saved!", Toast.LENGTH_LONG).show();
				}
			});
			
			MediaPlayer mPlayer = MediaPlayer.create(context, finishSoundResource);
			mPlayer.start();
			Thread.sleep(2000);
			mPlayer.release();
		} catch (IOException | InterruptedException ioexception) {
			Log.e("Recorder", ioexception.toString());
		}
		data.clear();
	}
}
