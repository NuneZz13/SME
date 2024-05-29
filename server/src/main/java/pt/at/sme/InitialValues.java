package pt.at.sme;

public class InitialValues {

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String nif;

    public InitialValues(String nif){
        this.nif = nif;
    }
}
