
package it.unibs.fp.codicefiscale;

public class CodiceFiscaleMain {

    public static void main(String[] args) {
        String codiceFiscale = CodiceFiscaleGenerator.generaCodiceFiscale("Khater", "Abdelaziz", "2002-10-20", 'M',
                "Monza");
        System.out.println(codiceFiscale);
    }

}
