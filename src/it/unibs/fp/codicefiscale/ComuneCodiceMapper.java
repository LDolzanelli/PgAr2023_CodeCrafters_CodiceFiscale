package it.unibs.fp.codicefiscale;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class ComuneCodiceMapper {
    
    private static Map<String, String> comuneCodiceMap;

    private ComuneCodiceMapper () {}

    public static String getCodiceComune(String comune) {
        if(comuneCodiceMap == null) {
            initializeComuneCodiceMap();
        }

        return comuneCodiceMap.get(comune);
    }

    public static void initializeComuneCodiceMap() {
        comuneCodiceMap = new HashMap<>();
        XMLInputFactory xmlif = null;
        XMLStreamReader xmlr = null;

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader("inputXmlFiles/Comuni.xml", new FileInputStream("inputXmlFiles/Comuni.xml"));

            while (xmlr.hasNext()) { 
                if(xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("comune")) {

                    String nome = null;
                    String codice = null;

                    while(true) {
                        if(xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("nome")) {
                            xmlr.next();
                            nome = xmlr.getText();
                        } else if(xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("codice")) {
                            xmlr.next();
                            codice = xmlr.getText();
                            comuneCodiceMap.put(nome, codice);
                            break;

                        } else {
                            xmlr.next();
                        }
                    }
                }

                xmlr.next();
            } 

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }
    }


}
