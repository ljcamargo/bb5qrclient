package cam.aedes;

import cam.async.AsyncDownloadImage;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.MainScreen;

public class CInterface extends MainScreen {
	
	
	public void WriteSpan(Manager parent, long style) {
		SeparatorField sf = new SeparatorField(SeparatorField.LINE_HORIZONTAL | SeparatorField.USE_ALL_WIDTH);
		sf.setPadding(0,0,0,0);
		parent.add(sf);
		SeparatorField sf2 = new SeparatorField(SeparatorField.LINE_HORIZONTAL | SeparatorField.USE_ALL_WIDTH);
		sf2.setPadding(0,0,0,0);
		parent.add(sf2);
		SeparatorField sf3 = new SeparatorField(SeparatorField.LINE_HORIZONTAL | SeparatorField.USE_ALL_WIDTH);
		sf3.setPadding(0,0,0,0);
		parent.add(sf3);
		SeparatorField sf4 = new SeparatorField(SeparatorField.LINE_HORIZONTAL | SeparatorField.USE_ALL_WIDTH);
		sf4.setPadding(0,0,0,0);
		parent.add(sf4);
		SeparatorField sf5 = new SeparatorField(SeparatorField.LINE_HORIZONTAL | SeparatorField.USE_ALL_WIDTH);
		sf5.setPadding(0,0,2,0);
		parent.add(sf5);

	}
	
	public LabelField WriteFormatLabel(Manager parent, net.rim.device.api.ui.Font font, final int color, String content) {	
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

	public void WriteErrorImage(BitmapField bf, String error) {
		synchronized(UiApplication.getEventLock()) {
			bf.setBitmap(Bitmap.getBitmapResource("img/cargandosm.png"));
		}
	}
	
	public void WriteDownloadedImage(EncodedImage ei, BitmapField fImg, int wsize) {
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
		    if (currentWidthFixed32>currentHeightFixed32) {
			    int factor = Fixed32.div(wsize, image.getWidth());
			    int height = Fixed32.mul(image.getHeight(), factor);
			    int scaleXFixed32 = Fixed32.div(currentWidthFixed32,  Fixed32.toFP(wsize));
			    int scaleYFixed32 = Fixed32.div(currentHeightFixed32,  Fixed32.toFP(height));	
			    image = ei.scaleImage32(scaleXFixed32,scaleYFixed32);
		    } else {
			    int factor = Fixed32.div(wsize, image.getHeight());
			    int width = Fixed32.mul(image.getWidth(), factor);
			    int scaleXFixed32 = Fixed32.div(currentWidthFixed32,  Fixed32.toFP(width));
			    int scaleYFixed32 = Fixed32.div(currentHeightFixed32,  Fixed32.toFP(wsize));
		    	image = ei.scaleImage32(scaleXFixed32,scaleYFixed32);
		    }

		}
	    return image;
	}
	
	
	public BitmapField WriteWebImage(final Manager parent, final String url, int size) {
		BitmapField fImg = new BitmapField(Bitmap.getBitmapResource("img/cargandosm.png"), BitmapField.FOCUSABLE){
			protected void layout(int width, int height) {
	            setExtent(getBitmapWidth()+3, getBitmapHeight()+3);
	        }
		};
	    parent.add(fImg);
	    try {
	    	new AsyncDownloadImage(url,fImg,size).run();
	    	//AsyncDownloadImage(url,fImg, size);

		} catch (Exception e) {
			//LabelField l_A = new LabelField(e.toString(), Field.FOCUSABLE);
			//l_A.setFont(xsmall);
			//parent.add(l_A);
			e.printStackTrace();
		}
		return fImg;	
	}

}
