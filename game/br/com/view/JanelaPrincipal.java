package br.com.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Toolkit;
import java.awt.Button;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.Border;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import Jogo.ReversiWindow;
import br.com.chat.Servidor;
import br.com.jogo.Jogador;
import br.com.jogo.JogoReversi;
import br.com.socket.Conexao;
import br.com.socket.Transferencia;

public class JanelaPrincipal implements IObserver{

	private static final String Amarelo = null;

	private JFrame frmReversi;
	private final Border bordaPlayer1 = BorderFactory.createLineBorder(
			new Color(45, 137, 239), 4, false);
	private final Border bordaPlayer2 = BorderFactory.createLineBorder(
			Color.red, 4, false);

	private Color preto = Color.black;

	private Color amarelo = new Color(255, 196, 13);

	private JComboBox<Cor> cores;
	private JTextField txtNome;
	private String recebida;
	private IObserver o = this;
	private StringBuffer str;

	/**
	 * Create the application.
	 */
	public JanelaPrincipal() {
		componentes();
		this.frmReversi.setVisible(true);
	}

	void singlePlayer(ActionEvent event) {
		Cor cor = (Cor) cores.getSelectedItem();

		Color cor1 = preto;
		Border borda1 = bordaPlayer1;
		Color cor2 = amarelo;
		Border borda2 = bordaPlayer2;
		String nome1 = "Preto";
		String nome2 = "Amarelo";

		if (cor == Cor.Amarelo) {
			cor1 = amarelo;
			cor2 = preto;
			borda1 = bordaPlayer2;
			borda2 = bordaPlayer1;
			nome1 = "Amarelo";
			nome2 = "Preto";
		}

		Jogador player1 = new Jogador(nome1, true, cor1, borda1);

		Jogador player2 = new Jogador(nome2, false, cor2, borda2);

		JogoReversi jogo = new JogoReversi(player1, player2);

		new JanelaReversi(jogo).setVisible(true);
	}

	void multiplayer(ActionEvent event) {
		Cor cor = (Cor) cores.getSelectedItem();

		Color cor1 = preto;
		Border borda1 = bordaPlayer1;
		Color cor2 = amarelo;
		Border borda2 = bordaPlayer2;
		String nome1 = "Preto";
		String nome2 = "Amarelo";

		if (cor == Cor.Amarelo) {
			cor1 = amarelo;
			cor2 = preto;
			borda1 = bordaPlayer2;
			borda2 = bordaPlayer1;
			nome1 = "Amarelo";
			nome2 = "Preto";
		}

		Jogador player1 = new Jogador(nome1, true, cor1, borda1);

		Jogador player2 = new Jogador(nome2, true, cor2, borda2);
		
		if(player1.getCor() == amarelo){
			player1.setValido(true);
		}
		else{
			player2.setValido(true);
		}

		JogoReversi jogo = new JogoReversi(player1, player2);

		new JanelaReversi(jogo).setVisible(true);
	}
	

	void assistir(ActionEvent event) {

		Cor cor = (Cor) cores.getSelectedItem();

		Color cor1 = preto;
		Border borda1 = bordaPlayer1;
		Color cor2 = amarelo;
		Border borda2 = bordaPlayer2;
		String nome1 = "Preto";
		String nome2 = "Amarelo";

		if (cor == Cor.Amarelo) {
			cor1 = amarelo;
			cor2 = preto;
			borda1 = bordaPlayer2;
			borda2 = bordaPlayer1;
			nome1 = "Amarelo";
			nome2 = "Preto";
		}

		Jogador player1 = new Jogador(nome1, false, cor1, borda1);

		Jogador player2 = new Jogador(nome2, false, cor2, borda2);

		JogoReversi jogo = new JogoReversi(player1, player2);

		new JanelaReversi(jogo).setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void componentes() {
		frmReversi = new JFrame();
		frmReversi.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"Icones/Reverse.png"));
		frmReversi.setTitle("Reversi");
		frmReversi.getContentPane().setBackground(Color.DARK_GRAY);
		frmReversi.setBounds(100, 100, 1160, 571);
		frmReversi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReversi.getContentPane().setLayout(null);

		TextArea chat = new TextArea();
		chat.setBounds(25, 24, 645, 363);
		frmReversi.getContentPane().add(chat);
		// fazer

		TextArea mensagem = new TextArea();
		mensagem.setBounds(25, 422, 645, 76);
		frmReversi.getContentPane().add(mensagem);

		// ---------------------------------------------------------

		JButton botaoEnviar = new JButton("ENVIAR");
		botaoEnviar.setHorizontalAlignment(SwingConstants.LEFT);
		botaoEnviar.setIcon(new ImageIcon("Icones/Seta.png"));
		botaoEnviar.setBackground(SystemColor.inactiveCaption);
		botaoEnviar.setBounds(697, 422, 175, 76);
		frmReversi.getContentPane().add(botaoEnviar);
		str = new StringBuffer();
		

		botaoEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				str.append("\n"+ mensagem.getText());
				if(getRecebida()!=null){
					str.append(getRecebida()+ "\n");
					setRecebida(null);
				}
				chat.setText(str.toString());
				try {
					/**
					 * Teste
					 */
					
					Transferencia t = new Transferencia();
					t.setMsg(mensagem.getText());
					
					Conexao.cliente.enviarObjeto(t);
					
					
					
					//Conexao.cliente.enviar(mensagem.getText());
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		// ---------------------------------------------------------
		JButton btnComputador = new JButton("COMPUTADOR");
		btnComputador.setIcon(new ImageIcon("Icones/Games.png"));
		btnComputador.setBackground(SystemColor.inactiveCaption);
		btnComputador.setForeground(Color.BLACK);
		btnComputador.setBounds(902, 24, 217, 76);
		frmReversi.getContentPane().add(btnComputador);
		btnComputador.addActionListener(this::singlePlayer);

		JButton btnMultiplayer = new JButton("MULTIPLAYER");
		btnMultiplayer.setIcon(new ImageIcon("Icones/Multiplayer.png"));
		btnMultiplayer.setBackground(SystemColor.inactiveCaption);
		btnMultiplayer.setForeground(Color.BLACK);
		btnMultiplayer.setBounds(902, 202, 217, 76);
		frmReversi.getContentPane().add(btnMultiplayer);
		btnMultiplayer.addActionListener(this::multiplayer);

		JButton btnAssistir = new JButton("ASSISTIR");
		btnAssistir.setIcon(new ImageIcon("Icones/Assistir.png"));
		btnAssistir.setBackground(SystemColor.inactiveCaption);
		btnAssistir.setBounds(902, 113, 217, 76);
		frmReversi.getContentPane().add(btnAssistir);
		btnAssistir.addActionListener(this::assistir);
		// ---------------------------------------------------------
		JPanel panelOnline = new JPanel();
		panelOnline.setBounds(697, 24, 175, 334);
		frmReversi.getContentPane().add(panelOnline);
		// ---------------------------------------------------------
		JList listOnlines = new JList();
		panelOnline.add(listOnlines);
		// ---------------------------------------------------------
		JButton btnLimpar = new JButton("LIMPAR");
		btnLimpar.setBackground(SystemColor.inactiveCaption);
		btnLimpar.setBounds(697, 371, 175, 40);
		frmReversi.getContentPane().add(btnLimpar);

		btnLimpar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
			}
		});
		// ---------------------------------------------------------

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.windowBorder);
		panel.setBounds(902, 380, 217, 118);
		frmReversi.getContentPane().add(panel);
		panel.setLayout(null);
		// ---------------------------------------------------------
		txtNome = new JTextField();
		txtNome.setHorizontalAlignment(SwingConstants.CENTER);
		txtNome.setBackground(SystemColor.control);
		txtNome.setBounds(12, 38, 193, 22);
		panel.add(txtNome);
		txtNome.setColumns(10);
		// ---------------------------------------------------------

		JLabel lblNome = new JLabel("IP");
		lblNome.setForeground(SystemColor.textHighlight);
		lblNome.setBounds(12, -2, 204, 50);
		panel.add(lblNome);

		JButton btnConnectar = new JButton("CONECTAR");
		btnConnectar.setBackground(SystemColor.inactiveCaption);
		btnConnectar.setBounds(12, 80, 97, 25);
		panel.add(btnConnectar);

		btnConnectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Conexao.setConexao(txtNome.getText(), o);
			}
		});
		// ---------------------------------------------------------

		JButton btnSair = new JButton("SAIR");
		btnSair.setBackground(SystemColor.inactiveCaption);
		btnSair.setBounds(121, 80, 84, 25);
		panel.add(btnSair);

		btnSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// ---------------------------------------------------------

		cores = new JComboBox<Cor>(Cor.values());
		cores.setBackground(SystemColor.inactiveCaption);
		cores.setBounds(902, 291, 217, 76);
		frmReversi.getContentPane().add(cores);
	}

	@Override
	public void updateMensagem(String m) {
		setRecebida("Rede: "+ m);
		
	}

	public String getRecebida() {
		return recebida;
	}

	public void setRecebida(String recebida) {
		this.recebida = recebida;
	}

	
}