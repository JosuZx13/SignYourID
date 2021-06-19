package pdm.signyourid;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Informacion {

    private Activity fromActivity; // Activity que ha llamado a esta clase
    private Calendar cal;

    public Informacion(Activity a){
        fromActivity = a;
        cal = Calendar.getInstance();
    }

    // Se obtiene la fecha actual
    public String getDate(){

        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Se suma uno porque devuelve entre 0 y 11
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);

        return month + "-" + day + "-" + year;
    }

    // Se obtiene la hora actual
    public String getTime(){

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if(minute < 10){
            return  hour + ":0" + minute;
        }

        return  hour + ":" + minute;
    }

    // Sirve para mostrar mensajes por pantalla
    public void toastAdvice(String t){
        LayoutInflater inflater = fromActivity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, fromActivity.findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(t);
        text.setTextSize(15);

        Toast toast = new Toast(fromActivity);
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
