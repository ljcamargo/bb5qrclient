package cam.moozo.qr;

import java.util.Timer;
import java.util.TimerTask;

import cam.aedes.CMainScreen;
import om.ui.Layout;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.UiApplication;

public class MoozoSplash extends CMainScreen {
    public Layout La;
	public UiApplication theApp;
    private CMainScreen next;
    private Timer timer = new Timer();
    int waitms = 2000;

    public MoozoSplash() {
    	UiApplication ui = UiApplication.getUiApplication();
		theApp = (UiApplication) ui;
		next = new MoozoMain();	
		La = new Layout(this);
		Locale.setDefault(Locale.get(Locale.LOCALE_es, null));  
		La.splashScreen(this);
		timer.schedule(new CountDown(), waitms);;
    }
    
    public void dismiss() {
        timer.cancel();
        theApp.popScreen(this);
        theApp.pushScreen(next);
     }
     private class CountDown extends TimerTask {
        public void run() {
           DismissThread dThread = new DismissThread();
           theApp.invokeLater(dThread);
        }
     }
     private class DismissThread implements Runnable {
        public void run() {
           dismiss();
        }
     }
     
     
  }
    

