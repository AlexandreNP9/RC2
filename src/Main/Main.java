/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;

/**
 *
 * @author alexa
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        Cliente app = new Cliente();
        Criptografia cr = new Criptografia();
        app.Client();
        app.conectar();
        app.escutar();
        
    }

}
