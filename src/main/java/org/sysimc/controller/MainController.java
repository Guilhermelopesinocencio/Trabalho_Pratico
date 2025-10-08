package org.sysimc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.sysimc.model.Pessoa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    public Label lblClassificacao;

    // -----------------------------------------------------------------------------
    //dados na tela do historico do arquivo.txt

    @FXML
    private TableView<Pessoa> tabelaPessoas;

    @FXML
    private TableColumn<Pessoa, Integer> colId;

    @FXML
    private TableColumn<Pessoa, String> colNome;

    @FXML
    private TableColumn<Pessoa, Double> colAltura;

    @FXML
    private TableColumn<Pessoa, Double> colPeso;

    @FXML
    private TableColumn<Pessoa, Double> colIMC;

    @FXML
    private TableColumn<Pessoa, Double> colClassificacao;


    private ObservableList<Pessoa> listaPessoas = FXCollections.observableArrayList();

    private int proximoId = 1; // controle interno

    public final String ARQUIVO = "dados.txt";
    // -----------------------------------------------------------------------------

    Pessoa pessoa = new Pessoa();

    // -----------------------------------------------------------------------------
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colIMC.setCellValueFactory(new PropertyValueFactory<>("imc"));
        colClassificacao.setCellValueFactory(new PropertyValueFactory<>("classificacao"));

        tabelaPessoas.setItems(listaPessoas);
        carregarHistoricoAutomatico();
    }


    private void carregarHistoricoAutomatico() {
        try {
            File arquivo = new File("Banco_De_Dados/dados_pessoas.txt");
            if (!arquivo.exists()) return;

            listaPessoas.clear();
            int maiorId = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] partes = linha.split(",");
                    if (partes.length >= 6) {
                        int id = Integer.parseInt(partes[0].trim());
                        String nome = partes[1].trim();
                        float altura = Float.parseFloat(partes[2].trim());
                        float peso = Float.parseFloat(partes[3].trim());
                        double imc = Double.parseDouble(partes[4].trim());
                        String classificacao = partes[5].trim();

                        Pessoa p = new Pessoa(id, nome, altura, peso, imc);
                        p.setClassificacao(classificacao);

                        listaPessoas.add(p);
                        if (id > maiorId) maiorId = id;
                    }
                }
            }
            proximoId = maiorId + 1; // define o próximo ID
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------------------------



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
        try {
            // Cria um novo objeto Pessoa e preenche com os dados da tela
            Pessoa novaPessoa = new Pessoa();
            novaPessoa.setId(proximoId++); // gera ID automático
            novaPessoa.setNome(txtNome.getText());
            novaPessoa.setAltura(Float.parseFloat(txtAltura.getText()));
            novaPessoa.setPeso(Float.parseFloat(txtPeso.getText()));
            novaPessoa.calcularIMC(); // garante que o IMC esteja atualizado

            // 🔹 Formata o IMC com ponto e 2 casas decimais
            java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols(java.util.Locale.US);
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.##", symbols);
            String imcFormatado = df.format(novaPessoa.getImc());


            // Cria a pasta "Banco_De_Dados" caso não exista
            java.io.File pasta = new java.io.File("Banco_De_Dados");
            if (!pasta.exists()) {
                pasta.mkdirs();
            }

            // Caminho completo do arquivo dentro da pasta
            java.io.File arquivo = new java.io.File(pasta, "dados_pessoas.txt");

            // Linha que será salva
            String linha = novaPessoa.getId() + "," +
                    novaPessoa.getNome() + "," +
                    novaPessoa.getAltura() + "," +
                    novaPessoa.getPeso() + "," +
                    imcFormatado + "," + // Usa o IMC formatado
                    novaPessoa.getClassificacao() + System.lineSeparator();

            // Escreve (anexando no final, sem apagar os anteriores)
            try (java.io.FileWriter fw = new java.io.FileWriter(arquivo, true)) {
                fw.write(linha);
            }

            // Adiciona na tabela
            listaPessoas.add(novaPessoa);

            // Limpa os campos após salvar
            txtNome.clear();
            txtAltura.clear();
            txtPeso.clear();
            lblIMC.setText("");
            lblClassificacao.setText("");

            txtNome.requestFocus();

            System.out.println(" Dados salvos com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Erro ao salvar os dados: " + e.getMessage());
        }
    }


    @FXML
    protected void onCarregarClick() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Selecione o arquivo de dados");
        fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Arquivos de texto", "*.txt")
        );

        java.io.File arquivo = fileChooser.showOpenDialog(null);

        if (arquivo != null) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(arquivo))) {
                String linha;

                listaPessoas.clear();
                int maiorId = 0;

                while ((linha = br.readLine()) != null) {
                    String[] partes = linha.split(",");

                    // Lê todas as colunas, mas só usa Nome, Altura e Peso nos campos da tela
                    if (partes.length >= 6) {
                        int id = Integer.parseInt(partes[0].trim());
                        String nome = partes[1].trim();
                        float altura = Float.parseFloat(partes[2].trim());
                        float peso = Float.parseFloat(partes[3].trim());
                        double imc = Double.parseDouble(partes[4].trim());
                        String classificacao = partes[5].trim();

                        Pessoa p = new Pessoa(id, nome, altura, peso, imc);
                        p.setClassificacao(classificacao);

                        listaPessoas.add(p);

                        if (id > maiorId) maiorId = id;
                    } else {
                        System.out.println("Formato incorreto na linha: " + linha);
                    }
                }

                proximoId = maiorId + 1;

                // Mostra SOMENTE os campos que o usuário pode editar
                if (!listaPessoas.isEmpty()) {
                    Pessoa ultima = listaPessoas.get(listaPessoas.size() - 1);
                    txtNome.setText(ultima.getNome());
                    txtAltura.setText(String.valueOf(ultima.getAltura()));
                    txtPeso.setText(String.valueOf(ultima.getPeso()));
                    lblIMC.setText("");  // limpa o IMC (não é campo editável)
                    lblClassificacao.setText(""); // limpa a classificação
                }

                System.out.println("✅ Dados carregados do arquivo: " + arquivo.getName());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("❌ Erro ao ler o arquivo: " + e.getMessage());
            }
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }






}