package pdm.signyourid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import pdm.signyourid.database.FragmentFirestore;
import pdm.signyourid.database.UserDatabase;
import pdm.signyourid.database.WorkDatabase;

public class FragmentActivityWorker extends FragmentActivity {

    // Los elementos que servirán para mostrar la información del trabajador
    private TextView tv_info_name;
    private TextView tv_info_sname;
    private TextView tv_info_birth;
    private TextView tv_info_phone;
    private TextView tv_info_email;

    private Button bt_signwork;
    private Button bt_historial;
    private Button bt_logout;

    private Informacion info; // Objeto para mostrar mensajes en pantalla o calcular fecha u hora

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_worker);

        tv_info_name = findViewById(R.id.tv_info_name);
        tv_info_sname = findViewById(R.id.tv_info_sname);
        tv_info_birth = findViewById(R.id.tv_info_birth);
        tv_info_phone = findViewById(R.id.tv_info_phone);
        tv_info_email = findViewById(R.id.tv_info_email);

        bt_signwork = findViewById(R.id.bt_signwork);
        bt_historial = findViewById(R.id.bt_historial);
        bt_logout = findViewById(R.id.bt_logout);

        info = new Informacion(this);

        // Aqui el usuario ya se sabe que está correcto por lo que no hay que hacer ninguna comprobacion
        UserDatabase us = (UserDatabase) getIntent().getSerializableExtra("USR_LOG");

        tv_info_name.setText(us.getUsrName());
        tv_info_sname.setText(us.getUsrSName());
        tv_info_birth.setText(us.getUsrBirth());
        tv_info_phone.setText(us.getUsrPhone());
        tv_info_email.setText(us.getUsrEmail());

        info.toastAdvice(getString(R.string.log_welcome));

        // Botón que registra una nueva hora en la colección del usuario SIGNING dentro del día actual
        bt_signwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signDate = new Intent(FragmentActivityWorker.this, FragmentSignWork.class);
                signDate.putExtra("USRDNI_", us.getUsrDni());
                ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                startActivity(signDate, animacion.toBundle());
            }
        });

        // Botón que recoge el historial de trabajo del usuario que inició sesión
        bt_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent working = new Intent(FragmentActivityWorker.this, FragmentFirestore.class);
                working.putExtra("REQUEST", ActivityLogin.REQUEST_FIRESTORE_HISTORIAL);
                working.putExtra("USRDNI_", us.getUsrDni());
                ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                startActivityForResult(working, ActivityLogin.REQUEST_FIRESTORE_HISTORIAL, animacion.toBundle());
            }
        });

        // Botón que cierra la sesión del usuario
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.toastAdvice(getString(R.string.log_out_session));
                finish();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityLogin.REQUEST_FIRESTORE_HISTORIAL) {
            HashMap<String, ArrayList<WorkDatabase>> wkList  = (HashMap<String, ArrayList<WorkDatabase>>) data.getSerializableExtra("SGN_RETURN");

            if(wkList.size() > 0){

                Intent showCalendar = new Intent(FragmentActivityWorker.this, FragmentCalendar.class);
                showCalendar.putExtra("WK_", wkList);
                ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                startActivity(showCalendar, animacion.toBundle());

            }else{
                info.toastAdvice(getString(R.string.not_registers));
            }

        }

    }

}
