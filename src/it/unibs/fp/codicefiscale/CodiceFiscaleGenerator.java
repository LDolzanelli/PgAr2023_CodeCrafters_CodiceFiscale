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

    private static String generaCodiceNome(String nome) {
        // per evitare errori di input
        StringBuffer codiceNome = new StringBuffer();
        nome = nome.toUpperCase();
        nome = nome.trim();

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
        } else if (nomeSenzaVocali.length() == 3) {
            codiceNome = nomeSenzaVocali;
        } else {
            codiceNome = nomeSenzaVocali;
            if (nome.length() > 2) {

                for (int i = 0; codiceNome.length() != 3; i++) {
                    if (!isConsonante(nome.charAt(i))) {
                        codiceNome.append(nome.charAt(i));
                    }

                }
            } else {
                for (int i = 0; codiceNome.length() != 3; i++) {
                    if ((i + 1) <= nome.length() && !isConsonante(nome.charAt(i))) {
                        codiceNome.append(nome.charAt(i));
                    }

                    if ((i + 1) > nome.length()) {
                        codiceNome.append('X');
                    }

                }
            }
        }
        return codiceNome.toString();
    }

    private static String generaCodiceCognome(String cognome) {
        // per evitare errori di input
        cognome = cognome.toUpperCase();
        cognome = cognome.trim();

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
        } else {
            codiceCognome = cognomeSenzaVocali;
            if (cognome.length() > 2) {

                for (int i = 0; codiceCognome.length() != 3; i++) {
                    if (!isConsonante(cognome.charAt(i))) {
                        codiceCognome.append(cognome.charAt(i));
                    }

                }
            } else {
                for (int i = 0; codiceCognome.length() != 3; i++) {
                    if ((i + 1) <= cognome.length() && !isConsonante(cognome.charAt(i))) {
                        codiceCognome.append(cognome.charAt(i));
                    }

                    if ((i + 1) > cognome.length()) {
                        codiceCognome.append('X');
                    }

                }
            }
        }
        return codiceCognome.toString();
    }

    private static String generaCodiceAnno(String dataDiNascita) {
        // vengono presi i valori da indice 2 a 4 escluso della data di nascita e
        // convertiti in int
        String codiceAnno = dataDiNascita.substring(2, 4);
        return codiceAnno;
    }

    private static char generaCodiceMese(String dataDiNascita) {
        enum LetteraMese {
            A, B, C, D, E, H, L, M, P, R, S, T
        }

        // converte in int la data di nascita da posizione 5 a 7 esclusa
        int meseDiNascita = Integer.parseInt(dataDiNascita.substring(5, 7));

        // l'int viene usato come indice per accedere al valore Enum desiderato, viene
        // poi convertito in char
        char codiceMese = LetteraMese.values()[meseDiNascita - 1].toString().charAt(0);

        return codiceMese;
    }

    private static String generaCodiceGiorno(String dataDiNascita, char sesso) {
        // converte in int le ultime due cifre della data di nascita
        String codiceGiorno = dataDiNascita.substring(8, 10);

        // a seconda del sesso viene assegnato il giorno di nascita
        if (sesso == 'M') {
            return codiceGiorno;
        } else {
            return String.valueOf(Integer.parseInt(codiceGiorno) + 40);
        }
    }

    public static String getDataDiNascita(String codiceAnno, char codiceMese, String codiceGiorno) {
        String dataDiNascita = codiceAnno + "" + codiceMese + "" + codiceGiorno;
        return dataDiNascita;
    }

    private static boolean isConsonante(char carattere) {
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U') {
            return false;
        } else {
            return true;
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

    private static char generaCifraControllo(StringBuffer codiceFiscale) {
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

}
