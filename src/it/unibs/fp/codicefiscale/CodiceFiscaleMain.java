
package it.unibs.fp.codicefiscale;

public class CodiceFiscaleMain {

    public static void main(String[] args) {
        String codiceFiscale = CodiceFiscaleGenerator.generaCodiceFiscale("Dolzanelli", "Lorenzo", "1999-01-21", 'M',
                "Brescia");
        System.out.println(codiceFiscale);
    }

}
