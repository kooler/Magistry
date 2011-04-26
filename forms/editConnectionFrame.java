package forms;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import data.Connection;

import test.Processor;

public class editConnectionFrame extends JFrame {
	JTextField time;
	JTextField cost;
	JFrame current;
	Connection connection;
	
	public editConnectionFrame(final Processor p, Connection con) {
		super();
		
		setSize(300, 150);
		setLayout(new GridLayout(0,1));
		current = this;
		connection = con;
		
		time = new JTextField(new Integer(con.getTime()).toString());
		cost = new JTextField(new Integer(con.getCost()).toString());
		this.add(new JLabel("Time:"));
		this.add(time);
		this.add(new JLabel("Cost:"));
		this.add(cost);
		
		JButton add = new JButton("Save");
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				connection.setCost(Integer.parseInt(cost.getText()));
				connection.setTime(Integer.parseInt(time.getText()));
				current.hide();
			}
		});
		this.add(add);
		
		this.show();
	}
}
