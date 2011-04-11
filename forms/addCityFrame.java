package forms;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import test.Processor;

public class addCityFrame extends JFrame {
	JTextField name;
	JFrame current;
	Point point;
	
	public addCityFrame(final Processor p, final Point point) {
		super();
		
		setSize(300, 100);
		setLayout(new BorderLayout());
		current = this;
		this.point = point;
		
		name = new JTextField();
		this.add(name, BorderLayout.CENTER);
		
		JButton add = new JButton("Add");
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				p.addCity(name.getText(), point);
				current.hide();
			}
		});
		this.add(add, BorderLayout.SOUTH);
		
		this.show();
	}
}
