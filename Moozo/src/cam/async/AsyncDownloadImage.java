package cam.async;

import java.io.InputStream;

import javax.microedition.io.HttpConnection;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;
import cam.aedes.CInterface;

public class AsyncDownloadImage extends Thread {
	
	CInterface ci = new CInterface();
	final String furl;
	EncodedImage ei;
	BitmapField fImg;
	int resizew;
	
	public AsyncDownloadImage(String url, final BitmapField _fImg, final int _resizew){
		url += ";deviceside=true";
		furl = url;
		fImg = _fImg;
		resizew = _resizew;

	}
	
	public void run(){
		  HttpConnection connection = null;
	      InputStream inputStream = null;
		  try {
			ConnectionFactory cf = new ConnectionFactory();
			cf.setConnectionTimeout(8333);
			ConnectionDescriptor cd = cf.getConnection(furl);
			HttpConnection httpConn;
            httpConn = (HttpConnection)cd.getConnection();
            inputStream = httpConn.openInputStream();
            StringBuffer rawResponse = new StringBuffer();
            byte[] responseData = new byte[10000];
            int length = 0;
            while (-1 != (length = inputStream.read(responseData))) {
                rawResponse.append(new String(responseData, 0, length));
            }
            String result = rawResponse.toString();
            byte[] dataArray = result.getBytes(); 
            EncodedImage ei = EncodedImage.createEncodedImage(dataArray, 0, dataArray.length);
            httpConn.close();
            ci.WriteDownloadedImage(ei,fImg, resizew);
			
		} catch (Exception e1) {
			ci.WriteErrorImage(fImg, e1.toString());

		}
	  }

}
