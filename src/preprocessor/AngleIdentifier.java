package preprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.Triangle;
import geometry_objects.angle.Angle;
import geometry_objects.angle.AngleEquivalenceClasses;

public class AngleIdentifier
{
	protected List<Angle> _angles;
	protected Segment[] _segments;

	public AngleIdentifier(Map<Segment, Segment> segments)
	{
		_segments = segments.keySet().toArray(new Segment[0]);
	}

	/*
	 * Compute the figure triangles on the fly when requested; memorize results for subsequent calls.
	 */
	public AngleEquivalenceClasses getAngles()
	{
		AngleEquivalenceClasses classes = new AngleEquivalenceClasses();

		List<Angle> angles = getAngleList();

		for (Angle angle: angles) {
			classes.add(angle);
		}
		return classes;
	}

	public List<Angle> getAngleList() {
		if (_angles != null) return _angles;

		_angles = new ArrayList<>();

		computeAngles();

		return _angles;
	}

	/**
	 * Map contains segment, segment pairs. Add
	 * each of these to an AngleEquivalenceClass (AEC)
	 * return the size of the AEC
	 */
	private void computeAngles() {
		int n = _segments.length;

		// triangle requires at least 3 sides
		if (n < 2) return;

		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				processAngle(_segments[i], _segments[j]);
			}
		}
	}

	/**
	 * get the segments at the indices contained in indices param.
	 * try to use those segments to create an angle
	 * if successful, angle is added to _angles
	 * failure would cause a FactException, which is caught and moved past
	 */
	private void processAngle(Segment a, Segment b)
	{
		try {
			_angles.add(new Angle(a, b));
		} catch (FactException e) {}
	}
}
