import javax.swing.SwingUtilities;
import java.math.BigDecimal;

public class FinanciamentoController {

    private TelaFinanciamento tela;

    public FinanciamentoController(TelaFinanciamento tela) {
        this.tela = tela;
        tela.aoClicarCalcular(e -> calcular());
        tela.aoClicarLimpar(e -> tela.limpar());
    }

    private void calcular() {
        try {
            Financiamento financiamento = new Financiamento();

            financiamento.setMarca(tela.getMarca());
            financiamento.setModelo(tela.getModelo());
            financiamento.setAno(tela.getAno());
            financiamento.setValorVeiculo(converterValor(tela.getValor(), "Valor", Financiamento.Campo.VALOR));

            financiamento.setUsado(tela.isUsado());
            if (tela.isUsado()) {
                financiamento.setQuilometragem(converterInteiro(tela.getQuilometragem(), "Quilometragem", Financiamento.Campo.QUILOMETRAGEM));
                financiamento.setProprietarios(converterInteiro(tela.getProprietarios(), "Proprietários", Financiamento.Campo.PROPRIETARIOS));
            }

            financiamento.setPossuiEntrada(tela.isPossuiEntrada());
            if (tela.isPossuiEntrada()) {
                financiamento.setEntrada(converterValor(tela.getEntrada(), "Entrada", Financiamento.Campo.ENTRADA));
            }

            financiamento.setParcelas(tela.getParcelas());

            tela.mostrarResultado(financiamento.calcular());

        } catch (Financiamento.ValidacaoException ex) {
            tela.esconderResultado();
            tela.mostrarErro(ex.getMessage(), ex.getCampo());
        }
    }

    private BigDecimal converterValor(String texto, String nomeCampo, Financiamento.Campo campo)
            throws Financiamento.ValidacaoException {
        texto = texto.replace("R$", "").trim();
        if (texto.isEmpty()) {
            throw new Financiamento.ValidacaoException("Preencha o campo " + nomeCampo + ".", campo);
        }

        texto = texto.replace(".", "").replace(",", ".");

        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            throw new Financiamento.ValidacaoException("O campo " + nomeCampo + " aceita apenas números.", campo);
        }
    }

    private long converterInteiro(String texto, String nomeCampo, Financiamento.Campo campo)
            throws Financiamento.ValidacaoException {
        texto = texto.replace(".", "").trim();
        if (texto.isEmpty()) {
            throw new Financiamento.ValidacaoException("Preencha o campo " + nomeCampo + ".", campo);
        }
        try {
            return Long.parseLong(texto);
        } catch (NumberFormatException e) {
            throw new Financiamento.ValidacaoException("O campo " + nomeCampo + " aceita apenas números inteiros.", campo);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaFinanciamento tela = new TelaFinanciamento();
            new FinanciamentoController(tela);
            tela.setVisible(true);
        });
    }
}
