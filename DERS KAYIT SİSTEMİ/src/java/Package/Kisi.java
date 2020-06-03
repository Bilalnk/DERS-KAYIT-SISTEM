
package Package;

public class Kisi {

    int id;
    String isimSoyisim;
    String tc ;
    String cinsiyet;
    String il;
    String adres;
    String okul_num;
    boolean guncellenebilirlik;

    public boolean isGuncellenebilirlik() {
        return guncellenebilirlik;
    }

    public void setGuncellenebilirlik(boolean guncellenebilirlik) {
        this.guncellenebilirlik = guncellenebilirlik;
    }

    
    public String getIsimSoyisim() {
        return isimSoyisim;
    }

    public void setIsimSoyisim(String isimSoyisim) {
        this.isimSoyisim = isimSoyisim;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getOkul_num() {
        return okul_num;
    }

    public void setOkul_num(String okul_num) {
        this.okul_num = okul_num;
    }

    
    
    public String getIl() {
        return il;
    }

    public void setIl(String il) {
        this.il = il;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
   
    
    
}
