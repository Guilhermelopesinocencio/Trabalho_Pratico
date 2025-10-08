package org.sysimc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sysimc.model.Pessoa;

import java.text.DecimalFormat;

public class MainController {

    @FXML
    public TextField txtNome;

    @FXML
    public TextField txtAltura;

    @FXML
    public TextField txtPeso;

    @FXML
    public Label lblIMC;

    @FXML
    public Label lblImc;

    @FXML
    public Label lblClassificacao;

    Pessoa pessoa = new Pessoa();

    @FXML
    protected void onCalcularIMCClick() {
        DecimalFormat df = new DecimalFormat();
        this.pessoa.setNome(txtNome.getText() );
        this.pessoa.setAltura(Float.parseFloat(txtAltura.getText()) );
        this.pessoa.setPeso(Float.parseFloat(txtPeso.getText() ));

        df.applyPattern("#0.00");
        this.lblIMC.setText(df.format( this.pessoa.calcularIMC()) );
        this.lblClassificacao.setText( this.pessoa.classificacaoIMC() );
    }
    @FXML
    protected void onSalvarClick() {
        // Aqui você decide onde salvar (por exemplo, arquivo ou lista)
        System.out.println("Salvando pessoa...");
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("Altura: " + pessoa.getAltura());
        System.out.println("Peso: " + pessoa.getPeso());
        System.out.println("IMC: " + pessoa.getImc());
    }

    @FXML
    protected void onCarregarClick() {
        // Simula dados carregados de algum lugar (ex: banco, arquivo, API, etc.)
        String nomeExemplo = "Guilherme Lopes";
        double alturaExemplo = 170;
        double pesoExemplo = 78;

        // Preenche os campos da tela
        txtNome.setText(nomeExemplo);
        txtAltura.setText(String.valueOf(alturaExemplo));
        txtPeso.setText(String.valueOf(pesoExemplo));

        // Atualiza os cálculos automaticamente
        double imc = pesoExemplo / Math.pow(alturaExemplo / 100, 2);
        lblImc.setText(String.format("%.2f", imc));

        // Classificação automática
        String classificacao;
        if (imc < 18.5) classificacao = "Abaixo do peso";
        else if (imc < 24.9) classificacao = "Peso normal";
        else if (imc < 29.9) classificacao = "Sobrepeso";
        else if (imc < 34.9) classificacao = "Obesidade Grau 1";
        else if (imc < 39.9) classificacao = "Obesidade Grau 2";
        else classificacao = "Obesidade Grau 3";

        lblClassificacao.setText(classificacao);

        System.out.println("Dados carregados: " + nomeExemplo);
    }


}