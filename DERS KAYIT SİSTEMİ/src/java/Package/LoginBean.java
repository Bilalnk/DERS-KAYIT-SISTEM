package Package;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

    private String username;
    private String Password;
    private String girisYontemi;
    private String girisMesaj;
    
    private String mvcKullanici;

    girisBilgileri b;

    private boolean girisDogru;
    
     String url = "jdbc:oracle:thin:@localhost:1521:XE";
     
    @PostConstruct //class çalıştığında ilk çalışır 
    public void init() {
        
       
        this.username = null;
        this.Password = null;

    }

    public String loginGonder() {
        
        String yonlendırme = null;
        mvctKullaniciCIKIS();
        System.out.println("logingondere geldi" + username + Password);        
        b = new girisBilgileri();

        if (girisYontemi != null && girisYontemi.equals("Admin")) {
            girisDogru = login();
            b.setAdmin(1);
            yonlendırme = "mainMenu_Admin?faces-redirect=true";

        } else if (girisYontemi != null && girisYontemi.equals("Kullanıcı")) {
            girisDogru = loginUser();
            b.setAdmin(0);
            yonlendırme = "mainMenu?faces-redirect=true";
        }
        if (girisDogru == true) {
            mvctKullaniciCIKIS();
            girisMesaj = "Giriş Başarılı";
            girisMesaj();
            b.setKullaniciAdi(username);
            mevcutKullanici();

            return yonlendırme;
        } else {
            System.out.println("Giriş Başarısız..");
            girisMesaj = "Giriş Başarısız";

            girisMesaj();
            return null;
        }

    }

    public boolean login() {

        PreparedStatement ps = null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con = null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps = con.prepareStatement("SELECT * FROM ADMINLER WHERE ADMIN = ? AND PASSWORD = ?");
            ps.setString(1, username);
            ps.setString(2, Password);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println("Bir hata meydana geldi:" + exception);
            return false;
        } finally { //try'a da düşse catch'e de bu bloktaki kod çalıştırılacak.
            try {
                if (con != null) { //Connection nesnesi belki yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    con.close();
                }
                if (ps != null) { //PreparedStatement nesnesi yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    ps.close();
                }
            } catch (SQLException sqlException) {
                System.out.println("Bir hata meydana geldi:" + sqlException);
            }
        }

    }

    public boolean loginUser() {

        PreparedStatement ps = null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con = null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps = con.prepareStatement("SELECT * FROM KISI_TABLO WHERE OKUL_NUM = ? AND TC = ?");
            ps.setString(1, username);
            ps.setString(2, Password);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println("Bir hata meydana geldi:" + exception);
            return false;
        } finally { //try'a da düşse catch'e de bu bloktaki kod çalıştırılacak.
            try {
                if (con != null) { //Connection nesnesi belki yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    con.close();
                }
                if (ps != null) { //PreparedStatement nesnesi yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    ps.close();
                }
            } catch (SQLException sqlException) {
                System.out.println("Bir hata meydana geldi:" + sqlException);
            }
        }

    }

     public void mevcutKullanici() {
       
        System.out.println(b.getKullaniciAdi());
        PreparedStatement ps = null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con = null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps = con.prepareStatement("INSERT INTO MEVCUT_KULLANICI(USERNAME , ISADMIN) VALUES(?,?)");
            ps.setString(1, b.getKullaniciAdi());
            ps.setInt(2, b.getAdmin());
            ResultSet rs = ps.executeQuery();
            rs.next();

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println("Bir hata meydana geldi:" + exception);
           
        } finally { //try'a da düşse catch'e de bu bloktaki kod çalıştırılacak.
            try {
                if (con != null) { //Connection nesnesi belki yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    con.close();
                }
                if (ps != null) { //PreparedStatement nesnesi yukarıda null kalmış olabilir. Kontrol etmekte fayda var.
                    ps.close();
                }
            } catch (SQLException sqlException) {
                System.out.println("Bir hata meydana geldi:" + sqlException);
            }
        }

      
    }
    
     
     
     
     public void mvctKullaniciCIKIS(){
         
        PreparedStatement pss= null;
        Connection conn = null;
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        
        int i = 0;       
        try {
            
            Class.forName("oracle.jdbc.OracleDriver");
            conn=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            pss=conn.prepareStatement("DELETE FROM MEVCUT_KULLANICI ");
            
            i=pss.executeUpdate();
              
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.print(e);
            
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
            System.out.println("Çıkış Başarılı");
            
               
        }         
        else{
            System.out.println("Çıkış Başarısız");
            
        }
     }
     
     
     public String getMvcKullnici(){
         PreparedStatement ps=null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con=null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps=con.prepareStatement("SELECT * FROM MEVCUT_KULLANICI");
            ResultSet rs=ps.executeQuery();
            
            while(rs.next())
            {
                int i =0;
                girisBilgileri gb = new girisBilgileri();
                gb.setAdmin(rs.getInt("ISADMIN"));
                gb.setKullaniciAdi(rs.getString("USERNAME"));
              
                
                mvcKullanici = gb.getKullaniciAdi();
                System.out.println(gb.getKullaniciAdi());
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
        return mvcKullanici;
     }
     
    public void girisMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(girisMesaj, "kullanıcı adı ve şifrenizi kontrol edin"));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getGirisMesaj() {
        return girisMesaj;
    }

    public void setGirisMesaj(String girisMesaj) {
        this.girisMesaj = girisMesaj;
    }

    public boolean isGirisDogru() {
        return girisDogru;
    }

    public void setGirisDogru(boolean girisDogru) {
        this.girisDogru = girisDogru;
    }

    public String getGirisYontemi() {
        return girisYontemi;
    }

    public void setGirisYontemi(String girisYontemi) {
        this.girisYontemi = girisYontemi;
    }

    public String getMvcKullanici() {
        return mvcKullanici;
    }

    public void setMvcKullanici(String mvcKullanici) {
        this.mvcKullanici = mvcKullanici;
    }
    
    
}
