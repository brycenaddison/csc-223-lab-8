package preprocessor;

import java.util.*;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.Triangle;

public class TriangleIdentifier
{
	protected Set<Triangle> _triangles;
	protected Segment[] _segments;

	public TriangleIdentifier(Map<Segment, Segment> segments)
	{
		_segments = segments.keySet().toArray(new Segment[0]);
	}

	/*
	 * Compute the figure triangles on the fly when requested;
	 * memoize results for subsequent calls.
	 */
	public Set<Triangle> getTriangles()
	{
		if (_triangles != null) return _triangles;

		_triangles = new HashSet<Triangle>();

		computeTriangles();

		return _triangles;
	}

	private void computeTriangles()
	{
		int n = _segments.length;

		if (n < 3) return;

		int[] indices = new int[]{0, 1, 2};

		while (true) {
			processTriangle(indices);

			int i = incrementable(indices, n);

			// all combinations processed
			if (i == -1) return;

			increment(indices, i);
		}
	}

	private void increment(int[] indices, int i) {
		indices[i] += 1;
		for (int j = i + 1; j < 3; j++) {
			indices[j] = indices[j - 1] + 1;
		}
	}

	// return index of index that can be incremented, -1 if none found
	private int incrementable(int[] indices, int n) {
		for (int i = 2; i >= 0; i--) {
			if (indices[i] != i + n - 3) return i;
		}
		return -1;
	}

	private void processTriangle(int[] indices) {
		List<Segment> segments = new ArrayList<>();

		segments.add(_segments[indices[0]]);
		segments.add(_segments[indices[1]]);
		segments.add(_segments[indices[2]]);

		try {
			Triangle t = new Triangle(segments);
			_triangles.add(t);
		} catch (FactException e) {}
	}
}
