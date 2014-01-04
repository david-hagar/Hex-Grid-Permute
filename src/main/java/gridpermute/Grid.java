package gridpermute;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public interface Grid extends Comparable
{

	public void render( Graphics2D g );
	
	Point2D.Float getMaxBounds();
	
	public void generateNeighbors(GridSet gridSet);

	
}
