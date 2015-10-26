/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.am.prodam.FabPlacasScript;

import java.util.Date;

/**
 *
 * @author P001067
 */
public class AutorizaFabricacao {
    private String protocolo;
    private String placa;
    private Integer codMarca;
    private String descMarca;
    private Integer codCateg;
    private Integer tipoOficial;
    private Integer codMunic;
    private Integer tipoMoto;
    private Integer tipoColecao;
    private String localOrigem;
    private Integer qtPlacasDianteira;
    private Integer qtPlacasTraseira;
    private Integer qtTarjetasDianteira;
    private Integer qtTarjetasTraseira;
    private Integer qtLacres;
    private Integer codDespachante;
    private String nomeDespachante;
    private String nomeProprietario;
    private String dtHrProtocolo;
    private Integer codCorrecao;
    private String cgcFabrica;
    private String serialPlacaDianteira;
    private String serialPlacaTraseira;
    private String serialTarjetaDianteira;
    private String serialTarjetaTraseira;
    private Date dtHrFabricacao;
    private Date dtHrAtualizacaoFabricacao;
    private Integer indicaCorrecaoFab;
    
    
    public AutorizaFabricacao() {
    }

    public AutorizaFabricacao(String protocolo, Integer codCorrecao) {
        this.protocolo = protocolo;
        this.codCorrecao = codCorrecao;
    }
    
    

    public AutorizaFabricacao(String protocolo, String placa, Integer codMarca, String descMarca, Integer codCateg, Integer tipoOficial, Integer codMunic, Integer tipoMoto, Integer tipoColecao, String localOrigem, Integer qtPlacasDianteira, Integer qtPlacasTrazeira, Integer qtTarjetasDianteira, Integer qtTarjetasTrazeira, Integer codDespachante, String nomeProprietario, Integer codCorrecao) {
        this.protocolo = protocolo;
        this.placa = placa;
        this.codMarca = codMarca;
        this.descMarca = descMarca;
        this.codCateg = codCateg;
        this.tipoOficial = tipoOficial;
        this.codMunic = codMunic;
        this.tipoMoto = tipoMoto;
        this.tipoColecao = tipoColecao;
        this.localOrigem = localOrigem;
        this.qtPlacasDianteira = qtPlacasDianteira;
        this.qtPlacasTraseira = qtPlacasTrazeira;
        this.qtTarjetasDianteira = qtTarjetasDianteira;
        this.qtTarjetasTraseira = qtTarjetasTrazeira;
        this.codDespachante = codDespachante;
        this.nomeProprietario = nomeProprietario;
        this.codCorrecao = codCorrecao;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setCodMarca(Integer codMarca) {
        this.codMarca = codMarca;
    }

    public void setDescMarca(String descMarca) {
        this.descMarca = descMarca;
    }

    public void setCodCateg(Integer codCateg) {
        this.codCateg = codCateg;
    }

    public void setTipoOficial(Integer tipoOficial) {
        this.tipoOficial = tipoOficial;
    }

    public void setCodMunic(Integer codMunic) {
        this.codMunic = codMunic;
    }

    public void setTipoMoto(Integer tipoMoto) {
        this.tipoMoto = tipoMoto;
    }

    public void setTipoColecao(Integer tipoColecao) {
        this.tipoColecao = tipoColecao;
    }

    public void setLocalOrigem(String localOrigem) {
        this.localOrigem = localOrigem;
    }

    public void setQtPlacasDianteira(Integer qtPlacasDianteira) {
        this.qtPlacasDianteira = qtPlacasDianteira;
    }

    public void setQtPlacasTraseira(Integer qtPlacasTraseira) {
        this.qtPlacasTraseira = qtPlacasTraseira;
    }

    public void setQtTarjetasDianteira(Integer qtTarjetasDianteira) {
        this.qtTarjetasDianteira = qtTarjetasDianteira;
    }

    public void setQtTarjetasTraseira(Integer qtTarjetasTraseira) {
        this.qtTarjetasTraseira = qtTarjetasTraseira;
    }

    public void setQtLacres(Integer qtLacres) {
        this.qtLacres = qtLacres;
    }

    public void setCodDespachante(Integer codDespachante) {
        this.codDespachante = codDespachante;
    }

    public void setNomeDespachante(String nomeDespachante) {
        this.nomeDespachante = nomeDespachante;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public void setDtHrProtocolo(String dtHrProtocolo) {
        this.dtHrProtocolo = dtHrProtocolo;
    }

    public void setCodCorrecao(Integer codCorrecao) {
        this.codCorrecao = codCorrecao;
    }

    public void setCgcFabrica(String cgcFabrica) {
        this.cgcFabrica = cgcFabrica;
    }

    public void setSerialPlacaDianteira(String serialPlacaDianteira) {
        this.serialPlacaDianteira = serialPlacaDianteira;
    }

    public void setSerialPlacaTraseira(String serialPlacaTraseira) {
        this.serialPlacaTraseira = serialPlacaTraseira;
    }

    public void setSerialTarjetaDianteira(String serialTarjetaDianteira) {
        this.serialTarjetaDianteira = serialTarjetaDianteira;
    }

    public void setSerialTarjetaTraseira(String serialTarjetaTraseira) {
        this.serialTarjetaTraseira = serialTarjetaTraseira;
    }

    public void setDtHrFabricacao(Date dtHrFabricacao) {
        this.dtHrFabricacao = dtHrFabricacao;
    }
    
    public void setDtHrAtualizacaoFabricacao(Date dtHrAtualizacaoFabricacao) {
        this.dtHrAtualizacaoFabricacao = dtHrAtualizacaoFabricacao;
    }

    
    public void setIndicaCorrecaoFab(Integer indicaCorrecaoFab) {
        this.indicaCorrecaoFab = indicaCorrecaoFab;
    }
    
    

    public String getProtocolo() {
        return protocolo;
    }

    public String getPlaca() {
        return placa;
    }

    public Integer getCodMarca() {
        return codMarca;
    }

    public String getDescMarca() {
        return descMarca;
    }

    public Integer getCodCateg() {
        return codCateg;
    }

    public Integer getTipoOficial() {
        return tipoOficial;
    }

    public Integer getCodMunic() {
        return codMunic;
    }

    public Integer getTipoMoto() {
        return tipoMoto;
    }

    public Integer getTipoColecao() {
        return tipoColecao;
    }

    public String getLocalOrigem() {
        return localOrigem;
    }

    public Integer getQtPlacasDianteira() {
        return qtPlacasDianteira;
    }

    public Integer getQtPlacasTraseira() {
        return qtPlacasTraseira;
    }

    public Integer getQtTarjetasDianteira() {
        return qtTarjetasDianteira;
    }

    public Integer getQtTarjetasTraseira() {
        return qtTarjetasTraseira;
    }

    public Integer getQtLacres() {
        return qtLacres;
    }

    public Integer getCodDespachante() {
        return codDespachante;
    }

    public String getNomeDespachante() {
        return nomeDespachante;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public String getDtHrProtocolo() {
        return dtHrProtocolo;
    }

    public Integer getCodCorrecao() {
        return codCorrecao;
    }

    public String getCgcFabrica() {
        return cgcFabrica;
    }

    public String getSerialPlacaDianteira() {
        return serialPlacaDianteira;
    }

    public String getSerialPlacaTraseira() {
        return serialPlacaTraseira;
    }

    public String getSerialTarjetaDianteira() {
        return serialTarjetaDianteira;
    }

    public String getSerialTarjetaTraseira() {
        return serialTarjetaTraseira;
    }

    public Date getDtHrFabricacao() {
        return dtHrFabricacao;
    }
    
    public Date getDtHrAtualizacaoFabricacao() {
        return dtHrAtualizacaoFabricacao;
    }
    
    public Integer getIndicaCorrecaoFab() {
        return indicaCorrecaoFab;
    }
    
    
    
    
    
}
