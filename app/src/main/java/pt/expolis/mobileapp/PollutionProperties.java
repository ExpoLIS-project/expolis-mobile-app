package pt.expolis.mobileapp;

import pt.expolis.mobileapp.database.Data;
import pt.expolis.mobileapp.online.SensorData;

/**
 * Contains pollution properties used by the interface.  These properties are used to display
 * when a pollution level is okay, medium or bad, and to control the maximum displayed value.
 */
public enum PollutionProperties
{
	CO (10, 30, Data.CO, 0),
	NO2 (40, 50, Data.NO2, 0),
	PM1 (40, 50, Data.PM1f, 1),
	PM2_5 (10, 25, Data.PM25f, 1),
	PM10 (20, 40, Data.PM10f, 1);

	public final float thresholdGreenYellow, thresholdYellowRed;
	public final Data data;
	/**
	 * Which of the two online data plots this pollution is shown.
	 */
	public final int onlineDataPlot;

	PollutionProperties (int thresholdGreenYellow, int thresholdYellowRed, Data data, int onlineDataPlot)
	{
		this.thresholdGreenYellow = thresholdGreenYellow;
		this.thresholdYellowRed = thresholdYellowRed;
		this.data = data;
		this.onlineDataPlot = onlineDataPlot;
	}

	public AirQualityIndex getQuality (float value)
	{
		if (value < thresholdGreenYellow)
			return AirQualityIndex.VERY_GOOD;
		else if (value < thresholdYellowRed)
			return AirQualityIndex.MEDIUM;
		else
			return AirQualityIndex.BAD;
	}

	public AirQualityIndex getQuality (SensorData sensorData)
	{
		switch (this) {
			case CO:
				return getQuality (sensorData.co);
			case NO2:
				return getQuality (sensorData.no2);
			case PM1:
				return getQuality (sensorData.pm1);
			case PM2_5:
				return getQuality (sensorData.pm2_5);
			case PM10:
				return getQuality (sensorData.pm10);
		}
		throw new AssertionError ("not reached");
	}
}
