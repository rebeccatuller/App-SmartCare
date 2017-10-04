package br.com.mobile.smartcare.modelo;

/**
 * Created by TI01N-2 on 17/11/2015.
 */
public class Paciente {

    private int id;
    private String nome;
    private int idade;
    private String telefone;
    private int leito;
    private String doenca;
    private String situacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getLeito() {
        return leito;
    }

    public void setLeito(int leito) {
        this.leito = leito;
    }

    public String getDoenca() {
        return doenca;
    }

    public void setDoenca(String doenca) {
        this.doenca = doenca;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Paciente(int id, String nome, int idade, String telefone, int leito, String doenca, String situacao) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.leito = leito;
        this.doenca = doenca;
        this.situacao = situacao;
    }


    public Paciente() {

    }

    public Paciente(String nome, int idade, String telefone, int leito, String doenca, String situacao) {
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.leito = leito;
        this.doenca = doenca;
        this.situacao = situacao;
    }
}


