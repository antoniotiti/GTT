package com.example.antonio.gestiontrabajotemporal.turnodetalle;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.calcularHoraDecimal;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.calcularHorasTrabajadas;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TurnoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TurnoDetailFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{

    static final int TIME_DIALOG_ID = 0;

    private static final String ARG_TURNO_ID = "turnoId";

    OperacionesBaseDatos datos;
    Button btnSeleccionColorFondo, btnSeleccionColorTexto, btnBorrar, btnGuardar;
    EditText editTextNombreTurno, editTextAbreviaturaTurno, editTextHoraInicio1, editTextHoraFin1,
            editTextHoraInicio2, editTextHoraFin2, editTextPrecioHora, editTextPrecioHoraNocturna,
            editTextPrecioHoraExtra, editTextAvisoHora, activeTimeDisplay, editTextHoraTrabajadaNoche;
    TextView textViewHoraTrabajada,textViewPrevisualizacionTurno;
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
    //Formato de hora.
    SimpleDateFormat formatter = new SimpleDateFormat("kk:mm", new Locale("es", "ES"));
    //Diálogo selección de color.
    ColorPickerDialog colorPickerDialog;
    private String mTurnoId;
    private CollapsingToolbarLayout mCollapsingView;


    public TurnoDetailFragment() {
        // Required empty public constructor
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

        if (getArguments() != null) {
            mTurnoId = getArguments().getString(ARG_TURNO_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_turno_detail, container, false);

        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);

        activeTime = Calendar.getInstance();
        activeTime.set(0, 0, 0, 0, 0, 0);

/////////////////////Inicio Diálogo selección de hora///////////////
        /*timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                activeTime.set(Calendar.HOUR, hourOfDay);
                activeTime.set(Calendar.MINUTE, minute);
                updateDisplay(activeTimeDisplay, activeTime);
            }
        };*/
/////////////////////Fin Diálogo selección de hora///////////////

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
        editTextNombreTurno = (EditText) root.findViewById(R.id.editText_NombreTurno);//
        editTextNombreTurno.addTextChangedListener(new MyTextWatcher(editTextNombreTurno));

        editTextAbreviaturaTurno = (EditText) root.findViewById(R.id.editText_AbreviaturaTurno);
        editTextAbreviaturaTurno.addTextChangedListener(new MyTextWatcher(editTextAbreviaturaTurno));

        textViewPrevisualizacionTurno =(TextView) root.findViewById(R.id.textView_prev_turno);
        //btnPrevisualizacion = (Button) root.findViewById(R.id.btn_previsualizacion);

        btnSeleccionColorFondo = (Button) root.findViewById(R.id.btn_SeleccionColorFondo);
        btnSeleccionColorFondo.setOnClickListener(this);

        btnSeleccionColorTexto = (Button) root.findViewById(R.id.btn_SeleccionColorTexto);
        btnSeleccionColorTexto.setOnClickListener(this);

        editTextHoraInicio1 = (EditText) root.findViewById(R.id.editText_valor_hora_inicio_1);
        editTextHoraInicio1.setOnClickListener(this);
        editTextHoraInicio1.addTextChangedListener(new MyTextWatcher(editTextHoraInicio1));

        editTextHoraFin1 = (EditText) root.findViewById(R.id.editText_valor_hora_fin_1);
        editTextHoraFin1.setOnClickListener(this);
        editTextHoraFin1.addTextChangedListener(new MyTextWatcher(editTextHoraFin1));

        editTextHoraInicio2 = (EditText) root.findViewById(R.id.editText_valor_hora_inicio_2);
        editTextHoraInicio2.setOnClickListener(this);
        editTextHoraInicio2.addTextChangedListener(new MyTextWatcher(editTextHoraInicio2));

        editTextHoraFin2 = (EditText) root.findViewById(R.id.editText_valor_hora_fin_2);
        editTextHoraFin2.setOnClickListener(this);
        editTextHoraFin2.addTextChangedListener(new MyTextWatcher(editTextHoraFin2));

        textViewHoraTrabajada = (TextView) root.findViewById((R.id.textView_valor_horas_trabajadas));

        editTextHoraTrabajadaNoche = (EditText) root.findViewById((R.id.editText_valor_horas_trabajadas_noche));
        editTextHoraTrabajadaNoche.setOnClickListener(this);

        editTextPrecioHora = (EditText) root.findViewById((R.id.editTextValorPrecioHoras));
        editTextPrecioHora.addTextChangedListener(new MyTextWatcher(editTextPrecioHora));

        editTextPrecioHoraNocturna = (EditText) root.findViewById((R.id.editTextValorPrecioNoche));
        // editTextPrecioHoraNocturna.addTextChangedListener(new MyTextWatcher(editTextPrecioHoraNocturna));

        editTextPrecioHoraExtra = (EditText) root.findViewById((R.id.editTextValorPrecioExtra));
        // editTextPrecioHoraExtra.addTextChangedListener(new MyTextWatcher(editTextPrecioHoraExtra));

        editTextAvisoHora = (EditText) root.findViewById(R.id.editText_valor_aviso_hora);
        editTextAvisoHora.setOnClickListener(this);
        editTextAvisoHora.addTextChangedListener(new MyTextWatcher(editTextAvisoHora));

        relativeLayoutHorioTurnoPartido = (RelativeLayout) root.findViewById(R.id.layout_turno_partido);//Layout para mostrar el turno partido
        relativeLayoutHorioTurnoPartido.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout

        relativeLayoutAviso = (RelativeLayout) root.findViewById(R.id.layout_seleccion_hora_aviso);//Layout para mostrar el aviso
        relativeLayoutAviso.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout

        linearLayoutModosTelefono = (LinearLayout) root.findViewById(R.id.layout_modos_telefono);//Layout para mostrar los modos del telefonoo
        linearLayoutModosTelefono.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout

//////////////////RadioGroup y RadioButton
        radioGroupWifiInicio = (RadioGroup) root.findViewById(R.id.radioGroup_modo_wifi_inicio);

        radioButtonWifiInicioActivar = (RadioButton) radioGroupWifiInicio.getChildAt(0);
        radioButtonWifiInicioDesactivar = (RadioButton) radioGroupWifiInicio.getChildAt(1);
        radioButtonWifiInicioNoCambiar = (RadioButton) radioGroupWifiInicio.getChildAt(2);

        radioGroupWifiFin = (RadioGroup) root.findViewById(R.id.radioGroup_modo_wifi_fin);

        radioButtonWifiFinActivar = (RadioButton) radioGroupWifiFin.getChildAt(0);
        radioButtonWifiFinDesactivar = (RadioButton) radioGroupWifiFin.getChildAt(1);
        radioButtonWifiFinNoCambiar = (RadioButton) radioGroupWifiFin.getChildAt(2);

        radioGroupBluetoothInicio = (RadioGroup) root.findViewById(R.id.radioGroup_modo_bluetooth_inicio);

        radioButtonBluetoothInicioActivar = (RadioButton) radioGroupBluetoothInicio.getChildAt(0);
        radioButtonBluetoothInicioDesactivar = (RadioButton) radioGroupBluetoothInicio.getChildAt(1);
        radioButtonBluetoothInicioNoCambiar = (RadioButton) radioGroupBluetoothInicio.getChildAt(2);

        radioGroupBluetoothFin = (RadioGroup) root.findViewById(R.id.radioGroup_modo_bluetooth_fin);

        radioButtonBluetoothFiniActivar = (RadioButton) radioGroupBluetoothFin.getChildAt(0);
        radioButtonBluetoothFinDesactivar = (RadioButton) radioGroupBluetoothFin.getChildAt(1);
        radioButtonBluetoothFinNoCambiar = (RadioButton) radioGroupBluetoothFin.getChildAt(2);

        radioGroupSonidoInicio = (RadioGroup) root.findViewById(R.id.radioGroup_modo_sonido_inicio);

        radioButtonSonidoInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(0);
        radioButtonVibracionInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(1);
        radioButtonSilencioInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(2);
        radioButtonSonidoInicioNoCambiar = (RadioButton) radioGroupSonidoInicio.getChildAt(3);

        radioGroupSonidoFin = (RadioGroup) root.findViewById(R.id.radioGroup_modo_sonido_fin);

        radioButtonSonidoFin = (RadioButton) radioGroupSonidoFin.getChildAt(0);
        radioButtonVibracionFin = (RadioButton) radioGroupSonidoFin.getChildAt(1);
        radioButtonSilencioFin = (RadioButton) radioGroupSonidoFin.getChildAt(2);
        radioButtonSonidoFinNoCambiar = (RadioButton) radioGroupSonidoFin.getChildAt(3);
//////////////////RadioGroup y RadioButton

        switchTurnoPartido = (Switch) root.findViewById(R.id.switch_turno_partido);
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
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, isChecked));
                    //editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,isChecked));
                }
            }
        });

        switchAviso = (Switch) root.findViewById(R.id.switch_avisos);
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
                //relativeLayoutAviso.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        switchAvisoDiaAntes = (Switch) root.findViewById(R.id.switch_aviso_dia_antes);
        switchAvisoDiaAntes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ///////TODO

            }
        });

        switchModosTelefono = (Switch) root.findViewById(R.id.switch_modo_telefono);
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

        btnBorrar = (Button) root.findViewById(R.id.button_borrar);
        btnBorrar.setOnClickListener(this);

        btnGuardar = (Button) root.findViewById(R.id.button_guardar);
        btnGuardar.setOnClickListener(this);

        // Instancia de helper
        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        loadTurno();

        return root;
    }

    private void loadTurno() {
        new GetTurnoByIdTask().execute();
    }

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
        //TODO modo telefono
        // if(turno.getModoTelefono()==1){}


    }

    private void showEditScreen() {
        /*Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, mLawyerId);
        startActivityForResult(intent, LawyersFragment.REQUEST_UPDATE_DELETE_LAWYER);*/
        //TODO edita el turno
    }

    private void showTurnoScreen(boolean requery) {
        if (!requery) {
            showDeleteError();
        }
        getActivity().setResult(requery ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error al eliminar el Turno seleccionado", Toast.LENGTH_SHORT).show();
    }

    public void showTimeDialog(EditText dateDisplay) {
        activeTimeDisplay = dateDisplay;

       // TimeDialog newFragment = new TimeDialog();
        //newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        //showDialog(TIME_DIALOG_ID);

        DialogFragment newFragment = new TimeDialog(); // creating DialogFragment which creates DatePickerDialog
        newFragment.setTargetFragment(TurnoDetailFragment.this,0);  // Passing this fragment DatePickerFragment.
        // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        activeTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        activeTime.set(Calendar.MINUTE, minute);
        updateDisplay(activeTimeDisplay, activeTime);
    }


    private void updateDisplay(EditText dateDisplay, Calendar time) {
        String hora = formatter.format(time.getTime());
        dateDisplay.setText(hora);

    }

    @Override
    public void onClick(View v) {
        // Obtención del manejador de fragmentos
        FragmentManager fragmentManager = getFragmentManager();

        switch (v.getId() /*to get clicked view id**/) {
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
            case R.id.button_borrar:
                new SimpleDialog().show(fragmentManager, "SimpleDialog");
                break;
            case R.id.button_guardar:
                boolean formularioValidado = validarFormulario();

                break;
            default:
                break;
        }
    }


    public void borrarTurno() {
        new DeleteLawyerTask().execute();
        Toast.makeText(getActivity(), "Turno Borrado", Toast.LENGTH_LONG).show();

    }



    private boolean validarFormulario() {
        boolean validado = false;
        nombreTurnoValidado = validarEditTextVacio(editTextNombreTurno);
        abreviaturaTurnoValidado = validarEditTextVacio(editTextAbreviaturaTurno);
        horaInicio1Validado = validarEditTextVacio(editTextHoraInicio1);
        horaFin1Validado = validarEditTextVacio(editTextHoraFin1);
        if (switchTurnoPartido.isChecked()) {
            horaInicio2Validado = validarEditTextVacio(editTextHoraInicio2);
            horaFin2Validado = validarEditTextVacio(editTextHoraFin2);
        }
        precioHoraValidado = validarEditTextVacio(editTextPrecioHora);
        if (switchAviso.isChecked()) {
            avisoHoraValidado = validarEditTextVacio(editTextAvisoHora);
        }

        if (nombreTurnoValidado && abreviaturaTurnoValidado && horaInicio1Validado && horaFin1Validado && precioHoraValidado && horaInicio2Validado && horaFin2Validado && avisoHoraValidado) {

            String nombreTurno = editTextNombreTurno.getText().toString();
            String abreviaturaTurno = editTextAbreviaturaTurno.getText().toString();
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
            int avisoDiaAntes = switchAvisoDiaAntes.isChecked() ? 1 : 0; //0(falso) o 1(verdadero)
            String horaAviso = editTextAvisoHora.getText().toString();
            String modotelefono = "prueba";
            int colorFondo = ((ColorDrawable) btnSeleccionColorFondo.getBackground()).getColor();
            int colorTexto = ((ColorDrawable) btnSeleccionColorTexto.getBackground()).getColor();

            Turno turnoInsertar = new Turno(nombreTurno, abreviaturaTurno, horaInicio1, horaFin1, turnoPartido, horaInicio2, horaFin2, horasTrabajadas, horasTrabajadasNoche, precioHora, precioHoraNocturna, precioHoraExtra, aviso, avisoDiaAntes, horaAviso, modotelefono, colorFondo, colorTexto);
            String turnoInsertado = datos.insertarTurno(turnoInsertar);
            if (turnoInsertado != null) {
                Toast.makeText(getActivity(), "Turno Creado", Toast.LENGTH_LONG).show();
                datos.close();
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Turno " + nombreTurno + " repetido", Toast.LENGTH_LONG).show();
            }
        }


        return validado;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "SimpleDialog");

                //new DeleteLawyerTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Acciones
    }

    private class GetTurnoByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return datos.obtenerTurnoById(mTurnoId);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showTurno(new Turno(cursor));
            } else {
                showLoadError();
            }
        }

    }

    private class DeleteLawyerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return datos.eliminarTurno(mTurnoId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showTurnoScreen(integer > 0);
        }

    }

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
                    nombreTurnoValidado = validarEditTextVacio(editTextNombreTurno);
                    break;
                case R.id.editText_AbreviaturaTurno:
                    abreviaturaTurnoValidado = validarEditTextVacio(editTextAbreviaturaTurno);
                    textViewPrevisualizacionTurno.setText(editTextAbreviaturaTurno.getText());
                    break;
                case R.id.editText_valor_hora_inicio_1:
                    validarEditTextVacio(editTextHoraInicio1);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    //editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editText_valor_hora_fin_1:
                    validarEditTextVacio(editTextHoraFin1);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    // editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editText_valor_hora_inicio_2:
                    validarEditTextVacio(editTextHoraInicio2);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    //editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editText_valor_hora_fin_2:
                    validarEditTextVacio(editTextHoraFin2);
                    textViewHoraTrabajada.setText(calcularHorasTrabajadas(editTextHoraInicio1, editTextHoraFin1, editTextHoraInicio2, editTextHoraFin2, switchTurnoPartido.isChecked()));
                    // editTextHoraTrabajadaNoche.setText(calcularHorasNocturnas(editTextHoraInicio1,editTextHoraFin1,editTextHoraInicio2,editTextHoraFin2,switchTurnoPartido.isChecked()));
                    break;
                case R.id.editTextValorPrecioHoras:
                    precioHoraValidado = validarEditTextVacio(editTextPrecioHora);
                    break;
                case R.id.editText_valor_aviso_hora:
                    validarEditTextVacio(editTextAvisoHora);
                    break;
            }
        }
    }

}