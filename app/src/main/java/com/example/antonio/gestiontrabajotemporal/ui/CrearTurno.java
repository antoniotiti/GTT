package com.example.antonio.gestiontrabajotemporal.ui;

import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.calcularHorasTrabajadas;

public class CrearTurno extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, SimpleDialog.OnSimpleDialogListener {

    static final int TIME_DIALOG_ID = 0;

    OperacionesBaseDatos datos;

    Button btnSeleccionColorFondo, btnSeleccionColorTexto, btnPrevisualizacion, btnBorrar, btnGuardar;

    EditText editTextNombreTurno, editTextAbreviaturaTurno, editTextHoraInicio1, editTextHoraFin1,
            editTextHoraInicio2, editTextHoraFin2, editTextPrecioHora, editTextPrecioHoraNocturna,
            editTextPrecioHoraExtra, editTextAvisoHora, activeTimeDisplay, editTextHoraTrabajadaNoche;

    TextView textViewHoraTrabajada;

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


    //Diálogo selección de hora.
    // private TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_turno);

        setToolbar();// Añadir la Toolbar.

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

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
        mSelectedColor = ContextCompat.getColor(this, R.color.grey05);

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
                        btnPrevisualizacion.setBackgroundColor(mSelectedColor);
                        break;
                    case "colorTexto":
                        btnSeleccionColorTexto.setBackgroundColor(mSelectedColor);
                        btnPrevisualizacion.setTextColor(mSelectedColor);
                        break;
                }
            }
        });
/////////////////////Fin Diálogo selección de color///////////////

        // Obtenemos las referencias de las vistas.

        editTextNombreTurno = (EditText) findViewById(R.id.editText_NombreTurno);//
        editTextNombreTurno.addTextChangedListener(new MyTextWatcher(editTextNombreTurno));

        editTextAbreviaturaTurno = (EditText) findViewById(R.id.editText_AbreviaturaTurno);
        editTextAbreviaturaTurno.addTextChangedListener(new MyTextWatcher(editTextAbreviaturaTurno));

        btnPrevisualizacion = (Button) findViewById(R.id.btn_previsualizacion);

        btnSeleccionColorFondo = (Button) findViewById(R.id.btn_SeleccionColorFondo);
        btnSeleccionColorFondo.setOnClickListener(this);

        btnSeleccionColorTexto = (Button) findViewById(R.id.btn_SeleccionColorTexto);
        btnSeleccionColorTexto.setOnClickListener(this);

        editTextHoraInicio1 = (EditText) findViewById(R.id.editText_valor_hora_inicio_1);
        editTextHoraInicio1.setOnClickListener(this);
        editTextHoraInicio1.addTextChangedListener(new MyTextWatcher(editTextHoraInicio1));

        editTextHoraFin1 = (EditText) findViewById(R.id.editText_valor_hora_fin_1);
        editTextHoraFin1.setOnClickListener(this);
        editTextHoraFin1.addTextChangedListener(new MyTextWatcher(editTextHoraFin1));

        editTextHoraInicio2 = (EditText) findViewById(R.id.editText_valor_hora_inicio_2);
        editTextHoraInicio2.setOnClickListener(this);
        editTextHoraInicio2.addTextChangedListener(new MyTextWatcher(editTextHoraInicio2));

        editTextHoraFin2 = (EditText) findViewById(R.id.editText_valor_hora_fin_2);
        editTextHoraFin2.setOnClickListener(this);
        editTextHoraFin2.addTextChangedListener(new MyTextWatcher(editTextHoraFin2));

        textViewHoraTrabajada = (TextView) findViewById((R.id.textView_valor_horas_trabajadas));

        editTextHoraTrabajadaNoche = (EditText) findViewById((R.id.editText_valor_horas_trabajadas_noche));
        editTextHoraTrabajadaNoche.setOnClickListener(this);

        editTextPrecioHora = (EditText) findViewById((R.id.editTextValorPrecioHoras));
        editTextPrecioHora.addTextChangedListener(new MyTextWatcher(editTextPrecioHora));

        editTextPrecioHoraNocturna = (EditText) findViewById((R.id.editTextValorPrecioNoche));
        // editTextPrecioHoraNocturna.addTextChangedListener(new MyTextWatcher(editTextPrecioHoraNocturna));

        editTextPrecioHoraExtra = (EditText) findViewById((R.id.editTextValorPrecioExtra));
        // editTextPrecioHoraExtra.addTextChangedListener(new MyTextWatcher(editTextPrecioHoraExtra));

        editTextAvisoHora = (EditText) findViewById(R.id.editText_valor_aviso_hora);
        editTextAvisoHora.setOnClickListener(this);
        editTextAvisoHora.addTextChangedListener(new MyTextWatcher(editTextAvisoHora));

        relativeLayoutHorioTurnoPartido = (RelativeLayout) findViewById(R.id.layout_turno_partido);//Layout para mostrar el turno partido
        relativeLayoutHorioTurnoPartido.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout

        relativeLayoutAviso = (RelativeLayout) findViewById(R.id.layout_seleccion_hora_aviso);//Layout para mostrar el aviso
        relativeLayoutAviso.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout

        linearLayoutModosTelefono = (LinearLayout) findViewById(R.id.layout_modos_telefono);//Layout para mostrar los modos del telefonoo
        linearLayoutModosTelefono.setVisibility(View.GONE);//Por defecto está invisible y no ocupa espacio en el layout

//////////////////RadioGroup y RadioButton
        radioGroupWifiInicio = (RadioGroup) findViewById(R.id.radioGroup_modo_wifi_inicio);

        radioButtonWifiInicioActivar = (RadioButton) radioGroupWifiInicio.getChildAt(0);
        radioButtonWifiInicioDesactivar = (RadioButton) radioGroupWifiInicio.getChildAt(1);
        radioButtonWifiInicioNoCambiar = (RadioButton) radioGroupWifiInicio.getChildAt(2);

        radioGroupWifiFin = (RadioGroup) findViewById(R.id.radioGroup_modo_wifi_fin);

        radioButtonWifiFinActivar = (RadioButton) radioGroupWifiFin.getChildAt(0);
        radioButtonWifiFinDesactivar = (RadioButton) radioGroupWifiFin.getChildAt(1);
        radioButtonWifiFinNoCambiar = (RadioButton) radioGroupWifiFin.getChildAt(2);

        radioGroupBluetoothInicio = (RadioGroup) findViewById(R.id.radioGroup_modo_bluetooth_inicio);

        radioButtonBluetoothInicioActivar = (RadioButton) radioGroupBluetoothInicio.getChildAt(0);
        radioButtonBluetoothInicioDesactivar = (RadioButton) radioGroupBluetoothInicio.getChildAt(1);
        radioButtonBluetoothInicioNoCambiar = (RadioButton) radioGroupBluetoothInicio.getChildAt(2);

        radioGroupBluetoothFin = (RadioGroup) findViewById(R.id.radioGroup_modo_bluetooth_fin);

        radioButtonBluetoothFiniActivar = (RadioButton) radioGroupBluetoothFin.getChildAt(0);
        radioButtonBluetoothFinDesactivar = (RadioButton) radioGroupBluetoothFin.getChildAt(1);
        radioButtonBluetoothFinNoCambiar = (RadioButton) radioGroupBluetoothFin.getChildAt(2);

        radioGroupSonidoInicio = (RadioGroup) findViewById(R.id.radioGroup_modo_sonido_inicio);

        radioButtonSonidoInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(0);
        radioButtonVibracionInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(1);
        radioButtonSilencioInicio = (RadioButton) radioGroupSonidoInicio.getChildAt(2);
        radioButtonSonidoInicioNoCambiar = (RadioButton) radioGroupSonidoInicio.getChildAt(3);

        radioGroupSonidoFin = (RadioGroup) findViewById(R.id.radioGroup_modo_sonido_fin);

        radioButtonSonidoFin = (RadioButton) radioGroupSonidoFin.getChildAt(0);
        radioButtonVibracionFin = (RadioButton) radioGroupSonidoFin.getChildAt(1);
        radioButtonSilencioFin = (RadioButton) radioGroupSonidoFin.getChildAt(2);
        radioButtonSonidoFinNoCambiar = (RadioButton) radioGroupSonidoFin.getChildAt(3);
//////////////////RadioGroup y RadioButton

        switchTurnoPartido = (Switch) findViewById(R.id.switch_turno_partido);
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

        switchAviso = (Switch) findViewById(R.id.switch_avisos);
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

        switchAvisoDiaAntes = (Switch) findViewById(R.id.switch_aviso_dia_antes);
        switchAvisoDiaAntes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ///////TODO

            }
        });

        switchModosTelefono = (Switch) findViewById(R.id.switch_modo_telefono);
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

        btnBorrar = (Button) findViewById(R.id.button_borrar);
        btnBorrar.setOnClickListener(this);

        btnGuardar = (Button) findViewById(R.id.button_guardar);
        btnGuardar.setOnClickListener(this);


    }

    /**
     *
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Inicio_turnos);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    public void showTimeDialog(EditText dateDisplay) {
        activeTimeDisplay = dateDisplay;

        TimeDialog newFragment = new TimeDialog();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        //showDialog(TIME_DIALOG_ID);
    }

    private void updateDisplay(EditText dateDisplay, Calendar time) {
        String hora = formatter.format(time.getTime());
        dateDisplay.setText(hora);

    }

    /*private void unregisterDateDisplay() {
        activeTime.set(0, 0, 0, 0, 0, 0);
    }*/

  /*  @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeSetListener, activeTime.get(Calendar.HOUR), activeTime.get(Calendar.MINUTE), true);
        }
        return null;
    }*/


    @Override
    public void onClick(View v) {

        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_SeleccionColorFondo:
                colorPickerDialog.show(getFragmentManager(), "colorFondo");
                break;
            case R.id.btn_SeleccionColorTexto:
                colorPickerDialog.show(getFragmentManager(), "colorTexto");
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
                new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");
                break;
            case R.id.button_guardar:
                boolean formularioValidado = validarFormulario();

                break;
            default:
                break;
        }
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        activeTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        activeTime.set(Calendar.MINUTE, minute);
        updateDisplay(activeTimeDisplay, activeTime);
    }

    @Override
    public void onPossitiveButtonClick(String tag, String fecha) {
        limpiarFormularioCreacionTurno();

    }

    @Override
    public void onNegativeButtonClick(String tag, String fecha) {
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();

    }


    private void limpiarFormularioCreacionTurno() {
        Toast.makeText(this, "Borrar Formulario", Toast.LENGTH_LONG).show();

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
            if (turnoInsertado!=null) {
                Toast.makeText(getApplicationContext(), "Turno Creado", Toast.LENGTH_LONG).show();
                datos.close();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Turno " + nombreTurno + " repetido", Toast.LENGTH_LONG).show();
            }
        }


        return validado;
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
                    btnPrevisualizacion.setText(editTextAbreviaturaTurno.getText());
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

