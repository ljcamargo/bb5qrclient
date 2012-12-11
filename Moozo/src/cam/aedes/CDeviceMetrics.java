package cam.aedes;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;

public class CDeviceMetrics {
	
	public CMainScreen c;
	
	public Manager write_area;
	public int image_size = 150;
	public int media_image_size_x = 150;
	public int media_image_size_y = 100;
	public int content_media_image_size_x = 150;
	public int content_media_image_size_y = 100;
	public int main_image_size = 150;
	public int primary_image_size = (int) (((double)Display.getWidth())-8);
	public int small_image_size = (int) (((double)Display.getWidth())/3);
	public boolean big_screen = false;
	public int actual_w = Display.getWidth();
	public int actual_h = Display.getHeight();
	
	public CDeviceMetrics(CMainScreen c) {
		this.c = c;
		if (actual_h>320) { big_screen = true; }
	}
	


}
