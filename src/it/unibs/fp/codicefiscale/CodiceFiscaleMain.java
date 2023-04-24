
package it.unibs.fp.codicefiscale;

public class CodiceFiscaleMain {

    public static void main(String[] args) {
        CodiceFiscale codiceFiscale = new CodiceFiscale("Mi", "Pi", "1999-01-21", 'M');
        System.out.println(codiceFiscale.getDataDiNascita());;
    }

}
