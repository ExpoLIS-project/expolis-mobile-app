package utils;

public class MathUtils
{
	public static double range (double x, double min, double max)
	{
		return Math.max (min, Math.min (x, max));
	}

	public static int range (int x, int min, int max)
	{
		return Math.max (min, Math.min (x, max));
	}
}
