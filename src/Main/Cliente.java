/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author alexa
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javafx.scene.layout.Background;
import javax.swing.*;

public class Cliente extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtIP;
    private JTextField txtPorta;
    private JTextField txtNome;
    private JPasswordField senha;
    private String Senha;

    Criptografia Crip = new Criptografia();

    public void Client() throws IOException {
        JLabel lblMessage = new JLabel("Verificar!");
        JLabel lblsenha = new JLabel("Senha Criptografia (maior que 1 Caracter)");
        txtIP = new JTextField("127.0.0.1");
        txtPorta = new JTextField("12345");
        txtNome = new JTextField("Cliente");
        senha = new JPasswordField();
        JPasswordField jPasswordField = new JPasswordField();
        Object[] texts = {lblMessage, txtIP, txtPorta, txtNome, lblsenha, senha};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        texto = new JTextArea(10, 20);
        texto.setEditable(false);
        texto.setBackground(new Color(255, 255, 255, 255));
        txtMsg = new JTextField(20);
        lblHistorico = new JLabel("Histórico");
        lblMsg = new JLabel("Mensagem");
        btnSend = new JButton("Enviar");
        btnSend.setToolTipText("Enviar Mensagem");
        btnSair = new JButton("Sair");
        btnSair.setToolTipText("Sair do Chat");
        btnSend.addActionListener(this);
        btnSair.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnSair);
        pnlContent.add(btnSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        texto.setBorder(BorderFactory.createEtchedBorder(Color.darkGray, Color.darkGray));
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.darkGray, Color.darkGray));
        setTitle(txtNome.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250, 300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Senha = new String(senha.getPassword());

    }

    public void conectar() throws IOException {

        socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtNome.getText() + "\r\n");
        bfw.flush();
    }

    public void enviarMensagem(String msg) throws IOException {
        if (Senha.equals("")) {
            if (msg.equals("Sair")) {
                bfw.write("Desconectado \r\n");
                texto.append("Desconectado \r\n");
            } else {
                bfw.write(msg + "\r\n");
                texto.append("VOCÊ - " + " diz -> " + txtMsg.getText() + "\r\n");
            }
        } else {
            if (msg.equals("Sair")) {
                bfw.write("Desconectado \r\n");
                texto.append("Desconectado \r\n");
            } else {
                bfw.write(Crip.Criptografa(msg, 0, Senha) + "\r\n");
                texto.append("VOCÊ - " + " diz -> " + txtMsg.getText() + "\r\n");
            }
        }

        bfw.flush();
        txtMsg.setText("");
    }

    public void escutar() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Sair".equalsIgnoreCase(msg)) {
            if (Senha.equals("")) {
                if (bfr.ready()) {
                    msg = bfr.readLine();
                    if (msg.equals("Sair")) {
                        texto.append("Servidor caiu! \r\n");
                    } else {
                        texto.append(msg + "\r\n");
                    }
                }
            } else {
                if (bfr.ready()) {
                    msg = Crip.Criptografa(bfr.readLine(), 3000, Senha);
                    String[] aux = msg.split("//narci//");
                    msg = aux[0];
                    if (msg.equals("Sair")) {
                        texto.append("Servidor caiu! \r\n");
                    } else {
                        texto.append(msg + "\r\n");
                    }
                }
            }

        }
    }

    public void sair() throws IOException {

        enviarMensagem("Sair");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }

    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand().equals(btnSend.getActionCommand())) {
                enviarMensagem(txtNome.getText() + " -> " + txtMsg.getText());
            } else if (e.getActionCommand().equals(btnSair.getActionCommand())) {
                sair();
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                enviarMensagem(txtNome.getText() + " -> " + txtMsg.getText());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub               
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub               
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }

}
