/**
 * Object Reflector API.
 * 
 * <p>
 * The job of the reflector is to create the meta-model, typically using annotations and
 * other conventions in its own applib.
 * 
 * <p>
 * Concrete implementations are in the <tt>programmingmodel-xxx-impl</tt> modules.  These
 * are expected to be based heavily on <tt>NakedObjectReflectorAbstract</tt>, defined in
 * <tt>nof-core</tt>.  This implementation defines two further sub-APIs which are based on 
 * the {@link org.nakedobjects.metamodel.facets.Facet}s and {@link org.nakedobjects.metamodel.facetdecorator.FacetDecorator}s:
 * <ul>
 * <li> the {@link ProgrammingModelInstaller} is used to specify the collection of {@link org.nakedobjects.metamodel.facets.FacetFactory}s
 *      that will be used to actually process and build up the metamodel.
 * <li> the {@link FacetDecoratorInstaller} API specifies how {@link org.nakedobjects.metamodel.facets.Facet}, once created, 
 *      can be additionally decorated to modify their behaviour.  A number of other components are implemented as
 *      {@link org.nakedobjects.metamodel.facetdecorator.FacetDecorator}s, such as {@link org.nakedobjects.authorization.AuthorisationFacetDecorator authorisation},
 *      {@link org.nakedobjects.help.HelpFacetDecorator help}, and {@link org.nakedobjects.transaction.facetdecorator.TransactionFacetDecorator transactions}.  However
 *      it is possible for other {@link org.nakedobjects.metamodel.facetdecorator.FacetDecorator}s to be defined and installed also (such as <tt>i18n</tt>).
 * </ul>
 * 
 */
package org.nakedobjects.metamodel.specloader;