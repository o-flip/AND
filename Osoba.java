package kontakty;

import java.util.HashSet;
import java.util.Set;

public class Osoba {
    private String prezdivka;
    private Set<Kontakt> kontakty;
    
    public Osoba(String prezdivka, Kontakt ... kontakty) {
        this.prezdivka = prezdivka;
        this.kontakty = new HashSet<>();
        for (Kontakt kontakty1 : kontakty) {
            this.kontakty.add(kontakty1);
        }
    }

    @Override
    public String toString() {
        return "Osoba{" + "prezdivka=" + prezdivka + ", kontakty=" + kontakty + '}';
    }
    
    public void pridejKontakt(Kontakt k) {
        kontakty.add(k);
    }
    
}
