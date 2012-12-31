package om.ui;

import cam.aedes.CStringProvider;
import java.util.Hashtable;

public class Strings extends CStringProvider {
	 
	public Strings(String ISOLocale) {
		super(ISOLocale);
		startContent();	
	}
	
	public Strings() {
		super();
		startContent();
	}
	
	public void startContent() {
		Hashtable def = new Hashtable();
		def.put("read_qr", "Leer QR");
		def.put("invite_friends", "Invitar Amigos");
		def.put("invalid_code", "Código no válido. Intente de Nuevo");
		
		Hashtable es = new Hashtable();
		es.put("read_qr", "Leer QR");
		es.put("invite_friends", "Invitar Amigos");
		es.put("invalid_code", "Código no válido. Intente de Nuevo");
		
		Hashtable en = new Hashtable();
		en.put("read_qr", "Read QR");
		en.put("invite_friends", "Invite Friends");
		en.put("invalid_code", "Invalid code. Retry.");
		
		map.put("def", def);
		map.put("es", es);
		map.put("en", en);
	}
	
	
}
