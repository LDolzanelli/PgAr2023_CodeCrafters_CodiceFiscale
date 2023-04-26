package it.unibs.fp.codicefiscale;

import java.util.ArrayList;

public class CodiceFiscaleMain {
    // Stringa di comando per "pulire" la console
    public static final String FLUSH = "\033[H\033[2J";
    static ArrayList<Persona> persone = new ArrayList<Persona>();
    static ArrayList<Persona> personeAssenti = new ArrayList<Persona>();
    private static ArrayList<String> codiciFiscali = new ArrayList<String>();
    private static ArrayList<String> codiciFiscaliSpaiati = new ArrayList<String>();
    private static ArrayList<String> codiciFiscaliErrati = new ArrayList<String>();

    public static void main(String[] args) {

        persone = MetodiLetturaScrittura.leggiPersone();
        codiciFiscali = MetodiLetturaScrittura.leggiCF();
        checkPersonePresenti();
        checkCodiciFiscali();
        MetodiLetturaScrittura.creaFileXml(persone, personeAssenti, codiciFiscaliErrati, codiciFiscaliSpaiati);

    }

    public static void checkPersonePresenti() {
        for (int i = 0; i < persone.size(); i++) {
            for (int j = 0; j < codiciFiscali.size(); j++) {
                if (persone.get(i).getCodiceFiscale().equals(codiciFiscali.get(j))) {
                    codiciFiscali.remove(j);
                    break;
                }

                if (j == codiciFiscali.size() - 1) {
                    personeAssenti.add(persone.get(i));
                    persone.remove(i);
                    i--;
                }
            }
        }
    }

    public static void checkCodiciFiscali() {
        for (int i = 0; i < codiciFiscali.size(); i++) {
            if (CodiceFiscaleChecker.checkCodiceFiscale(codiciFiscali.get(i))) {
                codiciFiscaliSpaiati.add(codiciFiscali.get(i));
            } else {
                codiciFiscaliErrati.add(codiciFiscali.get(i));
            }
        }
    }
}
