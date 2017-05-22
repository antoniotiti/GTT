package com.example.antonio.gestiontrabajotemporal.modelo;

import android.database.Cursor;

import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;

/**
 * Clase para la creación de objetos Operarios
 */
public class Operario {

    public String idOperario;
    public String dni;
    public String nombre;
    public String apellidos;
    public String foto;
    public String direccion;
    public String fechaNacimiento;
    public String telefono;
    public String email;
    public String fechaInicio;
    public String numeroSS;
    public String password;

    public Operario(String idOperario, String dni, String nombre, String apellidos, String foto, String direccion, String fechaNacimiento, String telefono, String email, String fechaInicio, String numeroSS, String password) {
        this.idOperario = idOperario;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.fechaInicio = fechaInicio;
        this.numeroSS = numeroSS;
        this.password = password;
    }

    public Operario(String idOperario, String dni, String nombre, String apellidos, String direccion, String fechaNacimiento, String telefono, String email, String numeroSS, String password) {
        this.idOperario = idOperario;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = null;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.fechaInicio = null;
        this.numeroSS = numeroSS;
        this.password = password;
    }

    public Operario(Cursor cursor) {
        this.idOperario = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.ID));
        this.dni = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.DNI));
        this.nombre = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.NOMBRE));
        this.apellidos = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.APELLIDOS));
        this.foto = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.FOTO));
        this.direccion = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.DIRECCION));
        this.fechaNacimiento = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.FECHA_NACIMIENTO));
        this.telefono = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.TELEFONO));
        this.email = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.EMAIL));
        this.fechaInicio = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.FECHA_INICIO));
        this.numeroSS = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.NUMERO_S_S));
        this.password = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Operarios.PASSWORD));
    }
    public String getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(String idOperario) {
        this.idOperario = idOperario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNumeroSS() {
        return numeroSS;
    }

    public void setNumeroSS(String numeroSS) {
        this.numeroSS = numeroSS;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Operario{" +
                "idTurno=" + idOperario +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", foto='" + foto + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", numeroSS='" + numeroSS + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}