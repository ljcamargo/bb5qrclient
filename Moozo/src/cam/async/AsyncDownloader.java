package cam.async;

import java.io.InputStream;

import javax.microedition.io.HttpConnection;

import cam.aedes.CMainScreen;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.ui.Field;


public class AsyncDownloader implements Runnable {
	
	InputStream is = null;
	String url;
	String[] callback;
	CMainScreen c;
	Field f;
	String es;
	
	public AsyncDownloader(CMainScreen c, String resurl, String[] callbackmessage) {
		this.c = c;
		url = resurl;
		callback = callbackmessage;	
		
	}

	public void run(){
		try {
			ConnectionFactory cf = new ConnectionFactory();
			//cf.setConnectionTimeout(5000);
			//cf.setTimeLimit(20000);
			ConnectionDescriptor cd = cf.getConnection(url);
			HttpConnection httpConn;
            httpConn = (HttpConnection)cd.getConnection();
            int response = httpConn.getResponseCode();
            if(response==HttpConnection.HTTP_OK) {
            	is = httpConn.openInputStream();
            	done(true);
			}
		} catch (Exception e) {
			es = e.toString();
			done(false);
		}
		
		
	}
	
	public void done(boolean ok) {
		if (ok) {
			c.callback_downloaded(is, callback);
		} else {

		}
		
	}
	
}