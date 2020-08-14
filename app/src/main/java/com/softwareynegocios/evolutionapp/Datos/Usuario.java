package com.softwareynegocios.evolutionapp.Datos;

public class Usuario {

    private int codusuario;
    private String nombreusuario;
    private String mail;
    private String password;

    public Usuario() {
    }

    public Usuario(int codusuario, String nombreusuario, String mail, String password) {
        this.codusuario = codusuario;
        this.nombreusuario = nombreusuario;
        this.mail = mail;
        this.password = password;
    }

    public int getCodusuario() {
        return codusuario;
    }

    public void setCodusuario(int codusuario) {
        this.codusuario = codusuario;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}