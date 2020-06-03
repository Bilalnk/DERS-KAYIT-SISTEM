
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


@ManagedBean
@ViewScoped
public class HocaBean {

    private String unvan;
    private String isim;
    private String soyisim;
    private String cinsiyet;
    private String bilimDali;
    private ArrayList listcek;
    private ArrayList<String> hocaList;
    private Hoca selectedHoca;

    private String KayitMesaji;
    private String SilMesaji;
    String errorMessage;
    
    String url = "jdbc:oracle:thin:@localhost:1521:XE";
    
    @PostConstruct //CLASS çalıştığında ilk çalışır 
    public void init() {

        this.unvan = null;
        this.isim = null;
        this.soyisim = null;
        this.cinsiyet = null;
        this.bilimDali = null;
        
        getCek();
        
        LoginBean bean = new LoginBean();
        bean.getMvcKullnici();
   
    }
    
    public void hocaKaydet(){
        
        if(isim != "" && soyisim != "" && cinsiyet != "" && bilimDali != "" )
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
            ps = con.prepareStatement("INSERT INTO HOCA_TABLO(Unvan , ADI ,SOYADI, CINSIYET ,BILIMDALI) VALUES(?,?,?,?,?)");//ps nesnesine SQL komutunu bildiriyoruz.İsterseniz parametre olarak SQL kodu yerine üstteki sql de verebilirsiniz.
            ps.setString(1, unvan);//ps nesnesine gelen simi koyduk.
            ps.setString(2, isim);//ps nesnesine gelen alanı koyduk.
            ps.setString(3, soyisim);//ps nesnesine gelen simi koyduk.
            ps.setString(4, cinsiyet);//ps nesnesine gelen alanı koyduk.
            ps.setString(5, bilimDali);//ps nesnesine gelen alanı koyduk.

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
        
        this.unvan = null;
        this.isim = null;
        this.soyisim = null;
        this.cinsiyet = null;
        this.bilimDali = null;
    }

    public void getCek(){
        PreparedStatement ps=null;//SQL sorgumuzu tutacak ve çalıştıracak nesne.
        Connection con=null;//Veri tabanına bağlantı yapmamızı sağlayacak nesne.
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection(url, "BILALNADIR", "Bilal.197");
            ps=con.prepareStatement("SELECT * FROM HOCA_TABLO");
            ResultSet rs=ps.executeQuery();
            listcek=new ArrayList();
            hocaList=new ArrayList();
            while(rs.next())
            {
                int i =0;
                Hoca hh =new Hoca();
                hh.setId(rs.getInt("ID"));
                hh.setUnvan(rs.getString("UNVAN"));
                hh.setIsim(rs.getString("ADI"));
                hh.setSoyisim(rs.getString("SOYADI"));    
                hh.setCinsiyet(rs.getString("CINSIYET"));
                hh.setBilimDali(rs.getString("BILIMDALI")); 
                hh.birleştir(rs.getString("UNVAN"));
                hh.birleştir(rs.getString("ADI"));
                hh.birleştir(rs.getString("SOYADI"));
                listcek.add(hh);
                hocaList.add(hh.HocaBilgi);
             
                System.out.println(hh.HocaBilgi);
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
            pss=conn.prepareStatement("DELETE FROM HOCA_TABLO WHERE ID=?");
            pss.setInt(1, selectedHoca.getId());
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
            selectedHoca = null;
            silMesaj();
            getCek();      
        }         
        else{
            System.out.println("Silme Başarısız");
            SilMesaji = "Silme Başarısız";
            silMesaj();
        }
            
    }
    
    
    public void onay() {
        addMessage("Silme Başarılı", selectedHoca.isim+" silindi.");
        kayitSil();
    }
    
    public void kayitMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( KayitMesaji, ""));
    }
    
     public void silMesaj() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage( SilMesaji, ""));
    }
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getKayitMesaji() {
        return KayitMesaji;
    }

    public void setKayitMesaji(String KayitMesaji) {
        this.KayitMesaji = KayitMesaji;
    }

    public ArrayList getListcek() {
        return listcek;
    }

    public void setListcek(ArrayList listcek) {
        this.listcek = listcek;
    }

    public Hoca getSelectedHoca() {
        return selectedHoca;
    }

    public void setSelectedHoca(Hoca selectedHoca) {
        this.selectedHoca = selectedHoca;
    }

    public String getSilMesaji() {
        return SilMesaji;
    }

    public void setSilMesaji(String SilMesaji) {
        this.SilMesaji = SilMesaji;
    }

    public ArrayList getHocaList() {
        return hocaList;
    }

    public void setHocaList(ArrayList hocaList) {
        this.hocaList = hocaList;
    }
    
    
    
    
    
    
    
    
    
    
}
