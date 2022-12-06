package preprocessor;

import java.util.Map;

import geometry_objects.Segment;
import geometry_objects.angle.AngleEquivalenceClasses;

public class AngleIdentifier
{
	protected AngleEquivalenceClasses _angles;
	protected Map<Segment, Segment> _segments;

	public AngleIdentifier(Map<Segment, Segment> segments)
	{
		_segments = segments;
		_angles = new AngleEquivalenceClasses();
	}

	/*
	 * Compute the figure triangles on the fly when requested; memorize results for subsequent calls.
	 */
	public AngleEquivalenceClasses getAngles()
	{
		if (_angles != null) return _angles;

		computeAngles();

		return _angles;
	}

	/**
	 * Map contains segment, segment pairs. Add
	 * each of these to an AngleEquivalenceClass (AEC)
	 * return the size of the AEC
	 */
	private void computeAngles()
	{
		// TODO
	}
}
