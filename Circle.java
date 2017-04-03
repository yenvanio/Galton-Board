package GaltonBoard;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Circle extends JPanel {
	

	int x;
	int y;
	int height;
	int width;
	
	public Circle(int x, int y, int height, int width) 
	{
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
	}

}
