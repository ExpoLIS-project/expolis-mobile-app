package pt.expolis.mobileapp.online;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

import pt.expolis.mobileapp.AirQualityIndex;
import pt.expolis.mobileapp.PollutionProperties;

/**
 * Sensor node data that is shown in the {@code OnlineDataFragment}.
 *
 * Pollution levels are displayed as bar plots by {@code OnlineDataPlotView}.  For each bar, a
 * smiley corresponding to its air quality index is drawn on top of the bar.
 *
 * Temperature and humidity are displayed in {@code TextView}s managed by {@code
 * OnlineDataFragment}.
 *
 * @see OnlineDataFragment
 * @see OnlineDataPlotView
 */
public class SensorData
	implements Serializable
{
	Date when = new Date ();
	public int co;
	public int no2;
	public int pm1;
	public int pm2_5;
	public int pm10;
	float temperature;
	float humidity;
	float longitude, latitude;

	AirQualityIndex getAirQuality ()
	{
		AirQualityIndex result = AirQualityIndex.VERY_GOOD;
		for (PollutionProperties pp: PollutionProperties.values ()) {
			AirQualityIndex pq = pp.getQuality (this.getBarValue (pp));
			if (pq.compareTo (result) > 0)
				result = pq;
		}
		return result;
	}

	/**
	 * Returns the pollution value that is displayed as a bar plot by the {@code
	 * OnlineDataPlotView} class.
	 * @param pp the pollution whose value is required.
	 * @return the pollution value of this sensor data.
	 */
	int getBarValue (@NotNull PollutionProperties pp)
	{
		switch (pp) {
			case CO:
				return co;
			case NO2:
				return no2;
			case PM1:
				return pm1;
			case PM2_5:
				return pm2_5;
			case PM10:
				return pm10;
		}
		throw new AssertionError ();
	}
	@NotNull
	@Override
	public String toString ()
	{
		return co + " " + no2 + " " + pm1 + " " + pm2_5 + " " + pm10 + " @" + when.toString ();
	}
}
