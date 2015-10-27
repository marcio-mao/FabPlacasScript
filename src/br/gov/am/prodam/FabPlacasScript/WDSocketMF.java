package br.gov.am.prodam.FabPlacasScript;


/*
 * WDSocketMF.java
 *
 * Created on 26 de Setembro de 2007, 10:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe para chamada de programas Natural cadastrados no CEUS  
 * @author Crinnger F. Oliveira
 * @since 27/12/2006
 */
public final class WDSocketMF
{
	/**
	 * Base CICS que sera executado:. @value WADA
	 */			
	//private static final String DATABASE = "WADB,";
    //Para WADB comentar linha abaixo e descomentar a de cima.
        private static final String DATABASE = "WADA,";

	/**
	 * Tamanho do paramentor de retorno:. @value 32000
	 */
//	private static final int TAM_PARAM = 32000;
	/**
	 * Endereco IP do servidor MainFrame:. @value "10.20.1.132"
	 */	
	private static String hostMainFrame = "10.20.1.132";
	/**
	 * Porta utilizada para realizar a conexção com o MainFrame:. @value 3000
	 */
	 //private static final int portMainFrame = 3002;	
	//private static final int portMainFrame = 3016; PRODUCAO
        //private static final int portMainFrame = 3018; HOMOLOGACAO
        private static int portMainFrame = 3018;
        
        /**
	 * Numero de Tentativas para verificar se canal de input esta pronto para ser usado
         * Tempo em Milisegundos para Esperar cada tentativa          
         * Espera ate 2 segundos para confirmar o Input
         */
	  
	private static final int quantidadeTentativasInputReady = 20;
        private static final int tempoTentativasInputReady = 100;
        
        private static boolean debug=false;
	
	/**
	 * Metodo que executa uma conexeção com a servidor MainFrame
	 * @param usuario Nome do usuario CEUS que fara a conexo
	 * @param senha Senha do usuario
	 * @param sistema Sistema que sera chamado
	 * @param programa Programa que ser executado
	 * @param argumento Argumentos a serem usados pelo programa chamado
         * @param debug Escreva na console o debug
	 * @return String com o valor retornado pelo programa NATURAL
	 * @throws UnknownHostException
	 * @throws IOException 
	 * @see Socket 	
	 * @see OutputStreamWriter
	 * @see InputStreamReader 
	 * @see StringBuffer
	 */
        public static String execute(String usuario, String senha, String sistema, String programa, String argumento,boolean debug) throws UnknownHostException, IOException, InterruptedException
	{
            WDSocketMF.debug=debug;
            return WDSocketMF.execute(usuario, senha, sistema, programa, argumento);
        }
        public static String execute(String ipMf, Integer portMf, String usuario, String senha, String sistema, String programa, String argumento,boolean debug) throws UnknownHostException, IOException, InterruptedException
	{            
            WDSocketMF.debug=debug;
            WDSocketMF.hostMainFrame=ipMf;
            WDSocketMF.portMainFrame=portMf;
            return WDSocketMF.execute(usuario, senha, sistema, programa, argumento);
        }
	public static String execute(String usuario, String senha, String sistema, String programa, String argumento) throws UnknownHostException, IOException, InterruptedException
	{
//            char[] buffer = new char[TAM_PARAM];
            StringBuffer    retorno= new StringBuffer();
            Socket          client = null;
            int i=0;
            int cont=0;
	    OutputStreamWriter 	output = null;
	    InputStreamReader 	input  = null;	
            String espaco64 = " ";            
            long tempoProcessamento  = 0;
            long inicioProcessamento = Calendar.getInstance().getTimeInMillis();
            long finalProcessamento  = 0;
                        
            espaco64 = ajustarTamanho(espaco64,64);
            
            argumento = espaco64 + argumento;
            
            usuario = ajustarTamanho(usuario,8);            
            senha = ajustarTamanho(senha,8);            
            sistema = ajustarTamanho(sistema,5);
            programa = ajustarTamanho(programa,8);			    
            if(debug){ 
                System.out.println("Ip Mainframe: " + hostMainFrame +":"+ portMainFrame);
                System.out.println("Conectando Mainfram: "+DATABASE + usuario + senha + sistema + programa);
                System.out.println("Dados enviados:" + argumento);
                System.out.println("Enviando Mainframe..: " + inicioProcessamento+"(milisegundos)");
            }
            client = new Socket(hostMainFrame, portMainFrame);            
            output = new OutputStreamWriter(client.getOutputStream());                 
            output.flush(); // esvazia buffer de saída enviar as informações de cabeçalho            
            input = new InputStreamReader(client.getInputStream());		    
            output.write(DATABASE + usuario + senha + sistema + programa);
            output.flush();	
            

            if (!argumento.equals("")) {
            	output.write(argumento);		    
            } else {
            	return "";
            }			
            output.flush();
            
            int tentativas=0;
            while(!input.ready()){                
                tentativas++;
                if(debug) System.out.println("Tentativa Leitura Input:" +tentativas);
                if (tentativas>WDSocketMF.quantidadeTentativasInputReady){                    
                    break;
                }                
                Thread.sleep(WDSocketMF.tempoTentativasInputReady);               
            }
            
            while((i=input.read())!=-1){	                
                cont++;
                retorno.append((char)i);                
            }
            //Para WADB comentar linha abaixo
            //retorno.delete(0,64);
            
            input.close();
            output.close();
            client.close();            
            String strRetorno = retorno.toString();         
            finalProcessamento = Calendar.getInstance().getTimeInMillis();
            if(debug) System.out.println("Retornando Mainframe..: " + finalProcessamento +"(milisegundos)");
            tempoProcessamento = finalProcessamento - inicioProcessamento;
            if(debug) System.out.println("Tempo Processamento Mainframe..: " + tempoProcessamento +"(milisegundos)");
            if(debug) System.out.println("Retorno Mainframe..: " + strRetorno.trim());
            return  strRetorno.trim();
	}
	
	/**
	 * Ajusta o tamanho de um String para o tamanho informado
	 * @param ajuste String a ser ajustado o tamanho
	 * @param tamanho Tamanho que ter a nova string
	 * @return String com o novo tamanho
	 */
	
	public static String ajustarTamanho(String ajuste,int tamanho){
		int tamanhoAjuste=0;
		char espaco = ' ';
		StringBuffer stringRetorno = new StringBuffer(ajuste);
		tamanhoAjuste = tamanho - ajuste.length();
                for(int i=0;i<tamanhoAjuste;i++){
                    stringRetorno.append(espaco);
                }		
		return stringRetorno.toString();
	}	
        
        public static void main (String args[]){
            
            long inicioProcessamento = Calendar.getInstance().getTimeInMillis();
            long finalProcessamento  = 0;
            System.out.println("Inicio lote..: " + inicioProcessamento+"(milisegundos)");
            
        String teste []= new String[4];
        teste[0]= "<XML><ONOFF>1</ONOFF><CDORG>103100</CDORG><SERIE>TX</SERIE><NMAUT>230</NMAUT><IDAGT>78307457491</IDAGT><NMAGT>EVELYNE W. DE SIQUEIRA</NMAGT><MATAGT>0011339</MATAGT><TIPGT>AGENTE</TIPGT><PLACA>JWZ6339</PLACA><UFPLAC>AM</UFPLAC><CHASS></CHASS><LOCAL>RUA JONATHAS PEDROSA</LOCAL><MUNIC>255</MUNIC><DTINF>19/10/2015</DTINF><HRINF>1613</HRINF><APCNH>N</APCNH><APVEI>N</APVEI><APDOC>N</APDOC><INSAF>0</INSAF><NUBAF></NUBAF><MDREA></MDREA><MDLIM></MDLIM><MDCON></MDCON><UNID></UNID><NMINF>MARIA ALICE</NMINF><CPFIN>03683934298</CPFIN><RGINF>0</RGINF><UFCNH></UFCNH><ASSIN>N</ASSIN><CDPRO>S</CDPRO><DTINC>19/10/2015</DTINC><HRINC>161445</HRINC><CDMUL>6726</CDMUL><CDTIP>1</CDTIP><OBS></OBS><ADM></ADM></XML>";
        teste[1]= "<XML><ONOFF>1</ONOFF><CDORG>103100</CDORG><SERIE>TX</SERIE><NMAUT>273</NMAUT><IDAGT>78307457491</IDAGT><NMAGT>EVELYNE W. DE SIQUEIRA</NMAGT><MATAGT>0011339</MATAGT><TIPGT>AGENTE</TIPGT><PLACA>JWF0038</PLACA><UFPLAC>AM</UFPLAC><CHASS></CHASS><LOCAL>RUA JONATHAS PEDROSA</LOCAL><MUNIC>255</MUNIC><DTINF>26/10/2015</DTINF><HRINF>0929</HRINF><APCNH>N</APCNH><APVEI>N</APVEI><APDOC>N</APDOC><INSAF>0</INSAF><NUBAF></NUBAF><MDREA></MDREA><MDLIM></MDLIM><MDCON></MDCON><UNID></UNID><NMINF>TESTE SO COM NOME</NMINF><CPFIN></CPFIN><RGINF>0</RGINF><UFCNH></UFCNH><ASSIN>N</ASSIN><CDPRO>N</CDPRO><DTINC>26/10/2015</DTINC><HRINC>092942</HRINC><CDMUL>6653</CDMUL><CDTIP>1</CDTIP><OBS></OBS><ADM></ADM></XML>";
        teste[2]= "<XML><ONOFF>1</ONOFF><CDORG>103100</CDORG><SERIE>TX</SERIE><NMAUT>272</NMAUT><IDAGT>78307457491</IDAGT><NMAGT>EVELYNE W. DE SIQUEIRA</NMAGT><MATAGT>0011339</MATAGT><TIPGT>AGENTE</TIPGT><PLACA>JWF0038</PLACA><UFPLAC>AM</UFPLAC><CHASS></CHASS><LOCAL>RUA JONATHAS PEDROSA</LOCAL><MUNIC>255</MUNIC><DTINF>26/10/2015</DTINF><HRINF>0928</HRINF><APCNH>N</APCNH><APVEI>N</APVEI><APDOC>N</APDOC><INSAF>1</INSAF><NUBAF>123456</NUBAF><MDREA>170</MDREA><MDLIM>0</MDLIM><MDCON>156</MDCON><UNID>1</UNID><NMINF>null</NMINF><CPFIN></CPFIN><RGINF>0</RGINF><UFCNH></UFCNH><ASSIN>N</ASSIN><CDPRO>N</CDPRO><DTINC>26/10/2015</DTINC><HRINC>092913</HRINC><CDMUL>5169</CDMUL><CDTIP>1</CDTIP><OBS></OBS><ADM></ADM></XML>";
        teste[3]= "<XML><ONOFF>1</ONOFF><CDORG>103100</CDORG><SERIE>TX</SERIE><NMAUT>271</NMAUT><IDAGT>78307457491</IDAGT><NMAGT>EVELYNE W. DE SIQUEIRA</NMAGT><MATAGT>0011339</MATAGT><TIPGT>AGENTE</TIPGT><PLACA>JWZ6339</PLACA><UFPLAC>AM</UFPLAC><CHASS></CHASS><LOCAL>RUA JONATHAS PEDROSA</LOCAL><MUNIC>255</MUNIC><DTINF>26/10/2015</DTINF><HRINF>0926</HRINF><APCNH>N</APCNH><APVEI>N</APVEI><APDOC>N</APDOC><INSAF>0</INSAF><NUBAF></NUBAF><MDREA></MDREA><MDLIM></MDLIM><MDCON></MDCON><UNID></UNID><NMINF>JAQUELINE SO</NMINF><CPFIN>03683934298</CPFIN><RGINF>0</RGINF><UFCNH></UFCNH><ASSIN>N</ASSIN><CDPRO>S</CDPRO><DTINC>26/10/2015</DTINC><HRINC>092750</HRINC><CDMUL>6599</CDMUL><CDTIP>2</CDTIP><OBS></OBS><ADM></ADM></XML>";
        try {
            for(int ind=0; ind<teste.length; ind++)
            System.out.println("RETORNO CONEXAO " + ind +":" + WDSocketMF.execute("CVMTWEB", "OTOSABE", "CVMTH", "CVMTW121", teste[ind],true));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finalProcessamento= Calendar.getInstance().getTimeInMillis();
        long tempoProcessamento = finalProcessamento - inicioProcessamento;
        System.out.println("Final lote..: " + finalProcessamento+"(milisegundos)");
        System.out.println("Tempo Processamento Lote..: " + tempoProcessamento +"(milisegundos)");
            
        }
}