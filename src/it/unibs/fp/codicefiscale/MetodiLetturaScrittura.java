package it.unibs.fp.codicefiscale;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class MetodiLetturaScrittura {

    /*
     * Metodo che serve per leggere tutti i dati delle persone del file "InputPersone.xml" 
     * e restiturli tramite un ArrayList
     */
    public static ArrayList<Persona> leggiPersone() {

        ArrayList<Persona> persone = new ArrayList<Persona>();

        String nome = "";
        String cognome = "";
        String dataDiNascita = "";
        String luogoDiNascita = "";
        char sesso = '0';

        XMLInputFactory xmlif = null;
        XMLStreamReader xmlr = null;

        // lettura file InputPersone.xml

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader("inputXmlFiles/InputPersone.xml",
                    new FileInputStream("inputXmlFiles/InputPersone.xml"));

            while (xmlr.hasNext()) {

                xmlr.nextTag();
                // Se il tag è un start element si legge il dato in base a che tipo di dato è,
                // altrimenti va avanti
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

                // Quando si arriva al tag di chiusura di persona (</persona>) viene creato un
                // nuovo oggetto di tipo Persona e viene aggiunto all'arrayList
                if (xmlr.getEventType() == XMLStreamReader.END_ELEMENT && xmlr.getLocalName().equals("persona")) {
                    Persona persona = new Persona(nome, cognome, dataDiNascita, luogoDiNascita, sesso);
                    persone.add(persona);
                }

            }

            // Arrivato alla fine chiude il file
            xmlr.close();

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
            System.out.println(CodiceFiscaleMain.FLUSH);
        }

        return persone;

    }

    /*
     * Metodo che serve per leggere tutti i codici fiscali del file "Comuni.xml"
     * e restituirli tramite un ArrayList
     */
    public static ArrayList<String> leggiCF() {

        ArrayList<String> codiciFiscali = new ArrayList<String>();

        // lettura codici fiscali

        try {
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = xmlif.createXMLStreamReader("inputXmlFiles/CodiciFiscali.xml",
                    new FileInputStream("inputXmlFiles/CodiciFiscali.xml"));

            while (xmlr.hasNext()) {
                xmlr.nextTag();

                // se è un tag di start e contine come nome codice allora legge il codice
                // altrimenti va avanti
                if (xmlr.isStartElement() && xmlr.getLocalName() == "codice") {
                    codiciFiscali.add(xmlr.getElementText());

                }
            }

            // arrivato alla fine chiude il file
            xmlr.close();

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
            System.out.println(CodiceFiscaleMain.FLUSH);
        }

        return codiciFiscali;
    }

    /**
     * Medodo che serve per generare il file di ouput "codiciPersone.xml", il quale conterrà:
     * @param PersonePresenti Tutti i dati delle persone con il rispettivo codice fiscale se è presente in "codiciFiscali.xml"
     * @param PersoneAssenti Nel caso in cui non sia presente il codice fiscale viene mostrata la dicitura ASSENTE
     * @param codiciFiscaliErrati il file di output conterrà anche i codici fiscali invalidi
     * @param codiciFiscaliSpaiati il file di output conterrà anche i codici fiscali spaiati
     */
    public static void creaFileXml(ArrayList<Persona> PersonePresenti, ArrayList<Persona> PersoneAssenti,
            ArrayList<String> codiciFiscaliErrati, ArrayList<String> codiciFiscaliSpaiati) {

        XMLOutputFactory xmlof = null;
        XMLStreamWriter xmlw = null;

        // scrittura del file XML

        try {

            xmlof = XMLOutputFactory.newInstance();
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream("outputXmlFiles/codiciPersone.xml"), "utf-8");

            // inizio documento
            xmlw.writeStartDocument("utf-8", "1.0");

            // inizio <output>
            xmlw.writeStartElement("output");

            // inizio <persone> con il suo attributo
            xmlw.writeStartElement("persone");
            xmlw.writeAttribute("numero",
                    String.format("%d", PersonePresenti.size() + CodiceFiscaleMain.personeAssenti.size()));

            // scrittura dei dati di tutte le persone presenti con il loro id
            for (int i = 0; i < CodiceFiscaleMain.persone.size(); i++) {
                xmlw.writeStartElement("persona");
                xmlw.writeAttribute("id", String.format("%d", i));

                // nome
                xmlw.writeStartElement("nome");
                xmlw.writeCharacters(PersonePresenti.get(i).getNome());
                xmlw.writeEndElement();

                // cognome
                xmlw.writeStartElement("cognome");
                xmlw.writeCharacters(PersonePresenti.get(i).getCognome());
                xmlw.writeEndElement();

                // sesso
                xmlw.writeStartElement("sesso");
                xmlw.writeCharacters(String.valueOf(PersonePresenti.get(i).getSesso()));
                xmlw.writeEndElement();

                // comune di nascita
                xmlw.writeStartElement("comune_nascita");
                xmlw.writeCharacters(PersonePresenti.get(i).getLuogoDiNascita());
                xmlw.writeEndElement();

                // data di nascita
                xmlw.writeStartElement("data_nascita");
                xmlw.writeCharacters(PersonePresenti.get(i).getDataDiNascita());
                xmlw.writeEndElement();

                // codice fiscale
                xmlw.writeStartElement("codice_fiscale");
                xmlw.writeCharacters(PersonePresenti.get(i).getCodiceFiscale());
                xmlw.writeEndElement();

                xmlw.writeEndElement(); // fine persona

            }

            // scrittura dei dati di tutte le persone presenti con il loro id
            for (int i = 0; i < CodiceFiscaleMain.personeAssenti.size(); i++) {

                xmlw.writeStartElement("persona");
                xmlw.writeAttribute("id", String.format("%d", i + CodiceFiscaleMain.persone.size()));

                // nome
                xmlw.writeStartElement("nome");
                xmlw.writeCharacters(CodiceFiscaleMain.personeAssenti.get(i).getNome());
                xmlw.writeEndElement();

                // cognome
                xmlw.writeStartElement("cognome");
                xmlw.writeCharacters(CodiceFiscaleMain.personeAssenti.get(i).getCognome());
                xmlw.writeEndElement();

                // sesso
                xmlw.writeStartElement("sesso");
                xmlw.writeCharacters(String.valueOf(CodiceFiscaleMain.personeAssenti.get(i).getSesso()));
                xmlw.writeEndElement();

                // comune di nascita
                xmlw.writeStartElement("comune_nascita");
                xmlw.writeCharacters(CodiceFiscaleMain.personeAssenti.get(i).getLuogoDiNascita());
                xmlw.writeEndElement();

                // data di nascita
                xmlw.writeStartElement("data_nascita");
                xmlw.writeCharacters(CodiceFiscaleMain.personeAssenti.get(i).getDataDiNascita());
                xmlw.writeEndElement();

                // codice fiscale
                xmlw.writeStartElement("codice_fiscale");
                xmlw.writeCharacters("ASSENTE");
                xmlw.writeEndElement();

                xmlw.writeEndElement(); // fine persona

            }

            xmlw.writeEndElement(); // fine persone <\persone>

            // inizio sezione codici
            xmlw.writeStartElement("codici");

            xmlw.writeStartElement("invalidi"); // inizio codici fiscali invalidi
            xmlw.writeAttribute("numero", String.format("%d", codiciFiscaliErrati.size())); // Attributo codici invalidi

            // stampa codici fiscali invalidi
            for (int i = 0; i < codiciFiscaliErrati.size(); i++) {
                xmlw.writeStartElement("codice");
                xmlw.writeCharacters(codiciFiscaliErrati.get(i));
                xmlw.writeEndElement();

            }

            xmlw.writeEndElement();// fine codici invalidi

            xmlw.writeStartElement("spaiati"); // inizio codici fiscali spaiati
            xmlw.writeAttribute("numero", String.format("%d", codiciFiscaliSpaiati.size())); // Attributo codici spaiati

            // stampa codici fiscali spaiati
            for (int i = 0; i < codiciFiscaliSpaiati.size(); i++) {
                xmlw.writeStartElement("codice");
                xmlw.writeCharacters(codiciFiscaliSpaiati.get(i));
                xmlw.writeEndElement();
            }

            xmlw.writeEndElement();// fine codici spaiati

            xmlw.writeEndElement();// fine codici fiscali invalidi e spaiati

            xmlw.writeEndElement();// fine output

            xmlw.writeEndDocument();// fine documento

            xmlw.flush();
            xmlw.close();

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }
    }

}
