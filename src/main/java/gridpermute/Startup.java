package gridpermute;

public class Startup
{
	public static GridSet gridSet = new GridSet();
	
	
	public static void main(String[] args)
	{
		try
		{
			//gridSet.initSame(100);
			//gridSet.initRandom(60, 8);
			gridSet.rebuild();
			
			new MainWindow( gridSet ).setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


