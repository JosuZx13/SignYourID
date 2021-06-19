package pdm.signyourid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pdm.signyourid.database.WorkDatabase;

public class AdapterTurn extends RecyclerView.Adapter<AdapterTurn.WorkViewHolder>{

    // Contendrá todos los turnos
    ArrayList<WorkDatabase> dias;
    // Se carga de gestionar la accion de cada vista
    private View.OnClickListener listener;

    public AdapterTurn(ArrayList<WorkDatabase> listWk){
        this.dias = listWk;
    }

    // Se recogen las referencias de cada componente del Layout
    public static class WorkViewHolder extends RecyclerView.ViewHolder{
        TextView tv_item_turn;
        TextView tv_item_timein;
        TextView tv_item_timeout;

        public WorkViewHolder(View itemDayView){
            super(itemDayView);

            tv_item_turn = itemDayView.findViewById(R.id.tv_day_turn);
            tv_item_timein = itemDayView.findViewById(R.id.tv_day_timein);
            tv_item_timeout = itemDayView.findViewById(R.id.tv_day_timeout);
        }
    } // Fin de la clase estática ViewHolder

    // El Layout Manager llama a este método para renderizar el RecyclerView
    @Override
    public AdapterTurn.WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Aquí se coge el modelo por defecto creado para usarlo como contenedor de diseño
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);

        return new WorkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterTurn.WorkViewHolder viewHolder, int pos) {
        WorkDatabase item = dias.get(pos);

        viewHolder.tv_item_turn.setText(item.getWrkTurn());
        viewHolder.tv_item_timein.setText(item.getWrkTimeIn());
        viewHolder.tv_item_timeout.setText(item.getWrkTimeOut());
    }

    @Override
    public int getItemCount() {
        return dias.size();
    }

}
