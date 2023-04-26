package it.unibs.fp.codicefiscale;

import java.io.FileInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class CodiceFiscaleGenerator {

    public static String generaCodiceFiscale(String cognome, String nome, String dataDiNascita, char sesso,
            String comune) {
        StringBuffer codiceFiscale = new StringBuffer();

        codiceFiscale.append(generaCodiceCognome(cognome));
        codiceFiscale.append(generaCodiceNome(nome));
        codiceFiscale.append(generaCodiceAnno(dataDiNascita));
        codiceFiscale.append(generaCodiceMese(dataDiNascita));
        codiceFiscale.append(generaCodiceGiorno(dataDiNascita, sesso));
        codiceFiscale.append(generaCodiceComune(comune));
        codiceFiscale.append(generaCifraControllo(codiceFiscale));

        return codiceFiscale.toString();
    }

    /**
     * Metodo che genera il codice (di tre lettere) del nome 
     * @param nome nome del quale di vuole generare il codice
     * @return codice di tre lettere generato
     */
     private static String generaCodiceNome(String nome) {
        nome = nome.toUpperCase().trim(); // per evitare errori di input
        StringBuffer codiceNome = new StringBuffer();
        StringBuffer nomeSenzaVocali = new StringBuffer();

        for (int i = 0; i < nome.length(); i++) {
            if (isConsonante(nome.charAt(i))) {
                nomeSenzaVocali.append(nome.charAt(i));
            }
        }

        if (nomeSenzaVocali.length() >= 4) {
            for (int i = 0; i <= 3; i++) {
                if (i != 1) {
                    codiceNome.append(nomeSenzaVocali.charAt(i));
                }
            }
        } 

        else if (nomeSenzaVocali.length() == 3) {
            codiceNome = nomeSenzaVocali;
        } 
        
        else {
            codiceNome = menoDiTreConsonanti(nome, nomeSenzaVocali);
        }

        return codiceNome.toString();
    }

    /**
     * Metodo che genera il codice (di tre lettere) del cognome 
     * @param cognome cognome del quale di vuole generare il codice
     * @return codice di tre lettere generato
     */

    private static String generaCodiceCognome(String cognome) {
        cognome = cognome.toUpperCase().trim(); // per evitare errori di input
        StringBuffer codiceCognome = new StringBuffer();
        StringBuffer cognomeSenzaVocali = new StringBuffer();

        for (int i = 0; i < cognome.length(); i++) {
            if (isConsonante(cognome.charAt(i))) {
                cognomeSenzaVocali.append(cognome.charAt(i));
            }

        }

        if (cognomeSenzaVocali.length() >= 3) {
            for (int i = 0; i < 3; i++) {
                codiceCognome.append(cognomeSenzaVocali.charAt(i));
            }
        } 
        
        else {
            codiceCognome = menoDiTreConsonanti(cognome, cognomeSenzaVocali);
        }

        return codiceCognome.toString();
    }

    /**
     * Metodo che genera il codice di un nome o cognome nel caso in cui siano presenti meno di tre consonanti
     * @param stringa nome o cognome del quale si vuole generare il codice
     * @param stringaSenzaVocali passaggio della parte iniziale del codice, ovvero il nome o cognome privato delle vocali
     * @return codice di tre lettere generato
     */

    private static StringBuffer menoDiTreConsonanti(String stringa, StringBuffer stringaSenzaVocali) {
        StringBuffer codiceStringa;
        codiceStringa = stringaSenzaVocali;

        if (stringa.length() > 2) {
            for (int i = 0; codiceStringa.length() != 3; i++) {
                if (!isConsonante(stringa.charAt(i))) {
                    codiceStringa.append(stringa.charAt(i));
                }
            }
        } 
        
        else {
            for (int i = 0; codiceStringa.length() != 3; i++) {
                if ((i + 1) <= stringa.length() && !isConsonante(stringa.charAt(i))) {
                    codiceStringa.append(stringa.charAt(i));
                }

                if ((i + 1) > stringa.length()) {
                    codiceStringa.append('X');
                }
            }
        }

        return codiceStringa;
    }

    /**
     * Metodo che genera il codice (di due cifre) dell'anno di nascita
     * @param dataDiNascita stringa che contiene l'anno di nascita del quale si vuole generare il codice
     * @return codice di due cifre generato
     */
    private static String generaCodiceAnno(String dataDiNascita) {
        //vengono prese le ultime due cifre dell'anno di nascita (posizione 2 e 3)
        String codiceAnno = dataDiNascita.substring(2, 4);
        return codiceAnno;
    }

    /**
     * Metodo che genera il codice (di una lettera) del mese di nascita
     * @param dataDiNascita stringa che contiene il mese di nascita del quale si vuole generare il codice
     * @return codice di una lettera generato
     */
    private static char generaCodiceMese(String dataDiNascita) {

        enum LetteraMese {
            A, B, C, D, E, H, L, M, P, R, S, T;
        }

        //converte in intero la stringa del mese di nascita (da posizione 5 inclusa a 7 esclusa)
        int meseDiNascita = Integer.parseInt(dataDiNascita.substring(5, 7));

        //"meseDiNascita" viene usato come indice per accedere al valore Enum corrispondente, poi convertito in char
        char codiceMese = LetteraMese.values()[meseDiNascita - 1].toString().charAt(0);

        return codiceMese;
    }

    /**
     * Metodo che genera il codice (di due cifre) del giorno di nascita
     * @param dataDiNascita stringa che contiene il giorno di nascita del quale si vuole generare il codice
     * @param sesso a seconda del sesso viene assegnato il giorno di nascita
     * @return codice di due cifre generato
     */
    private static String generaCodiceGiorno(String dataDiNascita, char sesso) {
        //converte in int le ultime due cifre della data di nascita
        int codiceGiorno = Integer.parseInt(dataDiNascita.substring(8, 10));

        if(sesso == 'M') {
            return String.valueOf(codiceGiorno);
        }
        
        else {
            return String.valueOf(codiceGiorno + 40);
        }
    }


    private static String generaCodiceComune(String comune) {
        XMLInputFactory xmlif = null;
        XMLStreamReader xmlr = null;

        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader("inputXmlFiles/Comuni.xml",
                    new FileInputStream("inputXmlFiles/Comuni.xml"));

            String nomeComune = "";
            String codiceComune = "";

            boolean trovato = false;

            while (xmlr.hasNext() && !trovato) {
                xmlr.nextTag();
                if (xmlr.isStartElement() && xmlr.getLocalName().equals("nome")) {
                    nomeComune = xmlr.getElementText();
                    xmlr.nextTag();
                }

                if (xmlr.isStartElement() && xmlr.getLocalName().equals("codice")) {
                    codiceComune = xmlr.getElementText();
                    xmlr.nextTag();
                }
                if (nomeComune.equalsIgnoreCase(comune)) {
                    trovato = true;
                }
            }

            if (trovato) {
                return codiceComune;
            } else {
                return "XXXX";
            }

        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
            return "XXXX";
        }

    }

    public static char generaCifraControllo(StringBuffer codiceFiscale) {
        char ultimaCifra;
        int[] valoriDispari = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11,
                3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };

        double somma = 0;

        for (int i = 0; i < codiceFiscale.length(); i++) {
            if (Character.isLetter(codiceFiscale.charAt(i))) {
                if ((i + 1) % 2 == 0) {
                    somma += (codiceFiscale.charAt(i) - 'A');
                } else {
                    somma += valoriDispari[codiceFiscale.charAt(i) - 'A' + 10];
                }
            } else {
                if ((i + 1) % 2 == 0) {
                    somma += (codiceFiscale.charAt(i) - '0');
                } else {
                    somma += valoriDispari[codiceFiscale.charAt(i) - '0'];
                }
            }
        }

        ultimaCifra = (char) ((somma % 26) + 65);

        return ultimaCifra;
    }

    public static String getDataDiNascita(String codiceAnno, char codiceMese, String codiceGiorno) {
        String dataDiNascita = codiceAnno + "" + codiceMese + "" + codiceGiorno;
        return dataDiNascita;
    }

    public static boolean isConsonante(char carattere) {
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U') {
            return false;
        } else {
            return true;
        }
    }

}
