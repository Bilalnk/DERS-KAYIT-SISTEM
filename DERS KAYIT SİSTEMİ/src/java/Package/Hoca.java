
package Package;


public class Hoca {
    
    int id;
    String unvan;
    String isim;
    String soyisim;
    String cinsiyet;
    String bilimDali;
    String HocaBilgi="";

    public void birleştir(String bilgi){
        
        HocaBilgi = HocaBilgi.concat(bilgi+" ");
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSoyisim() {
        return soyisim;
    }

    public void setSoyisim(String soyisim) {
        this.soyisim = soyisim;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getBilimDali() {
        return bilimDali;
    }

    public void setBilimDali(String bilimDali) {
        this.bilimDali = bilimDali;
    }

    public String getHocaBilgi() {
        return HocaBilgi;
    }

    public void setHocaBilgi(String HocaBilgi) {
        this.HocaBilgi = HocaBilgi;
    }
    
    
    
}
