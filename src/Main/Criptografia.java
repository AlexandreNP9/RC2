package Main;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Criptografia {

    String Criptografar(String stringOriginal, String senha) {

        System.out.println("----------CRIPTOGRAFANDO----------");
        System.out.println("Mensagem a ser enviada: " + stringOriginal);
        System.out.println("Senha: " + senha);

        //gera iv entre 10 e 99
        Random gerador = new Random();
        int ivInt = gerador.nextInt(99);
        String iv;
        if (ivInt < 10) {
            iv = "0" + String.valueOf(ivInt);
        } else {
            iv = String.valueOf(ivInt);
        }
        System.out.println("IV: " + iv);

        //deixa o iv do tamanho da mensagem a ser enviada
        String ivGrande = "";
        boolean p = false;
        int valor;
        for (int i = 0; i < stringOriginal.length(); i++) {
            if (p) {
                valor = 1;
            } else {
                valor = 0;
            }
            ivGrande += iv.charAt(valor);
            p = !p;
        }
        System.out.println("ivGrande: " + ivGrande);

         //deixa a senha do tamanho da mensagem a ser enviada
        String senhaGrande = "";
        
        int tamanhoMensagem = stringOriginal.length();
        for (int i = 0; i < tamanhoMensagem; i++) {
            senhaGrande += senha;
        }
        senhaGrande = senhaGrande.substring(0, tamanhoMensagem);
        System.out.println("Senha grande: " + senhaGrande);
        
        //define o parâmetro de criptografia (XOR entre senhaGrande e ivGrande)
        String pc = "";
        for (int i = 0; i < ivGrande.length(); i++) {
            pc += ivGrande.charAt(i) + senhaGrande.charAt(i);
        }
        System.out.println("Parâmetro de criptografia: " + pc);

        //criptografa a mensagem e transforma em ASCII (XOR entre mensagem a ser enviada e parâmetro de criptografia)
        int cript;
        String resultado = "";
        for (int i = 0; i < stringOriginal.length(); i++) {
            cript = (int) stringOriginal.charAt(i) + (int) pc.charAt(i);
            resultado += (char) cript;
        }
        resultado += iv;
        System.out.println("Mensagem criptografada mais iv: " + resultado);

        return resultado;
    }

    String Descriptografar(String mensagemCript, String senha) {
        System.out.println("----------DESCRIPTOGRAFANDO----------");
        System.out.println("Mensagem criptografada: " + mensagemCript);
        System.out.println("Senha: " + senha);

        //obter o iv
        String ivRec = mensagemCript.substring(mensagemCript.length() - 2, mensagemCript.length());
        System.out.println("ivRec " + ivRec);

        //obter a stringRecebida
        String stringRecebida = mensagemCript.substring(0, mensagemCript.length() - 2);

        //deixa a senha do tamanho da mensagem a ser enviada
        String senhaGrande = "";

        int tamanhoMensagem = stringRecebida.length();
        for (int i = 0; i < tamanhoMensagem; i++) {
            senhaGrande += senha;
        }
        senhaGrande = senhaGrande.substring(0, tamanhoMensagem);
        System.out.println("Senha grande: " + senhaGrande);

        //deixa o iv do tamanho da mensagem a ser enviada
        String ivGrande = "";
        boolean p = false;
        int valor;
        for (int i = 0; i < stringRecebida.length(); i++) {
            if (p) {
                valor = 1;
            } else {
                valor = 0;
            }
            ivGrande += ivRec.charAt(valor);
            p = !p;
        }
        System.out.println("ivGrande: " + ivGrande);

        //define o parâmetro de criptografia (XOR entre senhaGrande e ivGrande)
        String pc = "";
        for (int i = 0; i < ivGrande.length(); i++) {
            pc += ivGrande.charAt(i) + senhaGrande.charAt(i);
        }
        System.out.println("Parâmetro de criptografia: " + pc);

        //criptografa a mensagem e transforma em ASCII (XOR entre mensagem a ser enviada e parâmetro de criptografia)
        int cript;
        String resultado = "";
        for (int i = 0; i < stringRecebida.length(); i++) {
            cript = (int) stringRecebida.charAt(i) - (int) pc.charAt(i);
            resultado += (char) cript;
        }
        //resultado = resultado - cript;
        System.out.println("Mensagem descriptografada: " + resultado);

        return resultado;
    }

    public static void main(String[] args) {
        Criptografia criptografia = new Criptografia();

        String s = "alexandre nassar";
        String senha = "asdf";
        String mensagemCriptografada = "¡©¤§Q£¤¨£53";

        String resultado = criptografia.Criptografar(s, senha);
        String resultado1 = criptografia.Descriptografar(mensagemCriptografada, senha);

    }

}
