package it.unibs.fp.codicefiscale;

public class CodiceFiscaleChecker {

    /**
     * Metodo principale che controlla un codice fiscale qualsiasi per diversi criteri di valutazione
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il codice fiscale è valido, false altrimenti
     */
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
    /**
     * Controlla la lunghezza di un codice fiscale
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il codice fiscale è valido, false altrimenti
     */

    public static boolean checkLunghezza(String codiceFiscale) {
        //controllo validità a seconda della lunghezza
        return codiceFiscale.length() == 16;
    }

    /**
     * Corpo principale del check nome e cognome, generalizzato
     * @param codice il codice da controllare (nome o cognome)
     * @return true se il codice è valido, false altrimenti
     */

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

    /**
     * Controlla la validità di un cognome del codice fiscale
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il cognome è valido, false altrimenti
     */
    public static boolean checkCognome(String codiceFiscale) {
        String cognome = codiceFiscale.substring(0, 3);
        return checkNomeCognome(cognome);
    }

    /**
     * Controlla la validità di un nome del codice fiscale
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il nome è valido, false altrimenti
     */
    public static boolean checkNome(String codiceFiscale) {
        String nome = codiceFiscale.substring(3, 6);
        return checkNomeCognome(nome);
    }

    /**
     * Controlla se un carattere è una vocale o il carattere X, placeholder finale per nomi di lunghezza inferiori a 3
     * @param carattere il carattere da controllare
     * @return true se il carattere è una consonante diversa da X, false se è una vocale o X
     */
    public static boolean isConsonanteAndNotX(char carattere) {
        //La X viene trattata come caso speciale, dato che dopo una vocale ci può essere una X in nome e cognome
        if (carattere == 'A' || carattere == 'E' || carattere == 'I' || carattere == 'O' || carattere == 'U' || carattere == 'X') {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Controlla la validità del giorno all'interno di un codice fiscale
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il giorno è valido, false altrimenti
     */
    public static boolean checkGiorno(String codiceFiscale){
        int giorno = Integer.parseInt(codiceFiscale.substring(9, 11));
        return((giorno >= 1 && giorno <= 31) || (giorno >= 41 && giorno <= 71));
    }

    /**
     * Controlla la presenza del carattere corretto corrispondente al mese, e se i giorni corrispondono al mese presente
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il mese è valido, false altrimenti
     */
    public static boolean checkMese(String codiceFiscale) {
        char mese = codiceFiscale.charAt(8);
        //Per evitare errori di input
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
            
            //A seconda del mese selezionato durante la verifica si controlla quanti giorni ha
            if(giorno <= giorniDiOgniMese[posMese]) {
                return true;
            }
        }
    
    return false;
    }

    /**
     * Controlla la correttezza dell'ultimo carattere di un codice fiscale ricalcolandolo con l'algoritmo già impostato
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il codice di controllo è valido, false altrimenti
     */

    public static boolean checkCodiceControllo(String codiceFiscale) {
        char codiceDiControlloFornito = codiceFiscale.charAt(15);
        //Si crea lo stringbuffer per riutilizzare la funzione generaCifraControllo
        StringBuffer codiceFiscaleTrimmed = new StringBuffer();
        codiceFiscaleTrimmed.append(codiceFiscale.substring(0, 15));
        char codiceDiControlloGenerato = CodiceFiscaleGenerator.generaCifraControllo(codiceFiscaleTrimmed);

        return (codiceDiControlloFornito == codiceDiControlloGenerato);
    }

    /**
     * Controlla la validità dei caratteri all'interno del codice fiscale
     * @param codiceFiscale il codice fiscale da controllare
     * @return true se il codice fiscale è valido, false altrimenti
     */
    public static boolean checkPosizioneLettereNumeri(String codiceFiscale) {
        for (int i = 0; i < codiceFiscale.length(); i++) {
            //Controllo per ogni posizione del codice fiscale
            if((i >= 0 && i <= 5) || (i == 8) || (i == 11) || (i == 15)) {
                //Se viene trovato un numero al posto delle lettere il return è false
                if(Character.isDigit(codiceFiscale.charAt(i))) {
                    return false;
                }
            } else {
                //Se viene trovata una lettera al posto dei numeri il return è false
                if(Character.isLetter(codiceFiscale.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

}
