package pt.expolis.mobileapp.planner;

/**
 * Exception thrown by the {@code OSRMClient} class when an error occurs while
 * querying the ExpoLIS OSRM service.
 */
class OSRMException
	extends Exception
{
	final Type type;

	OSRMException (Type type)
	{
		this.type = type;
	}

	enum Type {
		SERVER_NOT_AVAILABLE,
		INVALID_RESPONSE,
		IO_ERROR,
	}
}
