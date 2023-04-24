package it.unibs.fp.codicefiscale;

public class CodiceFiscale {

    private StringBuffer codicefiscale = new StringBuffer();
    private StringBuffer codiceCognome = new StringBuffer(), codiceNome = new StringBuffer();
    private int codiceAnno, codiceGiorno;
    private char codiceMese;
    private String codiceLuogo;
    private char codiceControllo;

    public CodiceFiscale(String nome, String cognome, String dataDiNascita, char sesso) {
        generaCodiceNome(nome);
        generaCodiceCognome(cognome);
        generaCodiceAnno(dataDiNascita);
        generaCodiceMese(dataDiNascita);
        generaCodiceGiorno(dataDiNascita, sesso);


    }

    private void generaCodiceNome(String nome) {
        // per evitare errori di input
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
    }

    private void generaCodiceCognome(String cognome) {
        // per evitare errori di input
        cognome = cognome.toUpperCase();
        cognome = cognome.trim();

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
    }

    private void generaCodiceAnno(String dataDiNascita) {
        //vengono presi i valori da indice 2 a 4 escluso della data di nascita e convertiti in int
        codiceAnno = Integer.parseInt(dataDiNascita.substring(2, 4));
    }

    private void generaCodiceMese(String dataDiNascita) {
        enum LetteraMese {
            A, B, C, D, E, H, L, M, P, R, S, T
        }

        //converte in int la data di nascita da posizione 5 a 7 esclusa
        int meseDiNascita = Integer.parseInt(dataDiNascita.substring(5, 7));

        //l'int viene usato come indice per accedere al valore Enum desiderato, viene poi convertito in char
        codiceMese = LetteraMese.values()[meseDiNascita - 1].toString().charAt(0);
    }

    private void generaCodiceGiorno(String dataDiNascita, char sesso) {
        //converte in int le ultime due cifre della data di nascita
        int giornoDiNascita = Integer.parseInt(dataDiNascita.substring(8, 10));

        //a seconda del sesso viene assegnato il giorno di nascita
        if(sesso == 'M') {
            codiceGiorno = giornoDiNascita;
        }
        else {
            codiceGiorno = giornoDiNascita + 40;
        }
    }

    public String getDataDiNascita() {
        String dataDiNascita = codiceAnno + "" +  codiceMese + "" + codiceGiorno;
        return dataDiNascita;
    }


    private boolean isConsonante(char carattere) {
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U') {
            return false;
        } else {
            return true;
        }
    }

    
}
