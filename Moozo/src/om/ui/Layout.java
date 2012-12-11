package om.ui;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import cam.aedes.CInterface;
import cam.aedes.CMainScreen;

public class Layout extends UserInterface {
	
	UiApplication theApp;
	
	FontFamily fontFamily[] = FontFamily.getFontFamilies();

		
	Background bkg_splash = null;
	Background bkg_main = null;
	Bitmap splash = null;
	Bitmap main = null;
	Bitmap btn = null;
	String logo = "img/logo.png";
	String button = "img/button.png";
	String button_on = "img/button_on.png";
	String friends = "img/invite.png";
	String friends_on = "img/invite_on.png";
	String defSplash = "img/splash_360x480.png";
	String blank = "img/blank.png";
	
	

	public Layout(CMainScreen c) {
		super(c);
		UiApplication ui = UiApplication.getUiApplication();
		theApp = (UiApplication) ui;	
		setUpAssets();
	}
	
	public void setUpAssets() {
		int w = Display.getWidth();
		int h = Display.getHeight();
		String res = w + "x" + h;
		System.out.println("res__________"+res);
		splash = Bitmap.getBitmapResource("img/splash_"+res+".png");
		main = Bitmap.getBitmapResource("img/bkg_main.jpg");
		btn = Bitmap.getBitmapResource(button);
		if (splash==null) { splash = Bitmap.getBitmapResource(defSplash); }
		bkg_splash = BackgroundFactory.createBitmapBackground(splash);
		bkg_main = BackgroundFactory.createBitmapBackground(main);
		
	}
	
	public Manager splashScreen(Manager parent) {
		VerticalFieldManager fm_MainHolder = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Manager.USE_ALL_HEIGHT | Manager.NO_VERTICAL_SCROLL){
			   public int getPreferredWidth() {       
			       return Display.getWidth(); 
			   }
			   public int getPreferredHeight() {       
			       return Display.getHeight(); 
			   }
			   protected void sublayout(int width, int height) {
			       width = getPreferredWidth();
			       height = getPreferredHeight();
			       super.sublayout(width, height);
			       super.setExtent(width, height);
			   }
		};
		Background back = bkg_splash;
		fm_MainHolder.setBackground(back);
		parent.add(fm_MainHolder);
		return fm_MainHolder;
	}
	
	public Manager mainScreen(Manager parent) {
		CInterface ci = new CInterface();
		final int columnHeight0 = (int)((Display.getHeight()/2));
		final int columnHeight1 = (int)(Display.getHeight()/2);
		final int btnw = (int)((float)Display.getWidth()*.75);
		final EncodedImage buttonoff = ci.getScaledImage(EncodedImage.getEncodedImageResource(button),btnw);
		final EncodedImage buttonon = ci.getScaledImage(EncodedImage.getEncodedImageResource(button_on),btnw);
		final EncodedImage friendsoff = ci.getScaledImage(EncodedImage.getEncodedImageResource(friends),btnw);
		final EncodedImage friendson = ci.getScaledImage(EncodedImage.getEncodedImageResource(friends_on),btnw);
		int imagew = (int)((float)Display.getWidth()*.75);
		Bitmap blankbmp = Bitmap.getBitmapResource(blank);
		
		VerticalFieldManager fm_MainHolder = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Manager.USE_ALL_HEIGHT | Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL){
			   public int getPreferredWidth() {       
			       return Display.getWidth(); 
			   }
			   public int getPreferredHeight() {       
			       return Display.getHeight(); 
			   }
			   protected void sublayout(int width, int height) {
			       width = getPreferredWidth();
			       height = getPreferredHeight();
			       super.sublayout(width, height);
			       super.setExtent(width, height);
			   }
		};
		Background back = bkg_main;
		fm_MainHolder.setBackground(back);
				
		
		//MAIN LOGO
		HorizontalFieldManager lgo = new HorizontalFieldManager(Manager.NO_VERTICAL_SCROLL | Manager.NO_HORIZONTAL_SCROLL | Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH) {
			public int getPreferredHeight() {       
			       return columnHeight0; 
			   }
			   protected void sublayout(int width, int height) {
			       width = super.getPreferredWidth();
			       height = getPreferredHeight();
			       super.sublayout(width, height);
			       super.setExtent(width, height);
			   }
		};
			BitmapField img = ci.WriteSimpleImage(lgo, logo, BitmapField.FIELD_VCENTER | BitmapField.FIELD_HCENTER, imagew, "");
			fm_MainHolder.add(lgo);
	        
        //BUTTON HOLDER
		VerticalFieldManager vfmButton = new VerticalFieldManager(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH) {
			   public int getPreferredHeight() {       
			       return columnHeight1; 
			   }
			   public int getPreferredWidth() {       
			       return Display.getWidth(); 
			   }
			   protected void sublayout(int width, int height) {
			       width = getPreferredWidth();
			       height = getPreferredHeight();
			       super.sublayout(width, height);
			       super.setExtent(width, height);
			   }
		 };
		   
		   final BitmapField bmFieldSpan = new BitmapField(blankbmp,BitmapField.NON_FOCUSABLE);
				
		   CustomButtonField cbfFriends = new CustomButtonField(btnw, "", friendsoff.getBitmap(), friendson.getBitmap(),Field.FOCUSABLE | Field.FIELD_HCENTER) {
			   protected boolean navigationClick(int status, int time)
			    {
			    	c.action_callback("web_friends", "", "");
			       return true;
			    }
		   };
		    
		   CustomButtonField cbfButton = new CustomButtonField(btnw, "", buttonoff.getBitmap(), buttonon.getBitmap(),Field.FOCUSABLE | Field.FIELD_HCENTER) {
				protected boolean navigationClick(int status, int time)
			    {
			       c.action_callback("qr_start", "", "");
			       return true;
			    }
		   };
		

			
	    vfmButton.add(cbfButton);
	    vfmButton.add(cbfFriends);
		fm_MainHolder.add(vfmButton);
			
		parent.add(fm_MainHolder);	
        return fm_MainHolder;
	}
	
	public void construct_makescrollable_attache(Manager parent){
		parent.add(new NullField(NullField.FOCUSABLE));
	}
			
	
	
	
	

}
