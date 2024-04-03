/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.model.negocio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 *
 * @author ALUNO
 */

@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int codigo;
    @Column(unique=true)
    private String usuario;
    private String senha;
    private String email;
    private boolean mamae;
    @Lob
    private byte[] imagemUsuario = null;
    private List<String> idsDePetsRecebidos = new ArrayList();

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMamae() {
        return mamae;
    }

    public void setMamae(boolean mamae) {
        this.mamae = mamae;
    }
    
    public byte[] getImagemUsuario() {
        return imagemUsuario;
    }

    public void setImagemUsuario(byte[] imagemUsuario) {
        this.imagemUsuario = imagemUsuario;
    }

    public List<String> getIdsDePetsRecebidos() {
        return idsDePetsRecebidos;
    }

    public void setIdsDePetsRecebidos(List<String> idsDePetsRecebidos) {
        this.idsDePetsRecebidos = idsDePetsRecebidos;
    }

    public void addIdDePetRecebido(int i){
        String id = Integer.toString(i);        
        this.idsDePetsRecebidos.add(id);
    }
}
