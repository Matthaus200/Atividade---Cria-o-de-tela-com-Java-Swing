import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

// Tela da simulação.
public class TelaFinanciamento extends JFrame {

    private NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    private JComboBox<String> cbMarca;
    private JTextField txtModelo;
    private JComboBox<Integer> cbAno;
    private JTextField txtValor;
    private JRadioButton rbNovo;
    private JRadioButton rbUsado;

    private JPanel painelUsado;
    private JTextField txtQuilometragem;
    private JTextField txtProprietarios;

    private JCheckBox chkEntrada;
    private JLabel lblEntrada;
    private JTextField txtEntrada;
    private JComboBox<Integer> cbParcelas;

    private JButton btnCalcular;
    private JButton btnLimpar;

    private JPanel painelResultado;
    private JLabel lblValorFinanciado;
    private JLabel lblValorParcela;
    private JLabel lblTotalPagar;

    public TelaFinanciamento() {
        setTitle("Financiamento de Carros");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Financiamento de Carros", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        painelUsado = montarPainelUsado();
        painelResultado = montarPainelResultado();

        principal.add(montarPainelVeiculo());
        principal.add(Box.createVerticalStrut(8));
        principal.add(painelUsado);
        principal.add(Box.createVerticalStrut(8));
        principal.add(montarPainelFinanciamento());
        principal.add(Box.createVerticalStrut(10));
        principal.add(montarPainelBotoes());
        principal.add(Box.createVerticalStrut(10));
        principal.add(painelResultado);

        JPanel aux = new JPanel(new BorderLayout());
        aux.add(principal, BorderLayout.NORTH);
        add(new JScrollPane(aux), BorderLayout.CENTER);

        rbNovo.addActionListener(e -> atualizarTela());
        rbUsado.addActionListener(e -> atualizarTela());
        chkEntrada.addActionListener(e -> atualizarTela());

        limpar();
        setSize(480, 740);
        setLocationRelativeTo(null);
    }

    private JPanel montarPainelVeiculo() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Veículo"));

        cbMarca = new JComboBox<>();
        cbMarca.addItem("Selecione...");
        for (String marca : Financiamento.MARCAS) {
            cbMarca.addItem(marca);
        }

        cbAno = new JComboBox<>();
        for (int ano = Financiamento.ANO_MAXIMO; ano >= Financiamento.ANO_MINIMO; ano--) {
            cbAno.addItem(ano);
        }

        txtModelo = new JTextField(20);
        txtValor = new JTextField(20);

        rbNovo = new JRadioButton("Novo");
        rbUsado = new JRadioButton("Usado");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbNovo);
        grupo.add(rbUsado);

        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.add(rbNovo);
        painelTipo.add(rbUsado);

        adicionarCampo(painel, 0, new JLabel("Marca:"), cbMarca);
        adicionarCampo(painel, 1, new JLabel("Modelo:"), txtModelo);
        adicionarCampo(painel, 2, new JLabel("Ano:"), cbAno);
        adicionarCampo(painel, 3, new JLabel("Valor:"), txtValor);
        adicionarCampo(painel, 4, new JLabel("Tipo:"), painelTipo);
        return painel;
    }

    private JPanel montarPainelUsado() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createDashedBorder(Color.GRAY), "Dados do Veículo Usado"));

        txtQuilometragem = new JTextField(20);
        txtProprietarios = new JTextField(20);

        adicionarCampo(painel, 0, new JLabel("Quilometragem:"), txtQuilometragem);
        adicionarCampo(painel, 1, new JLabel("Proprietários:"), txtProprietarios);
        return painel;
    }

    private JPanel montarPainelFinanciamento() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Financiamento"));

        chkEntrada = new JCheckBox("Possui entrada?");
        lblEntrada = new JLabel("Entrada:");
        txtEntrada = new JTextField(20);
        cbParcelas = new JComboBox<>(Financiamento.PARCELAS);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 4, 4);
        painel.add(chkEntrada, gbc);

        adicionarCampo(painel, 1, lblEntrada, txtEntrada);
        adicionarCampo(painel, 2, new JLabel("Parcelas:"), cbParcelas);
        return painel;
    }

    private JPanel montarPainelBotoes() {
        JPanel painel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnCalcular = new JButton("Calcular");
        btnLimpar = new JButton("Limpar");
        painel.add(btnCalcular);
        painel.add(btnLimpar);
        return painel;
    }

    private JPanel montarPainelResultado() {
        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 6));
        painel.setBorder(BorderFactory.createTitledBorder("Resultado"));

        lblValorFinanciado = new JLabel();
        lblValorParcela = new JLabel();
        lblTotalPagar = new JLabel();

        painel.add(new JLabel("Valor financiado:"));
        painel.add(lblValorFinanciado);
        painel.add(new JLabel("Valor da parcela:"));
        painel.add(lblValorParcela);
        painel.add(new JLabel("Total a pagar:"));
        painel.add(lblTotalPagar);
        return painel;
    }

    private void adicionarCampo(JPanel painel, int linha, JComponent label, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = linha;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        painel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gbc);
    }

    private void atualizarTela() {
        painelUsado.setVisible(rbUsado.isSelected());
        lblEntrada.setVisible(chkEntrada.isSelected());
        txtEntrada.setVisible(chkEntrada.isSelected());
        revalidate();
        repaint();
    }


    public void aoClicarCalcular(ActionListener acao) {
        btnCalcular.addActionListener(acao);
    }

    public void aoClicarLimpar(ActionListener acao) {
        btnLimpar.addActionListener(acao);
    }

    public String getMarca() {
        if (cbMarca.getSelectedIndex() == 0) {
            return null; 
        }
        return (String) cbMarca.getSelectedItem();
    }

    public String getModelo() { return txtModelo.getText(); }
    public int getAno() { return (Integer) cbAno.getSelectedItem(); }
    public String getValor() { return txtValor.getText(); }
    public boolean isUsado() { return rbUsado.isSelected(); }
    public String getQuilometragem() { return txtQuilometragem.getText(); }
    public String getProprietarios() { return txtProprietarios.getText(); }
    public boolean isPossuiEntrada() { return chkEntrada.isSelected(); }
    public String getEntrada() { return txtEntrada.getText(); }
    public int getParcelas() { return (Integer) cbParcelas.getSelectedItem(); }

    public void mostrarResultado(Financiamento.Resultado resultado) {
        lblValorFinanciado.setText(formatoMoeda.format(resultado.getValorFinanciado()));
        lblValorParcela.setText(formatoMoeda.format(resultado.getValorParcela()));
        lblTotalPagar.setText(formatoMoeda.format(resultado.getTotalPagar()));
        painelResultado.setVisible(true);
        revalidate();
        repaint();
    }

    public void esconderResultado() {
        painelResultado.setVisible(false);
        revalidate();
        repaint();
    }

    public void mostrarErro(String mensagem, Financiamento.Campo campo) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);

        switch (campo) {
            case MARCA: cbMarca.requestFocus(); break;
            case MODELO: txtModelo.requestFocus(); break;
            case ANO: cbAno.requestFocus(); break;
            case VALOR: txtValor.requestFocus(); break;
            case QUILOMETRAGEM: txtQuilometragem.requestFocus(); break;
            case PROPRIETARIOS: txtProprietarios.requestFocus(); break;
            case ENTRADA: txtEntrada.requestFocus(); break;
        }
    }

    public void limpar() {
        cbMarca.setSelectedIndex(0);
        txtModelo.setText("");
        cbAno.setSelectedIndex(0);
        txtValor.setText("");
        rbNovo.setSelected(true);
        txtQuilometragem.setText("");
        txtProprietarios.setText("");
        chkEntrada.setSelected(false);
        txtEntrada.setText("");
        cbParcelas.setSelectedItem(36);
        painelResultado.setVisible(false);
        atualizarTela();
    }
}
