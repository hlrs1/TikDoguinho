/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.controllers;

import com.devcaotics.model.dao.ManagerDao;
import com.devcaotics.model.negocio.Pet;
import com.devcaotics.model.negocio.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ALUNO
 */
@ManagedBean
@SessionScoped
public class PetSelecionado {

    private static PetSelecionado myself = null;
    public static PetSelecionado getCurrentInstance(){
        if(myself == null)
            myself = new PetSelecionado();
        
        return myself;
    }
    private Pet petEscolhido = null;

    public Pet getPetEscolhido() {
        return petEscolhido;
    }

    public void setPetEscolhido(Pet petEscolhido) {
        this.petEscolhido = petEscolhido;
    }
    
    public void setPetSelecionado(Pet p){
        this.petEscolhido = p;
    }

    }
   
