package seng300.software.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.SystemColor;

public class PinPad extends JPanel {

	private static final int ONE 	= 1;
	private static final int TWO 	= 2;
	private static final int THREE 	= 3;
	private static final int FOUR 	= 4;
	private static final int FIVE 	= 5;
	private static final int SIX 	= 6;
	private static final int SEVEN 	= 7;
	private static final int EIGHT 	= 8;
	private static final int NINE 	= 9;
	private static final int ZERO 	= 0;
	private static final int DELETE 	= -1;
	private static final int ENTER 	= -2;
	
//	private JPanel contentPane;
	private JTextField displayInput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PinPad frame = new PinPad();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PinPad() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 450);
//		contentPane = new JPanel();
		setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{1, 1, 1};
		gbl_contentPane.rowHeights = new int[]{1, 1, 1, 1, 1};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
		setLayout(gbl_contentPane);
		
		displayInput = new JTextField();
		displayInput.setHorizontalAlignment(SwingConstants.CENTER);
		displayInput.setFont(new Font("Tahoma", Font.PLAIN, 28));
		GridBagConstraints gbc_displayInput = new GridBagConstraints();
		gbc_displayInput.gridwidth = 3;
		gbc_displayInput.fill = GridBagConstraints.BOTH;
		gbc_displayInput.insets = new Insets(0, 0, 5, 0);
		gbc_displayInput.gridx = 0;
		gbc_displayInput.gridy = 0;
		add(displayInput, gbc_displayInput);
		displayInput.setColumns(10);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 4;
		gbc_panel.gridwidth = 3;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		panel.setLayout(new GridLayout(4, 3, 5, 5)); // rows, cols, hgap, vgap
		add(panel, gbc_panel);
		
		JButton oneBtn = new JButton("1");
		oneBtn.setBackground(new Color(240, 248, 255));
		oneBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		oneBtn.addActionListener(e -> push(ONE));
		panel.add(oneBtn);
		
		JButton twoBtn = new JButton("2");
		twoBtn.setBackground(new Color(240, 248, 255));
		twoBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		twoBtn.addActionListener(e -> push(TWO));
		panel.add(twoBtn);
		
		JButton threeBtn = new JButton("3");
		threeBtn.setBackground(new Color(240, 248, 255));
		threeBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		threeBtn.addActionListener(e -> push(THREE));
		panel.add(threeBtn);
		
		JButton fourBtn = new JButton("4");
		fourBtn.setBackground(new Color(240, 248, 255));
		fourBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		fourBtn.addActionListener(e -> push(FOUR));
		panel.add(fourBtn);
		
		JButton fiveBtn = new JButton("5");
		fiveBtn.setBackground(new Color(240, 248, 255));
		fiveBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		fiveBtn.addActionListener(e -> push(FIVE));
		panel.add(fiveBtn);
		
		JButton sixBtn = new JButton("6");
		sixBtn.setBackground(new Color(240, 248, 255));
		sixBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		sixBtn.addActionListener(e -> push(SIX));
		panel.add(sixBtn);
		
		JButton sevenBtn = new JButton("7");
		sevenBtn.setBackground(new Color(240, 248, 255));
		sevenBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		sevenBtn.addActionListener(e -> push(SEVEN));
		panel.add(sevenBtn);
		
		JButton eightBtn = new JButton("8");
		eightBtn.setBackground(new Color(240, 248, 255));
		eightBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		eightBtn.addActionListener(e -> push(EIGHT));
		panel.add(eightBtn);
		
		JButton nineBtn = new JButton("9");
		nineBtn.setBackground(new Color(240, 248, 255));
		nineBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		nineBtn.addActionListener(e -> push(NINE));
		panel.add(nineBtn);
		
		JButton delBtn = new JButton("Delete");
		delBtn.setBackground(new Color(255, 240, 245));
		delBtn.setForeground(new Color(139, 0, 0));
		delBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		delBtn.addActionListener(e -> push(DELETE));
		panel.add(delBtn);

		JButton zeroBtn = new JButton("0");
		zeroBtn.setBackground(new Color(240, 248, 255));
		zeroBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		zeroBtn.addActionListener(e -> push(ZERO));
		panel.add(zeroBtn);

		JButton padEnterBtn = new JButton("Enter");
		padEnterBtn.setBackground(new Color(240, 255, 240));
		padEnterBtn.setForeground(new Color(0, 100, 0));
		padEnterBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		padEnterBtn.addActionListener(e -> push(ENTER));
		panel.add(padEnterBtn);
	}
	
	void push(int opt)
	{
		if (opt == DELETE)
		{	// remove last char from current input
			String oldText = displayInput.getText();
			if (!oldText.isEmpty())
			{
				String newText = oldText.substring(0, oldText.length()-1);
				displayInput.setText(newText);
			}
		}
		else if(opt == ENTER)
		{
			//todo
		}
		else
		{
			String oldText = displayInput.getText();
			String newText = oldText + opt;
			displayInput.setText(newText);
		}
	}

}
