package pdm.signyourid.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;


import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import pdm.signyourid.ActivityLogin;
import pdm.signyourid.Informacion;
import pdm.signyourid.R;

// Esta clase se usará de forma estática para que baste con abrir una conexión y hacer las peticiones
// pertinentes, cuando la aplicación se cierra, la conexión finalizará
public class FragmentFirestore extends FragmentActivity {

    private static final String TAG = "ERRO";
    // Atributos necesarios para la conexión a la base de datos MySQL
    private FirebaseFirestore fire;
    private Informacion info;

    private ImageView loadFlush;

    private int request;
    private String dni_ask;
    private String pass_ask;
    private String turn_ask;

    private String tUsers;
    private String tSigning;
    private String tTurn;

    // La estructura de la tabla es la siguiente
    /*
    * La Tabla USERS (Collection USERS) tendrá un documento con identifiador único (DNI) por trabajador
    * Cada documento tiene asociado unos campos con datos personales (DNI, NOMBRE, CONTRASEÑA...)
    *   Y además una Collection "SIGNING" donde se guardarán las fechas en las que ha trabajado
    * Esta nueva collection tendrá un documento con identificador único (fecha)
    *   que contendrá una única collection TURN donde se guardará hasta un máximo de 2 documentos
    *   si el nombre de estos es 1 ó 2, o uno solo si el documento tiene por nombre U
    * En estos documentos estará la información de la hora de entrada y salida del trabajador, así
    * como la fecha
    *
    * */

    // CAMPOS DE LA TABLA TUSERS
    private String usrDNI;
    private String usrPASSWORD;
    private String usrNAME;
    private String usrSNAME;
    private String usrBIRTH;
    private String usrPHONE;
    private String usrEMAIL;

    // CAMPOS DE LA TABLA TURN
    private String sgnDAY;
    private String sgnIN;
    private String sgnOUT;
    private String sgnTURN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_firestore);

        loadFlush = findViewById(R.id.loadFlush);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        loadFlush.startAnimation(rotate);

        tUsers = "USERS"; // Nombre de la Tabla Usuarios
        tSigning = "SIGNING"; // Nombre de la collection Fichaje
        tTurn = "TURN"; // Nombre de la collection Turno

        // TABLA USERS
        usrDNI = "DNI";
        usrPASSWORD = "PASSWORD";
        usrNAME = "NAME";
        usrSNAME = "SNAME";
        usrBIRTH = "BIRTH";
        usrPHONE = "PHONE";
        usrEMAIL = "EMAIL";

        // COLLECTION SIGNING
        sgnDAY = "DAY";
        sgnIN = "TIMEIN";
        sgnOUT = "TIMEOUT";
        sgnTURN = "TURN";

        request = getIntent().getIntExtra("REQUEST", -100);
        dni_ask = getIntent().getStringExtra("USRDNI_");

        fire = FirebaseFirestore.getInstance();

        info = new Informacion(this);

        // Depende del codigo que reciba al llamar a esta aplicación realizará una función u otra
        switch (request){

            case ActivityLogin.REQUEST_FIRESTORE_SEARCH_USER:

                pass_ask = getIntent().getStringExtra("USRPASS_");
                getUser(dni_ask, pass_ask);

            break;

            case ActivityLogin.REQUEST_FIRESTORE_HISTORIAL:

                getDay(dni_ask);

                break;

            case ActivityLogin.REQUEST_FIRESTORE_SIGN:

                turn_ask = getIntent().getStringExtra("USRTURN_");
                searchDay(dni_ask, turn_ask);

                break;

        }
    }

    private void getUser(String d, String p) {
        UserDatabase userFound = new UserDatabase();

        // Se selecciona el documento de Firestore que se quiere buscar
        DocumentReference docRef = fire.collection(tUsers).document(d);

        // Se crea un Listener para realizar una acción en caso de que encuentre el objeto
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;

                    if (document.exists()) {

                        userFound.setUsrPassword(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrPASSWORD)).toString());

                        // Solo si la contraseña introducida es correcta es que se inicia la sesión
                        if(userFound.getUsrPassword().equals(p)){
                            userFound.setUsrDni(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrDNI)).toString());
                            userFound.setUsrName(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrNAME)).toString());
                            userFound.setUsrSName(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrSNAME)).toString());
                            userFound.setUsrBirth(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrBIRTH)).toString());
                            userFound.setUsrPhone(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrPHONE)).toString());
                            userFound.setUsrEmail(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(usrEMAIL)).toString());
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        }

                    }

                }

                Intent returnUser = new Intent();
                returnUser.putExtra("USR_RETURN", userFound);
                setResult(request, returnUser);
                finish();

            }
        });

    }

    // Devuelve todos los días en los que un usuario ha trabajado
    private void getDay(String dni) {
        HashMap<String, ArrayList<WorkDatabase>> turnos = new HashMap<>();

        Task<QuerySnapshot> tarea = fire.collection(tUsers).document(dni).collection(tSigning).get();

        Tasks.whenAllComplete(tarea).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
            @Override
            public void onSuccess(List<Task<?>> tasks) {
                List<DocumentSnapshot> documentos = Objects.requireNonNull(tarea.getResult()).getDocuments();

                if(documentos.size() > 0){

                    for (DocumentSnapshot ds : documentos){
                        //System.out.println(ds.get(sgnDAY));


                        ArrayList<HashMap<String, String>> listmap = (ArrayList<HashMap<String, String>>) ds.get(sgnDAY);
                        ArrayList<WorkDatabase> wk = new ArrayList<>();
                        assert listmap != null;
                        for(HashMap<String, String> m : listmap){ // Puede tener un turno o dos
                            WorkDatabase w = new WorkDatabase(
                                    ds.getId(),
                                    m.get((sgnIN)),
                                    m.get(sgnOUT),
                                    m.get(sgnTURN)
                            );
                            wk.add(w);
                        }
                        turnos.put(ds.getId(), wk);

                    }

                    Intent returnUser = new Intent();
                    returnUser.putExtra("SGN_RETURN", turnos);
                    setResult(request, returnUser);
                    finish();
                }
            }
        });
    }

    private void searchDay(String d, String t){
        String fecha = info.getDate();
        Task<DocumentSnapshot> tabla = fire.collection(tUsers).document(d).collection(tSigning).document(fecha).get();

        Tasks.whenAllSuccess(tabla).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {

                DocumentSnapshot docFecha = tabla.getResult();

                if(Objects.requireNonNull(docFecha).exists()){
                    // Si existe al menos hay un turno guardado

                    ArrayList<HashMap<String, String>> dias = (ArrayList<HashMap<String, String>>) docFecha.get(sgnDAY);
                    assert dias != null;

                    if(t.equals("2")){

                        if(dias.size() == 2){ //Es porque antes aquí se guardo un turno 1, si no sería de tamaño 1 ó 2 de no haber registrado nada antes

                            boolean primeroCompleto = false;

                            HashMap<String, String> turno1 = dias.get(0); // Se coge el primer turno

                            if(!turno1.get(sgnIN).equals("") && !turno1.get(sgnOUT).equals("")){
                                primeroCompleto = true;
                            }

                            if(primeroCompleto){ //Existe un turno 1 completo
                                // Como ya se sabe que hay dos turnos, se coge el turno 2
                                HashMap<String, String> turno2 = dias.get(1);
                                if(!turno2.get(sgnIN).equals("") && !turno2.get(sgnOUT).equals("")){
                                    reportFullDay();

                                }else{
                                    if(turno2.get(sgnIN).equals("")){
                                        //Añado al timein del segundo turno
                                        updateDay(d, t, fecha, sgnIN);
                                    }else{
                                        //Añado al timeout del segundo turno
                                        updateDay(d, t, fecha, sgnOUT);
                                    }
                                }
                            }else{
                                reportTurnMistake();
                            }
                        }

                        else{
                            reportTurnMistake();
                        }

                    }else{
                        // Si el turno es U y existe un documento de la fecha
                        //Y hay dos elementos, entonces este día no es de turno único
                        if(t.equals("U") && dias.size() == 2){
                            reportTurnMistakeU();

                        }else { // Entones el turno es único ó el turno es 1
                            HashMap<String, String> turno = dias.get(0); //Se coge por tanto el primer elemento

                            if (!turno.get(sgnIN).equals("") && !turno.get(sgnOUT).equals("")) {
                                reportFullDay();
                            } else {
                                if (turno.get(sgnOUT).equals("")) {
                                    //Se añade la hora en el OUT del turno único o el primer turno
                                    updateDay(d, t, fecha, sgnOUT);
                                }
                            }
                        }
                    }

                }else{
                    if(!t.equals("2")){
                        // Se guarda el turno y se crea el documento con el identificador fecha por primera vez
                        // SE añade al IN
                        setNewDay(d, t, fecha, sgnIN);
                    }else{
                        reportTurnMistake();
                    }

                }

            }
        });

    }

    /*
    * d = DNI
    * t = TURN
    * f = CURRENTLY DATE
    * tm = TIMEIN or TIMEOUT
    * */
    private void updateDay(String d, String t, String f, String tm){
        String h = info.getTime();

        DocumentReference dref = fire.collection(tUsers).document(d).collection(tSigning).document(f);

        Task<DocumentSnapshot> obtenerCampos = dref.get();

        // Como el update machaca el objeto dentro del array, es necesario coger los datos del documento en cuestión
        Tasks.whenAllSuccess(obtenerCampos).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                ArrayList<HashMap<String, String>> campos = (ArrayList<HashMap<String, String>>) obtenerCampos.getResult().get(sgnDAY);
                assert campos != null;

                // ESTE ES EL FORMATO DE UN DAY
                //[{TURN=1, TIMEOUT=12:30, TIMEIN=08:30}, {TURN=2, TIMEOUT=20:00, TIMEIN=16:30}]

                if(t.equals("U")){
                    // En este caso, solo tendrá un elemento HashMap dentro
                    // O si es el turno 1, igualmente solo interesa el primer elemento de DAY
                    HashMap<String, String> datosAnteriores = campos.get(0);

                    // Para mantener la misma estructura de un mapa de arrays, es necesario generar de nuevo el objeto
                    ArrayList<HashMap<String, String>> updateDay = new ArrayList<>();
                    // Se genera el turno con las actualizaciones
                    HashMap<String, String> updateData = new HashMap<>();

                    updateData.put(sgnIN, datosAnteriores.get(sgnIN));
                    updateData.put(sgnOUT, h);
                    updateData.put(sgnTURN, datosAnteriores.get(sgnTURN));

                    updateDay.add(updateData);

                    // Si el turno es único o el 1 y ha llegado aquí es porque va a actualizar el campo TIMEOUT
                    dref.update(sgnDAY, updateDay).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            info.toastAdvice(getString(R.string.report_success));
                            finish();
                        }
                    });
                }

                if(t.equals("1")){
                    // El primer elemento será el que se modificará
                    HashMap<String, String> datosAnteriores = campos.get(0);
                    // Y el segundo el que se conservará
                    HashMap<String, String> datosConservar = campos.get(1);

                    ArrayList<HashMap<String, String>> updateDay = new ArrayList<>();
                    // Se genera el turno con las actualizaciones
                    HashMap<String, String> updateData = new HashMap<>();

                    updateData.put(sgnIN, datosAnteriores.get(sgnIN));
                    updateData.put(sgnOUT, h);
                    updateData.put(sgnTURN, datosAnteriores.get(sgnTURN));

                    updateDay.add(updateData);
                    updateDay.add(datosConservar);

                    // Si el turno es único o el 1 y ha llegado aquí es porque va a actualizar el campo TIMEOUT
                    dref.update(sgnDAY, updateDay).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            info.toastAdvice(getString(R.string.report_success));
                            finish();
                        }
                    });
                }

                if(t.equals("2")){ // Sin importar si se actualiza el TIMEIN o TIMEOUT

                    // El primer elemento será el que se conserve (turno 1)
                    HashMap<String, String> datosConservar = campos.get(0);
                    // El segundo elemento será el que se modificara
                    HashMap<String, String> datosAnteriores = campos.get(1);

                    ArrayList<HashMap<String, String>> updateDay = new ArrayList<>();
                    // Se genera el turno con las actualizaciones
                    HashMap<String, String> updateData = new HashMap<>();

                    //Hay que comprobar cual es la hora de actualización, si la 1 o la 2 para crear la sentencia
                    if(tm.equals(sgnIN)){ // Se actualiza la hora de entrada

                        updateData.put(sgnIN, h);
                        updateData.put(sgnOUT, datosAnteriores.get(sgnOUT));
                        updateData.put(sgnTURN, datosAnteriores.get(sgnTURN));

                        updateDay.add(datosConservar);
                        updateDay.add(updateData);

                        dref.update(sgnDAY, updateDay).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                info.toastAdvice(getString(R.string.report_success));
                                finish();
                            }
                        });

                    }else{ //Se actualiza la hora de salida
                        updateData.put(sgnIN, datosAnteriores.get(sgnIN));
                        updateData.put(sgnOUT, h);
                        updateData.put(sgnTURN, datosAnteriores.get(sgnTURN));

                        updateDay.add(datosConservar);
                        updateDay.add(updateData);

                        dref.update(sgnDAY, updateDay).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                info.toastAdvice(getString(R.string.report_success));
                                finish();
                            }
                        });
                    }

                }

            }
        });

    }

    private void setNewDay(String d, String t, String f, String tm){
        String h = info.getTime();
        DocumentReference dref = fire.collection(tUsers).document(d).collection(tSigning).document(f);

        HashMap<String, ArrayList<HashMap<String, String>>> day = new HashMap<>(); //Se genera la estructura de un elemento en un documento

        ArrayList<HashMap<String, String>> datos = new ArrayList<>();
        // Se genera el turno
        HashMap<String, String> turno = new HashMap<>();

        //Para crear o reemplazar un solo documento, usa el método set
        // Sin embargo, se va a crear el contenido vacio y de forma distinta si es para un turno único y para un turno 1
        if( t.equals("U") && tm.equals(sgnIN) ){ // Si el turno es único y se quiere registrar el timein

            // Si el turno es único, solo se va crear una entrada
            turno.put(sgnIN, h);
            turno.put(sgnOUT, "");
            turno.put(sgnTURN, t);

            datos.add(turno);

            day.put(sgnDAY, datos);

            // Es sabido que no existirá
            dref.set(day).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    info.toastAdvice(getString(R.string.report_new_success));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    info.toastAdvice(getString(R.string.report_error));
                    finish();
                }
            });


        }else{
            // Si el turno es 1, se va a crear ademas el campo del turno 2, pero vacio
            turno.put(sgnIN, h);
            turno.put(sgnOUT, "");
            turno.put(sgnTURN, t);

            datos.add(turno);

            HashMap<String, String> turno2 = new HashMap<>();

            turno2.put(sgnIN, "");
            turno2.put(sgnOUT, "");
            turno2.put(sgnTURN, "2");

            datos.add(turno2);

            day.put(sgnDAY, datos);

            // Es sabido que no existirá
            dref.set(day).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    info.toastAdvice(getString(R.string.report_new_success));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    info.toastAdvice(getString(R.string.report_error));
                    finish();
                }
            });
        }
    }

    private void reportFullDay(){
        info.toastAdvice(getString(R.string.report_full_day));
        finish();
    }

    private void reportTurnMistake(){
        info.toastAdvice(getString(R.string.report_turn_mistake));
        finish();
    }

    private void reportTurnMistakeU(){
        info.toastAdvice(getString(R.string.report_turn_mistake_u));
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
