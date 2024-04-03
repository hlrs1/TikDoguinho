/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.model.negocio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Dry
 */
@Entity
public class Pet_Usuario {
   
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private int tutor_codigo;
    private int pet_codigo;

    public int getTutor_codigo() {
        return tutor_codigo;
    }

    public void setTutor_codigo(int tutor_codigo) {
        this.tutor_codigo = tutor_codigo;
    }

    public int getPet_codigo() {
        return pet_codigo;
    }

    public void setPet_codigo(int pet_codigo) {
        this.pet_codigo = pet_codigo;
    }
    
    
}
