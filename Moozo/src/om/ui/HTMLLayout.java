package om.ui;


public class HTMLLayout {
	
	public HTMLLayout() {
		
	}
	
	public String Article(String title, String family, String section, String byline, String hedline, String tabstract, String text, final String img, final String navurl) {	
		String html = "";
		String head = "<head><title>" + title + "</title></head>";
		String h_title = "<body><p><h1>"+title+"</h1></p>";
		String h_img = "";
		if (img!="") {
			h_img = "<p><img src=\"" + img + "\"></p>";
		}
		String h_byline = "<p>"+byline+"</p>";
		String h_hedline = "<p><b>"+byline+"</b></p>";
		String h_abstract = "<p><i>"+tabstract+"</i></p>";
		String h_text = "<p><small>"+text+"</small></p>";
		html += head + h_title + h_img + h_byline + h_hedline + h_abstract + h_text;
		return html;
	}
}
