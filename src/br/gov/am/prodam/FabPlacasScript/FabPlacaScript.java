/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.am.prodam.FabPlacasScript;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author P001067
 */
public class FabPlacaScript {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException {
        FtpAcao ftpa = null;
        String local = "";
        String serverFTP = "10.20.1.154";
        String userFTP = "fplacas";
        String passFTP = "detranwsfb";
        String remoto = "Fabricas_Placas";

        //  String serverMF = "10.20.1.132"; // Ambiente de Desenvolvimento
        //  Integer portMF = 3002;
        //  String sistMF = "CVMTW";     
        String serverMF = "10.20.1.132";
        Integer portMF = 3016;           // Ambiente de Produção 
        String sistMF = "CVMTI";

        //  String jdbcDB = "jdbc:postgresql://10.20.1.217:5432/webservices_detran_fplacas"; // Ambiente de Homologação
        //  String userDB = "sa_detran_fplacas";
        //  String passDB = "ps451cv";        
        String jdbcDB = "jdbc:postgresql://10.20.1.103:5432/webservices_detran_fplacas"; // Ambiente de Produção
        String userDB = "sa_detran_fplacas";
        String passDB = "HIgsfewjo456";

        List<String> Arquivos = new ArrayList();
        /**
         * Dependendendo do 1o. parametro ele busca no FTP: Isto é: java -jar
         * FabPlacaCarga.jar ftp pasta_temp_local servidor usuario senha
         * pasta_remota - busca arquivos no FTP e baixa em pasta_temp_local para
         * processar ou, Caso contrário, cada parametro contem o nome path+file
         * a ser procecessado. Ex: java -jar FabPlacaCarga.jar
         * /tmp/fplacas_20100101_0102033.txt /tmp/fplacas_20100101_0102033.txt
         */

        for (int i = 0; i < args.length; i++) {
            if (args[0].equalsIgnoreCase("UPLOAD")) {
  /*
                switch (i) {
                    case 2:
                        serverMF = args[1];
                        portMF = Integer.parseInt(args[2]);
                        break;
                    case 3:
                        sistMF = args[3];
                        break;
                    default:
                        break;

                }
*/          
            } else {
                if (args[0].equalsIgnoreCase("DOWNLOAD")) {
                    switch (i) {
                        case 3:
                            serverFTP = args[1];
                            userFTP = args[2];
                            passFTP = args[3];
                            break;
                        case 4:
                            local = args[4] + "/";
                        case 5:
                            remoto = args[5];
                            break;
                        default:
                            break;
                    }
                } else {
                    Arquivos.add(args[i]);
                }
            } // End-If
            if (args[i].equalsIgnoreCase("-J")) {
                jdbcDB = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-U")) {
                userDB = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-P")) {
                passDB = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-C")) {
                portMF = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equalsIgnoreCase("-S")) {
                sistMF = args[i + 1];
            }
        } // End-For
        
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("UPLOAD")) {
                Post2MF p2m = new Post2MF(serverMF, portMF, sistMF);
                p2m.setStrJdbc(jdbcDB);
                p2m.setUserDB(userDB);
                p2m.setPwdDB(passDB);
                p2m.run();
            } else {
                if (args[0].equalsIgnoreCase("DOWNLOAD")) {
                    ftpa = new FtpAcao(serverFTP, userFTP, passFTP);
                    Arquivos = ftpa.getFilesMenosLido(local, remoto);
                }
                for (String nometxt : Arquivos) {
                    System.out.println("Lendo arquivo " + nometxt + " ...");
                    ReadCSV obj = new ReadCSV(local + nometxt);
                    obj.setStrJdbc(jdbcDB);
                    obj.setUser(userDB);
                    obj.setPwd(passDB);
                    obj.run();
                }
            } // End-If  
        }

    }

}
