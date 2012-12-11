package cam.aedes;

import java.io.InputStream;

public interface CInteraction {
	
	public void callback_downloaded(InputStream is, String content);
	
	public void callback_error(int errorcode);
	
	public void content_dispatch(InputStream is, String content);

}
