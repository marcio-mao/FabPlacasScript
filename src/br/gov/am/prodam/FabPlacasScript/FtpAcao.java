 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.am.prodam.FabPlacasScript;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

/**
 *
 * @author P001067
 */
public class FtpAcao extends FTPClient {


    public FtpAcao(String server, String user, String pass) throws IOException {
        this.connect(server);
        this.login(user, pass);
    }

    public List<String> getFilesMenosLido(String local, String remoto) throws IOException {
        
        List<String> getFilesBaixados = new ArrayList();
        
        if (remoto.length() > 0) {
            this.changeWorkingDirectory("/" + remoto);
        }
        FTPFileFilter filter = new FTPFileFilter() {

            @Override
            public boolean accept(FTPFile ftpFile) {

                return (ftpFile.isFile() && ftpFile.getName().contains("fplacas")
                        && !ftpFile.getName().startsWith("Lido_")
                        );
            }
        };

        this.setFileType(ASCII_FILE_TYPE);
        
        FTPFile[] files = this.listFiles(".", filter);
        for (FTPFile file : files) {
            OutputStream os = new FileOutputStream(local + file.getName());
            this.retrieveFile(file.getName(), os);
            getFilesBaixados.add(file.getName());
            this.rename(file.getName(), "Lido_" + file.getName());
        }
        return getFilesBaixados;
    }

}
