package gridpermute;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JPanel;

public class GridView extends JPanel implements Printable
{

	GridSet gridSet;


	GridView(GridSet gridSet)
	{
		this.gridSet = gridSet;
	}


	public void paint(Graphics g0)
	{
		// super.paint(g0);
		Graphics2D g = (Graphics2D) g0;

		if (gridSet == null)
			return;

		Rectangle panelBounds = this.getBounds();
		render(gridSet, g, panelBounds.width, panelBounds.height);
	}


	public static void render(GridSet gridSet, Graphics2D g, final double width,
			final double height)
	{

		// Init scale

		final float lableFraction = 0.015f;

		double renderHeight = (gridSet.settings.showNumber ) ? height
				* (1 - lableFraction) : height;

		float max = gridSet.getMax();

		int n = gridSet.renderList.size();

		float winAspect = (float) (renderHeight / width);
		int nx = 1;
		int ny = 1;

		while (nx * ny <= n)
		{
			float aspect = ((float) ny) / nx;
			if (aspect >= winAspect)
				nx++;
			else
				ny++;
		}

		float aspect = ((float) ny) / nx;

		if (aspect > winAspect)
			nx++;

		float cellWidth = (float) (width / nx);


		// Draw

		g.setColor(Color.white);
		g.fill(new Rectangle2D.Float(0, 0, (float) width, (float) height));
		g.setColor(Color.black);
		//g.draw(new Rectangle2D.Float(0, 0, (float) width, (float) height));

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		AffineTransform t = g.getTransform();

		double x = 0.0;
		double y = 0.0;
		int i = 0;
		double scale = cellWidth / max;

		g.setStroke(new BasicStroke((float) (1 / scale)));

		boolean enableBorder = gridSet.settings.enableBorder;

		for (Grid grid : gridSet.renderList)
		{
			g.setTransform(t);
			g.translate(x, y);
			g.scale(scale, scale);
			if (enableBorder)
				g.draw(new Rectangle2D.Float(0, 0, max, max));

			grid.render(g);

			x += cellWidth;
			if (++i >= nx)
			{
				i = 0;
				x = 0.0;
				y += cellWidth;
			}
		}

		if (gridSet.settings.showNumber)
		{
			String s = String.format("Level = %d   Item Count = %d  ",
					gridSet.settings.level, gridSet.renderList.size());

			double textScale = height * lableFraction / 9;
			FontMetrics f = g.getFontMetrics();
			Rectangle2D b = f.getStringBounds(s, g);

			g.setTransform(t);
			g.translate(width / 2 - b.getWidth() / 2 * textScale, height
					* (1 - lableFraction * 0.2));
			g.scale(textScale, textScale);


			g.drawString(s, 0, 0);
		}

		// System.out.println("draw finish");
	}


	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException
	{
		if (pageIndex >= 1)
			return Printable.NO_SUCH_PAGE;

		Graphics2D g2d = (Graphics2D) graphics;

		double h = pageFormat.getImageableHeight();
		double w = pageFormat.getImageableWidth();
		double x = pageFormat.getImageableX();
		double y = pageFormat.getImageableY();
		
		double maragin = 0.25 * 72 ;
		
//		h -= maragin * 2;
//		w -= maragin * 2;
//		x += maragin;
//		y += maragin;
		

		if (pageFormat.getOrientation() == PageFormat.LANDSCAPE)
			{
			y += maragin;
			h -= maragin;
			System.out.println("-pageFormat.LANDSCAPE");
			}
		if (pageFormat.getOrientation() == PageFormat.PORTRAIT)
			System.out.println("-pageFormat.PORTRAIT");

		System.out.println("pf:" + x + "," + y + "," + w + "," + h);

		// g2d.translate(x,y);
		// g2d.scale();

		// Rectangle r = g2d.getClipBounds();
		// r.x -= 1;
		// r.y -= 1;
		// r.width += 2;
		// r.height += 2;
		// g2d.setClip(r);

		g2d.translate(x, y);

		// Dimension size = new Dimension((int) w, (int) h);
		// AffineTransform t = g2d.getTransform();
		// paintIt(g2d, size);

		render(gridSet, g2d, w, h);

		// g2d.setTransform(t);
		// paintTestPattern(g2d, size);

		return Printable.PAGE_EXISTS;
	}


	// public void misc(Graphics2D g)
	// {
	// g.drawLine(100, 0, 100, 500);
	//
	// g.setColor(Color.blue);
	// g.fillOval(50, 50, 100, 100);
	//
	// g.setColor(Color.green);
	// g.translate(100, 100);
	// g.scale(50, 50);
	// // g.fill( new Ellipse2D.Float( -1f,-1f,2f,2f) );
	// g.setColor(Color.red);
	// g.fill(HexGrid.hexPath);
	//
	// }


}
