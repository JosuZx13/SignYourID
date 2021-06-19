package pdm.signyourid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;

import pdm.signyourid.database.WorkDatabase;

public class FragmentCalendar extends FragmentActivity {

    private CalendarView calendarView;
    private HashMap<String, ArrayList<WorkDatabase>> listWorkDay;

    private Informacion info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar);

        info = new Informacion(this);

        calendarView = findViewById(R.id.calendar_record);

        listWorkDay = (HashMap<String, ArrayList<WorkDatabase>>) getIntent().getSerializableExtra("WK_");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String key = (month+1) + "-" + dayOfMonth + "-" + year;


                ArrayList<WorkDatabase> list = searchDate(key);

                if(list.size() > 0){
                    Intent showDay = new Intent(FragmentCalendar.this, FragmentTurn.class);
                    showDay.putExtra("LST_WK", list);
                    ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                    startActivity(showDay, animacion.toBundle());
                }else{
                    info.toastAdvice(getString(R.string.any_turn_date, key));
                }

            }
        });
    }

    private ArrayList<WorkDatabase> searchDate(String date){
        ArrayList<WorkDatabase> trabajo = new ArrayList<>();

        if(listWorkDay.containsKey(date)){
            trabajo = listWorkDay.get(date);
        }

        return trabajo;
        /*for (HashMap.Entry<String, ArrayList<WorkDatabase>> entry : listWorkDay.entrySet()) {
            String key = entry.getKey();
            ArrayList<WorkDatabase> value = entry.getValue();
            for(WorkDatabase w : value){
                System.out.println("\tvalue : " + w.toString());
            }
        }*/

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}