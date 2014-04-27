package gridpermute;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GridSet
{

	private ArrayList<Grid> list = new ArrayList<Grid>();
	private HashSet<Grid> set = new HashSet<Grid>();
	private ArrayList<Grid> lastLevel = new ArrayList<Grid>();
	ArrayList<Grid> renderList = list;

	Settings settings = new Settings();


	public void add(Grid g)
	{
		if (set.contains(g))
			return;

		list.add(g);
		set.add(g);

		lastLevel.add(g);
	}


	public void initRandom(int n, int width)
	{
		int count = width * width / 2;

		for (int i = 0; i < n; i++)
		{
			HexGrid g = new HexGrid();

			for (int j = 0; j < count; j++)
			{
				int x = (int) Math.round(Math.random() * width);
				int y = (int) Math.round(Math.random() * width);
				g.addPoint(x, y);
			}

			add(g);
		}
	}


	public void initSame(int n)
	{
		HexGrid g = new HexGrid();

		g.addPoint(0, 0);
		g.addPoint(0, 1);
		g.addPoint(1, 0);
		g.addPoint(2, 1);

		for (int i = 0; i < n; i++)
		{
			add(g);
		}
	}


	public float getMax()
	{
		float x = 0;
		float y = 0;

		for (Grid grid : list)
		{
			Point2D.Float d = grid.getMaxBounds();
			if (d.x > x)
				x = d.x;
			if (d.y > y)
				y = d.y;

		}

		//System.out.println("max x=" + x);
		//System.out.println("max y=" + y);

		return Math.max(x, y);
	}


	public void rebuild()
	{
		list.clear();
		set.clear();
		lastLevel.clear();

		HexGrid g = new HexGrid();
		g.addPoint(0, 0);
		add(g);

		for (int i = 0; i < settings.level-1; i++)
		{
			ArrayList<Grid> currentLevel = new ArrayList<Grid>(lastLevel);

			lastLevel.clear();

			for (Grid grid : currentLevel)
			{
				grid.generateNeighbors(this);
			}

			System.out.println("i=" + i + " n=" + lastLevel.size() + " total="
					+ list.size());
		}

		if (settings.lastOnly)
			renderList = lastLevel;
		else
			renderList = list;

		if (settings.sort)
			Collections.sort(renderList);
	}


	public static class Settings
	{
		int level = 4;
		boolean lastOnly = false;
		boolean enableBorder = false;
		boolean showNumber = true;
		boolean sort = false;
	}
}
