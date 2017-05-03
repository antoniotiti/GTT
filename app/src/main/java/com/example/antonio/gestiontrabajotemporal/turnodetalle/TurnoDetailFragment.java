package com.example.antonio.gestiontrabajotemporal.turnodetalle;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Turno;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;
import com.example.antonio.gestiontrabajotemporal.util.TimeDialog;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.util.Calendar;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.calcularHoraDecimal;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.calcularHorasTrabajadas;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_hora_minutos;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TurnoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TurnoDetailFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private static final String ARG_TURNO_ID = "turnoId";

    OperacionesBaseDatos datos;
    Button btnSeleccionColorFondo, btnSeleccionColorTexto;
    EditText editTextNombreTurno, editTextAbreviaturaTurno, editTextHoraInicio1, editTextHoraFin1,
            editTextHoraInicio2, editTextHoraFin2, editTextPrecioHora, editTextPrecioHoraNocturna,
            editTextPrecioHoraExtra, editTextAvisoHora, activeTimeDisplay, editTextHoraTrabajadaNoche;
    TextView textViewHoraTrabajada, textViewPrevisualizacionTurno;
    Switch switchTurnoPartido, switchAviso, switchAvisoDiaAntes, switchModosTelefono;
    RadioGroup radioGroupWifiInicio, radioGroupWifiFin, radioGroupBluetoothInicio, radioGroupBluetoothFin,
            radioGroupSonidoInicio, radioGroupSonidoFin;
    RadioButton radioButtonWifiInicioActivar, radioButtonWifiInicioDesactivar,
            radioButtonWifiInicioNoCambiar, radioButtonWifiFinActivar, radioButtonWifiFinDesactivar,
            radioButtonWifiFinNoCambiar, radioButtonBluetoothInicioActivar, radioButtonBluetoothInicioDesactivar,
            radioButtonBluetoothInicioNoCambiar, radioButtonBluetoothFiniActivar, radioButtonBluetoothFinDesactivar,
            radioButtonBluetoothFinNoCambiar, radioButtonSonidoInicio, radioButtonVibracionInicio,
            radioButtonSilencioInicio, radioButtonSonidoInicioNoCambiar, radioButtonSonidoFin, radioButtonVibracionFin,
            radioButtonSilencioFin, radioButtonSonidoFinNoCambiar;
    Calendar activeTime;
    RelativeLayout relativeLayoutHorioTurnoPartido, relativeLayoutAviso;
    LinearLayout linearLayoutModosTelefono;
    int mSelectedColor;
    //Campos validados
    Boolean nombreTurnoValidado = false, abreviaturaTurnoValidado = false, horaInicio1Validado = false, horaFin1Validado = false, horaInicio2Validado = true, horaFin2Validado = true, precioHoraValidado = false,
            avisoHoraValidado = true;

    //Diálogo selección de color.
    ColorPickerDialog colorPickerDialog;
    private String mTurnoId;
    private CollapsingToolbarLayout mCollapsingView;
    private Context context;

    /**
     * Constructor por defecto.
     */
    public TurnoDetailFragment() {
    }

    public static TurnoDetailFragment newInstance(String turnoId) {
        TurnoDetailFragment fragment = new TurnoDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TURNO_ID, turnoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        if (getArguments() != null) {
            mTurnoId = getArguments().getString(ARG_TURNO_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_turno_detail, container, false);

        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout_turno);

        activeTime = Calendar.getInstance(); //Obtenemos la hora actual
        activeTime.set(0, 0, 0, 0, 0, 0); //Establecemos la hora a 0

/////////////////////Inicio Diálogo selección de color///////////////
        //Colores para seleccionar.
        int[] mColors = getResources().getIntArray(R.array.colores_dialogo);

        //Color seleccionado por defecto.
        mSelectedColor = ContextCompat.getColor(getActivity(), R.color.grey05);

        //Creamos el diálogo selección de color.
        colorPickerDialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors, mSelectedColor, 5, ColorPickerDialog.SIZE_SMALL);

        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                switch (colorPickerDialog.getTag()) {
                    case "colorFondo":
                        btnSeleccionColorFondo.setBackgroundColor(mSelectedColor);
                        textViewPrevisualizacionTurno.setBackgroundColor(mSelectedColor);
                        break;
                    case "colorTexto":
                        btnSeleccionColorTexto.setBackgroundColor(mSelectedColor);
                        textViewPrevisualizacionTurno.setTextColor(mSelectedColor);
                        break;
                }
            }
        });
/////////////////////Fin Diálogo selección de color///////////////

        //Obtenemos las referencias de las vistas.
        textViewPrevisualizacionTurno = (TextView) root.findViewById(R.id.textView_prev_turno);
        editTextNombreTurno = (EditText) root.findViewById(R.id.editText_NombreTurno);//
        editTextAbreviaturaTurno = (EditText) root.findViewById(R.id.editText_AbreviaturaTurno);
        btnSeleccionColorFondo = (Button) root.findViewById(R.id.btn_SeleccionColorFondo);
        btnSeleccionColorTexto = (Button) root.findViewById(R.id.btn_SeleccionColorTexto);
        editTextHoraInicio1 = (EditText) root.findViewById(R.id.editText_valor_hora_inicio_1);
        editTextHoraFin1 = (EditText) root.findViewById(R.id.editText_valor_hora_fin_1);
        editTextHoraInicio2 = (EditText) root.findViewById(R.id.editText_valor_hora_inicio_2);
        editTextHoraFin2 = (EditText) root.findViewById(R.id.editText_valor_hora_fin_2);
        textViewHoraTrabajada = (TextView) root.findViewById((R.id.textView_valor_horas_trabajadas));
        editTextHoraTrabajadaNoche = (EditText) root.findViewById((R.id.editText_valor_horas_trabajadas_noche));
        editTextPrecioHora = (EditText) root.findViewById((R.id.editTextValorPrecioHoras));
        editTextPrecioHoraNocturna = (EditText) root.findViewById((R.id.editTextValorPrecioNoche));
        editTextPrecioHoraExtra = (EditText) root.findViewById((R.id.editTextValorPrecioExtra));
        editTextAvisoHora = (EditText) root.findViewById(R.id.editText_valor_aviso_hora);
        relativeLayoutHorioTurnoPartido = (RelativeLayout) root.findViewById(R.id.layout_turno_partido);//Layout para mostrar el turno partido
        relativeLayoutHorioTurnoPartido.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout
        relativeLayoutAviso = (RelativeLayout) root.findViewById(R.id.layout_seleccion_hora_aviso);//Layout para mostrar el aviso
        relativeLayoutAviso.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout
        linearLayoutModosTelefono = (LinearLayout) root.findViewById(R.id.layout_modos_telefono);//Layout para mostrar los modos del telefonoo
        linearLayoutModosTelefono.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout
        radioGroupWifiInicio = (RadioGroup) root.findViewById(R.id.radioGroup_modo_wifi_inicio);
        radioGroupWifiFin = (RadioGroup) root.findViewById(R.id.radioGroup_modo_wifi_fin);
        radioGroupBluetoothInicio = (RadioGroup) root.findViewById(R.id.radioGroup_modo_bluetooth_inicio);
        radioGroupBluetoothFin = (RadioGroup) root.findViewById(R.id.radioGroup_modo_bluetooth_fin);
        radioGroupSonidoInicio = (RadioGroup) root.findViewById(R.id.radioGroup_modo_sonido_inicio);
        radioGroupSonidoFin = (RadioGroup) root.findViewById(R.id.radioGroup_modo_sonido_fin);
        radioButtonWifiInicioActivar = (RadioButton) radioGroupWifiInicio.getChildAt(0);
        radioButtonWifiInicioDesactivar = (RadioButton) radioGroupWifiInicio.getChildAt(1);
        radioButtonWifiInicioNoCambiar = (RadioButton) radioGroupWifiInicio.getChildAt(2);
        radioButtonWifiFinActivar = (RadioButton) radioGroupWifiFin.getChildAt(0);
        radioButtonWifiFinDesactivar = (RadioButton) radioGroupWifiFin.getChildAt(1);
        radioButtonWifiFinNoCambiar = (RadioButton) radioGroupWifiFin.getChildAt(2);
        radioButtonBluetoothInicioActivar = (RadioButton) radioGroupBluetoothInicio.getChildAt(0);
        radioButtonBluetoothInicioDesactivar = (RadioButton) radioGroupBluetoothInicio.getChildAt(1);
        radioButtonBluetoothInicioNoCambiar = (RadioButton) radioGroupBluetoothInicio.getChildAt(2);
        radioButtonBluetoothFiniActivar = (RadioButton) radioGroupBluetoothFin.getChildAt(0);
        radioButtonBluetoothFinDesactivar = (RadioButton) radioGroupBluetoothFin.getChildAt(1);
        radioButtonBluetoothFinNoCambiar = (RadioButton) radioGroupBluetoothFin.getChildAt(2);
        radioButtonSonidoInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(0);
        radioButtonVibracionInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(1);
        radioButtonSilencioInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(2);
        radioButtonSonidoInicioNoCambiar = (RadioButton) radioGroupSonidoInicio.getChildAt(3);
        radioButtonSonidoFin = (RadioButton) radioGroupSonidoFin.getChildAt(0);
        radioButtonVibracionFin = (RadioButton) radioGroupSonidoFin.getChildAt(1);
        radioButtonSilencioFin = (RadioButton) radioGroupSonidoFin.getChildAt(2);
        radioButtonSonidoFinNoCambiar = (RadioButton) radioGroupSonidoFin.getChildAt(3);
        switchTurnoPartido = (Switch) root.findViewById(R.id.switch_turno_partido);
        switchAviso = (Switch) root.findViewById(R.id.switch_avisos);
        switchAvisoDiaAntes = (Switch) root.findViewById(R.id.switch_aviso_dia_antes);
        switchModosTelefono = (Switch) root.findViewById(R.id.switch_modo_telefono);

        //Establecemos los Listener
        editTextNombreTurno.addTextChangedListener(new MyTextWatcher(editTextNombreTurno));
        editTextAbreviaturaTurno.addTextChangedListener(new MyTextWatcher(editTextAbreviaturaTurno));
        editTextHoraInicio1.setOnClickListener(this);
        editTextHoraInicio1.addTextChangedListener(new MyTextWatcher(editTextHoraInicio1));
        editTextHoraFin1.setOnClickListener(this);
        editTextHoraFin1.addTextChangedListener(new MyTextWatcher(editTextHoraFin1));
        editTextHoraInicio2.setOnClickListener(this);
        editTextHoraInicio2.addTextChangedListener(new MyTextWatcher(editTextHoraInicio2));
        editTextHoraFin2.setOnClickListener(this);
        editTextHoraFin2.addTextChangedListener(new MyTextWatcher(editTextHoraFin2));
        editTextHoraTrabajadaNoche.setOnClickListener(this);
        editTextPrecioHora.addTextChangedListener(new MyTextWatcher(editTextPrecioHora));
        editTextAvisoHora.setOnClickListener(this);
        editTextAvisoHora.addTextChangedListener(new MyTextWatcher(editTextAvisoHora));

        switchTurnoPartido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    relativeLayoutHorioTurnoPartido.setVisibility(View.VISIBLE);
                } else {
                    relativeLayoutHorioTurnoPartido.setVisibility(View.GONE);
                    editTextHoraInicio2.setText("");
                    editTextHoraInicio2.setError(null);
                    editTextHoraFin2.setText("");
                    editTextHoraFin2.setError(null);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, false));
                }
            }
        });

        switchAviso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    relativeLayoutAviso.setVisibility(View.VISIBLE);
                } else {
                    relativeLayoutAviso.setVisibility(View.GONE);
                    editTextAvisoHora.setText("");
                    editTextAvisoHora.setError(null);
                    switchAvisoDiaAntes.setChecked(false);
                }
            }
        });

        switchAvisoDiaAntes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        switchModosTelefono.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    linearLayoutModosTelefono.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutModosTelefono.setVisibility(View.GONE);
                    radioButtonWifiInicioNoCambiar.setChecked(true);
                    radioButtonWifiFinNoCambiar.setChecked(true);
                    radioButtonSonidoInicioNoCambiar.setChecked(true);
                    radioButtonSonidoFinNoCambiar.setChecked(true);
                    radioButtonBluetoothInicioNoCambiar.setChecked(true);
                    radioButtonBluetoothFinNoCambiar.setChecked(true);
                }
            }
        });

        btnSeleccionColorFondo.setOnClickListener(this);
        btnSeleccionColorTexto.setOnClickListener(this);

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        // Carga de datos
        if (mTurnoId != null) {
            new GetTurnoByIdTask().execute();//Cargamos los datos del turno seleccionado
        }
        return root;
    }

    /**
     * Método encargado de mostrar un TimeDialog
     *
     * @param timeDisplay EditText sobre el que se lanza el TimeDialog
     */
    private void showTimeDialog(EditText timeDisplay) {
        activeTimeDisplay = timeDisplay;

        DialogFragment newFragment = new TimeDialog();
        newFragment.setTargetFragment(TurnoDetailFragment.this, 0);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        activeTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        activeTime.set(Calendar.MINUTE, minute);
        updateDisplay(activeTimeDisplay, activeTime);
    }

    private void updateDisplay(EditText dateDisplay, Calendar time) {
        String hora = formatter_hora_minutos.format(time.getTime());
        dateDisplay.setText(hora);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId() /*Obtenemos el id de la vista clicada.*/) {
            case R.id.btn_SeleccionColorFondo:
                colorPickerDialog.show(getActivity().getFragmentManager(), "colorFondo");
                break;
            case R.id.btn_SeleccionColorTexto:
                colorPickerDialog.show(getActivity().getFragmentManager(), "colorTexto");
                break;
            case R.id.editText_valor_hora_inicio_1:
                showTimeDialog(editTextHoraInicio1);
                break;
            case R.id.editText_valor_hora_fin_1:
                showTimeDialog(editTextHoraFin1);
                break;
            case R.id.editText_valor_hora_inicio_2:
                showTimeDialog(editTextHoraInicio2);
                break;
            case R.id.editText_valor_hora_fin_2:
                showTimeDialog(editTextHoraFin2);
                break;
            case R.id.editText_valor_aviso_hora:
                showTimeDialog(editTextAvisoHora);
                break;
            case R.id.editText_valor_horas_trabajadas_noche:
                showTimeDialog(editTextHoraTrabajadaNoche);
                break;
            default:
                break;
        }
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para borrar un turno.
     */
    public void borrarTurno() {
        new DeleteTurnoTask().execute();
    }

    /**
     * Ejecutamos una acción al seleccionar una opcion del menú.
     *
     * @param item Opcion del menu seleccionada
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addEditTurno();
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "EliminarTurno");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que se encarga de añadir un Turno a la BBDD validando los datos previamente.
     */
    private void addEditTurno() {

        nombreTurnoValidado = validarEditTextVacio(context, editTextNombreTurno);
        abreviaturaTurnoValidado = validarEditTextVacio(context, editTextAbreviaturaTurno);
        horaInicio1Validado = validarEditTextVacio(context, editTextHoraInicio1);
        horaFin1Validado = validarEditTextVacio(context, editTextHoraFin1);
        if (switchTurnoPartido.isChecked()) {
            horaInicio2Validado = validarEditTextVacio(context, editTextHoraInicio2);
            horaFin2Validado = validarEditTextVacio(context, editTextHoraFin2);
        }
        precioHoraValidado = validarEditTextVacio(context, editTextPrecioHora);
        if (switchAviso.isChecked()) {
            avisoHoraValidado = validarEditTextVacio(context, editTextAvisoHora);
        }
        //Comprobamos que todos los datos esten validados
        if (nombreTurnoValidado && abreviaturaTurnoValidado && horaInicio1Validado && horaFin1Validado && precioHoraValidado && horaInicio2Validado && horaFin2Validado && avisoHoraValidado) {
            try {
                String nombreTurno = editTextNombreTurno.getText().toString();
                String abreviaturaTurno = editTextAbreviaturaTurno.getText().toString();
                int colorFondo = ((ColorDrawable) btnSeleccionColorFondo.getBackground()).getColor();
                int colorTexto = ((ColorDrawable) btnSeleccionColorTexto.getBackground()).getColor();
                String horaInicio1 = editTextHoraInicio1.getText().toString();
                String horaFin1 = editTextHoraFin1.getText().toString();
                int turnoPartido = switchTurnoPartido.isChecked() ? 1 : 0; //0(falso) o 1(verdadero)
                String horaInicio2 = editTextHoraInicio2.getText().toString();
                String horaFin2 = editTextHoraFin2.getText().toString();
                float horasTrabajadas = calcularHoraDecimal(textViewHoraTrabajada.getText().toString());
                float horasTrabajadasNoche = calcularHoraDecimal(editTextHoraTrabajadaNoche.getText().toString());
                double precioHora = Double.parseDouble(editTextPrecioHora.getText().toString());
                double precioHoraNocturna = Double.parseDouble(editTextPrecioHoraNocturna.getText().toString());
                double precioHoraExtra = Double.parseDouble(editTextPrecioHoraExtra.getText().toString());
                int aviso = switchAviso.isChecked() ? 1 : 0; //0(falso) o 1(verdadero)
                String horaAviso = editTextAvisoHora.getText().toString();
                int avisoDiaAntes = switchAvisoDiaAntes.isChecked() ? 1 : 0; //0(falso) o 1(verdadero)
                String modosTelefono;
                if (switchModosTelefono.isChecked()) {
                    modosTelefono = obtenerModosTelefono();
                } else {
                    modosTelefono = "";
                }
                //Creamos un turno con todos los datos validados.
                Turno turnoInsertar = new Turno(nombreTurno, abreviaturaTurno, horaInicio1, horaFin1, turnoPartido, horaInicio2, horaFin2, horasTrabajadas, horasTrabajadasNoche, precioHora, precioHoraNocturna, precioHoraExtra, aviso, avisoDiaAntes, horaAviso, modosTelefono, colorFondo, colorTexto);
                //Añadimos el turno a la BBDD
                new AddEditTurnoTask().execute(turnoInsertar);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método encargado de obtener los modos del teléfono.
     *
     * @return String con los modos del teléfono
     */
    private String obtenerModosTelefono() {
        String modoTelefono = "";

        int radioButtonIdWifiInicio = radioGroupWifiInicio.getCheckedRadioButtonId();
        switch (radioButtonIdWifiInicio) {
            case R.id.radioButton_wifi_inicio_activar:
                modoTelefono += getString(R.string.modo_activar);
                break;
            case R.id.radioButton_wifi_inicio_desactivar:
                modoTelefono += getString(R.string.modo_desactivar);
                break;
            case R.id.radioButton_wifi_inicio_no_cambiar:
                modoTelefono += getString(R.string.modo_no_cambiar);
                break;
        }

        int radioButtonIdWifiFin = radioGroupWifiFin.getCheckedRadioButtonId();
        switch (radioButtonIdWifiFin) {
            case R.id.radioButton_wifi_fin_activar:
                modoTelefono += getString(R.string.modo_activar);
                break;
            case R.id.radioButton_wifi_fin_desactivar:
                modoTelefono += getString(R.string.modo_desactivar);
                break;
            case R.id.radioButton_wifi_fin_no_cambiar:
                modoTelefono += getString(R.string.modo_no_cambiar);
                break;
        }

        int radioButtonIdSonidoInicio = radioGroupSonidoInicio.getCheckedRadioButtonId();
        switch (radioButtonIdSonidoInicio) {
            case R.id.radioButton_sonido_inicio_activar:
                modoTelefono += getString(R.string.modo_activar);
                break;
            case R.id.radioButton_sonido_inicio_no_cambiar:
                modoTelefono += getString(R.string.modo_no_cambiar);
                break;
            case R.id.radioButton_sonido_inicio_silencio:
                modoTelefono += getString(R.string.silencio_modo);
                break;
            case R.id.radioButton_sonido_inicio_vibrate:
                modoTelefono += getString(R.string.vibrate_modo);
                break;
        }

        int radioButtonIdSonidoFin = radioGroupSonidoFin.getCheckedRadioButtonId();
        switch (radioButtonIdSonidoFin) {
            case R.id.radioButton_sonido_fin_activar:
                modoTelefono += getResources().getString(R.string.modo_activar);
                break;
            case R.id.radioButton_sonido_fin_no_cambiar:
                modoTelefono += getString(R.string.modo_no_cambiar);
                break;
            case R.id.radioButton_sonido_fin_silencio:
                modoTelefono += getString(R.string.silencio_modo);
                break;
            case R.id.radioButton_sonido_fin_vibrate:
                modoTelefono += getString(R.string.vibrate_modo);
                break;
        }

        int radioButtonIdBluetoothInicio = radioGroupBluetoothInicio.getCheckedRadioButtonId();
        switch (radioButtonIdBluetoothInicio) {
            case R.id.radioButton_bluetooth_inicio_activar:
                modoTelefono += getString(R.string.modo_activar);
                break;
            case R.id.radioButton_bluetooth_inicio_desactivar:
                modoTelefono += getString(R.string.modo_desactivar);
                break;
            case R.id.radioButton_bluetooth_inicio_no_cambiar:
                modoTelefono += getString(R.string.modo_no_cambiar);
                break;
        }

        int radioButtonIdBluetoothFin = radioGroupBluetoothFin.getCheckedRadioButtonId();
        switch (radioButtonIdBluetoothFin) {
            case R.id.radioButton_bluetooth_fin_activar:
                modoTelefono += getString(R.string.modo_activar);
                break;
            case R.id.radioButton_bluetooth_fin_desactivar:
                modoTelefono += getString(R.string.modo_desactivar);
                break;
            case R.id.radioButton_bluetooth_fin_no_cambiar:
                modoTelefono += getString(R.string.modo_no_cambiar);
                break;
        }
        return modoTelefono;
    }


    /**
     * Método que se encarga de mostrar en pantalla los datos del Turno seleccionado.
     *
     * @param turno Turno del cual queremos mostrar los datos en pantalla.
     */
    private void showTurno(Turno turno) {

        mCollapsingView.setTitle(turno.getNombreTurno());

        textViewPrevisualizacionTurno.setBackgroundColor(turno.getColorFondo());
        textViewPrevisualizacionTurno.setTextColor(turno.getColorTexto());
        textViewPrevisualizacionTurno.setText(turno.getAbreviaturaNombreTurno());

        editTextNombreTurno.setText(turno.getNombreTurno());
        editTextAbreviaturaTurno.setText(turno.getAbreviaturaNombreTurno());
        btnSeleccionColorFondo.setBackgroundColor(turno.getColorFondo());
        btnSeleccionColorTexto.setBackgroundColor(turno.getColorTexto());
        editTextHoraInicio1.setText(turno.getHoraInicio1());
        editTextHoraFin1.setText(turno.getHoraFin1());
        if (turno.getTurnoPartido() == 1) {//0(falso) o 1(verdadero)
            switchTurnoPartido.setChecked(true);
            editTextHoraInicio2.setText(turno.getHoraInicio2());
            editTextHoraFin2.setText(turno.getHoraFin2());
        }
        editTextHoraTrabajadaNoche.setText(String.valueOf(turno.getHorasTrabajadasNocturnas()));
        editTextPrecioHora.setText(String.valueOf(turno.getPrecioHora()));
        editTextPrecioHoraNocturna.setText(String.valueOf(turno.getPrecioHoraNocturnas()));
        editTextPrecioHoraExtra.setText(String.valueOf(turno.getPrecioHoraExtra()));
        if (turno.getAviso() == 1) {
            switchAviso.setChecked(true);
            editTextAvisoHora.setText(turno.getHoraAviso());
            switchAvisoDiaAntes.setChecked(turno.getAvisoDiaAntes() == 1);
        }
        if (turno.getModoTelefono().equals("")) {
            switchModosTelefono.setChecked(false);
        } else {
            switchModosTelefono.setChecked(true);
            activarModosTelefono(turno);
        }
    }

    /**
     * Método que se encarga de establecer los modos del teléfono en la pantalla de detalle.
     *
     * @param turno Turno del cual se quiere visualizar los modos del teléfono
     */
    private void activarModosTelefono(Turno turno) {

        String modoWifiInicio = turno.getModoTelefono().substring(0, 1);
        if (modoWifiInicio.equals(getString(R.string.modo_activar))) {
            radioButtonWifiInicioActivar.setChecked(true);
        } else if (modoWifiInicio.equals(getString(R.string.modo_desactivar))) {
            radioButtonWifiInicioDesactivar.setChecked(true);
        } else if (modoWifiInicio.equals(getString(R.string.modo_no_cambiar))) {
            radioButtonWifiInicioNoCambiar.setChecked(true);
        }

        String modoWifiFin = turno.getModoTelefono().substring(1, 2);
        if (modoWifiFin.equals(getString(R.string.modo_activar))) {
            radioButtonWifiFinActivar.setChecked(true);
        } else if (modoWifiFin.equals(getString(R.string.modo_desactivar))) {
            radioButtonWifiFinDesactivar.setChecked(true);
        } else if (modoWifiFin.equals(getString(R.string.modo_no_cambiar))) {
            radioButtonWifiFinNoCambiar.setChecked(true);
        }

        String modoSonidoInicio = turno.getModoTelefono().substring(2, 3);
        if (modoSonidoInicio.equals(getString(R.string.modo_activar))) {
            radioButtonSonidoInicio.setChecked(true);
        } else if (modoSonidoInicio.equals(getString(R.string.silencio_modo))) {
            radioButtonSilencioInicio.setChecked(true);
        } else if (modoSonidoInicio.equals(getString(R.string.modo_no_cambiar))) {
            radioButtonSonidoInicioNoCambiar.setChecked(true);
        } else if (modoSonidoInicio.equals(getString(R.string.vibrate_modo))) {
            radioButtonVibracionInicio.setChecked(true);
        }

        String modoSonidoFin = turno.getModoTelefono().substring(3, 4);
        if (modoSonidoFin.equals(getString(R.string.modo_activar))) {
            radioButtonSonidoFin.setChecked(true);
        } else if (modoSonidoFin.equals(getString(R.string.silencio_modo))) {
            radioButtonSilencioFin.setChecked(true);
        } else if (modoSonidoFin.equals(getString(R.string.modo_no_cambiar))) {
            radioButtonSonidoFinNoCambiar.setChecked(true);
        } else if (modoSonidoFin.equals(getString(R.string.vibrate_modo))) {
            radioButtonVibracionFin.setChecked(true);
        }

        String modoBluetoothInicio = turno.getModoTelefono().substring(4, 5);
        if (modoBluetoothInicio.equals(getString(R.string.modo_activar))) {
            radioButtonBluetoothInicioActivar.setChecked(true);
        } else if (modoBluetoothInicio.equals(getString(R.string.modo_desactivar))) {
            radioButtonBluetoothInicioDesactivar.setChecked(true);
        } else if (modoBluetoothInicio.equals(getString(R.string.modo_no_cambiar))) {
            radioButtonBluetoothInicioNoCambiar.setChecked(true);
        }

        String modoBluetoothFin = turno.getModoTelefono().substring(5);
        if (modoBluetoothFin.equals(getString(R.string.modo_activar))) {
            radioButtonBluetoothFiniActivar.setChecked(true);
        } else if (modoBluetoothFin.equals(getString(R.string.modo_desactivar))) {
            radioButtonBluetoothFinDesactivar.setChecked(true);
        } else if (modoBluetoothFin.equals(getString(R.string.modo_no_cambiar))) {
            radioButtonBluetoothFinNoCambiar.setChecked(true);
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha borrado o no el turno de la BBDD.
     *
     * @param requery Si se ha borrado o no el turno.
     */
    private void showTurnoScreenFromDelete(boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_eliminar_turno), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.turno_eliminado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha creado o no el turno en la BBDD.
     *
     * @param requery Si se ha creado o no el turno.
     */
    private void showTurnoScreenFromAdd(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_crear_turno), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.turno_creado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha editado o no el turno en la BBDD.
     *
     * @param requery Si se ha editado o no el turno.
     */
    private void showTurnoScreenFromEdit(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_editar_turno), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.turno_editado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Clase asíncrona encargada de obtener los datos del turno selecciondo y mostrarlos en pantalla,
     * en caso de que no se obtengan los datos muetra un mensaje de error.
     */
    private class GetTurnoByIdTask extends AsyncTask<Void, Void, Cursor> {
        /**
         * Obtenemos los datos del turno seleccionado
         *
         * @param voids
         * @return Cursor con los datos del turno seleccionado.
         */
        @Override
        protected Cursor doInBackground(Void... voids) {
            return datos.obtenerTurnoById(mTurnoId);
        }

        /**
         * Mostramos los datos obtenidos del turno en pantalla, si no hemos obtenido los datos del
         * turno mostramos un menaje de error.
         *
         * @param cursor Cursor que contiene los datos del Turno para mostrar.
         */
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showTurno(new Turno(cursor));
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_cargar_informacion), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Clase asíncrona encargada de borrar un turno seleccionado
     */
    private class DeleteTurnoTask extends AsyncTask<Void, Void, Integer> {
        /**
         * Eliminamos el turno seleccionado
         *
         * @param voids
         * @return Si se ha eliminado el turno
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            return datos.eliminarTurno(mTurnoId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showTurnoScreenFromDelete(integer > 0);
        }
    }

    /**
     * Clase asíncrona encargada de crear o editar un turno.
     */
    private class AddEditTurnoTask extends AsyncTask<Turno, Void, Boolean> {
        /**
         * Insertamos o editamos el turno
         * @param turnos
         * @return
         */
        @Override
        protected Boolean doInBackground(Turno... turnos) {
            if (mTurnoId != null) {
                return datos.editarTurno(turnos[0], mTurnoId) > 0;
            } else {
                return datos.insertarTurno(turnos[0]) != null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (mTurnoId != null) {
                showTurnoScreenFromEdit(result);
            } else {
                showTurnoScreenFromAdd(result);
            }
        }
    }

    /**
     * Clase privada encargada de validar los datos mientras se introducen.
     */
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_NombreTurno:
                    nombreTurnoValidado = validarEditTextVacio(context, editTextNombreTurno);
                    break;
                case R.id.editText_AbreviaturaTurno:
                    abreviaturaTurnoValidado = validarEditTextVacio(context, editTextAbreviaturaTurno);
                    textViewPrevisualizacionTurno.setText(editTextAbreviaturaTurno.getText());
                    break;
                case R.id.editText_valor_hora_inicio_1:
                    validarEditTextVacio(context, editTextHoraInicio1);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    //editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editText_valor_hora_fin_1:
                    validarEditTextVacio(context, editTextHoraFin1);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    // editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editText_valor_hora_inicio_2:
                    validarEditTextVacio(context, editTextHoraInicio2);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    //editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editText_valor_hora_fin_2:
                    validarEditTextVacio(context, editTextHoraFin2);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    // editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editTextValorPrecioHoras:
                    precioHoraValidado = validarEditTextVacio(context, editTextPrecioHora);
                    break;
                case R.id.editText_valor_aviso_hora:
                    validarEditTextVacio(context, editTextAvisoHora);
                    break;
            }
        }
    }
}