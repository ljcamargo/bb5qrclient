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
import om.ui.Strings;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;

public class MoozoMain extends CMainScreen {
    public Layout La;
	public UiApplication theApp;
	String model = "/bar/id_bar/table/table_nr/fb/fb_id";
    String param = "/bar/id_bar/table/table_nr/fb/fb_id";
    String baseurl = "http://dev.moozo.com.ar/moozo/webapi/checkin";
    String inviteurl = "http://dev.moozo.com.ar/moozo/webapi/friends";
    String key = "84609bc63adb84151759d496144403b8";
    LabelField barcodeResult;
    ViewFinderScreen vfs;
    Strings strings;
       

    public MoozoMain() {
    	UiApplication ui = UiApplication.getUiApplication();
		theApp = (UiApplication) ui;
		La = new Layout(this);
		strings = new Strings();
		Locale.setDefault(Locale.get(Locale.LOCALE_es, null));  
		La.mainScreen(this);
		ApplicationPermissionsManager manager = ApplicationPermissionsManager.getInstance();
		int current = manager.getPermission(ApplicationPermissions.PERMISSION_RECORDING);
		if (current != ApplicationPermissions.VALUE_ALLOW) {
		    ApplicationPermissions permissions = new ApplicationPermissions();
		    permissions.addPermission(ApplicationPermissions.PERMISSION_RECORDING);
		    manager.invokePermissionsRequest(permissions);
		}
		
		
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
    		System.out.println("is valid, redirecting to:"+baseurl+param);
    		Browser.getDefaultSession().displayPage(baseurl+param);
    	} else {
    		System.out.println("non valid: "+param);
    		UiApplication.getUiApplication().invokeLater (new Runnable() {
    		    public void run() 
    		    {
    		    	Dialog.alert(strings.getString("invalid_code"));
    		    }
    		});
    		
    	}
    }
    
    public boolean paramIsValid(String param) {
    	int locus = param.indexOf("api_key/");
    	int sign = param.indexOf(key);
    	if (locus>0&&sign>0) {
    		return true;
    	} else {
        	return false;
    	}
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
			try {
				synchronized (Application.getEventLock()) {
					UiApplication.getUiApplication().popScreen(ViewFinderScreen.this);
				}
			} catch (Exception e) {
				
			}
		}

		public ViewFinderScreen() {

			Hashtable hints = new Hashtable(1);
			Vector formats = new Vector(1);

			formats.addElement(BarcodeFormat.QR_CODE); 
			hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);			
			
			decoder = new BarcodeDecoder(hints);

			listener = new BarcodeDecoderListener() {

				public void barcodeFailDecoded(Exception ex) {
					System.out.println("RESULT    not working");
				}

				public void barcodeDecoded(String rawText) {
					System.out.println("RESULT    "+rawText);
					param = rawText;
					decodedQR();
				}

				public void barcodeDecodeProcessFinish() {
					terminateScanner();	
				}
			};
			try {
				scanner = new BarcodeScanner(decoder, listener);
				viewFinder = scanner.getViewFinder();
				add(viewFinder);
				scanner.startScan();
			} catch (Exception e) {
				Status.show(e.getMessage());
				terminateScanner();
				e.printStackTrace();
			}
		}
		
		public boolean onClose() {
			terminateScanner();
			return super.onClose();
		}

	}
    
    
}
