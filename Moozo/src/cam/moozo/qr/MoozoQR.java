package cam.moozo.qr;

import cam.moozo.qr.MoozoMain.ViewFinderScreen;
import net.rim.device.api.ui.UiApplication;

public class MoozoQR extends UiApplication {
	
	public ViewFinderScreen vfs;
	
    public static void main( String[] args ) {
        MoozoQR theApp = new MoozoQR();
        theApp.enterEventDispatcher();
    }

    public MoozoQR() {
        pushScreen( new MoozoSplash() );
    }
    
    public void deactivate() {
    	System.out.println("   _____________SLEEPING APP");
    	if (vfs!=null) {
    		vfs.terminateScanner();
    	}
    }
    
    public void susbcribeScanner(ViewFinderScreen vfs) {
    	this.vfs = vfs;
    }
}
