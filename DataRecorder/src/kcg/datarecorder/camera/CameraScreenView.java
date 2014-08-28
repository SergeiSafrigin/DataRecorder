package kcg.datarecorder.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import kcg.core.ImageConvert;
import kcg.core.ImageRotate;
import kcg.core.bitmap.BitmapScale;
import kcg.datarecorder.main.Config;

public class CameraScreenView extends View {
	private Bitmap bitmapFrame;
	private Bitmap scaledBitmap;
	
	private Config config;
	private int[] container;
	private Paint paint;
	
	private int height, width;
	private int previewHeight, previewWidth;
	

	public CameraScreenView(Context context, Config config) {
		super(context);
		this.config = config;
		
		init(context);
	}

	private void init(Context context) {
		paint = new Paint();
		previewHeight = config.getPreviewHeight();
		previewWidth = config.getPreviewWidth();
		bitmapFrame = Bitmap.createBitmap(previewHeight, previewWidth, Bitmap.Config.ARGB_4444);
		container = new int[previewHeight * previewWidth];
	}

	public void draw(Canvas canvas) {
		if (scaledBitmap != null) {
			canvas.drawBitmap(scaledBitmap, canvas.getWidth() / 2 - scaledBitmap.getWidth() / 2,
					canvas.getHeight() / 2 - scaledBitmap.getHeight() / 2, paint);
		}
	}

	protected void onMeasure(int width, int height) {
		width = MeasureSpec.getSize(width);
		height = MeasureSpec.getSize(height);
		setMeasuredDimension(width, height);
		super.onMeasure(width, height);
	}

	public void setFrame(byte[] frame) {
		boolean flip = false;
		int rotation;
		if (config.getCamera() == Config.Camera.BACK) {
			rotation = 90;
		} else {
			rotation = -90;
			flip = true;
		}
		
		if (previewHeight != config.getPreviewHeight() || previewWidth != config.getPreviewWidth()) {
			config.setPreviewHeight(previewHeight);
			config.setPreviewWidth(previewWidth);
			bitmapFrame = Bitmap.createBitmap(previewHeight, previewWidth, Bitmap.Config.ARGB_4444);
			container = new int[previewHeight * previewWidth];
		}
		
		container = ImageConvert.NV21TORGB(frame, container.clone(), previewWidth, previewHeight);
		
		try {
			container = ImageRotate.rotate(container, container.clone(), previewWidth, previewHeight, rotation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (flip)
			container = ImageRotate.flip(container, (int[]) container.clone(), previewHeight, previewWidth);
		
		bitmapFrame = ImageConvert.ArrayToBitmap(container.clone(), bitmapFrame, previewHeight, previewWidth);
		scaledBitmap = BitmapScale.scaleToFit(bitmapFrame, width, height);
		invalidate();
	}
	
	public void onResume() {
		previewHeight = config.getPreviewHeight();
		previewWidth = config.getPreviewWidth();
		bitmapFrame = Bitmap.createBitmap(previewHeight, previewWidth, Bitmap.Config.ARGB_4444);
		container = new int[previewHeight * previewWidth];
	}
}
