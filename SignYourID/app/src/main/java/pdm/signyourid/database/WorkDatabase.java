package pdm.signyourid.database;

import java.io.Serializable;

public class WorkDatabase implements Serializable {

    // Atributos para registrar que un trabajador ha fichado
    private String wrkDate; // Fecha del registro
    private String wrkTimeIn; // Hora de entrada
    private String wrkTimeOut; // Hora de salida
    private String wrkTurn; // Por si es turno 1, 2 o único

    // Vendra bien tener el constructor por defecto para crear un objeto de este tipo
    // y comprobar que si el valor del dni es -wd es que no ha devuelto nada la consulta de SQL
    // y entonces no existe ningun campo en la tabla donde coincida ese DNI para esa fecha
    public WorkDatabase() { // Cada objeto de este tipo deberá ser único por usuario
        this.wrkDate = "-wdt";
        this.wrkTimeIn = "";
        this.wrkTimeOut = "";
        this.wrkTurn = "";
    }

    public WorkDatabase(String wrkDate, String wrkIn, String wrkOut, String wrkTurn) {
        this.wrkDate = wrkDate;
        this.wrkTimeIn = wrkIn;
        this.wrkTimeOut = wrkOut;
        this.wrkTurn = wrkTurn;
    }

    public String getWrkDate() {return wrkDate;}
    public String getWrkTimeIn() {return wrkTimeIn;}
    public String getWrkTimeOut() {return wrkTimeOut;}
    public String getWrkTurn() {return wrkTurn;}

    public void setWrkDate(String wrkDate) {this.wrkDate = wrkDate;}
    public void setWrkTimeIn(String wrkIn) {this.wrkTimeIn = wrkIn;}
    public void setWrkTimeOut(String wrkOut) {this.wrkTimeOut = wrkOut;}
    public void setWrkTurn(String wrkTurn) {this.wrkTurn = wrkTurn;}

    @Override
    public String toString() {
        return "WorkDatabase{" +
                "wrkDate='" + wrkDate + '\'' +
                ", wrkTimeIn='" + wrkTimeIn + '\'' +
                ", wrkTimeOut='" + wrkTimeOut + '\'' +
                ", wrkTurn='" + wrkTurn + '\'' +
                '}';
    }
}
