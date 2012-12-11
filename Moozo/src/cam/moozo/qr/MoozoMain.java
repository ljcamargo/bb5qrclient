package cam.moozo.qr;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.media.MediaException;

import org.jackpot.device.blackberry.api.barcodelib.BarcodeDecoder;
import org.jackpot.device.blackberry.api.barcodelib.BarcodeDecoderListener;
import org.jackpot.device.blackberry.api.barcodelib.BarcodeScanner;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import cam.aedes.CMainScreen;
import om.ui.Layout;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;

public class MoozoMain extends CMainScreen {
    public Layout La;
	public UiApplication theApp;
    String param = "/bar/id_bar/table/table_nr/fb/fb_id";
    String baseurl = "http://dev.moozo.com.ar/moozo/webapi";
    String inviteurl = "http://dev.moozo.com.ar/moozo/webapi/friends";
    LabelField barcodeResult;
    ViewFinderScreen vfs;
       

    public MoozoMain() {
    	UiApplication ui = UiApplication.getUiApplication();
		theApp = (UiApplication) ui;
		La = new Layout(this);
		Locale.setDefault(Locale.get(Locale.LOCALE_es, null));  
		La.mainScreen(this);
		
    }
   
    public void action_callback(String action, String specific, String param) {
    	System.out.println("action received___");
		if (action.equalsIgnoreCase("qr_start")) {
			startQR();
		}
		if (action.equalsIgnoreCase("web_friends")) {
			webInviteFriends();
		}	
	}
    
    public void webInviteFriends() {
    	System.out.println("Invoke Browser Invite Friends");
    	Browser.getDefaultSession().displayPage(inviteurl);
    }
    
    public void startQR() {
    	System.out.println("QR start_____");
    	vfs = new ViewFinderScreen();
    	((MoozoQR)theApp).susbcribeScanner(vfs);
    	UiApplication.getUiApplication().pushScreen(vfs);
    }
    
    public void decodedQR() {
    	System.out.println("if valid, goto: "+baseurl+param);
    	if (paramIsValid(param)) {
    		Browser.getDefaultSession().displayPage(baseurl+param);
    	} else {
    		
    	}
    }
    
    public boolean paramIsValid(String param) {
    	if ((param.indexOf("table/")>=0)&&(param.indexOf("table/"))>0) {
    		return true;
    	}
    	return false;
    }
    
    public final class ViewFinderScreen extends MainScreen {
		BarcodeScanner scanner;
		BarcodeDecoder decoder;
		BarcodeDecoderListener listener;
		Field viewFinder;
		
	    public void close() {
	        System.out.println("OBSCURED");
	        	terminateScanner();
	    }
	    
	    
		public void terminateScanner() {
			try {
				scanner.stopScan();
			} catch (MediaException e) {
				e.printStackTrace();
			}
			/*
			synchronized (Application.getEventLock()) {
				UiApplication.getUiApplication().popScreen(ViewFinderScreen.this);
			}
			*/
		}

		public ViewFinderScreen() {

			Hashtable hints = new Hashtable(1);
			Vector formats = new Vector(1);

			formats.addElement(BarcodeFormat.QR_CODE); 
			hints.put(DecodeHintType.TRY_HARDER, formats);
			
			
			decoder = new BarcodeDecoder(hints);

			listener = new BarcodeDecoderListener() {

				public void barcodeFailDecoded(Exception ex) {
					System.out.println("RESULT    not working");
				}

				public void barcodeDecoded(String rawText) {
					System.out.println("RESULT    "+rawText);
					param = rawText;
					synchronized (Application.getEventLock()) {
						decodedQR();
					}
				}

				public void barcodeDecodeProcessFinish() {
					try {
						scanner.stopScan();
						
					} catch (MediaException e) {
						e.printStackTrace();
					}
					
					try {
						synchronized (Application.getEventLock()) {
							UiApplication.getUiApplication().popScreen(ViewFinderScreen.this);
						}
					} catch (Exception e) {
						
					}
					
				}
			};
			try {
				scanner = new BarcodeScanner(decoder, listener);
				viewFinder = scanner.getViewFinder();
				//scanner.getVideoControl().setDisplayFullScreen(true);
				add(viewFinder);
				scanner.startScan();
			} catch (Exception e) {
				Status.show(e.getMessage());
				terminateScanner();
				e.printStackTrace();
			}
		}

		protected boolean navigationClick(int arg0, int arg1) {
			// TODO Auto-generated method stub
			try {
				scanner.stopScan();
			} catch (MediaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.close();
			decodedQR();
			return true;
		}

		public boolean onClose() {
			terminateScanner();
			return super.onClose();
		}

	}
    
    
}
