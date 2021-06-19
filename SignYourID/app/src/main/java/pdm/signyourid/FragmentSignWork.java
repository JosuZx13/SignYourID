package pdm.signyourid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import pdm.signyourid.database.FragmentFirestore;

public class FragmentSignWork extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    // Los elementos que servirán para mostrar la información del trabajador
    private Button bt_signDay;

    private String dni_ask;
    private String turn_ask;

    private Spinner selectTurno;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signwork);
        turn_ask = "1"; //Por defecto se elegirá el 1

        selectTurno = findViewById(R.id.spinner_turno);
        ArrayAdapter<CharSequence> adapterSpinnner = ArrayAdapter.createFromResource(this, R.array.array_turnos, android.R.layout.simple_spinner_item);
        adapterSpinnner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTurno.setAdapter(adapterSpinnner);
        selectTurno.setOnItemSelectedListener(this);

        dni_ask = getIntent().getStringExtra("USRDNI_");

        bt_signDay = findViewById(R.id.bt_signDay);

        bt_signDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signDate = new Intent(FragmentSignWork.this, FragmentFirestore.class);
                signDate.putExtra("REQUEST", ActivityLogin.REQUEST_FIRESTORE_SIGN);
                signDate.putExtra("USRDNI_", dni_ask);
                signDate.putExtra("USRTURN_", turn_ask);
                ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                startActivity(signDate, animacion.toBundle());
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Se puede recoger el elemento pulsado de la siguiente forma

        switch (position){

            case 0:
                turn_ask = "1";
            break;

            case 1:
                turn_ask = "2";
            break;

            case 2:
                turn_ask = "U";
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
