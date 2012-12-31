package cam.aedes;

import java.util.Hashtable;

public class CStringProvider {
	
	String locale;
	String region;
	String descr;
	public String def;
	
	public Hashtable map = new Hashtable();
	
	public CStringProvider(String ISOLocale) {
		this.locale = ISOLocale;
	}
	
	public CStringProvider() {
		autoStartLocale();
	}

	public void setLocale(String ISOLocale) {
		this.locale = ISOLocale;
	}
	
	public String getLocale() {
		return this.locale;
	}
	
	public void autoStartLocale() {
		this.def = net.rim.device.api.i18n.Locale.getDefaultForSystem().getLanguage();
		this.locale = this.def;
		System.out.println("#### DEVICE LANGUAGE CODE "+this.def);
		
	}

	
	public String getString(String alias) {
		if (map.containsKey(this.getLocale())) {
			System.out.println("##### locale set"+getLocale());
			Hashtable m = (Hashtable) map.get(getLocale());
			if (m.containsKey(alias)) {
				return (String)m.get(alias);
			} else {
				return "";
			}
		} else {
			Hashtable m = (Hashtable) map.get("def");
			if (m.containsKey(alias)) {
				return (String)m.get(alias);
			} else {
				return "";
			}
		}
	}

}
