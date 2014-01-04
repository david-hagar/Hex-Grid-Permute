package gridpermute;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class HexGrid implements Grid
{
	static final float Sqrt3Over2 = (float) (Math.sqrt(3) / 2);
	static final float Sqrt3 = (float) (Math.sqrt(3));

	static GeneralPath hexPath;

	static
	{
		hexPath = new GeneralPath();
		hexPath.moveTo(1.0f, 0.0f);
		hexPath.lineTo(0.5f, Sqrt3Over2);
		hexPath.lineTo(-0.5f, Sqrt3Over2);
		hexPath.lineTo(-1.0f, 0.0f);
		hexPath.lineTo(-0.5f, -Sqrt3Over2);
		hexPath.lineTo(0.5f, -Sqrt3Over2);
		hexPath.closePath();
	}

	// static int hexNeighborsEven[][] = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0
	// },
	// { 1, -1 }, { -1, -1 } };
	// static int hexNeighborsOdd[][] = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0
	// },
	// { 1, 1 }, { -1, 1 } };

	static int hexNeighborsEven[][] = { { 0, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 },
			{ -1, -1 }, { -1, 0 } };
	static int hexNeighborsOdd[][] = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 0, -1 },
			{ -1, 1 }, { -1, 0 } };

	ArrayList<Point> points = new ArrayList<Point>();
	Point[][] grid = null;
	float aspectRatio = 0;


	public void render(Graphics2D g)
	{
		//g.setColor(Color.black);

		AffineTransform t = g.getTransform();
		for (Point p : points)
		{

			float hx = (p.x + 1) * 3.0f / 2;
			float hy = ((p.x & 1) == 1) ? ((p.y + 1) + 0.5f) * Sqrt3 : (p.y + 1)
					* Sqrt3;


			g.setTransform(t);
			g.translate(hx, hy);
			//g.setColor(Color.black);
			g.fill(hexPath);
			//g.setColor(Color.white);
			//g.draw(hexPath);
		}

	}


	public int compareTo(Object arg0)
	{
		HexGrid other = (HexGrid) arg0;

		int size = points.size();
		int otherSize = other.points.size();

		if (size > otherSize)
			return +1;
		if (size < otherSize)
			return -1;

		return Float.compare(getAspectRatio(), other.getAspectRatio());
	}


	public static class Point implements Comparable
	{
		int x;

		int y;


		public Point(int x, int y)
		{
			this.x = x;
			this.y = y;
		}


		public Point(Point p)
		{
			this.x = p.x;
			this.y = p.y;
		}


		public int compareTo(Object obj)
		{
			Point other = (Point) obj;

			if (x < other.x)
				return -1;
			if (x > other.x)
				return +1;

			if (y < other.y)
				return -1;
			if (y > other.y)
				return +1;

			return 0;
		}


		public boolean equals(Object obj)
		{
			Point other = (Point) obj;
			return x == other.x && y == other.y;
		};


		@Override
		public String toString()
		{
			return x + "," + y;
		}

	}


	public void printPoints()
	{
		System.out.println("Points:");
		for (Point p : points)
		{
			System.out.println(p);
		}
	}


	public Point getMaxIndexes()
	{
		int maxX = 0;
		int maxY = 0;

		for (Point p : points)
		{
			if (p.x > maxX)
				maxX = p.x;
			if (p.y > maxY)
				maxY = p.y;
		}

		return new Point(maxX, maxY);
	}


	public Point2D.Float getMaxBounds()
	{
		Point p = getMaxIndexes();

		return new Point2D.Float((p.x + 0) * 3 / 2 + 4, (p.y + 1) * Sqrt3);
	}


	public Point getMin()
	{
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;

		for (Point p : points)
		{
			if (p.x < minX)
				minX = p.x;
			if (p.y < minY)
				minY = p.y;
		}

		return new Point(minX, minY);
	}


	public HexGrid copy()
	{
		HexGrid copy = new HexGrid();

		for (Point p : points)
		{
			copy.points.add(new Point(p));
		}


		// if (grid != null)
		// copy.grid = grid.clone();

		return copy;

	}


	public void addPoint(int x, int y)
	{
		Point p = new Point(x, y);
		points.add(p);

	}


	// public static float toHexX(int x)
	// {
	// return x * 3.0f / 2;
	// }


	// public static float toHexY(int y)
	// {
	// if ((y & 1) == 1)
	// return (y + 0.5f) * Sqrt3;
	// else
	// return y * Sqrt3;
	// }


	public void buildXYGrid()
	{
		Point max = getMaxIndexes();

		grid = new Point[max.x + 4][max.y + 4];

		for (Point p : points)
		{
			grid[p.x + 2][p.y + 2] = p;
		}
	}


	public boolean isOccupied(int x, int y)
	{
		return grid[x + 2][y + 2] != null;
	}


	public boolean isEven(int i)
	{
		return (i & 1) == 0;
	}


	public boolean isOdd(int i)
	{
		return (i & 1) == 1;
	}


	public int[][] getNeighbors(int x)
	{
		return ((x & 1) == 0) ? hexNeighborsEven : hexNeighborsOdd;

	}


	public void generateNeighbors(GridSet gridSet)
	{
		buildXYGrid();

		for (Point p : points)
		{
			int[][] hexNeighbors = getNeighbors(p.x);
			for (int i = 0; i < hexNeighborsEven.length; i++)
			{
				int x = p.x + hexNeighbors[i][0];
				int y = p.y + hexNeighbors[i][1];

				if (!isOccupied(x, y))
				{
					HexGrid g = this.copy();
					g.points.add(new Point(x, y));
					g.normalize();
					g.normalize();
					gridSet.add(g);
				}
			}
		}


		grid = null;
	}


	private void normalize()
	{
		Point min = getMin();

		// min.x -= min.x % 2;
		boolean minXIsOdd = isOdd(min.x);

		for (Point p : points)
		{
			if (isOdd(p.x) && minXIsOdd)
				p.y++;
			p.x -= min.x;
			p.y -= min.y;
		}

		Collections.sort(points);

		// printPoints();
	}


	@Override
	public int hashCode()
	{
		int hash = 0;
		for (Point p : points)
		{
			hash *= 4;
			hash += p.x;
			hash *= 4;
			hash += p.y;
		}
		return hash;
	}


	public boolean equals(Object obj)
	{
		HexGrid other = (HexGrid) obj;

		int size = points.size();

		if (other.points.size() != size)
			return false;

		for (int i = 0; i < size; i++)
		{
			Point p1 = points.get(i);
			Point p2 = other.points.get(i);
			if (!p1.equals(p2))
				return false;
		}

		return true;
	};


	float getAspectRatio()
	{
		if (aspectRatio != 0)
			return aspectRatio;


		int x = 0;
		int y = 0;
		for (Point p : points)
		{
			x += p.x + 1;
			y += p.y + 1;
		}

		if (x == 0)
			return Float.MAX_VALUE;

		aspectRatio = ((float) y) / x;
		return aspectRatio;

	}

}
