package pdm.signyourid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pdm.signyourid.database.FragmentFirestore;
import pdm.signyourid.database.UserDatabase;


public class ActivityLogin extends AppCompatActivity {

    public final static int REQUEST_FIRESTORE_SEARCH_USER = 10;
    public final static int REQUEST_FIRESTORE_HISTORIAL = 11;
    public final static int REQUEST_FIRESTORE_SIGN = 12;

    private EditText et_log_dni;
    private EditText et_log_password;
    private Button bt_logging;

    private Informacion info;

    private String dni_;
    private String pass_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_log_dni = findViewById(R.id.et_log_dni);
        et_log_password = findViewById(R.id.et_log_password);

        bt_logging = findViewById(R.id.bt_logging);

        info = new Informacion(this);

        bt_logging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dni_ = et_log_dni.getText().toString();
                pass_ = et_log_password.getText().toString();

                if(dni_.trim().equals("") || pass_.trim().equals("")){
                    info.toastAdvice(getString(R.string.dni_pass_blank));

                }else{

                    if(checkInternet()){

                        Intent search = new Intent(ActivityLogin.this, FragmentFirestore.class);
                        search.putExtra("REQUEST", REQUEST_FIRESTORE_SEARCH_USER);
                        search.putExtra("USRDNI_", dni_);
                        search.putExtra("USRPASS_", pass_);
                        ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                        startActivityForResult(search, REQUEST_FIRESTORE_SEARCH_USER, animacion.toBundle());

                    }else{
                        info.toastAdvice(getString(R.string.not_internet));
                    }
                }
            }
        });

    }

    // COMPRUEBA SI EL USUARIO TIENE ACCESO A INTERNET
    private boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    // Controlar el salir de la aplicación para que no quede abierta y asegurar que el usuario quería salir
    protected void finishApp(){
        new AlertDialog.Builder(ActivityLogin.this)
            .setIcon(R.drawable.ic_baseline_exit_to_app)
            .setTitle(getResources().getString(R.string.app_cerrar))
            .setMessage(getResources().getString(R.string.cerrar_mensaje))
            .setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent salir = new Intent(getApplicationContext(), ActivityExit.class);
                    salir.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(salir);
                    finish();
                }

            }).setNegativeButton(getResources().getString(R.string.cancelar), null).show();
    }

    // El menú se gestionará en el Fragment que es donde se quiere usar
    @Override
    public void onBackPressed() {
        finishApp();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Para cuando el usuario cierre sesion no se queden los datos escritos
        et_log_dni.setText("");
        et_log_password.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Para cuando el usuario cierre sesion no se queden los datos escritos
        et_log_dni.setText("");
        et_log_password.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FIRESTORE_SEARCH_USER) {
            UserDatabase us = (UserDatabase) data.getSerializableExtra("USR_RETURN");

            if (!us.getUsrName().equals("-n")) {
                Intent log = new Intent(ActivityLogin.this, FragmentActivityWorker.class);
                log.putExtra("USR_LOG", us);
                ActivityOptions animacion = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                startActivity(log, animacion.toBundle());
            } else {
                info.toastAdvice(getString(R.string.not_user_found, dni_));
            }
        }

    }
}