package org.sysimc.model;

public class Pessoa {
    private int id;
    private String nome;
    private float altura;
    private float peso;
    private double imc;
    private String classificacao;

    public Pessoa() {
    }

    public Pessoa(int id, String nome, float altura, float peso, double imc) {
        this.id = id;
        this.nome = nome;
        this.altura = altura;
        this.peso = peso;
        this.imc = imc;
    }

    public Pessoa(int id, String nome, float altura, float peso) {
        this.id = id;
        this.nome = nome;
        this.altura = altura;
        this.peso = peso;
        this.imc = calcularIMC(); // calcula o IMC automaticamente
        this.classificacao = classificacaoIMC();
    }
    //--------------------------------------------------------

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public double getImc() {return imc;}

    public void setImc(float imc) {
        this.imc = imc;
    }

    public String getClassificacao() {return classificacao;}

    public void setClassificacao(String classificacao) {this.classificacao = classificacao;}
    //--------------------------------------------------------
    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", altura=" + altura +
                ", peso=" + peso +
                '}';
    }
    //--------------------------------------------------------

    public double calcularIMC() {
        float alturaMetros = this.altura / 100; // conversão cm → m
        this.imc = this.peso / (alturaMetros * alturaMetros);

        // 🔹 Arredonda para 2 casas decimais
        this.imc = Math.round(this.imc * 100.0) / 100.0;

        this.classificacao = classificacaoIMC();
        return this.imc;
    }

    //--------------------------------------------------------

    public String classificacaoIMC(){
        String classificacao;
        if (this.imc < 18.5)
            return "Abaixo do peso";
        else if (this.imc >=18.5 && this.imc < 24.9)
            return "Peso normal";
        else if (this.imc >=25 && this.imc < 29.9)
            return "Sobrepeso";
        else if (this.imc >=30 && this.imc < 34.9)
            return "Obesidade Grau 1";
        else if (this.imc >=35 && this.imc < 39.9)
            return "Obesidade Grau 2";
        else
            return "Obesidade Grau 3";
    }
}
