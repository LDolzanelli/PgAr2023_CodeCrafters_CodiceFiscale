
package it.unibs.fp.codicefiscale;

public class CodiceFiscaleMain {
    // Stringa di comando per "pulire" la console
    public static final String FLUSH = "\033[H\033[2J";

    public static void main(String[] args) {
        String codiceFiscale = CodiceFiscaleGenerator.generaCodiceFiscale("SINGH", "RAJDEEP", "2003-10-25", 'M',
                "ALAMPUR");
        
        System.out.println(codiceFiscale);
    }

}
