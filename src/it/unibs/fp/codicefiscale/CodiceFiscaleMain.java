
package it.unibs.fp.codicefiscale;

import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class CodiceFiscaleMain {
    // Stringa di comando per "pulire" la console
    public static final String FLUSH = "\033[H\033[2J";
    private static ArrayList<Persona> persone = new ArrayList<Persona>();

    public static void main(String[] args) {

        leggiPersone();

    }

    public static void leggiPersone() {
        String nome = "";
        String cognome = "";
        String dataDiNascita = "";
        String luogoDiNascita = "";
        char sesso = '0';

        XMLInputFactory xmlif = null;
        XMLStreamReader xmlr = null;

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader("inputXmlFiles/InputPersone.xml",
                    new FileInputStream("inputXmlFiles/InputPersone.xml"));

            while (xmlr.hasNext()) {

                xmlr.nextTag();
                if (xmlr.isStartElement()) {
                    switch (xmlr.getLocalName()) {
                        case "nome":
                            nome = xmlr.getElementText();
                            break;

                        case "cognome":
                            cognome = xmlr.getElementText();
                            break;
                        case "sesso":
                            sesso = xmlr.getElementText().charAt(0);
                            break;

                        case "comune_nascita":
                            luogoDiNascita = xmlr.getElementText();
                            break;

                        case "data_nascita":
                            dataDiNascita = xmlr.getElementText();
                            break;

                        default:
                            break;
                    }
                }

                if (xmlr.getEventType() == XMLStreamReader.END_ELEMENT && xmlr.getLocalName().equals("persona")) {
                    Persona persona = new Persona(nome, cognome, dataDiNascita, luogoDiNascita, sesso);
                    persone.add(persona);
                }

            }

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

    }

}
