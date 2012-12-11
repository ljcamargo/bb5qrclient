package om.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.*;

public class CustomButtonField extends Field
{
    Bitmap Unfocus_img, Focus_img, current_pic;
    int width;
    String text;
    Font font;    
    CustomButtonField(int width, String text, Bitmap onFocus, Bitmap onUnfocus, long style)
    {
        super(style);
        Unfocus_img = onUnfocus;
        Focus_img = onFocus;
        current_pic = onFocus;
        this.text = text;
        this.width = width;
    }
    protected void layout(int width, int height) 
    {
        setExtent(current_pic.getWidth(), current_pic.getHeight());        
    }
    protected void paint(Graphics graphics) 
    {
        try
        {
                FontFamily fntFamily = FontFamily.forName("BBAlpha Sans");
                font = fntFamily.getFont(Font.BOLD,14);              
        }
        catch(Exception e)
        {
            font = Font.getDefault();
          
        }
        graphics.setFont(font);
        graphics.setColor(Color.WHITE); 
        graphics.drawBitmap(0, 0, current_pic.getWidth(), current_pic.getHeight(), current_pic, 0, 0);
        graphics.drawText(text, width, 7);
    }
    protected void onFocus(int direction) 
    {
        super.onFocus(direction);
        current_pic = Unfocus_img;
        this.invalidate();
    }
  protected void drawFocus(Graphics graphics, boolean on) 
  {
        
    }
    protected void onUnfocus() 
    {
        super.onUnfocus();
        current_pic = Focus_img;
        invalidate();
    }
    public boolean isFocusable() {
        return true;
    }
    protected boolean navigationClick(int status, int time) {
        fieldChangeNotify(0);
        return true;
    }
} 
