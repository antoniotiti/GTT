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

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.form;


public class NominaCursorAdapter extends BaseAdapter {

    double  pref_RetencionIrpf, pref_RetencionHorasExtras, pref_RetencionContingenciasComunes,
            pref_RetencionFormacionDesempleoAccd;


    protected Activity activity;
    protected ArrayList<NominaTurno> arrayNominaTurno;

    public NominaCursorAdapter(Activity activity, ArrayList<NominaTurno> arrayNominaTurno) {
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
        pref_RetencionIrpf = Double.parseDouble(sharedPref.getString("pref_retencion_irpf", "0"));
        pref_RetencionHorasExtras = Double.parseDouble(sharedPref.getString("pref_retencion_extras", "0"));
        pref_RetencionContingenciasComunes = 4.7;//Double.parseDouble(sharedPref.getString("pref_retencion_contingencias_comunes", ""));pref_RetencionFormacionDesempleoAccd
        pref_RetencionFormacionDesempleoAccd = 1.7;//Double.parseDouble(sharedPref.getString("pref_retencion_formacion_desempleo_accd", ""));

        NominaTurno nominaTurno = arrayNominaTurno.get(position);

        double baseIrpf = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas() + nominaTurno.getTotalEurosHorasTrabajadasExtras();//+indemnizacion
        double baseHorasExtras = nominaTurno.getTotalEurosHorasTrabajadasExtras();
        double baseContingenciasComunes = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas();
        double baseFormacionDesempleoAccd = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas() + nominaTurno.getTotalEurosHorasTrabajadasExtras();

        double retencionIrpf = baseIrpf * pref_RetencionIrpf / 100;
        double retencionHorasExtras = baseHorasExtras * pref_RetencionHorasExtras / 100;
        double retencionContingenciasComunes = baseContingenciasComunes * pref_RetencionContingenciasComunes / 100;
        double retencionFormacionDesempleoAccd = baseFormacionDesempleoAccd * pref_RetencionFormacionDesempleoAccd / 100;

        double totalNetoTurno = nominaTurno.getTotalEurosHorasTrabajadas() + nominaTurno.getTotalEurosHorasTrabajadasNocturnas() + nominaTurno.getTotalEurosHorasTrabajadasExtras()- retencionIrpf - retencionHorasExtras - retencionContingenciasComunes - retencionFormacionDesempleoAccd;

        TextView textViewNombreTurno = (TextView) v.findViewById(R.id.textView_nombre_turno);
        textViewNombreTurno.setText(nominaTurno.getNombreTurno());

        TextView textViewHorasTrabajadas = (TextView) v.findViewById(R.id.textView_valor_horas);
        textViewHorasTrabajadas.setText(form.format(nominaTurno.getHorasTrabajadas()));
        TextView textViewPrecioHorasNormal = (TextView) v.findViewById(R.id.textView_precio_hora);
        textViewPrecioHorasNormal.setText(form.format(nominaTurno.getPrecioHoraTrabajada()) + "€");
        TextView textViewTOtalHorasNormal = (TextView) v.findViewById(R.id.textView_total_hora);
        textViewTOtalHorasNormal.setText(form.format(nominaTurno.getTotalEurosHorasTrabajadas()) + "€");

        TextView textViewHorasNocturnas = (TextView) v.findViewById(R.id.textView_valor_horas_nocturnas);
        textViewHorasNocturnas.setText(form.format(nominaTurno.getHorasTrabajadasNocturnas()));
        TextView textViewPrecioHorasNocturnas = (TextView) v.findViewById(R.id.textView_precio_hora_nocturnas);
        textViewPrecioHorasNocturnas.setText(form.format(nominaTurno.getPrecioHoraTrabajadaNocturna()) + "€");
        TextView textViewTOtalHorasNocturnas = (TextView) v.findViewById(R.id.textView_total_hora_nocturnas);
        textViewTOtalHorasNocturnas.setText(form.format(nominaTurno.getTotalEurosHorasTrabajadasNocturnas()) + "€");

        TextView textViewHorasExtras = (TextView) v.findViewById(R.id.textView_valor_horas_extras);
        textViewHorasExtras.setText(form.format(nominaTurno.getHorasTrabajadasExtras()));
        TextView textViewPrecioHorasExtras = (TextView) v.findViewById(R.id.textView_precio_hora_extras);
        textViewPrecioHorasExtras.setText(form.format(nominaTurno.getPrecioHoraTrabajadaExtras()) + "€");
        TextView textViewTOtalHorasExtras = (TextView) v.findViewById(R.id.textView_total_hora_extras);
        textViewTOtalHorasExtras.setText(form.format(nominaTurno.getTotalEurosHorasTrabajadasExtras()) + "€");

        TextView textViewTotalBrutoTurno = (TextView) v.findViewById(R.id.textView_total_bruto_turno);
        textViewTotalBrutoTurno.setText(form.format(totalNetoTurno) + "€");

        return v;
    }

}
