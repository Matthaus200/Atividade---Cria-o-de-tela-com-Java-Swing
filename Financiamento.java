import java.math.BigDecimal;
import java.math.RoundingMode;


public class Financiamento {

    public static final BigDecimal TAXA = new BigDecimal("0.32");

    public static final String[] MARCAS = {"Chevrolet", "Fiat", "Ford", "Honda", "Hyundai",
            "Jeep", "Nissan", "Renault", "Toyota", "Volkswagen"};
    public static final Integer[] PARCELAS = {12, 24, 36, 48, 60};
    public static final int ANO_MINIMO = 2000;
    public static final int ANO_MAXIMO = 2026;

    public enum Campo { MARCA, MODELO, ANO, VALOR, QUILOMETRAGEM, PROPRIETARIOS, ENTRADA }

    private String marca;
    private String modelo;
    private int ano;
    private BigDecimal valorVeiculo;
    private boolean usado;
    private long quilometragem;
    private long proprietarios;
    private boolean possuiEntrada;
    private BigDecimal entrada = BigDecimal.ZERO;
    private int parcelas;

    public void setMarca(String marca) { this.marca = marca; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setAno(int ano) { this.ano = ano; }
    public void setValorVeiculo(BigDecimal valorVeiculo) { this.valorVeiculo = valorVeiculo; }
    public void setUsado(boolean usado) { this.usado = usado; }
    public void setQuilometragem(long quilometragem) { this.quilometragem = quilometragem; }
    public void setProprietarios(long proprietarios) { this.proprietarios = proprietarios; }
    public void setPossuiEntrada(boolean possuiEntrada) { this.possuiEntrada = possuiEntrada; }
    public void setEntrada(BigDecimal entrada) { this.entrada = entrada; }
    public void setParcelas(int parcelas) { this.parcelas = parcelas; }

    public void validar() throws ValidacaoException {
        if (marca == null) {
            throw new ValidacaoException("Escolha a marca do carro.", Campo.MARCA);
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new ValidacaoException("Faltou informar o modelo do carro.", Campo.MODELO);
        }
        if (ano < ANO_MINIMO || ano > ANO_MAXIMO) {
            throw new ValidacaoException("O ano precisa estar entre " + ANO_MINIMO + " e " + ANO_MAXIMO + ".", Campo.ANO);
        }
        if (valorVeiculo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("O valor do carro precisa ser maior que zero.", Campo.VALOR);
        }

        if (usado) {
            if (quilometragem < 0) {
                throw new ValidacaoException("A quilometragem não pode ser negativa.", Campo.QUILOMETRAGEM);
            }
            if (proprietarios < 1) {
                throw new ValidacaoException("Carro usado precisa ter pelo menos 1 proprietário.", Campo.PROPRIETARIOS);
            }
        }

        if (possuiEntrada) {
            if (entrada.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidacaoException("Informe o valor da entrada ou desmarque a opção \"Possui entrada?\".", Campo.ENTRADA);
            }
            if (entrada.compareTo(valorVeiculo) >= 0) {
                throw new ValidacaoException("A entrada não pode ser maior ou igual ao valor do carro.", Campo.ENTRADA);
            }
        }
    }

    public Resultado calcular() throws ValidacaoException {
        validar();

        BigDecimal valorEntrada = possuiEntrada ? entrada : BigDecimal.ZERO;
        BigDecimal qtdParcelas = new BigDecimal(parcelas);

        BigDecimal valorFinanciado = valorVeiculo.subtract(valorEntrada);
        BigDecimal valorTotal = valorFinanciado.multiply(BigDecimal.ONE.add(TAXA));
        BigDecimal valorParcela = valorTotal.divide(qtdParcelas, 2, RoundingMode.HALF_EVEN);
        BigDecimal totalPagar = valorParcela.multiply(qtdParcelas);

        return new Resultado(valorFinanciado, valorParcela, totalPagar);
    }

    public static class Resultado {
        private BigDecimal valorFinanciado;
        private BigDecimal valorParcela;
        private BigDecimal totalPagar;

        public Resultado(BigDecimal valorFinanciado, BigDecimal valorParcela, BigDecimal totalPagar) {
            this.valorFinanciado = valorFinanciado;
            this.valorParcela = valorParcela;
            this.totalPagar = totalPagar;
        }

        public BigDecimal getValorFinanciado() { return valorFinanciado; }
        public BigDecimal getValorParcela() { return valorParcela; }
        public BigDecimal getTotalPagar() { return totalPagar; }
    }

    public static class ValidacaoException extends Exception {
        private static final long serialVersionUID = 1L;
        private Campo campo;

        public ValidacaoException(String mensagem, Campo campo) {
            super(mensagem);
            this.campo = campo;
        }

        public Campo getCampo() { return campo; }
    }
}