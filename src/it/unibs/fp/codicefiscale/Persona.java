package it.unibs.fp.codicefiscale;

public class Persona {

    private String nome, cognome,dataDiNascita, luogoDiNascita;
    private char sesso;
    private String codiceFiscale;
    public Persona(String nome, String cognome, String dataDiNascita, String luogoDiNascita, char sesso) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.luogoDiNascita = luogoDiNascita;
        this.sesso = sesso;
        this.codiceFiscale = CodiceFiscaleGenerator.generaCodiceFiscale(cognome, nome, dataDiNascita, sesso, luogoDiNascita);
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getDataDiNascita() {
        return dataDiNascita;
    }
    public void setDataDiNascita(String dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }
    public String getLuogoDiNascita() {
        return luogoDiNascita;
    }
    public void setLuogoDiNascita(String luogoDiNascita) {
        this.luogoDiNascita = luogoDiNascita;
    }
    public char getSesso() {
        return sesso;
    }
    public void setSesso(char sesso) {
        this.sesso = sesso;
    }

    
}
