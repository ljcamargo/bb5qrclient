package cam.aedes;


import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

public class CSocial {
	
	CMainScreen c;
	String this_link;
	String msg;
	String title;
	String to;
	

 	public static final String BITLY_USER = "lajornada";
 	public static final String BITLY_KEY = "R_15664b5e98a241527cff996471d9f4ec";
 	public static final String FACEBOOK_ID = "113354525422703";
 	public static final String FACEBOOK_API_KEY ="1de0f0b749c33fc83785687fe8a7b45c";
 	public static final String FACEBOOK_SECRET = "4f1176e32fc1d747c2acf3a8d86159ee";
	
	public CSocial(CMainScreen c) {
		this.c = c;
	}

	

    public void Mail(String text, String subject, String address, String link) {
    	Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES , 
    	new MessageArguments(MessageArguments.ARG_NEW, address, subject, text+"  "+link));
    }
    
    public void Facebook(String text, String link) {
          //BrowserSession bSession = Browser.getDefaultSession();
          String shareUrl = "http://m.facebook.com/dialog/feed?app_id="+FACEBOOK_ID+"&link="+link+"&caption="+text+"&display=touch";
          System.out.println(shareUrl);
          //bSession.displayPage( shareUrl );
          //Screen screen = new Nav(shareUrl);    
      	  //UiApplication.getUiApplication().pushScreen(screen);
    }
    
    public void Twitter(String text, String link) {

          //BrowserSession bSession = Browser.getDefaultSession();
          String shareUrl = "http://twitter.com/share?text="+text+"&url=" + link;
          System.out.println(shareUrl);
         // bSession.displayPage( shareUrl );
      	  //Screen screen = new Nav(shareUrl);    
    	  //UiApplication.getUiApplication().pushScreen(screen);
    }
    
    public String BitlyQuery(String myurl) {
    	myurl =  URLencode(myurl);
    	String bitly_web="";
    	bitly_web = "http://api.bit.ly/v3/shorten?login="+ BITLY_USER + "" + "&apiKey="  + BITLY_KEY + "&longUrl=" + myurl + "&format=json";
		return bitly_web;
		
		
    }
    
    public static String URLencode(String s)
    {
        if (s!=null) {
            StringBuffer tmp = new StringBuffer();
            int i=0;
            try {
                while (true) {
                    int b = (int)s.charAt(i++);
                    if ((b>=0x30 && b<=0x39) || (b>=0x41 && b<=0x5A) || (b>=0x61 && b<=0x7A)) {
                        tmp.append((char)b);
                    }
                    else {
                        tmp.append("%");
                        if (b <= 0xf) tmp.append("0");
                        tmp.append(Integer.toHexString(b));
                    }
                }
            }
            catch (Exception e) {}
            return tmp.toString();
        }
        return null;
    }

    public void DoMail() {
    	
    }
}
