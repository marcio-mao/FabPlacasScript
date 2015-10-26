package br.gov.am.prodam.FabPlacasScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author P001067
 */
public class Post2MF {
    
    private String hostMF = "10.20.1.132";
    private Integer portMF = 3016;
    private String sistMF = "CVMTW";
    private String userMF = "CVMTWEB";
    private String passMF = "OTOSABE"; 
    private String strJdbc;
    private String userDB;
    private String pwdDB;
    

    public Post2MF() {
    }
    
    public Post2MF(String hostMF, Integer portMF, String sistMF  ) {
        this.hostMF = hostMF;
        this.portMF = portMF;
        this.sistMF = sistMF;
    }    

    public void setHostMF(String hostMF) {
        this.hostMF = hostMF;
    }

    public void setPortMF(Integer portMF) {
        this.portMF = portMF;
    }

    public void setSistMF(String sistMF) {
        this.sistMF = sistMF;
    }

    public void setUserMF(String userMF) {
        this.userMF = userMF;
    }

    public void setPassMF(String passMF) {
        this.passMF = passMF;
    }

    public void setStrJdbc(String strJdbc) {
        this.strJdbc = strJdbc;
    }

    public void setUserDB(String userDB) {
        this.userDB = userDB;
    }

    public void setPwdDB(String pwdDB) {
        this.pwdDB = pwdDB;
    }
    

    private String callMainframe(String pin) throws IOException {
     //   this.mf = new Natural(this.hostMF, this.portMF);
        String pout = "";        
        System.out.println("Host:" + this.hostMF + "  Porta:" +  this.portMF + "  Ambiente:" + this.sistMF);
        System.out.println("Enviando.:" + pin);
        
        //pout = WDSocketMF.execute(this.userMF, this.passMF, this.sistMF, "CVMTWFP3", pin);

        try {
            pout = WDSocketMF.execute(this.hostMF,this.portMF, this.userMF, this.passMF, this.sistMF, "CVMTWFP3", pin,false);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return pout;

    }

    public void run() throws SQLException {        
        AutorizaFabricacaoDAO afd = new AutorizaFabricacaoDAO();
        Connection con = afd.getConnectionSql(this.strJdbc, this.userDB, this.pwdDB);        
        String retornos = "";     
        Calendar data = Calendar.getInstance();
        String arquivo="placas-" + data.get(Calendar.YEAR)+"-"+data.get(Calendar.MONTH)+"-"+ data.get(Calendar.DAY_OF_MONTH)+"-"+data.get(Calendar.HOUR_OF_DAY)+data.get(Calendar.MINUTE)+data.get(Calendar.SECOND)+".txt";
        File file = new File(arquivo);               
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Post2MF.class.getName()).log(Level.SEVERE, null, ex);        
        }
        
        try {
            System.setOut(new PrintStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Post2MF.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("erro arquivo nao encontrado");
        }
        System.out.println("Upload..." );
        System.out.println("Iniciando..: " + new java.util.Date(Calendar.getInstance().getTimeInMillis() ) );
        AutorizaFabricacao fplaca = null;
        List<AutorizaFabricacao> placaFabricadas = afd.getPlacasFab(con);
        int ind = placaFabricadas.size();   
        System.out.println("Quantidade de Placas para Enviar ao mainframe:" + ind);        
        String parin = "";
        String parout = "";
        for (int i=0;i<ind;i++) {                        
            fplaca = placaFabricadas.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            parin  = right("000000000000" + fplaca.getProtocolo(), 12)
                   + (fplaca.getPlaca() + "       ").substring(0, 7)
                   + right("00000000000000" + fplaca.getCgcFabrica(), 14)
                   + (fplaca.getSerialPlacaDianteira()   + "            ").substring(0, 12)
                   + (fplaca.getSerialTarjetaDianteira() + "            ").substring(0, 12)
                   + (fplaca.getSerialPlacaTraseira()    + "            ").substring(0, 12)
                   + (fplaca.getSerialTarjetaTraseira()  + "            ").substring(0, 12)
                   + sdf.format(fplaca.getDtHrFabricacao())
                   + right("0" + fplaca.getIndicaCorrecaoFab(), 1)
                   + sdf.format(fplaca.getDtHrAtualizacaoFabricacao());
            System.out.println("Protocolo: " + fplaca.getProtocolo() + " sendo enviado ao Mainframe..." + i);                
                
            try {
                System.out.println("Chamando WDSocketMF.:" + parin);
                parout = callMainframe(parin);
                System.out.println("Retorno WDSocketMF.:" + parout);                        
                if (parout.matches("/7027TIMEOUT|7005WRITE|7041PERFIL/i") || parout.trim().length()==0) { // Confirma Postgres
                    System.out.println(">>> Erro no processamento dos protocolos, RC:");
                    break;
                } else {            
                    String codigo    = parout.substring(0, 4);                
                    System.out.println("Codigo erro.:" + codigo);
                    if (codigo.equalsIgnoreCase("0000")) {    
                        afd.setDataHoraEnvio(con, fplaca.getProtocolo());
                        System.out.println("Protocolo: " + fplaca.getProtocolo() +  " Atualizado!");
                    } else {
                        String desc      = parout.substring(4, parout.length());                                                        
                        System.out.println("Protocolo: " + fplaca.getProtocolo() + "Erro->   Cod:" + codigo + " Desc:" + desc);
                    }                        
                }
            } catch (UnknownHostException ex) {
                System.out.println("Erro UnknownHostException");
            } catch (IOException ex) {
                System.out.println("Erro IOException");
            } catch (Exception ex) {
                System.out.println("Erro Exception");
            } finally{
               System.out.println("**********************************************************************************");  
            }
            
        } // end-while
        con.close();
        System.out.println("Finalizando: " + new java.util.Date(Calendar.getInstance().getTimeInMillis() ) );        
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }
}
