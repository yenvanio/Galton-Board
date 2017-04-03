package GaltonBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame {

	private JPanel panel;

	private SQueue circleList = new SQueue();

	int rectx;

	boolean circle = false;
	boolean fill = false;

	double[] heights;

	private JButton start = new JButton("Start Simulation");
	private ActionListener startAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			disable_components();
			try {
				solve();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	};

	private JButton reStart = new JButton("Reset");
	private ActionListener reStartAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("");
			enable_components();
			panel.repaint();
			setup();
		}
	};

	private int[] bins;

	private int balls;
	private JLabel ballsLabel = new JLabel("No. of Balls : ");
	private JLabel ballsNLabel = new JLabel();

	private JLabel runTime = new JLabel("");

	private int rows;
	private JLabel rowLabel = new JLabel("No. of Rows : ");
	private JLabel rowNLabel = new JLabel();
	private String[] rowsList = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
	private JComboBox rowBox = new JComboBox(rowsList);
	private ItemListener rowListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Object item = e.getItem();
				rows = Integer.parseInt(item.toString());
				bins = new int[rows + 1];
				rowNLabel.setText(item.toString());
			}
		}
	};

	private JSlider slider;
	private ChangeListener changeListener = new ChangeListener() {

		public void stateChanged(ChangeEvent ce) {
			JSlider slider = (JSlider) ce.getSource();
			ballsNLabel.setText(Integer.toString(slider.getValue()));
			if (!slider.getValueIsAdjusting())
				balls = (slider.getValue());
		}
	};

	public Main() throws IOException {

		setTitle("Galton Board Simulation");
		setSize(1000, 1000);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());

		panel = new JPanel();
		panel.setLayout(null);
		panel.setLocation(0, 0);
		panel.setPreferredSize(new Dimension(950, 950));
		panel.setOpaque(false);
		setup();
	}

	public void setup() {
		slider = new JSlider(0, 1000000, 10000);
		slider.setMajorTickSpacing(100000);
		slider.setMinorTickSpacing(10000);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setBounds(0, 50, 900, 200);
		slider.addChangeListener(changeListener);

		ballsLabel.setBounds(0, 0, 100, 50);
		ballsNLabel.setBounds(100, 0, 100, 50);
		ballsNLabel.setText(Integer.toString(slider.getValue()));

		runTime.setBounds(700, 800, 200, 50);

		rowLabel.setBounds(500, 0, 100, 50);
		rowNLabel.setBounds(600, 0, 100, 50);
		rowBox.setBounds(700, 0, 150, 50);
		rowBox.addItemListener(rowListener);
		rowBox.setSelectedItem(rowsList[9]);
		bins = new int[(Integer.parseInt(rowsList[9])) + 1];

		start.setBounds(350, 250, 150, 50);
		start.addActionListener(startAction);

		reStart.setBounds(700, 850, 150, 50);
		reStart.addActionListener(reStartAction);
		reStart.setEnabled(false);

		panel.add(reStart);
		panel.add(runTime);
		panel.add(slider);
		panel.add(ballsLabel);
		panel.add(ballsNLabel);
		panel.add(rowLabel);
		panel.add(rowNLabel);
		panel.add(rowBox);
		panel.add(start);

		getContentPane().add(panel);
		setVisible(true);
	}

	public void enable_components() {
		reStart.setEnabled(false);
		rowBox.setEnabled(true);
		start.setEnabled(true);
		slider.setEnabled(true);
		rows = Integer.parseInt(rowBox.getSelectedItem().toString());
		balls = slider.getValue();
		runTime.setText("");
	}

	public void disable_components() {
		reStart.setEnabled(true);
		rowBox.setEnabled(false);
		start.setEnabled(false);
		slider.setEnabled(false);
		rows = Integer.parseInt(rowBox.getSelectedItem().toString());
		heights = new double[rows + 1];
		balls = slider.getValue();
	}

	public void solve() throws InterruptedException {

		circle = true;
		generateRows();
		paint();
		circle = false;

		int num = divide();

		final long startTime = System.currentTimeMillis();
		Random generate = new Random();

		for (int b = 0; b < balls; b++) {
			int counter = 0;
			for (int i = 0; i < rows; i++) {// 1 = right(success)
				int direction = generate.nextInt(2); // 0 = left(fail)
				if (direction == 1) {
					counter++;
				}
			}
			bins[counter]++;
			if (b % num == 0 && b > 1) {
				animate(b);
			}
		}
		final long endTime = System.currentTimeMillis();
		for (int i = 0; i < bins.length; i++) {
			System.out.print(bins[i] + ", ");
		}
		final long runTimeN = endTime - startTime;
		if (runTimeN > 1000) {
			runTime.setText("Run Time: " + (runTimeN) / 1000 + "s");
		} else {
			runTime.setText("Run Time: " + (runTimeN) + "ms");
		}
	}

	public int divide() {
		int num = 100;
		switch(balls)
		{
		case 1000:
			num = 100;
			break;
		case 10000:
			num = 1000;
			break;
		case 100000:
			num = 10000;
			break;
		case 1000000:
			num = 50000;
			break;
		}
		return num;
	}

	public void paint() throws InterruptedException {
		Graphics g;
		g = getGraphics();
		paintComponent(g);
	}

	public void generateRows() {
		Point p = new Point(850 / 2, 400);
		int counter = 1;
		int spacer = 40;
		for (int i = 0; i < rows; i++) {
			for (int j = 1; j <= counter; j++) {
				int x = p.x + (j * spacer);
				circleList.enqueue(new Circle(x, p.y, 10, 10));
			}
			p.x -= 20;
			p.y += 20;
			counter++;
		}
		Circle c = circleList.get(circleList.size() - (rows));
		rectx = c.x;
	}

	public void paintComponent(Graphics g) throws InterruptedException {
		if (circle) {
			while (!circleList.isEmpty()) {
				Circle c = circleList.dequeue();
				g.drawOval(c.x, c.y, c.width, c.height);
			}
		}
		if (fill) {
			Graphics2D g2d = (Graphics2D) g;
			int x = rectx - 50;
			int y = 600;
			for (int i = 0; i < heights.length; i++) {
				Rectangle2D.Double r = new Rectangle2D.Double(x, y, 40, heights[i]);
				g2d.draw(r);
				g2d.fill(r);
				x += 45;
				Thread.sleep(50);
			}
		}
	}

	public void animate(int total) throws InterruptedException {
		for (int i = 0; i <= rows; i++) {
			int value = bins[i];
			double height = (value / (total / 100)) * 10;
			heights[i] = height;
		}
		fill = true;
		paint();
		fill = false;
	}

	public static void main(String[] args) throws IOException {
		Main run = new Main();
	}
}