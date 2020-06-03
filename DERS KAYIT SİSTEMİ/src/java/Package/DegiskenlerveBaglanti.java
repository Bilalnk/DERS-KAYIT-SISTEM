package Package;

import Package.Kisi;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import java.sql.*;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class DegiskenlerveBaglanti implements Serializable {

    private String isimSoyisim;
    private String tc;
    private String cinsiyet;
    private String il;
    private String adres;
    
    
    private String okul_num;
    
    
    private Kisi selectedKisi;
    private List list;
    private String KayitMesaji;
    private String SilMesaji;
    private String GuncelleMesaji;
    private String HataMesaji;
    private ArrayList listList;
    
    String errorMessage;
    
     String url = "jdbc:oracle:thin:@localhost:1521:XE";
  
    @PostConstruct //CLASS çalıştığında ilk çalışır 
    public void init() {

        list = new ArrayList();
        this.isimSoyisim = null;
        this.okul_num = null;
        this.tc = null;
        this.cinsiyet = null;
        this.il = null;
        this.adres = null;
        getCek();
   
    }

    

    public void veriTabaninaGonder()//Sayfadan girilen verileri veri tabanına gönderem metot.
    {
       
        int i = 0;
        PreparedStatement ps=null;
        Connection con = null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");//Bağlanacağı veri tabanını ve kullanacağı kullanıcı adı-parolayı bildiriyoruz.
            ps = con.prepareStatement("INSERT INTO KISI_TABLO(ADI , TC ,CINSIYET, IL ,ADRES, OKUL_NUM) VALUES(?,?,?,?,?,?)");//ps nesnesine SQL komutunu bildiriyoruz.İsterseniz parametre olarak SQL kodu yerine üstteki sql de verebilirsiniz.
            ps.setString(1, isimSoyisim);//ps nesnesine gelen simi koyduk.
            ps.setString(2, tc);//ps nesnesine gelen alanı koyduk.
            ps.setString(3, cinsiyet);//ps nesnesine gelen simi koyduk.
            ps.setString(4, il);//ps nesnesine gelen alanı koyduk.
            ps.setString(5, adres);//ps nesnesine gelen alanı koyduk.
            ps.setString(6, okul_num);
            i = ps.executeUpdate();//executeUpdate verilen sorguyu çalıştırır ve integer değer döndürür.
            //exequteUdate eğer 0'dan büyük değer döndürürse kayıt başarılı olmuş demektir. 

        

        } catch (ClassNotFoundException | SQLException exception)//Hata olduğunda konsola verilecek.
        {
            System.out.println("Hata:"+exception);
            setErrorMessage(exception.toString());
            HataMesaji = exception.toString();
            

        } finally //Ne olursa olsun her koşulda çalışacak kısım 
        {
            
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            }
        }
        if (i > 0) {
            getCek();
            System.out.println("Kayıt başarılı");
            KayitMesaji = "Kayıt başarılı";
            kayitMesaj();
            
                 
        } else //Sorgu başarısız ise .
        {
            System.out.println("Kayıt başarısız");
            KayitMesaji = "Kayıt başarısız";
            kayitMesaj();
          
        }
        
        this.isimSoyisim = null;
        this.tc = null;
        this.cinsiyet = null;
        this.il = null;
        this.adres = null;
        this.okul_num = null;
    }

    public void guncelleDebug()//Güncellemeyi veri tabanına aktaracak metod.
    {
        System.out.println("güncelle çağrıldı.");

        int i = 5;
        Connection con ;
        

        try {
           Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            PreparedStatement prepareds = con.prepareStatement("UPDATE KISI_TABLO SET ADI=?,TC=?,CINSIYET=?,IL=?,ADRES=?, OKUL_NUM=? WHERE ID=?");

            prepareds.setInt(6, selectedKisi.getId());
            prepareds.setString(1, selectedKisi.getIsimSoyisim());
            prepareds.setString(2, selectedKisi.getTc());
            prepareds.setString(3, selectedKisi.getCinsiyet());
            prepareds.setString(4, selectedKisi.getIl());
            prepareds.setString(5, selectedKisi.getAdres());
            prepareds.setString(6, selectedKisi.getOkul_num());
            
                
           // i = prepareds.executeUpdate();//İşlem  başarılı olursa i 0'dan büyük değer alır. Olmazsa küçük değer alır.
            i = prepareds.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.print(e);
            HataMesaji = e.toString();
        }
        if (i>0  ) {
            System.out.println("Güncelleme Başarılı");
            GuncelleMesaji = "Güncelleme Başarılı.." + i;
            guncelleMesaj();
            
        } else {
            System.out.println("Güncelleme Başarısız");
            GuncelleMesaji = "Güncelleme Başarısız..!" +i;
            guncelleMesaj();
        }
    
    }
    
    public void kayitSil()
    {
        PreparedStatement pss= null;
        Connection conn = null;
        int i = 0;       
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            pss=conn.prepareStatement("DELETE FROM KISI_TABLO WHERE ID=?");
            pss.setInt(1, selectedKisi.getId());
            i=pss.executeUpdate();
              
        }
        catch(SQLException | ClassNotFoundException exception)
        {
            System.out.print(exception);
            HataMesaji = exception.toString();
        }
        finally {
           
            try {
                if(conn!= null){
                    conn.close();
                }
                if (pss!=null) {
                    pss.close();
                }
            } 
            catch (SQLException exception) {
                System.out.println("Bir Hata Meydana Geldi!\nHata:"+exception);
            }
        }
        if(i>0){
            System.out.println("Silme Başarılı");
            SilMesaji = "Silme Başarılı";
            selectedKisi = null;
            silMesaj();
            getCek();      
        }         
        else{
            System.out.println("Silme Başarısız");
            SilMesaji = "Silme Başarısız";
            silMesaj();
        }
            
    }
    
    public void getCek(){
        PreparedStatement ps=null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con=null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps=con.prepareStatement("SELECT * FROM KISI_TABLO");
            ResultSet rs=ps.executeQuery();
            listList=new ArrayList();
            while(rs.next())
            {
                Kisi aa =new Kisi();
                aa.setId(rs.getInt("ID"));
                aa.setIsimSoyisim(rs.getString("ADI"));
                aa.setTc(rs.getString("TC"));
                aa.setOkul_num(rs.getString("OKUL_NUM"));
                aa.setCinsiyet(rs.getString("CINSIYET"));
                aa.setIl(rs.getString("IL"));
                aa.setAdres(rs.getString("ADRES"));              
                listList.add(aa);
            }
            
        } 
        catch (ClassNotFoundException | SQLException exception) {
            System.out.println("Bir hata meydana geldi:"+exception);
            
        }
        finally{ //try'a da düşse catch'e de bu bloktaki kod çalıştırılacak.
            try {
                if(con!=null){ //Connection nesnesi belki yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    con.close();
                }
                if(ps!=null){ //PreparedStatement nesnesi yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    ps.close();
                }
            } catch (SQLException sqlException) {
                System.out.println("Bir hata meydana geldi:"+sqlException);
            }
        }
            
    }
    
    
     public void kayitMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( KayitMesaji, ""));
    }
     
     public void silMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( SilMesaji, ""));
    }
     
     public void guncelleMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( GuncelleMesaji, ""));
    }
     
    public void HataMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( "Bir Hata Meydana Geldi", HataMesaji));
    }
     
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Selected", ((Kisi) event.getObject()).isimSoyisim);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        

    }

    public String guncellenebilirligiDegistir() {
        selectedKisi.setGuncellenebilirlik(true);
        return null;
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

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Kisi getSelectedKisi() {
        return selectedKisi;
    }
    public void setSelectedKisi(Kisi selectedKisi) {
        this.selectedKisi = selectedKisi;
    }

    public List getList() {
        return list;
    }
    public void setList(List list) {
        this.list = list;
    }

    public String getKayitMesaji() {
        return KayitMesaji;
    }
    public void setKayitMesaji(String KayitMesaji) {
        this.KayitMesaji = KayitMesaji;
    }
    
    public String getSilMesaji() {
        return SilMesaji;
    }
    public void setSilMesaji(String SilMesaji) {
        this.SilMesaji = SilMesaji;
    }
    
    public String getGuncelleMesaji() {
        return GuncelleMesaji;
    }
    public void setGuncelleMesaji(String GuncelleMesaji) {
        this.GuncelleMesaji = GuncelleMesaji;
    }
    
    public String getHataMesaji() {
        return HataMesaji;
    }
    public void setHataMesaji(String HataMesaji) {
        this.HataMesaji = HataMesaji;
    }
    
    public ArrayList getListList() {
        return listList;
    }

    public void setListList(ArrayList listList) {
        this.listList = listList;
    }
}
