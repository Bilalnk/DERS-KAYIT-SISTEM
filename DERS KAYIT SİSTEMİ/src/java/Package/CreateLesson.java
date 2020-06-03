
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
import org.primefaces.event.CloseEvent;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class CreateLesson implements Serializable {

    private String ders;
    private String hoca;
    private String kontenjan;
    private ArrayList list;
    private ArrayList listList;
    private Ders selectedDers;
    private String SilMesaji;
    
    String errorMessage;
    boolean secildi = false;
    
     String url = "jdbc:oracle:thin:@localhost:1521:XE";
    
    @PostConstruct
     public void init() {

        list = new ArrayList();
        this.ders = null;
        this.hoca = null;
        this.kontenjan = null;
        getCek();
   
    }
    
    public void dersKaydet(){
        
        if(ders != "" && kontenjan != "" && hoca != "" )
            veriTabaninaGonder();
        else 
          addMessage("Kayıt Başarısız.","");   
    }
    
    public void veriTabaninaGonder()//Sayfadan girilen verileri veri tabanına gönderem metot.
    {
       
        int i = 0;
        PreparedStatement ps=null;
        Connection con = null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");//Bağlanacağı veri tabanını ve kullanacağı kullanıcı adı-parolayı bildiriyoruz.
            ps = con.prepareStatement("INSERT INTO DERS_TABLO(Ders , Kontenjan ,Hoca) VALUES(?,?,?)");//ps nesnesine SQL komutunu bildiriyoruz.İsterseniz parametre olarak SQL kodu yerine üstteki sql de verebilirsiniz.
            ps.setString(1, ders);//ps nesnesine gelen simi koyduk.
            ps.setString(2, kontenjan);//ps nesnesine gelen alanı koyduk.
            ps.setString(3, hoca);
           
            i = ps.executeUpdate();//executeUpdate verilen sorguyu çalıştırır ve integer değer döndürür.
            //exequteUdate eğer 0'dan büyük değer döndürürse kayıt başarılı olmuş demektir. 

        

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
            kayitMesaj(i);
            getCek();
            
                 
        } else //Sorgu başarısız ise .
        {
            System.out.println("Kayıt başarısız"); 
            kayitMesaj(i);
          
        }
        
        this.ders = null;
        this.hoca = null;
        this.kontenjan = null;
        
       
    }
    
    
    public void getCek(){
        PreparedStatement ps=null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con=null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps=con.prepareStatement("SELECT * FROM DERS_TABLO");
            ResultSet rs=ps.executeQuery();
            listList=new ArrayList();
            while(rs.next())
            {
                Ders dd =new Ders();
                dd.setId(rs.getInt("ID"));
                dd.setDers(rs.getString("Ders"));
                dd.setKontenjan(rs.getString("Kontenjan"));
                dd.setHoca(rs.getString("Hoca"));    
                listList.add(dd);
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
    
    
     public void kayitSil()
    {
        PreparedStatement pss= null;
        Connection conn = null;
        int i = 0;       
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            pss=conn.prepareStatement("DELETE FROM DERS_TABLO WHERE ID=?");
            pss.setInt(1, selectedDers.getId());
            i=pss.executeUpdate();
              
        }
        catch(SQLException | ClassNotFoundException exception)
        {
            System.out.print(exception);
            
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
            selectedDers = null;
            silMesaj();
            getCek();      
        }         
        else{
            System.out.println("Silme Başarısız");
            SilMesaji = "Silme Başarısız";
            silMesaj();
        }
            
    }
    
    
     
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Selected", ((Ders) event.getObject()).ders);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        secildi = true ;

    }
    
    public void handleClose(CloseEvent event) {
        addMessage(event.getComponent().getId() + " closed", "So you don't like nature?");
    }
     
    public void onay() {
        addMessage("Silme Başarılı", selectedDers.ders+" dersi silindi.");
        kayitSil();
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    
    
    public void kayitMesaj(int i) {
        if(i>0)
        addMessage("Kayıt Başarıllı.", ders+" dersi eklendi.");
        else
        addMessage("Kayıt Başarısız."," ders eklenemedi.");
    }

    
    public void silMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( SilMesaji, ""));
    }
    
    
    public String getDers() {
        return ders;
    }
    public void setDers(String ders) {
        this.ders = ders;
    }

    public String getHoca() {
        return hoca;
    }
    public void setHoca(String hoca) {
        this.hoca = hoca;
    }

    public String getKontenjan() {
        return kontenjan;
    }
    public void setKontenjan(String kontenjan) {
        this.kontenjan = kontenjan;
    }

    public String getSilMesaji() {
        return SilMesaji;
    }
    public void setSilMesaji(String SilMesaji) {
        this.SilMesaji = SilMesaji;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
  
    public ArrayList getList() {
        return list;
    }
    public void setList(ArrayList list) {
        this.list = list;
    }

    public ArrayList getListList() {
        return listList;
    }
    public void setListList(ArrayList listList) {
        this.listList = listList;
    }

    public Ders getSelectedDers() {
        return selectedDers;
    }
    public void setSelectedDers(Ders selectedDers) {
        this.selectedDers = selectedDers;
    }

    public boolean isSecildi() {
        return secildi;
    }

    public void setSecildi(boolean secildi) {
        this.secildi = secildi;
    }
            
    
    
    
    
    
}
