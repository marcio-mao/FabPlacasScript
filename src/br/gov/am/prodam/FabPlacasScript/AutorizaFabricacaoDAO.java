/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.am.prodam.FabPlacasScript;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 *
 * @author P001067
 */
public class AutorizaFabricacaoDAO {
    
    private String strJdbc;
    private String strUser;
    private String strPwd;

    public AutorizaFabricacaoDAO() {
    }

    public AutorizaFabricacaoDAO(String strJdbc, String strUser, String strPwd) {
        this.strJdbc = strJdbc;
        this.strUser = strUser;
        this.strPwd = strPwd;
    }
    
    public Connection getConnectionSql(String strJdbc, String strUser, String strPwd) throws SQLException {
        this.strJdbc = strJdbc;
        this.strUser = strUser;
        this.strPwd  = strPwd;
        return  getConnectionSql();
    }
    
     public Connection getConnectionSql() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }         
        return DriverManager.getConnection(this.strJdbc, this.strUser, this.strPwd);
    }

    /*
     public static Connection getConnectionSql() throws SQLException, NamingException {
     Connection conexao=null;
     try {
     Context context = new InitialContext();
     Context lautx = (Context) context.lookup("java:");
     DataSource ds = (DataSource) lautx.lookup("jdbc/Datasource");
     conexao = ds.getConnection();

     } catch (SQLException sqle) {
     System.err.print("ClassNotFoundException: ");
     System.err.println(sqle.getMessage());
     } finally {

     }
     return (conexao);    
     }

     */
    public Boolean encontraProtocolo(String protocolo, Connection con) throws SQLException {

        String strsql = "select protocolo FROM TAB_AUTORIZA_FABRICACAO where protocolo = ?"
                + " AND dt_hr_cancelamento IS NULL";

        PreparedStatement psp = con.prepareStatement(strsql);
        psp.setString(1, protocolo);
        ResultSet rs = psp.executeQuery();
        return rs.next(); // &&(rs.getString('protocolo')==null)
    }

    public void AtualizaCancelado(String protocolo, Connection con) throws SQLException {

        String strsql = "UPDATE TAB_AUTORIZA_FABRICACAO SET dt_hr_cancelamento = ? "
                + " where protocolo = ?"
                + " AND dt_hr_cancelamento IS NULL";

        PreparedStatement psp = con.prepareStatement(strsql);
        psp.setTimestamp(1, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        psp.setString(2, protocolo);
        psp.execute();
        psp.clearParameters();
        psp.close();
    }

    public void insere(AutorizaFabricacao af, Connection con) throws SQLException {
        String strsql = "insert into TAB_AUTORIZA_FABRICACAO (protocolo, placa, cod_marca_modelo, desc_marca_modelo"
                + ", cod_categoria, tipo_placa_oficial, cod_municipio, tipo_moto, tipo_colecao"
                + ", local_origem_solicitacao, qt_placas_dianteira, qt_placas_traseira"
                + ", qt_tarjetas_dianteira, qt_tarjetas_traseira, qt_lacres, cod_despachante"
                + ", nome_despachante, nome_proprietario, dt_hr_solicitacao, dt_hr_autorizacao"
                + ", cod_correcao_detran) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement psp = con.prepareStatement(strsql);
        psp.setString(1, af.getProtocolo());
        psp.setString(2, af.getPlaca());
        if (af.getCodMarca() > 0) {
            psp.setInt(3, af.getCodMarca());
        } else {
            psp.setNull(3, java.sql.Types.INTEGER);
        }
        psp.setString(4, af.getDescMarca());
        if (af.getCodCateg() > 0) {
            psp.setInt(5, af.getCodCateg());
        } else {
            psp.setNull(5, java.sql.Types.INTEGER);
        }
        psp.setInt(6, af.getTipoOficial());
        if (af.getCodMunic() > 0) {
            psp.setInt(7, af.getCodMunic());
        } else {
            psp.setNull(7, java.sql.Types.INTEGER);
        }
//        if (af.getTipoMoto() >  0) {
        psp.setInt(8, af.getTipoMoto());
//        } else {
//            psp.setNull(8, java.sql.Types.INTEGER);
//        }
//        if (af.getTipoColecao() > 0) {
        psp.setInt(9, af.getTipoColecao());
//        } else {
//            psp.setNull(9, java.sql.Types.INTEGER);
//        }    
        psp.setString(10, af.getLocalOrigem());
        psp.setInt(11, af.getQtPlacasDianteira());
        psp.setInt(12, af.getQtPlacasTraseira());
        psp.setInt(13, af.getQtTarjetasDianteira());
        psp.setInt(14, af.getQtTarjetasTraseira());
        psp.setInt(15, af.getQtLacres());
//        if (af.getCodDespachante() > 0) {
        psp.setInt(16, af.getCodDespachante());
//        } else {
//            psp.setNull(16, java.sql.Types.INTEGER);
//        }
        psp.setString(17, af.getNomeDespachante());
        psp.setString(18, af.getNomeProprietario());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            psp.setTimestamp(19, new java.sql.Timestamp(sdf.parse(af.getDtHrProtocolo()).getTime()));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        psp.setTimestamp(20, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        if (af.getCodCorrecao() > 0) {
            psp.setInt(21, af.getCodCorrecao());
        } else {
            psp.setNull(21, java.sql.Types.INTEGER);
        }

        psp.executeUpdate();
        psp.clearParameters();
        psp.close();

    }

    public List<AutorizaFabricacao> getPlacasFab(Connection con) throws SQLException {

        List<AutorizaFabricacao> getPlacas = new ArrayList();
        String strsql = "select * FROM TAB_AUTORIZA_FABRICACAO where dt_hr_fabricacao IS NOT NULL"
                + " AND dt_hr_cancelamento IS NULL"
                + " AND dt_hr_envio_mainframe IS NULL"
                + " ORDER BY protocolo";

        PreparedStatement psp = con.prepareStatement(strsql);
        ResultSet rs = psp.executeQuery();
        while (rs.next()) {
            AutorizaFabricacao placaFab = new AutorizaFabricacao();
            placaFab.setProtocolo(rs.getString("protocolo"));
            placaFab.setPlaca(rs.getString("placa"));
            placaFab.setCodMarca(rs.getInt("cod_marca_modelo"));
            placaFab.setDescMarca(rs.getString("desc_marca_modelo"));
            placaFab.setCgcFabrica(rs.getString("cgc_fabrica") == null ? "" : rs.getString("cgc_fabrica"));
            placaFab.setSerialPlacaDianteira(rs.getString("serial_placa_dianteira") == null ? "" : rs.getString("serial_placa_dianteira"));
            placaFab.setSerialPlacaTraseira(rs.getString("serial_placa_traseira") == null ? "" : rs.getString("serial_placa_traseira"));
            placaFab.setSerialTarjetaDianteira(rs.getString("serial_tarjeta_dianteira") == null ? "" : rs.getString("serial_tarjeta_dianteira"));
            placaFab.setSerialTarjetaTraseira(rs.getString("serial_tarjeta_traseira") == null ? "" : rs.getString("serial_tarjeta_traseira"));
            placaFab.setDtHrFabricacao(rs.getTimestamp("dt_hr_fabricacao"));
            placaFab.setDtHrAtualizacaoFabricacao(rs.getTimestamp("dt_hr_atualizacao_fabricacao"));
            placaFab.setIndicaCorrecaoFab(Integer.parseInt(rs.getString("cod_correcao_fabrica") == null ? "0" : "0" + rs.getString("cod_correcao_fabrica")));
            getPlacas.add(placaFab);
        }
        return getPlacas;
    }

    public void setDataHoraEnvio(Connection con, List<String> protocolos) throws SQLException {
        for (String protocolo : protocolos) {
            setDataHoraEnvio(con, protocolo);
        }
    }

    public void setDataHoraEnvio(Connection con, String protocolo) throws SQLException {
        String strsql = "update TAB_AUTORIZA_FABRICACAO "
                + " SET dt_hr_envio_mainframe = ?"
                + " WHERE protocolo = ?"
                + " AND dt_hr_cancelamento IS NULL";
        PreparedStatement psp = con.prepareStatement(strsql);
        psp.setTimestamp(1, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        psp.setString(2, protocolo);
        psp.execute();
        psp.clearParameters();
        psp.close();
    }
}
