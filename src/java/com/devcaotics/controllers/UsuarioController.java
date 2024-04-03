/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devcaotics.controllers;

import com.devcaotics.model.dao.ManagerDao;
import com.devcaotics.model.negocio.Compartilhamentos;
import com.devcaotics.model.negocio.Pet;
import com.devcaotics.model.negocio.Pet_Usuario;
import com.devcaotics.model.negocio.Usuario;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ALUNO
 */
@ManagedBean
@SessionScoped
public class UsuarioController {

    private Usuario usuarioCadastro;
    private Usuario selection = new Usuario();
    private Usuario usuarioLogado = new Usuario();
    private String modalType;
    private String nome_usuario = "";
    private String nome_pet = "";
    private List<Compartilhamentos> compartilhamentos = new ArrayList();

    private List<Pet> pets = new ArrayList();

    //isto é para a gambiarra da imagem
    private String tagImagem;
    private int tabIndex = 0;

    @PostConstruct
    public void init() {
        this.usuarioCadastro = new Usuario();
        this.modalType = "create";
    }

    private Usuario getUlogado() {
        usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute("loginController")).getUsuarioLogado();

        return this.usuarioLogado;
    }

    public void inserir(String confirma) {

        List<Usuario> u = new ArrayList();
        u = ManagerDao.getCurrentInstance().readAll("select u from Usuario u where u.usuario='" + usuarioCadastro.getUsuario() + "'", Usuario.class);

        if (u.isEmpty()) {

            if (!this.usuarioCadastro.getSenha().equals("") && !confirma.equals("")) {
                if (!this.usuarioCadastro.getSenha().equals(confirma)) {

                    FacesContext.getCurrentInstance().addMessage("formCadUsuario:pswSenha",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro Severo", "A senha e a confirmação não batem!"));

                    return;
                }

                ManagerDao.getCurrentInstance().insert(this.usuarioCadastro);

                this.usuarioCadastro = new Usuario();

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Usuario cadastrado com sucesso!"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "A senha não pode ficar em branco!", "Crie uma senha válida."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Nome de usuario já exite!", "tente outro username"));

        }

    }

    public List<Usuario> readUsuarios() {

        List<Usuario> usuarios = null;

        usuarios = ManagerDao.getCurrentInstance()
                .readAll("select u from Usuario u", Usuario.class);

        return usuarios;

    }

    public Usuario getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Usuario usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public Usuario getSelection() {
        return selection;
    }

    public void setSelection(Usuario selection) {
        this.selection = selection;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getNome_pet() {
        return nome_pet;
    }

    public void setNome_pet(String nome_pet) {
        this.nome_pet = nome_pet;
    }

    public List<Compartilhamentos> getCompartilhamentos() {
        return compartilhamentos;
    }

    public void setCompartilhamentos(List<Compartilhamentos> compartilhamentos) {
        this.compartilhamentos = compartilhamentos;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public String alterar() {

        ManagerDao.getCurrentInstance().update(this.selection);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso!", "usuario Cadastrado com Sucesso"));

        return "usuarios";
    }

    public void editarPerfil() {

        ManagerDao.getCurrentInstance().update(this.selection);

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Sucesso!", "usuario alterado com Sucesso"));

    }

    public void deletar() {

        ManagerDao.getCurrentInstance().delete(this.selection);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso!", "usuario deletado"));

    }

    public void alterarSenha(String senha, String novaSenha, String confirma) {

        //código para recuperar qualquer atributo na sessão
        Usuario uLogado = getUlogado();

        if (!uLogado.getSenha().equals(senha)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A senha digitada está incorreta. "
                            + "Por favor, tente novamente"));
            return;
        }

        if (!novaSenha.equals(confirma)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A nova senha não bate com a confirmação. "
                            + "Por favor, tente novamente"));
            return;
        }

        uLogado.setSenha(novaSenha);

        ManagerDao.getCurrentInstance().update(uLogado);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Senha alterada com sucesso!"));
    }

    public List<Usuario> buscarUsuario() {
        List<Usuario> usuarios = null;
        usuarios = ManagerDao.getCurrentInstance().readAll("select u from Usuario u where u.usuario='" + this.selection.getUsuario() + "'", Usuario.class);
        return usuarios;
    }

    public void compartilharPet(Pet p) {

        int contador = 0;
        if (p != null && this.selection != null) {

            for (String idsDePetsRecebido : this.selection.getIdsDePetsRecebidos()) {
                if (idsDePetsRecebido.contains(Integer.toString(p.getCodigo()))) {
                    contador++;
                }
            }

            if (contador == 0) {
                this.selection.addIdDePetRecebido(p.getCodigo());
                ManagerDao.getCurrentInstance().update(this.selection);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Você ofereceu o compartilhamento de " + p.getNome() + " com " + this.selection.getUsuario(), ""));

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "O compartilhamento do pet já foi solicitado para esse usuario", "caso ele rejeite você poderá fazer uma nova solicitação"));

            }
        }
    }

    public void buscarCompartilhados(Usuario u) {

        Pet p;
        List<Pet> pet = new ArrayList();
        List<Usuario> usuario = new ArrayList();
        Usuario usu = new Usuario();

        this.compartilhamentos.clear();

        if (u != null) {
            for (int i = 0; i < u.getIdsDePetsRecebidos().size(); i++) {

                p = (Pet) ManagerDao.getCurrentInstance().read("select p from Pet p where p.codigo='" + u.getIdsDePetsRecebidos().get(i) + "'", Pet.class);
                pet.add(p);
                pets.add(p);
            }
            List<Pet_Usuario> pu = new ArrayList();
            Pet_Usuario petUsuario;
            Compartilhamentos c = new Compartilhamentos();

            if (pet.size() > 0) {
                for (int i = 0; i < pet.size(); i++) {

                    petUsuario = (Pet_Usuario) ManagerDao.getCurrentInstance().read("select pu from Pet_Usuario pu where pu.pet_codigo='" + pet.get(i).getCodigo()
                            + "'" + " and pu.tutor_codigo='" + pet.get(i).getTutor().get(0).getCodigo() + "'", Pet_Usuario.class);

                    pu.add(petUsuario);

                    if (pu.size() > i) {
                        usu = (Usuario) ManagerDao.getCurrentInstance().read("select u from Usuario u where u.codigo='" + pu.get(i).getTutor_codigo() + "'", Object.class);
                    }
                    if (usu != null) {
                        usuario.add(usu);
                        pet.get(i).setTutor(usuario);
                        this.nome_usuario = pet.get(i).getTutor().get(i).getUsuario();
                        c.setNomeUsuario(nome_usuario);
                        c.setCodigoUsuario(pet.get(i).getTutor().get(i).getCodigo());
                    }
                    this.nome_pet = pet.get(i).getNome();
                    c.setNomePet(nome_pet);
                    c.setCodigoPet(pet.get(i).getCodigo());

                    this.compartilhamentos.add(c);
                    c = new Compartilhamentos();
                }
            }
        }

    }

    public void aceitarPet(String codigoPet) {
        Pet p;
        p = (Pet) ManagerDao.getCurrentInstance().read("select p from Pet p where p.codigo='" + codigoPet + "'", Object.class);
        List<Usuario> tutores = new ArrayList();
        tutores.add(p.getTutor().get(0));
        tutores.add(getUlogado());
        p.setTutor(tutores);
        p.setCompartilhado(true);
        inserirPet(p);

    }

    public void inserirPet(Pet p) {

        ManagerDao.getCurrentInstance().update(p);

        List<Usuario> usuarios = ManagerDao.getCurrentInstance().readAll("select u from Usuario u", Usuario.class);

        List<String> ids;

        for (Usuario usu : usuarios) {
            ids = usu.getIdsDePetsRecebidos();

            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).equals(Integer.toString(p.getCodigo()))) {
                    ids.remove(i);
                    usu.setIdsDePetsRecebidos(ids);
                    ManagerDao.getCurrentInstance().update(usu);

                    if (usu.getCodigo() == this.getUlogado().getCodigo()) {
                        getUlogado().setIdsDePetsRecebidos(ids);
                    }
                }

            }
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso", "O pet foi aceito!"));

        buscarCompartilhados(getUlogado());
    }

    public List<Compartilhamentos> nomes() {

        return this.compartilhamentos;
    }
    
    public void rejeitarPet(String codigo){
        
       Usuario usuario = (Usuario)ManagerDao.getCurrentInstance().read("select u from Usuario u where u.codigo='"+getUlogado().getCodigo()+"'", Usuario.class);

        List<String> ids;

            ids = usuario.getIdsDePetsRecebidos();

            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).equals(codigo)) {
                    ids.remove(i);
                    usuario.setIdsDePetsRecebidos(ids);
                    ManagerDao.getCurrentInstance().update(usuario);
                }

            }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso", "você rejeitou o pet!"));

        ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute("loginController")).setUsuarioLogado(usuario);
        buscarCompartilhados(getUlogado());
        
    }
 
  public StreamedContent getImagemAtual() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            return new DefaultStreamedContent(new ByteArrayInputStream(((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                        .getSession(true)).getAttribute("loginController")).getUsuarioLogado().getImagemUsuario())); // nesta parte do código ele pega o array de bytes e converte em uma imagem de verdade.
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        //FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        //FacesContext.getCurrentInstance().addMessage(null, msg);

        byte[] im = new byte[(int) event.getFile().getSize()];

        event.getFile().getInputstream().read(im);

        this.usuarioCadastro.setImagemUsuario(im);
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage("Imagem Apiloidada"));
        //gambiarra
        ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true)).setAttribute("imagem",
                 this.usuarioCadastro.getImagemUsuario());

        this.tagImagem = "http://localhost:8080/TikDoguinho/ServletExibirImagemUsuarioGambiarra";
    }

    //gambiarra
    public String getTagImagem() {
        return tagImagem;
    }

    public void setTagImagem(String tagImagem) {
        this.tagImagem = tagImagem;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    //gambiarra para controlar a abertura do modal, porque Ana Paulo
    //exigiu... porque a graça da gambiarra é ser auto explicativa
    public void modalType(String type) {
        this.modalType = type;
    }

    public String getModalType() {
        return modalType;
    }

}
