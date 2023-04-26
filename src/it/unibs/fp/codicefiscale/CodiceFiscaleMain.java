
package it.unibs.fp.codicefiscale;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.XMLReader;

public class CodiceFiscaleMain {
    // Stringa di comando per "pulire" la console
    public static final String FLUSH = "\033[H\033[2J";
    private static ArrayList<Persona> persone = new ArrayList<Persona>();
    private static ArrayList<Persona> personeAssenti = new ArrayList<Persona>();
    private static ArrayList<String> codiciFiscaliNonPresenti = new ArrayList<String>();

    public static void main(String[] args) {

        leggiPersone();
        leggiCF();
        checkPersonePresenti();
        creaFileXml();

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
            xmlr.close();

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
            System.out.println(FLUSH);
        }

    }

    public static void leggiCF() {

        try {
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = xmlif.createXMLStreamReader("inputXmlFiles/CodiciFiscali.xml",
                    new FileInputStream("inputXmlFiles/CodiciFiscali.xml"));

            while (xmlr.hasNext()) {
                xmlr.nextTag();
                if (xmlr.isStartElement() && xmlr.getLocalName() == "codice") {
                    codiciFiscaliNonPresenti.add(xmlr.getElementText());

                }
            }
            xmlr.close();

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
            System.out.println(FLUSH);
        }
    }

    public static void checkPersonePresenti() {
        for (int i = 0; i < persone.size(); i++) {
            for (int j = 0; j < codiciFiscaliNonPresenti.size(); j++) {
                if (persone.get(i).getCodiceFiscale().equals(codiciFiscaliNonPresenti.get(j))) {
                    codiciFiscaliNonPresenti.remove(j);
                    break;
                }

                if (j == codiciFiscaliNonPresenti.size() - 1) {
                    personeAssenti.add(persone.get(i));
                    persone.remove(i);
                    i--;
                }
            }
        }
    }

    public static void creaFileXml() {

        XMLOutputFactory xmlof = null;
        XMLStreamWriter xmlw = null;

        try {
            xmlof = XMLOutputFactory.newInstance();
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream("outputXmlFiles/codiciPersone.xml"), "utf-8");
            xmlw.writeStartDocument("utf-8", "1.0");
            xmlw.writeCharacters("\n");

            xmlw.writeStartElement("output");
            xmlw.writeCharacters("\n");
            xmlw.writeCharacters("\t");

            xmlw.writeStartElement("persone");
            xmlw.writeAttribute("numero", String.format("%d", persone.size() + personeAssenti.size()));
            xmlw.writeCharacters("\n");
            xmlw.writeCharacters("\t\t");

            for (int i = 0; i < persone.size(); i++) {
                xmlw.writeStartElement("persona");
                xmlw.writeAttribute("id", String.format("%d", i));
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");
                // nome
                xmlw.writeStartElement("nome");
                xmlw.writeCharacters(persone.get(i).getNome());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // cognome
                xmlw.writeStartElement("cognome");
                xmlw.writeCharacters(persone.get(i).getCognome());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // sesso
                xmlw.writeStartElement("sesso");
                xmlw.writeCharacters(String.valueOf(persone.get(i).getSesso()));
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // comune di nascita
                xmlw.writeStartElement("comune_nascita");
                xmlw.writeCharacters(persone.get(i).getLuogoDiNascita());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // data di nascita
                xmlw.writeStartElement("data_nascita");
                xmlw.writeCharacters(persone.get(i).getDataDiNascita());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // codice fiscale
                xmlw.writeStartElement("codice_fiscale");
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t\t");
                xmlw.writeCharacters(persone.get(i).getCodiceFiscale());
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");

                xmlw.writeCharacters("\t\t");
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t");

            }

            for (int i = 0; i < personeAssenti.size(); i++) {
                xmlw.writeStartElement("persona");
                xmlw.writeAttribute("id", String.format("%d", i + persone.size()));
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");
                // nome
                xmlw.writeStartElement("nome");
                xmlw.writeCharacters(personeAssenti.get(i).getNome());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // cognome
                xmlw.writeStartElement("cognome");
                xmlw.writeCharacters(personeAssenti.get(i).getCognome());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // sesso
                xmlw.writeStartElement("sesso");
                xmlw.writeCharacters(String.valueOf(personeAssenti.get(i).getSesso()));
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // comune di nascita
                xmlw.writeStartElement("comune_nascita");
                xmlw.writeCharacters(personeAssenti.get(i).getLuogoDiNascita());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // data di nascita
                xmlw.writeStartElement("data_nascita");
                xmlw.writeCharacters(personeAssenti.get(i).getDataDiNascita());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");

                // codice fiscale
                xmlw.writeStartElement("codice_fiscale");
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t\t");
                xmlw.writeCharacters("ASSENTE");
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t\t");
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");

                xmlw.writeCharacters("\t\t");
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                xmlw.writeCharacters("\t\t");

            }

            xmlw.writeCharacters("\t");

            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");

            xmlw.writeEndDocument();

            xmlw.flush();
            xmlw.close();

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }
    }
}
