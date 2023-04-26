package it.unibs.fp.codicefiscale;

public class CodiceFiscaleChecker {
    public static boolean checkCodiceFiscale(String codiceFiscale) {
        if (!checkLunghezza(codiceFiscale)) return false;
        else if (!checkNome(codiceFiscale)) return false;
        else if (!checkCognome(codiceFiscale)) return false;
        else if (!checkGiorno(codiceFiscale)) return false;
        else if (!checkMese(codiceFiscale)) return false;
        else if (!checkCodiceControllo(codiceFiscale)) return false;
        else if (!checkPosizioneLettereNumeri(codiceFiscale)) return false;
        
        return true;
    }

    public static boolean checkLunghezza(String codiceFiscale) {
        //controllo validità a seconda della lunghezza
        return codiceFiscale.length() == 16;
    }

    public static boolean checkNomeCognome(String codice) {
        //Per evitare errori di input o di check
        codice = codice.toUpperCase().trim();

        //Viene impostata a true per evitare controlli inutili nel primo loop
        boolean isConsonante = true;

        for(int i = 0; i < codice.length(); i++) {
            //Se è presente un numero si esce immediatamente dal for loop
            if(!Character.isLetter(codice.charAt(i))) {
                return false;
            }

            //Check per controllare che dopo una vocale ci devono essere solo vocali
            if(!isConsonante) {
                //Il boolean controlla che l'ultimo carattere sia una vocale, nel caso viene controllato che il carattere
                //corrente sia anch'esso una vocale. Nel caso di consonante, il codice non è valido.
                if (isConsonanteAndNotX(codice.charAt(i))) {
                    return false;
                }
            }

            //Impostato il boolean per il prossimo check
            isConsonante = isConsonanteAndNotX(codice.charAt(i));
        }

        return true;
    }

    public static boolean checkCognome(String codiceFiscale) {
        String cognome = codiceFiscale.substring(0, 3);
        return checkNomeCognome(cognome);
    }

    public static boolean checkNome(String codiceFiscale) {
        String nome = codiceFiscale.substring(3, 6);
        return checkNomeCognome(nome);
    }

    public static boolean isConsonanteAndNotX(char carattere) {
        //La X viene trattata come caso speciale, dato che dopo una vocale ci può essere una X in nome e cognome
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U' || carattere == 'X') {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkGiorno(String codiceFiscale){
        int giorno = Integer.parseInt(codiceFiscale.substring(9, 11));
        return((giorno >= 1 && giorno <= 31) || (giorno >= 41 && giorno <= 71));
    }

    public static boolean checkMese(String codiceFiscale) {
        char mese = codiceFiscale.charAt(8);
        mese = Character.toUpperCase(mese);
        boolean presente = false;
        int posMese = 0;

        char[] carattereMese = {'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T'};

        for (char c : carattereMese) {
            if (c == mese) {
                presente = true;
                break;
            }
            //Ci tornerà utile nel paragonare il giorno al mese selezionato
            posMese++;
        }

        if(presente) {
            int giorno = Integer.parseInt(codiceFiscale.substring(9, 11));

            //Nel caso sia una donna
            if (giorno >= 41) {
                giorno = giorno - 40;
            }

            int[] giorniDiOgniMese = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            
            if(giorno <= giorniDiOgniMese[posMese]) {
                return true;
            }
        }
    
    return false;
    }

    public static boolean checkCodiceControllo(String codiceFiscale) {
        char codiceDiControlloFornito = codiceFiscale.charAt(15);
        StringBuffer codiceFiscaleTrimmed = new StringBuffer();
        codiceFiscaleTrimmed.append(codiceFiscale.substring(0, 15));
        char codiceDiControlloGenerato = CodiceFiscaleGenerator.generaCifraControllo(codiceFiscaleTrimmed);

        return (codiceDiControlloFornito == codiceDiControlloGenerato);
    }

    public static boolean checkPosizioneLettereNumeri(String codiceFiscale) {
        for (int i = 0; i < codiceFiscale.length(); i++) {
            if((i >= 0 && i <= 5) || (i == 8) || (i == 11) || (i == 15)) {
                if(Character.isDigit(codiceFiscale.charAt(i))) {
                    return false;
                }
            } else {
                if(Character.isLetter(codiceFiscale.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

}
