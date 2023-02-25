package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.actions.choices.ActionChoicesFacetNone;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacetNone;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacetAtDefault;
import org.nakedobjects.metamodel.facets.help.HelpFacetNone;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacetNone;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetNone;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacetNone;
import org.nakedobjects.metamodel.facets.object.notpersistable.NotPersistableFacetNull;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacetNone;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthFacetUnlimited;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionParamPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectMemberPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaOneToOneAssociationPeer;


/**
 * Central point for providing some kind of default for any {@link Facet}s required by the Naked Objects
 * Framework itself.
 * 
 */
public class FallbackFacetFactory extends FacetFactoryAbstract {

    @SuppressWarnings("unused")
	private final static Map<Class<?>, Integer> TYPICAL_LENGTHS_BY_CLASS = new HashMap<Class<?>, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            putTypicalLength(byte.class, Byte.class, 3);
            putTypicalLength(short.class, Short.class, 5);
            putTypicalLength(int.class, Integer.class, 10);
            putTypicalLength(long.class, Long.class, 20);
            putTypicalLength(float.class, Float.class, 20);
            putTypicalLength(double.class, Double.class, 20);
            putTypicalLength(char.class, Character.class, 1);
            putTypicalLength(boolean.class, Boolean.class, 1);
        }

        private void putTypicalLength(final Class<?> primitiveClass, final Class<?> wrapperClass, final int length) {
            put(primitiveClass, new Integer(length));
            put(wrapperClass, new Integer(length));
        }
    };

    public FallbackFacetFactory() {
        super(NakedObjectFeatureType.EVERYTHING);
    }

    public boolean recognizes(final Method method) {
        return false;
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacets(new Facet[] { new DescribedAsFacetNone(holder),
        // commenting these out, think this whole isNoop business is a little bogus
                // new ImmutableFacetNever(holder),
                new NotPersistableFacetNull(holder), new TitleFacetNone(holder), });
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final List<Facet> facets = new ArrayList<Facet>();

        if (holder instanceof JavaNakedObjectMemberPeer) {
            facets.add(new NamedFacetNone(holder));
            facets.add(new DescribedAsFacetNone(holder));
            facets.add(new HelpFacetNone(holder));
        }

        if (holder instanceof JavaOneToOneAssociationPeer) {
            @SuppressWarnings("unused")
			final JavaOneToOneAssociationPeer association = (JavaOneToOneAssociationPeer) holder;
            facets.add(new MaxLengthFacetUnlimited(holder));
            facets.add(new MultiLineFacetNone(true, holder));
        }

        if (holder instanceof JavaNakedObjectActionPeer) {
            facets.add(new ExecutedFacetAtDefault(holder));
            facets.add(new ActionDefaultsFacetNone(holder));
            facets.add(new ActionChoicesFacetNone(holder));
        }

        return FacetUtil.addFacets(facets);
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final List<Facet> facets = new ArrayList<Facet>();

        if (holder instanceof JavaNakedObjectActionParamPeer) {
            @SuppressWarnings("unused")
			final JavaNakedObjectActionParamPeer param = (JavaNakedObjectActionParamPeer) holder;

            facets.add(new NamedFacetNone(holder));
            facets.add(new DescribedAsFacetNone(holder));
            facets.add(new HelpFacetNone(holder));
            facets.add(new MultiLineFacetNone(false, holder));

            facets.add(new MaxLengthFacetUnlimited(holder));
        }

        return FacetUtil.addFacets(facets);
    }

}

// Copyright (c) Naked Objects Group Ltd.
