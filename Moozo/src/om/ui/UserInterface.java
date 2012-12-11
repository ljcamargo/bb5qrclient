package om.ui;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.HttpConnection;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cam.aedes.CDeviceMetrics;
import cam.aedes.CMainScreen;
import cam.async.AsyncDownloadImage;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;


public class UserInterface extends CDeviceMetrics {
		
	
	public UserInterface(CMainScreen c)  {
		super(c);
		InitializeFonts();
	}
	
 	public void InitializeFonts() {

 	}
 	
 	public Font GetFont (String opt1, String opt2, Font previous) {
 		Font newfont = previous;
 		int actual = -1;
 		int pstyle = previous.getStyle();
 		int psize = previous.getHeight();
 		FontFamily fontFamily[] = FontFamily.getFontFamilies();
 		for (int x = 0; x< fontFamily.length; x++) {
			if (fontFamily[x].getName().equalsIgnoreCase(opt1)) {
				actual = x;
				break;
			}
		}
 		if (actual==-1) {
 	 		for (int x = 0; x< fontFamily.length; x++) {
 				if (fontFamily[x].getName().equalsIgnoreCase(opt2)) {
 					actual = x;
 					break;
 				}
 			}
 		}
 		if (actual==-1) {
 			newfont = Font.getDefault().derive(pstyle, psize);
 		} else {
 			newfont = fontFamily[actual].getFont(pstyle, psize);
 		}
 		return newfont;
 	}
 	
	public void plus_font_size(Manager parent) {
		if (parent==null) { return; }
		for (int index = 0; index < parent.getFieldCount(); index++) {
			parent.getField(index);
			if (parent.getField(index) instanceof TextField) {
				TextField tf = (TextField) parent.getField(index);
				int ts = tf.getFont().getHeight();
				int ns = ts;
				if (ts <=14) { ns = 16; }
				if (ts <=16 && ts > 14) { ns = 18; }
				if (ts <=18 && ts > 16) { ns = 20; }
				if (ts <=20 && ts > 18) { ns = 22; }
				if (ts <=22 && ts > 20) { ns = 14; }
				tf.setFont(tf.getFont().derive(tf.getFont().getStyle() , ns));
			}
		}
		
	}
	
	public void main_image_resize() {
		double proportion = (((double) (Display.getWidth()) / 360));
		main_image_size = (int) (proportion*((double) main_image_size));
		small_image_size = (int) (proportion*((double)small_image_size));
	}
	
	public static EncodedImage getImage(String url, Manager parent) throws IOException {
		url += ";deviceside=true";
	    HttpConnection connection = null;
	    InputStream inputStream = null;
			try {
				ConnectionFactory cf = new ConnectionFactory();
				cf.setConnectionTimeout(2500);
				ConnectionDescriptor cd = cf.getConnection(url);
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
	            //Image.createImage(InputStream);
	            httpConn.close();
	            return EncodedImage.createEncodedImage(dataArray, 0, dataArray.length);
				
			} catch (IOException e1) {
				//
			}

	    return null;
	}

	public void AsyncDownloadImage(String url, final BitmapField fImg, final int resizew){
		url += ";deviceside=true";
		final String furl = url;
		EncodedImage ei;
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
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
					DownloadedImage(ei,fImg, resizew); 			
	      		} catch (Exception e1) {
	      			ErrorImage(fImg, e1.toString());
	      		}
			}
		});	
	}

	public InputStream AsyncDownloadDoc(String url, String action){
		InputStream is = null;
		
		try {
			ConnectionFactory cf = new ConnectionFactory();
			cf.setConnectionTimeout(3333);
			cf.setTimeLimit(33000);
			ConnectionDescriptor cd = cf.getConnection(url);
			HttpConnection httpConn;
	        httpConn = (HttpConnection)cd.getConnection();
	        int response = httpConn.getResponseCode();
	        if(response==HttpConnection.HTTP_OK) {
	        	is = httpConn.openInputStream();
	        	//httpConn.close();
			}
	        //httpConn.close();
	        //DebugLabel("RES: "+response,tsmanager);
				
		} catch (IOException e1) {
			//DebugLabel(error_code("unknown"), j.gparent);
		} catch (Exception e) {
			//c.DebugLabel(c.error_code("unknown"), j.gparent);
		}	

		return is;
	}

	public void DownloadedImage(EncodedImage ei, BitmapField fImg) {
		synchronized(UiApplication.getEventLock()) {
	        fImg.setBitmap(ei.getBitmap());

		}
	}

	public void DownloadedImage(EncodedImage ei, BitmapField fImg, int wsize) {
		ei = getScaledImage(ei, wsize);
		synchronized(UiApplication.getEventLock()) {		
	        fImg.setBitmap(ei.getBitmap());
		}
	}
	
	public EncodedImage getScaledImage(EncodedImage ei, int wsize){    
		EncodedImage image =ei;
		if (wsize!=0) {
		    int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		    int currentHeightFixed32 = Fixed32.toFP(image.getHeight());
		    int factor = Fixed32.div(wsize, image.getWidth());
		    int height = Fixed32.mul(image.getHeight(), factor);
		    int scaleXFixed32 = Fixed32.div(currentWidthFixed32,  Fixed32.toFP(wsize));
		    int scaleYFixed32 = Fixed32.div(currentHeightFixed32,  Fixed32.toFP(height));	
		    image = ei.scaleImage32(scaleXFixed32,scaleYFixed32);
		}
	    return image;
	}

	public EncodedImage ResizeToWidth(EncodedImage ei, int wsize) {
		if (wsize!=0) {
			int prew, prey, factor;
			prew = ei.getWidth();
			prey = ei.getHeight();
			factor = wsize / prew;
			ei = ei.scaleImage32(wsize, (prey*factor));
		}
		return ei;
	}

	public void ErrorImage(BitmapField bf, String error) {
		synchronized(UiApplication.getEventLock()) {
			bf.setBitmap(Bitmap.getBitmapResource("img/errorload.png"));
		}
	}
	
	public String GetChildPlainContent(Node myNode) {
		String parsedString = "";
		try {		
			if (myNode.hasChildNodes()) {		
				NodeList subNodes = myNode.getChildNodes();
				for(int c=0;c<subNodes.getLength();c++) {	
					Node innerNode = subNodes.item(c);
					if (innerNode.getNodeName().equalsIgnoreCase("#text")) { 
							parsedString += innerNode.getNodeValue().toString();	
					} 
					if (innerNode.hasChildNodes()) {
						if (innerNode.getFirstChild().getNodeName().equalsIgnoreCase("#text")) {
							parsedString += innerNode.getFirstChild().getNodeValue().toString();		
						}							
					}
				}
			} 

			if (myNode.getNodeName().equalsIgnoreCase("#text")) { parsedString += myNode.getNodeValue().toString(); }
			//parsedString = c.replaceAll(parsedString, "\r", "");
			//parsedString = c.replaceAll(parsedString, "\n", "");
			return parsedString;
			
		} catch (Exception e) { return parsedString; }
	
	}	

	public BitmapField WriteWebImage(final Manager parent, final String url, final int size) {
		final BitmapField fImg = new BitmapField(Bitmap.getBitmapResource("img/cargandosm.png"), BitmapField.FOCUSABLE){
			protected void layout(int width, int height) {
	            setExtent(getBitmapWidth()+3, getBitmapHeight()+3);
	        }
		};
	    parent.add(fImg);
	    try {
	    	UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					new AsyncDownloadImage(url,fImg,size).run();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fImg;	
	}
	
	public LabelField FormatLabel(Manager parent, net.rim.device.api.ui.Font font, final int color, String content) {	
		content = content.trim();

	    LabelField l_Label = new LabelField(content, Field.NON_FOCUSABLE) {
	        public void paint(Graphics g){
	            g.setColor(color);  
	            super.paint(g);
	        }  
	    };
	    l_Label.setMargin(1,1,1,1);
	    l_Label.setPadding(0,0,0,0);
	    l_Label.setFont(font);
	    parent.add(l_Label);
		return l_Label;
	}
	
	public LabelField FocusableFormatLabel(Manager parent, net.rim.device.api.ui.Font font, final int color, String content) {	
		content = content.trim();

	    LabelField l_Label = new LabelField(content, Field.FOCUSABLE) {
	        public void paint(Graphics g){
	            g.setColor(color);  
	            super.paint(g);
	        }  
	    };
	    l_Label.setMargin(1,1,1,1);
	    l_Label.setPadding(0,0,0,0);
	    l_Label.setFont(font);
	    parent.add(l_Label);
		return l_Label;
	}
	
	public BitmapField WriteSimpleImage(Manager parent, String resource, long style, int size, String listener) {
		EncodedImage ei = EncodedImage.getEncodedImageResource(resource);
		EncodedImage ei2 = getScaledImage(ei, size);
		BitmapField fImg = new BitmapField(ei2.getBitmap(), style);	
		parent.add(fImg);
		return fImg;
	}
	
	public TextField WriteParragraph(Manager parent, String text, net.rim.device.api.ui.Font font, long style) {
		String alignedtxt  = text;
		TextField tf = new TextField(style);
		tf.setEditable(false);
		tf.setFont(font);
		alignedtxt = alignedtxt.trim();
		tf.setText(alignedtxt);
		tf.setMargin(10,5,5,5);
		parent.add(tf);  
		return tf;	
	}
}
