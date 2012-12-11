package org.jackpot.device.blackberry.api.barcodelib;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.PopupScreen;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;


public final class BarcodeScanner {
	private BarcodeDecoder _decoder;
	private BarcodeDecoderListener _listener;
	private VideoControl _vc;
	private Player _player;
	private Field _viewFinder;
	private String _encoding;
	private PopupScreen _waitingScreen;

	private BarcodeScanTask task;
	private Timer timer;

	/**
	 * Create the barcode scanner instance
	 * 
	 * @param decoder
	 *            specify the decoder instance
	 * @param listener
	 *            listener for handling events
	 * @throws IOException
	 *             occurred when video support is unavailable
	 * @throws MediaException
	 *             occurred when video cannot be started
	 */
	public BarcodeScanner(BarcodeDecoder decoder,
			BarcodeDecoderListener listener) throws IOException, MediaException {
		_decoder = decoder;
		_listener = listener;
		init();

	}

	/*
	 * initialize resource
	 */
	private void init() throws IOException, MediaException {
		 String encs = System.getProperty("video.snapshot.encodings");
		 System.out.println("ENCODINGS_____"+encs);
		_encoding = "encoding=jpeg&width=640&height=480&quality=normal";
		_player = Manager.createPlayer("capture://video?encoding=jpeg&width=640&height=480&quality=normal");
		_player.realize();
		_vc = (VideoControl) _player.getControl("VideoControl");
		_viewFinder = (Field) _vc.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
		_vc.setDisplayFullScreen(true);
        _vc.setVisible(true);
        _player.start();		
	}

	public Player getPlayer() {
		return _player;
	}

	public VideoControl getVideoControl() {
		return _vc;
	}

	public Field getViewFinder() {
		return _viewFinder;
	}

	public void startScan() throws MediaException {
		setupDefaultEncoding();
		if (_vc != null) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					_viewFinder.setFocus();

				}
			});
			_vc.setVisible(true);

			task = new BarcodeScanTask();
			timer = new Timer();
			timer.schedule(task, 0, 1250); // 2 seconds once

		} else {
			throw new MediaException("Video Control is not initialized");
		}
	}

	public void stopScan() throws MediaException {
		if (timer != null) {
			timer.cancel(); // stop the timer
		}
		_player.stop();
		_player.deallocate();
	}

	public void setupDefaultEncoding() {

	}

	public void setupEncoding(String encodingType) {
		_encoding = encodingType;
	}
	
	public void readNow() {
		if (timer != null) {
			timer.cancel(); // stop the timer
		}
		System.out.println("--------------------------------->TimerTask run... preparing Capture");
		Bitmap bmpScreenshot = new Bitmap(Display.getWidth(),Display.getHeight());
		try {
			System.out.println("--------------------------------->Capturing First Method");
			byte[] rawImage = _vc.getSnapshot(null);
			Bitmap newbitmap = Bitmap.createBitmapFromBytes(rawImage, 0,rawImage.length, 1);
			bmpScreenshot = newbitmap;
		} catch (Exception e1) {
			System.out.println("--------------------------------->Failed First Method, second attempted");
			Display.screenshot(bmpScreenshot);
			e1.printStackTrace();
		}      
		System.out.println("--------------------------------->Captured");
		MultiFormatReader reader = new MultiFormatReader();
		LuminanceSource source = new BitmapLuminanceSource(bmpScreenshot);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		Hashtable hints = null;
		if (_decoder != null) {
			hints = _decoder._hints;
		}

		if (hints == null) {
			hints = new Hashtable(1);
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		}

		Result result;
		String rawText = "";
		try {
			System.out.println("--------------------------------->Trying DECODE");
			result = reader.decode(bitmap, hints);
			System.out.println("--------------------------------->QR Code DECODED");
			rawText = result.getText();
			_listener.barcodeDecoded(rawText);
			_listener.barcodeDecodeProcessFinish();
		} catch (NotFoundException e) {
			System.out.println("--------------------------------->FAIL TO DECODE!!!!");
			_listener.barcodeFailDecoded(e);
		}
	}

	final class BarcodeScanTask extends TimerTask {

		public void run() {

				System.out.println("--------------------------------->TimerTask run... preparing Capture");
				Bitmap bmpScreenshot = new Bitmap(Display.getWidth(),Display.getHeight());
				try {
					System.out.println("--------------------------------->Capturing First Method");
					byte[] rawImage = _vc.getSnapshot(null);
					Bitmap newbitmap = Bitmap.createBitmapFromBytes(rawImage, 0,rawImage.length, 1);
					bmpScreenshot = newbitmap;
				} catch (Exception e1) {
					System.out.println("--------------------------------->Failed First Method, second attempted");
					Display.screenshot(bmpScreenshot);
					e1.printStackTrace();
				}      
				//saveScreenShot(bmpScreenshot);
				System.out.println("--------------------------------->Captured");

				MultiFormatReader reader = new MultiFormatReader();

				// creating luminance source
				LuminanceSource source = new BitmapLuminanceSource(
						bmpScreenshot);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
						source));

				Hashtable hints = null;
				if (_decoder != null) {
					hints = _decoder._hints;
				}

				if (hints == null) {
					hints = new Hashtable(1);
					//hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
				}

				Result result;
				String rawText = "";
				try {
					System.out.println("--------------------------------->Trying DECODE");
					result = reader.decode(bitmap, hints);
					System.out.println("--------------------------------->QR Code DECODED");
					rawText = result.getText();
					_listener.barcodeDecoded(rawText);
					_listener.barcodeDecodeProcessFinish();
				} catch (NotFoundException e) {
					System.out.println("--------------------------------->FAIL TO DECODE!!!!");
					_listener.barcodeFailDecoded(e);
				}

		}
	}
	
	public void saveScreenShot(Bitmap currentScreen) {  
        try {  
            Date date = new Date(System.currentTimeMillis());  
            String name = date.toString().replace(' ', '-');  
            name = name.replace(':', '-');  
            System.out.println(name);  
            FileConnection fconn = (FileConnection) Connector.open(System  
                    .getProperty("fileconn.dir.photos")  
                    + name + ".png", Connector.READ_WRITE);  
  
            if (!fconn.exists()) {  
                fconn.create();  
            }  
  
            PNGEncodedImage fullImage = PNGEncodedImage  
                    .encode(currentScreen);  
  
            OutputStream os = fconn.openOutputStream();  
            os.write(fullImage.getData());  
  
            os.close();  
            fconn.close();  
        } catch (Exception e) {  
            System.out.println("Output file error: " + e.getMessage());  
        }  
  
    }  
}