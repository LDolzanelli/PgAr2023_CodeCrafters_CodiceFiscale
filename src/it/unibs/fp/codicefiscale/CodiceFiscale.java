package it.unibs.fp.codicefiscale;

public class CodiceFiscale {

    private StringBuffer codicefiscale = new StringBuffer();
    private StringBuffer codiceCognome = new StringBuffer(), codiceNome = new StringBuffer();
    private int codiceAnno, codiceGiorno;
    private char codiceMese;
    private String codiceLuogo;
    private char codiceControllo;
    private char sesso;

    public CodiceFiscale(String nome, String cognome, char sesso) {

        generaCodiceNome(nome);
        generaCodiceCognome(cognome);
        this.sesso = sesso;

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

    private boolean isConsonante(char carattere) {
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U') {
            return false;
        } else {
            return true;
        }
    }

}
