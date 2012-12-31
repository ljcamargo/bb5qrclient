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
                font = fntFamily.getFont(Font.BOLD,20);              
        }
        catch(Exception e)
        {
            font = Font.getDefault();          
        }
        graphics.setFont(font);
        graphics.setColor(Color.WHITE); 
        graphics.drawBitmap(0, 0, current_pic.getWidth(), current_pic.getHeight(), current_pic, 0, 0);
        int x = (int)(current_pic.getWidth()-font.getAdvance(text))/2;
        int y = (int)(current_pic.getHeight()-font.getHeight())/2;
        graphics.drawText(text, x,y);
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
