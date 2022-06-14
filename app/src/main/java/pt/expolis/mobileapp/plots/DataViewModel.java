package pt.expolis.mobileapp.plots;


import pt.expolis.mobileapp.database.Data;

/**
 * Interface used by all graph view models to represent the get and set operations
 * on the data that is shown on all graphs.
 */
public interface DataViewModel
{
	/**
	 * Get the data of the view model.
	 * @return the data of the view model.
	 */
	Data getData ();

	/**
	 * Set the data of the view model.
	 * @param value the new data of the view model.
	 */
	void setData (Data value);
}
