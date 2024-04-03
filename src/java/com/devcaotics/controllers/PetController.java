/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.controllers;

import com.devcaotics.model.dao.ManagerDao;

import com.devcaotics.model.negocio.Pet;
import com.devcaotics.model.negocio.Pet_Usuario;
import com.devcaotics.model.negocio.Usuario;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author ALUNO
 */
@ManagedBean
@SessionScoped
public class PetController {

    private Pet petCadastro;

    private Pet selection;

    private String mes;
    private String ano;
    private String usuario;
    private String op = "alterado";
private int contador=0;
    //isto é para a gambiarra da imagem
    private String imagemPet;
    private int tabIndex = 0;

    @PostConstruct
    public void init() {
        this.petCadastro = new Pet();
    }

    public Pet getPetCadastro() {
        return petCadastro;
    }

    public void setPetCadastro(Pet petCadastro) {
        this.petCadastro = petCadastro;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Pet getSelection() {
        return selection;
    }

    public void setSelection(Pet selection) {
        this.selection = selection;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImagemPet() {
        return imagemPet;
    }

    public void setImagemPet(String imagemPet) {
        this.imagemPet = imagemPet;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        //FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        //FacesContext.getCurrentInstance().addMessage(null, msg);

        byte[] im = new byte[(int) event.getFile().getSize()];

        event.getFile().getInputstream().read(im);
        this.petCadastro.setImagemPet(im);
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage("Imagem Apiloidada"));
        //gambiarra
        ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true)).setAttribute("imagem",
                this.petCadastro.getImagemPet());

        this.imagemPet = "http://localhost:8080/TikDoguinho/ServletExibirImagemPetGambiarra";
    }

    public StreamedContent getImagemDoPet() throws IOException {
        
        Pet p;
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            p = this.selection;
            return new DefaultStreamedContent(new ByteArrayInputStream(p.getImagemPet())); // nesta parte do código ele pega o array de bytes e converte em uma imagem de verdade.
        }
    }
    
    public Pet setarSelection(Pet codigo){
        contador++;
        Pet p = new Pet();
        List<Pet> pets = readPets();
        for(Pet pAux : pets){
            if(pAux.getCodigo()== codigo.getCodigo()){
                p = pAux;
                this.selection = p;
            }
        }
        
        System.out.println("setou pet atual " +selection.getNome()+" contador "+contador);
        return p;
    }

    public StreamedContent getImagemDoPet(int codigo) throws IOException {
        Pet p = new Pet();
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            List<Pet> pets;
            pets = readPets();

            for (Pet pAux : pets) {
                if (pAux.getCodigo() == codigo) {
                    p = pAux;
                    this.selection = p;
                }
            }
            return new DefaultStreamedContent(new ByteArrayInputStream(p.getImagemPet())); // nesta parte do código ele pega o array de bytes e converte em uma imagem de verdade.
            
        }
    }

    public StreamedContent getImagemDoTutor() throws IOException {

        if (this.selection.getTutor().size() > 1) {

            FacesContext context = FacesContext.getCurrentInstance();

            if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {

                return new DefaultStreamedContent();

            } else {
                if (this.selection.getTutor().get(0).getCodigo() == ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                        .getSession(true)).getAttribute("loginController")).getUsuarioLogado().getCodigo()) {
                    return new DefaultStreamedContent(new ByteArrayInputStream(this.selection.getTutor().get(1).getImagemUsuario())); // nesta parte do código ele pega o array de bytes e converte em uma imagem de verdade.
                } else {
                    return new DefaultStreamedContent(new ByteArrayInputStream(this.selection.getTutor().get(0).getImagemUsuario()));
                }

            }
        } else {
            return new DefaultStreamedContent();
        }
    }

    public String inserir() {

        Usuario tutor = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(true)).getAttribute("loginController")).getUsuarioLogado();

        List<Usuario> tutores = new ArrayList();

        tutores.add(tutor);
        this.petCadastro.setTutor(tutores);

        petCadastro.setMesAnoDeNascimento(mes + "/" + ano);
        ManagerDao.getCurrentInstance().insert(this.petCadastro);

        this.petCadastro = new Pet();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso", "O pet foi cadastrado"));

        List<Pet> pets = null;
        pets = ManagerDao.getCurrentInstance().readAll("select p from Pet p where p.nome is null", Pet.class);

        if (pets.size() > 0) {
            for (Pet item : pets) {
                ManagerDao.getCurrentInstance().delete(item);
            }
        }
        return "meusPets";
    }

    public List<Pet> readPets() {

        List<Pet> pets;

        pets = ManagerDao.getCurrentInstance()
                .readAll("select p from Pet p", Pet.class);

        return pets;

    }

    public List<Pet> readPetsByTutor(LoginController loginController) {

        List<Pet> pets = new ArrayList();

        List<Pet_Usuario> pet_tutor = null;

        pet_tutor = ManagerDao.getCurrentInstance().readPetsByTutor("select c from Pet_Usuario c where c.tutor_codigo=" + loginController.getUsuarioLogado().getCodigo(), Pet_Usuario.class);

        for (Pet_Usuario item : pet_tutor) {

            List<Pet> pet = ManagerDao.getCurrentInstance().readAll("select p from Pet p where p.codigo=" + item.getPet_codigo(), Pet.class);

            pets.add(pet.get(0));
            
            //this.selection = pet.get(0);

        }
        return pets;

    }

    public String alterar() {

        this.selection.setMesAnoDeNascimento(mes + "/" + ano);
        ManagerDao.getCurrentInstance().update(this.selection);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso!", "pet " + this.op + " com Sucesso"));
        this.op = "alterado";

        return "meusPets";
    }

    public void editarPerfil() {

        this.selection.setMesAnoDeNascimento(mes + "/" + ano);
        ManagerDao.getCurrentInstance().update(this.selection);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso!", "pet " + this.op + " com Sucesso"));
        this.op = "alterado";

    }

    public void deletar() {

        if (!this.selection.isCompartilhado()) {

            List<Usuario> usuarios = ManagerDao.getCurrentInstance().readAll("select u from Usuario u", Usuario.class);

            List<String> ids = new ArrayList();

            for (Usuario usu : usuarios) {
                ids = usu.getIdsDePetsRecebidos();

                for (int i = 0; i < ids.size(); i++) {
                    if (ids.get(i).equals(Integer.toString(selection.getCodigo()))) {
                        ids.remove(i);
                        usu.setIdsDePetsRecebidos(ids);
                        ManagerDao.getCurrentInstance().update(usu);
                    }

                }
            }
            ManagerDao.getCurrentInstance().delete(this.selection);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Sucesso!", "pet deletado!"));
        } else {
            List<Usuario> tutor = new ArrayList();
            tutor = this.selection.getTutor();
            for (int i = 0; i < this.selection.getTutor().size(); i++) {
                if (tutor.get(i).getCodigo() == ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                        .getSession(true)).getAttribute("loginController")).getUsuarioLogado().getCodigo()) {

                    tutor.remove(i);

                    this.selection.setTutor(tutor);
                    updateVar();
                    this.selection.setCompartilhado(false);
                    this.op = "deletado";
                    alterar();

                    break;
                }
            }
        }
    }

    public void updateVar() {
        String mes = this.selection.getMesAnoDeNascimento().substring(0, 3);
        String ano = this.selection.getMesAnoDeNascimento().substring(4);
        this.mes = mes;
        this.ano = ano;
    }

    public List<Usuario> buscarUsuario() {
        List<Usuario> usuarios = null;
        usuarios = ManagerDao.getCurrentInstance().readAll("select u from Usuario u where u.usuario='" + usuario + "'", Usuario.class);

        if (!usuario.equals(((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(true)).getAttribute("loginController")).getUsuarioLogado().getUsuario())) {
            return usuarios;
        } else {
            return null;
        }
    }

    //gambiarra
    /*public String getTagImagem() {
        return imagemPet;
    }

    public void setTagImagem(String imagemPet) {
        this.imagemPet = imagemPet;
    }*/
}
