package org.nakedobjects.applib.query;


/**
 * Convenience adapter class for {@link Query}.
 * 
 * <p>
 * Handles implementation of {@link #getResultType()}
 */
public abstract class QueryAbstract<T> implements Query<T> {

	private static final long serialVersionUID = 1L;
	
	private final String resultTypeName;
	/**
	 * Derived from {@link #getResultTypeName()}, with respect to the
	 * {@link Thread#getContextClassLoader() current thread's class loader}.
	 */
	private transient Class<T> resultType;

	public QueryAbstract(final Class<T> type) {
		this.resultTypeName = type.getName();
	}

	public QueryAbstract(final String typeName) {
		this.resultTypeName = typeName;
	}

	/**
	 * @throws IllegalStateException
	 *             (wrapping a {@link ClassNotFoundException}) if the class
	 *             could not be determined.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getResultType() {
		if (resultType == null) {
			try {
				resultType = (Class<T>) Thread.currentThread().getContextClassLoader()
						.loadClass(resultTypeName);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
		return resultType;
	}

	public String getResultTypeName() {
		return resultTypeName;
	}
	
}
