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
            xmlr = xmlif.createXMLStreamReader(
                "inputXmlFiles/Comuni.xml",
                new FileInputStream("inputXmlFiles/Comuni.xml"));

                String nome = null;
                String codice = null;
                String currentTag = null; 


                while (xmlr.hasNext()) { 
                    switch (xmlr.getEventType()) {
                        // continua a leggere finché ha eventi a disposizione // switch sul tipo di evento
                        case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
                            System.out.println("Start Read Doc "); 
                            break;
                        case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
                            if(xmlr.getLocalName().equals("nome")) {
                                currentTag = "nome";
                            } else if(xmlr.getLocalName().equals("codice")) {
                                currentTag = "codice";
                            }
                            break;
                        case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
                            if("comune".equals(xmlr.getLocalName())) {
                                System.out.println(nome + " - " + codice);
                                comuneCodiceMap.put(nome, codice);
                            }
                            break;
                        case XMLStreamConstants.CHARACTERS: 
                            if (xmlr.getText().trim().length() > 0)
                                if("nome".equals(currentTag)) {
                                    nome = xmlr.getText();
                                } else if("codice".equals(currentTag)) {
                                    codice = xmlr.getText();
                                }
                            
                            break;

                    } xmlr.next();
                }

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }
    }

}
