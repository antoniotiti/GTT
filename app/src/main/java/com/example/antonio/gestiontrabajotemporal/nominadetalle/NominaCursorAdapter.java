package com.example.antonio.gestiontrabajotemporal.nominadetalle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;

import java.util.ArrayList;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.FORMATO_DECIMAL;

class NominaCursorAdapter extends BaseAdapter {

    protected Activity activity;
    private ArrayList<NominaTurno> arrayNominaTurno;

    NominaCursorAdapter(Activity activity, ArrayList<NominaTurno> arrayNominaTurno) {
        this.activity = activity;
        this.arrayNominaTurno = arrayNominaTurno;
    }

    @Override
    public int getCount() {
        return arrayNominaTurno.size();
    }

    public void clear() {
        arrayNominaTurno.clear();
    }

    public void addAll(ArrayList<NominaTurno> category) {
        for (int i = 0; i < category.size(); i++) {
            arrayNominaTurno.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return arrayNominaTurno.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.list_item_nomina, null);
        }
        //Obtenemos las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        double pref_RetencionIrpf = Double.parseDouble(sharedPref.getString("pref_retencion_irpf", "0"));
        double pref_RetencionHorasExtras = Double.parseDouble(sharedPref.getString("pref_retencion_extras", "0"));
        double pref_RetencionContingenciasComunes =  Double.parseDouble(sharedPref.getString("pref_retencion_contingencias_comunes", "4.7"));
        double pref_RetencionFormacionDesempleoAccd = Double.parseDouble(sharedPref.getString("pref_retencion_formacion_desempleo_accd", "1.7"));

        NominaTurno nominaTurno = arrayNominaTurno.get(position);//Obtenemos la nomina de un turno

        double baseIrpf = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas() + nominaTurno.getTotalEurosHorasTrabajadasExtras();//+indemnizacion
        double baseHorasExtras = nominaTurno.getTotalEurosHorasTrabajadasExtras();
        double baseContingenciasComunes = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas();
        double baseFormacionDesempleoAccd = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas() + nominaTurno.getTotalEurosHorasTrabajadasExtras();

        double retencionIrpf = baseIrpf * pref_RetencionIrpf / 100;
        double retencionHorasExtras = baseHorasExtras * pref_RetencionHorasExtras / 100;
        double retencionContingenciasComunes = baseContingenciasComunes * pref_RetencionContingenciasComunes / 100;
        double retencionFormacionDesempleoAccd = baseFormacionDesempleoAccd * pref_RetencionFormacionDesempleoAccd / 100;

        double totalNetoTurno = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas() + nominaTurno.getTotalEurosHorasTrabajadasExtras() - retencionIrpf - retencionHorasExtras - retencionContingenciasComunes - retencionFormacionDesempleoAccd;

        TextView textViewNombreTurno = (TextView) v.findViewById(R.id.textView_nombre_turno);
        textViewNombreTurno.setText(nominaTurno.getNombreTurno());

        TextView textViewHorasTrabajadas = (TextView) v.findViewById(R.id.textView_valor_horas);
        textViewHorasTrabajadas.setText(FORMATO_DECIMAL.format(nominaTurno.getHorasTrabajadas()));
        TextView textViewPrecioHorasNormal = (TextView) v.findViewById(R.id.textView_precio_hora);
        textViewPrecioHorasNormal.setText(String.format("%s€", FORMATO_DECIMAL.format(nominaTurno.getPrecioHoraTrabajada())));
        TextView textViewTOtalHorasNormal = (TextView) v.findViewById(R.id.textView_total_hora);
        textViewTOtalHorasNormal.setText(String.format("%s€", FORMATO_DECIMAL.format(nominaTurno.getTotalEurosHorasTrabajadas())));

        TextView textViewHorasNocturnas = (TextView) v.findViewById(R.id.textView_valor_horas_nocturnas);
        textViewHorasNocturnas.setText(FORMATO_DECIMAL.format(nominaTurno.getHorasTrabajadasNocturnas()));
        TextView textViewPrecioHorasNocturnas = (TextView) v.findViewById(R.id.textView_precio_hora_nocturnas);
        textViewPrecioHorasNocturnas.setText(String.format("%s€", FORMATO_DECIMAL.format(nominaTurno.getPrecioHoraTrabajadaNocturna())));
        TextView textViewTOtalHorasNocturnas = (TextView) v.findViewById(R.id.textView_total_hora_nocturnas);
        textViewTOtalHorasNocturnas.setText(String.format("%s€", FORMATO_DECIMAL.format(nominaTurno.getTotalEurosHorasTrabajadasNocturnas())));

        TextView textViewHorasExtras = (TextView) v.findViewById(R.id.textView_valor_horas_extras);
        textViewHorasExtras.setText(FORMATO_DECIMAL.format(nominaTurno.getHorasTrabajadasExtras()));
        TextView textViewPrecioHorasExtras = (TextView) v.findViewById(R.id.textView_precio_hora_extras);
        textViewPrecioHorasExtras.setText(String.format("%s€", FORMATO_DECIMAL.format(nominaTurno.getPrecioHoraTrabajadaExtras())));
        TextView textViewTOtalHorasExtras = (TextView) v.findViewById(R.id.textView_total_hora_extras);
        textViewTOtalHorasExtras.setText(String.format("%s€", FORMATO_DECIMAL.format(nominaTurno.getTotalEurosHorasTrabajadasExtras())));

        TextView textViewTotalBrutoTurno = (TextView) v.findViewById(R.id.textView_total_bruto_turno);
        textViewTotalBrutoTurno.setText(String.format("%s€", FORMATO_DECIMAL.format(totalNetoTurno)));

        return v;
    }
}