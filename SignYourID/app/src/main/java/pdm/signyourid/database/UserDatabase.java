package pdm.signyourid.database;

import java.io.Serializable;

public class UserDatabase implements Serializable {

    // Atributos para completar la información de un usuario
    private String usrDni;
    private String usrName;
    private String usrSName;
    private String usrPassword;
    private String usrBirth;
    private String usrPhone;
    private String usrEmail;

    // Vendra bien tener el constructor por defecto para crear un objeto de este tipo
    // y comprobar que si el valor del dni es -d es que no ha devuelto nada la consulta de SQL
    // Y por tanto, querrá decir que este usuario no existe
    public UserDatabase(){
        this.usrDni = "-d";
        this.usrName = "-n";
        this.usrSName = "";
        this.usrPassword = "";
        this.usrBirth = "";
        this.usrPhone = "0";
        this.usrEmail = "";

    }

    //public UserDatabase(String usrDni, String usrName, String usrSName, String usrPassword, String usrBirth, int usrPhone, String usrEmail, String usrSex) {
    public UserDatabase(String usrDni, String usrPassword, String usrName, String usrSName, String usrBirth, String usrPhone, String usrEmail) {
        this.usrDni = usrDni;
        this.usrName = usrName;
        this.usrSName = usrSName;
        this.usrPassword = usrPassword;
        this.usrBirth = usrBirth;
        this.usrPhone = usrPhone;
        this.usrEmail = usrEmail;
    }

    public String getUsrDni() {return usrDni;}
    public String getUsrName() {return usrName;}
    public String getUsrSName() {return usrSName;}
    public String getUsrPassword() {return usrPassword;}
    public String getUsrBirth() {return usrBirth;}
    public String getUsrPhone() {return usrPhone;}
    public String getUsrEmail() {return usrEmail;}


    public void setUsrDni(String usrDni) {this.usrDni = usrDni;}
    public void setUsrName(String usrName) {this.usrName = usrName;}
    public void setUsrSName(String usrSName) {this.usrSName = usrSName;}
    public void setUsrPassword(String usrPassword) {this.usrPassword = usrPassword;}
    public void setUsrBirth(String usrBirth) {this.usrBirth = usrBirth;}
    public void setUsrPhone(String usrPhone) {this.usrPhone = usrPhone;}
    public void setUsrEmail(String usrEmail) {this.usrEmail = usrEmail;}

    @Override
    public String toString() {
        return "UserDatabase{" +
                "usrDni='" + usrDni + '\'' +
                ", usrName='" + usrName + '\'' +
                ", usrSName='" + usrSName + '\'' +
                ", usrPassword='" + usrPassword + '\'' +
                ", usrBirth='" + usrBirth + '\'' +
                ", usrPhone=" + usrPhone +
                ", usrEmail='" + usrEmail + '\'' +
                '}';
    }
}
