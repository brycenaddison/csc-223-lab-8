package preprocessor;

/**
 * Idenify and create all Triangles from an array of Segment objects.
 * 
 * @author Brycen Addison, Hanna King
 */

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
		_triangles = new HashSet<>();

	}

	/*
	 * Compute the figure triangles on the fly when requested;
	 * memorize results for subsequent calls
	 */
	public Set<Triangle> getTriangles()
	{
		if (_triangles.size() != 0) return _triangles;

		computeTriangles();

		return _triangles;
	}

	/**
	 * create all triangles from _segments
	 */
	private void computeTriangles()
	{
		int n = _segments.length;

		// triangle requires at least 3 sides
		if (n < 3) return;

		// parallel array
		int[] indices = new int[]{0, 1, 2};

		while (true)
		{
			// create and add Triangle
			processTriangle(indices);

			// check if you can move
			// avoids IndexOutOfBounds later
			int i = incrementable(indices, n);

			// all combinations processed
			if (i == -1) return;

			// move to next combination
			increment(indices, i);
		}
	}

	/**
	 * increment all values in indices
	 * used to move through _segments three at a time
	 * @param indices
	 * @param i index to begin incrementing at
	 */
	private void increment(int[] indices, int i)
	{
		indices[i] += 1;
		for (int j = i + 1; j < 3; j++)
		{
			indices[j] = indices[j - 1] + 1;
		}
	}

	/**
	 * return index of index that can be incremented, -1 if none found
	 * used to tell the computeTriangle method when to stop
	 * @param indices index to consider incrementability of
	 * @param int n, value to consider incrementing by
	 * @return int largest incrementable value contained in indices,
	 *         -1 if there are no more incrementable values
	 */
	private int incrementable(int[] indices, int n)
	{
		// work backwards theough indices array
		for (int i = 2; i >= 0; i--)
		{
			if (indices[i] != i + n - 3) return i;
		}
		return -1;
	}

	/**
	 * get the segments at the indices contained in indices param.
	 * try to use those segments to create a triangle
	 * if successful, triangle is added to _triangles
	 * failure wouold cause a FactException, which is caught and moved past
	 * 
	 * @param indices array of indices of segments to use in this triangle
	 */
	private void processTriangle(int[] indices)
	{
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
