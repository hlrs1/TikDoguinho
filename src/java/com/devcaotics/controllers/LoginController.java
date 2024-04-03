/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.controllers;

import com.devcaotics.model.dao.ManagerDao;
import com.devcaotics.model.negocio.Pet;
import com.devcaotics.model.negocio.Usuario;
import java.util.ArrayList;
import java.util.List;
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
public class LoginController {

    private static LoginController myself = null;
    public static LoginController getCurrentInstance(){
        if(myself == null)
            myself = new LoginController();
        
        return myself;
    }
    private Usuario usuarioLogado;
    
    private List<Pet> petsDaSessao = new ArrayList();
    
    public String realizarLogin(String login, String senha) {

        try {
            Usuario uLogin = (Usuario) ManagerDao.getCurrentInstance()
                    .readAll("select u from Usuario u"
                            + " where u.usuario = '" + login
                            + "' and u.senha = '" + senha+"'", Usuario.class).get(0);
            
            
            this.usuarioLogado = uLogin;
            
//            PetController p = new PetController();
//            
//            this.petsDaSessao = p.readPetsByTutor(this);
            
            return "indexUsuario";
        } catch (Exception e) {
            
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Logar","Usuário e/ou senha estão incorretos"));
            return null;
        }

    }
    
    public String logout(){
        this.usuarioLogado = null;
        
        return "login";
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public List<Pet> getPetsDaSessao() {
        return petsDaSessao;
    }

    public void setPetsDaSessao(List<Pet> petsDaSessao) {
        this.petsDaSessao = petsDaSessao;
    }
    
    
    

}
