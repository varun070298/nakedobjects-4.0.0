package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.applib.adapters.DefaultsProvider;
import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.adapters.ValueSemanticsProvider;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.facets.MultipleValueFacetAbstract;
import org.nakedobjects.metamodel.facets.object.defaults.DefaultedFacetUsingDefaultsProvider;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacetUsingEncoderDecoder;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacetUsingParser;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacetUsingParser;
import org.nakedobjects.metamodel.facets.propparam.typicallength.TypicalLengthFacetUsingParser;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.ClassUtil;


public abstract class ValueFacetAbstract extends MultipleValueFacetAbstract implements ValueFacet {

    public static Class<? extends Facet> type() {
        return ValueFacet.class;
    }

    private static ValueSemanticsProvider newValueSemanticsProviderOrNull(
            final Class<?> semanticsProviderClass, 
            final FacetHolder holder,
    		final NakedObjectConfiguration configuration, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        if (semanticsProviderClass == null) {
            return null;
        }
        return (ValueSemanticsProvider) ClassUtil.newInstance(
        		semanticsProviderClass, 
        		new Class<?>[]{ FacetHolder.class, NakedObjectConfiguration.class, SpecificationLoader.class, RuntimeContext.class},
        		new Object[] { holder, configuration, specificationLoader, runtimeContext});
    }

    // to look after the facets (since MultiTyped)
    private final FacetHolder facetHolder = new FacetHolderImpl();

    private final ValueSemanticsProvider semanticsProvider;

	private final RuntimeContext runtimeContext;

    public ValueFacetAbstract(
    		final Class<?> semanticsProviderClass, 
    		final boolean addFacetsIfInvalid, 
    		final FacetHolder holder, 
    		final NakedObjectConfiguration configuration, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        this(newValueSemanticsProviderOrNull(semanticsProviderClass, holder, configuration, specificationLoader, runtimeContext), addFacetsIfInvalid, holder, runtimeContext);
    }

    public ValueFacetAbstract(
            final ValueSemanticsProvider semanticsProvider,
            final boolean addFacetsIfInvalid,
            final FacetHolder holder,
            final RuntimeContext runtimeContext) {
        super(type(), holder);

        this.semanticsProvider = semanticsProvider;
        this.runtimeContext = runtimeContext;
        
        // note: we can't use the runtimeContext to inject dependencies into the semanticsProvider,
        // because there won't be any PersistenceSession when initially building the metamodel.
        // so, we defer until we use the parser.
        
        if (!isValid() && !addFacetsIfInvalid) {
            return;
        }

        // we now figure add all the facets supported. Note that we do not use FacetUtil.addFacet,
        // because we need to add them explicitly to our delegate facetholder but have the
        // facets themselves reference this value's holder.

        facetHolder.addFacet((Facet) this); // add just ValueFacet.class initially.

        // we used to add aggregated here, but this was wrong.  
        // An immutable value is not aggregated, it is shared.

        // ImmutableFacet, if appropriate
        final boolean immutable = semanticsProvider != null ? semanticsProvider.isImmutable() : true;
        if (immutable) {
            facetHolder.addFacet(new ImmutableFacetViaValueSemantics(holder));
        }

        // EqualByContentFacet, if appropriate
        final boolean equalByContent = semanticsProvider != null ? semanticsProvider.isEqualByContent() : true;
        if (equalByContent) {
            facetHolder.addFacet(new EqualByContentFacetViaValueSemantics(holder));
        }

        if (semanticsProvider != null) {

            // install the EncodeableFacet if we've been given an EncoderDecoder
            final EncoderDecoder encoderDecoder = semanticsProvider.getEncoderDecoder();
            if (encoderDecoder != null) {
                facetHolder.addFacet(new EncodableFacetUsingEncoderDecoder(encoderDecoder, holder, getRuntimeContext()));
            }

            // install the ParseableFacet and other facets if we've been given a Parser
            final Parser parser = semanticsProvider.getParser();
            if (parser != null) {
                facetHolder.addFacet(new ParseableFacetUsingParser(parser, holder, getRuntimeContext()));
                facetHolder.addFacet(new TitleFacetUsingParser(parser, holder, getRuntimeContext()));
                facetHolder.addFacet(new TypicalLengthFacetUsingParser(parser, holder, getRuntimeContext()));
            }

            // install the DefaultedFacet if we've been given a DefaultsProvider
            final DefaultsProvider defaultsProvider = semanticsProvider.getDefaultsProvider();
            if (defaultsProvider != null) {
                facetHolder.addFacet(new DefaultedFacetUsingDefaultsProvider(defaultsProvider, holder, getRuntimeContext()));
            }
        }
    }

	public boolean isValid() {
        return this.semanticsProvider != null;
    }

    // /////////////////////////////
    // MultiTypedFacet impl
    // /////////////////////////////
    public Class<? extends Facet>[] facetTypes() {
        return facetHolder.getFacetTypes();
    }

    public <T extends Facet> T getFacet(Class<T> facetType) {
        return facetHolder.getFacet(facetType);
    }


    
    // /////////////////////////////////////////
    // Dependencies (from constructor)
    // /////////////////////////////////////////

    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}


}
