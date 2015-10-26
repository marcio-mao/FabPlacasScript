/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.am.prodam.FabPlacasScript;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author P001067
 */
public class ReadCSV {

    private String csvFile;
    private String strJdbc;
    private String user;
    private String pwd;

    public void setStrJdbc(String strJdbc) {
        this.strJdbc = strJdbc;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    

    public ReadCSV(String csvFile) {
        this.csvFile = csvFile;
    }

    public void run() throws SQLException {

//	String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
        BufferedReader br = null;
        Connection con = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

//		Map<String, String> maps = new HashMap<String, String>();
            br = new BufferedReader(new FileReader(this.csvFile));

            AutorizaFabricacaoDAO afd = new AutorizaFabricacaoDAO(this.strJdbc, this.user, this.pwd);
            con = afd.getConnectionSql();


            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] campo = line.split(cvsSplitBy);
                if (campo.length != 20) {
                    System.out.println("Registro invalido:" + line);
                    continue;
                }

                AutorizaFabricacao af = new AutorizaFabricacao();
                String protocolo = campo[0];
                Integer codCorrecao = Integer.parseInt(campo[19]);

                af.setProtocolo(protocolo);
                af.setPlaca(                               campo[1]);
                af.setCodMarca(Integer.parseInt(           campo[2]));
                af.setDescMarca(                           campo[3]);
                af.setCodCateg(Integer.parseInt(           campo[4]));
                af.setTipoOficial(Integer.parseInt(        campo[5]));
                af.setCodMunic(Integer.parseInt(           campo[6]));
                af.setTipoMoto(Integer.parseInt(           campo[7]));
                af.setTipoColecao(Integer.parseInt(        campo[8]));
                af.setLocalOrigem(                         campo[9]);
                af.setQtPlacasDianteira(Integer.parseInt(  campo[10]));
                af.setQtPlacasTraseira(Integer.parseInt(   campo[11]));
                af.setQtTarjetasDianteira(Integer.parseInt(campo[12]));
                af.setQtTarjetasTraseira(Integer.parseInt( campo[13]));
                af.setQtLacres(Integer.parseInt(           campo[14]));
                af.setCodDespachante(Integer.parseInt(     campo[15]));
                af.setNomeDespachante(                     campo[16]);
                af.setNomeProprietario(                    campo[17]);
                af.setDtHrProtocolo(                       campo[18]);
                af.setCodCorrecao(codCorrecao);
//
                if (afd.encontraProtocolo(protocolo, con)) {
                    if (codCorrecao == 1) {
                        afd.AtualizaCancelado(protocolo, con);
                        System.out.println("Protocolo "+protocolo+" cancelado!");
                        afd.insere(af, con);
                        System.out.println("Protocolo "+protocolo+" corrigido!");                          
                    } else {
                        System.out.println("Protocolo "+protocolo+" ignorado!");
                        continue;
                    }
                } else {
                    afd.insere(af, con);
                    System.out.println("Protocolo "+protocolo+" inserido!");                    
                }
            } // end-while

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.close();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // end-try

//        return 1;
    }

}
