package pdm.signyourid;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pdm.signyourid.AdapterTurn;
import pdm.signyourid.R;
import pdm.signyourid.database.WorkDatabase;

public class FragmentTurn extends FragmentActivity {

    // Se recogen los elementos que harán posible la carga de los distintos turnos
    AdapterTurn adapter;
    private RecyclerView rviewListLanguage;
    private LinearLayoutManager linearManager;

    private ArrayList<WorkDatabase> works; // Esta variable guarda si el usuario llego a esta actividad para cambiar el idioma de entrada o salida


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_showday);

        works = (ArrayList<WorkDatabase>) getIntent().getSerializableExtra("LST_WK");

        for(WorkDatabase w : works){
            System.out.println("|"+w.getWrkTimeOut()+"|");
        }

        // Se recoge el RecyclerView que contendrá todas las backup del usuario
        rviewListLanguage = findViewById(R.id.rview_work);
        linearManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // Mejora de rendimiento
        // Se usa solo si se sabe que el contenido no afecta al tamaño del RV
        rviewListLanguage.setHasFixedSize(true);

        adapter = new AdapterTurn(works);

        rviewListLanguage.setAdapter(adapter);
        rviewListLanguage.setLayoutManager(linearManager);
        rviewListLanguage.setPadding(0,50,0,50);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
