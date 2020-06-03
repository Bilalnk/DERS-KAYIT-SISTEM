
package Package;


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
import org.primefaces.event.SelectEvent;


@ManagedBean
@ViewScoped
public class DersSecBean {

    private ArrayList mevcutDersList ;
    private ArrayList seciliDersList = new ArrayList() ;
    private Ders selectedDers ;
    private Ders selectedSecilenDers ;
    String errorMessage;
    Ders secili ;
    boolean dersSecRender ;
    
    
    String url = "jdbc:oracle:thin:@localhost:1521:XE";
    
    @PostConstruct
     public void init() {
         
        
        getCekmevcut();
        getCekSecilen();
        
      
        
      
    }
    
    
    public void getCekmevcut(){
        PreparedStatement ps=null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con=null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps=con.prepareStatement("SELECT * FROM DERS_TABLO");
            ResultSet rs=ps.executeQuery();
            mevcutDersList=new ArrayList();
            while(rs.next())
            {
                Ders dd =new Ders();
                dd.setId(rs.getInt("ID"));
                dd.setDers(rs.getString("Ders"));
                dd.setKontenjan(rs.getString("Kontenjan"));
                dd.setHoca(rs.getString("Hoca"));    
                mevcutDersList.add(dd);
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

    
     public void getCekSecilen(){
        PreparedStatement ps=null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con=null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        LoginBean lb = new LoginBean();
        String kullanici = lb.getMvcKullnici();
        
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps=con.prepareStatement("SELECT * FROM SECILENDERS_TABLO WHERE KULLANICI = ? ");
            ps.setString(1, kullanici);
            
            ResultSet rs=ps.executeQuery();
            seciliDersList=new ArrayList();
            
        
            while(rs.next())
            {
                //UsersDers dd =new UsersDers();
                Ders dd =new Ders();
                dd.setId(rs.getInt("ID"));
                dd.setDers(rs.getString("Ders"));
                dd.setKontenjan(rs.getString("Kontenjan"));
                dd.setHoca(rs.getString("Hoca"));  
                //dd.setKullanici(rs.getString("KULLANICI"));
                
                System.out.println(dd.ders);
                
                seciliDersList.add(dd);
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
        
        if(seciliDersList.isEmpty())
            dersSecRender = true;
        else
            dersSecRender = false;
            
    }
    
    public void veriTabaninaGonder()//Sayfadan girilen verileri veri tabanına gönderem metot.
    {
        LoginBean lb = new LoginBean();
        String kullanici = lb.getMvcKullnici();
        
        int i = 0,j=0;
        PreparedStatement ps=null;
        Connection con = null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");//Bağlanacağı veri tabanını ve kullanacağı kullanıcı adı-parolayı bildiriyoruz.
            
            while(j<seciliDersList.size()){
            secili = new Ders();
            secili = (Ders) seciliDersList.get(j);    
            
                System.out.println(secili.ders);
            
            ps = con.prepareStatement("INSERT INTO SECILENDERS_TABLO(Ders , Kontenjan ,Hoca, KULLANICI) VALUES(?,?,?,?)");//ps nesnesine SQL komutunu bildiriyoruz.İsterseniz parametre olarak SQL kodu yerine üstteki sql de verebilirsiniz.
            ps.setString(1, secili.ders);//ps nesnesine gelen simi koyduk.
            ps.setString(2, secili.kontenjan);//ps nesnesine gelen alanı koyduk.
            ps.setString(3, secili.hoca);
            ps.setString(4, kullanici);
           
            i = ps.executeUpdate();//executeUpdate verilen sorguyu çalıştırır ve integer değer döndürür.
            //exequteUdate eğer 0'dan büyük değer döndürürse kayıt başarılı olmuş demektir. 
            j++;
            }
        

        } catch (ClassNotFoundException | SQLException exception)//Hata olduğunda konsola verilecek.
        {
            System.out.println("Hata:"+exception);
            setErrorMessage(exception.toString());
            
            

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
            
            System.out.println("Kayıt başarılı");
            
            
                 
        } else //Sorgu başarısız ise .
        {
            System.out.println("Kayıt başarısız"); 
            
          
        }
       
    }
    
     
     public void kayitSil()
    {
        PreparedStatement pss= null;
        Connection conn = null;
        LoginBean lb = new LoginBean();
        String kullanici = lb.getMvcKullnici();
        int i = 0;       
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            pss=conn.prepareStatement("DELETE FROM SECILENDERS_TABLO WHERE KULLANICI = ? ");
            pss.setString(1, kullanici);
            
            i=pss.executeUpdate();
              
        }
        catch(Exception e)
        {
            e.printStackTrace();
//            System.out.print(exception);
            
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
            
               
        }         
        else{
            System.out.println("Silme Başarısız");
            
        }
            
    }
    
    
      
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Ders Seçildi", ((Ders) event.getObject()).ders);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        secili = new Ders();
        secili = selectedDers;
        seciliDersList.add(secili);
        mevcutDersList.remove(selectedDers);   
        
    }
    public void onRowSelect2(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Ders Bıraklıdı", ((Ders) event.getObject()).ders);
        FacesContext.getCurrentInstance().addMessage(null, msg);
 
        mevcutDersList.add(selectedSecilenDers);       
        seciliDersList.remove(selectedSecilenDers);       
     
    }
    
    public String DersKaydetonay() {
        addMessage("Ders Eklendi",seciliDersList.size()+" ders eklendi.");
        veriTabaninaGonder();
        return "derslerim?faces-redirect=true";
    }
    
      public String DersSilonay() {
        addMessage("Ders Silme",seciliDersList.size()+" dersler silindi.");
        kayitSil();
        return "mainMenu?faces-redirect=true";
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public String getDersSilonay(){
        return DersSilonay();
    }
    
    
    public ArrayList getMevcutDersList() {
        return mevcutDersList;
    }

    public void setMevcutDersList(ArrayList mevcutDersList) {
        this.mevcutDersList = mevcutDersList;
    }

    public ArrayList getSeciliDersList() {
        return seciliDersList;
    }

    public void setSeciliDersList(ArrayList seciliDersList) {
        this.seciliDersList = seciliDersList;
    }
      
    
    public Ders getSelectedDers() {
        return selectedDers;
    }

    public void setSelectedDers(Ders selectedDers) {
        this.selectedDers = selectedDers;
    }

    public Ders getSelectedSecilenDers() {
        return selectedSecilenDers;
    }

    public void setSelectedSecilenDers(Ders selectedSecilenDers) {
        this.selectedSecilenDers = selectedSecilenDers;
    }

    public boolean isDersSecRender() {
        return dersSecRender;
    }

    public void setDersSecRender(boolean dersSecRender) {
        this.dersSecRender = dersSecRender;
    }

    
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    
    
    
      
              
      
      

      
              
}
