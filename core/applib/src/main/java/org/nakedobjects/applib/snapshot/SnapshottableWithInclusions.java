package org.nakedobjects.applib.snapshot;

import java.util.List;

/**
 * Optional subinterface of {@link Snapshottable}s, used by <tt>XmlSnapshot</tt>
 * to automatically include additional paths within the snapshot.
 */
public interface SnapshottableWithInclusions extends Snapshottable {

	/**
	 * Paths to include in the snapshot.
	 */
	List<String> snapshotInclusions();
}
