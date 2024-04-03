/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.model.negocio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int codigo;
    private String nome;
    private String mesAnoDeNascimento;
    private String porte;
    private boolean compartilhado = false;
    @Lob
    private byte[] imagemPet;
    
    private List<Usuario> tutor = new ArrayList();

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMesAnoDeNascimento() {
        return mesAnoDeNascimento;
    }

    public void setMesAnoDeNascimento(String mesAnoDeNascimento) {
        this.mesAnoDeNascimento = mesAnoDeNascimento;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public boolean isCompartilhado() {
        return compartilhado;
    }

    public void setCompartilhado(boolean compartilhado) {
        this.compartilhado = compartilhado;
    }

    public byte[] getImagemPet() {
        return imagemPet;
    }

    public void setImagemPet(byte[] imagemPet) {
        this.imagemPet = imagemPet;
    }

    
    
    public List<Usuario> getTutor() {
        return tutor;
    }

    public void setTutor(List<Usuario> tutor) {
        this.tutor = tutor;
    }
    
}
