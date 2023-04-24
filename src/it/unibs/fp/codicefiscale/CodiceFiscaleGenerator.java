package it.unibs.fp.codicefiscale;

public class CodiceFiscaleGenerator {

    public static String generaCodiceFiscale(String cognome, String nome, String dataDiNascita, char sesso,
        String comune) {
        StringBuffer codiceFiscale = new StringBuffer();

        codiceFiscale.append(generaCodiceCognome(cognome));
        codiceFiscale.append(generaCodiceNome(nome));
        codiceFiscale.append(generaCodiceAnno(dataDiNascita));
        codiceFiscale.append(generaCodiceMese(dataDiNascita));
        codiceFiscale.append(generaCodiceGiorno(dataDiNascita, sesso));
        codiceFiscale.append(generaCodiceLuogo(comune));


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

    private static int generaCodiceAnno(String dataDiNascita) {
        //vengono presi i valori da indice 2 a 4 escluso della data di nascita e convertiti in int
        int codiceAnno = Integer.parseInt(dataDiNascita.substring(2, 4));
        return codiceAnno;
    }

    private static char generaCodiceMese(String dataDiNascita) {
        enum LetteraMese {
            A, B, C, D, E, H, L, M, P, R, S, T
        }

        //converte in int la data di nascita da posizione 5 a 7 esclusa
        int meseDiNascita = Integer.parseInt(dataDiNascita.substring(5, 7));

        //l'int viene usato come indice per accedere al valore Enum desiderato, viene poi convertito in char
        char codiceMese = LetteraMese.values()[meseDiNascita - 1].toString().charAt(0);

        return codiceMese;
    }

    private static int generaCodiceGiorno(String dataDiNascita, char sesso) {
        //converte in int le ultime due cifre della data di nascita
        int codiceGiorno = Integer.parseInt(dataDiNascita.substring(8, 10));

        //a seconda del sesso viene assegnato il giorno di nascita
        if(sesso == 'M') {
            return codiceGiorno;
        }
        else {
            return codiceGiorno + 40;
        }
    }


    public static String getDataDiNascita(int codiceAnno, char codiceMese, int codiceGiorno) {
        String dataDiNascita = codiceAnno + "" +  codiceMese + "" + codiceGiorno;
        return dataDiNascita;
    }


    private static boolean isConsonante(char carattere) {
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U') {
            return false;
        } else {
            return true;
        }
    }

    public static Object generaCodiceLuogo(String comune) {
        return ComuneCodiceMapper.getCodiceComune(comune);
    }
}
