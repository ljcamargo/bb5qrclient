package cam.async;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.HttpConnection;

import cam.aedes.CMainScreen;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;

public class AsyncDownloadResource implements Runnable {
	
	InputStream is = null;
	String url;
	String callback;
	CMainScreen cms;
	
	public AsyncDownloadResource(CMainScreen cms, String resurl, String callbackmessage) {
		
		url = resurl;
		callback = callbackmessage;	
		this.cms = cms;

	}

	public void run(){
		
		try {
			ConnectionFactory cf = new ConnectionFactory();
			cf.setConnectionTimeout(3333);
			ConnectionDescriptor cd = cf.getConnection(url);
			HttpConnection httpConn;
            httpConn = (HttpConnection)cd.getConnection();
            int response = httpConn.getResponseCode();
            if(response==HttpConnection.HTTP_OK) {
            	is = httpConn.openInputStream();
            	done(true);
			}
			
		} catch (IOException e1) {
			done(false);
		} catch (Exception e) {
			
			done(false);
		}
		
		
	}
	
	public void done(boolean ok) {
		if (ok) {
			cms.callback_downloaded(is, null);
		} else {
			cms.callback_error(0);
		}
		
	}
	
}
