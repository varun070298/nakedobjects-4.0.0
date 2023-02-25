package org.nakedobjects.metamodel.examples.facets.namefile;

import java.io.IOException;
import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.spec.identifier.Identified;


public class NameFileFacetFactory implements FacetFactory {

    private final NameFileParser nameFileParser;
    
    public NameFileFacetFactory() {
        nameFileParser = new NameFileParser();
        try {
            nameFileParser.parse();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public NakedObjectFeatureType[] getFeatureTypes() {
        return NakedObjectFeatureType.EVERYTHING_BUT_PARAMETERS;
    }

    
    /**
     * Simply attaches a {@link NameFileFacet}.
     */
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(cls, holder));
    }

    private NameFileFacet create(final Class<?> cls, FacetHolder holder) {
        String memberNameInFile = nameFileParser.getName(cls);
        return memberNameInFile!=null?new NameFileFacet(holder, memberNameInFile): null;
    }
    
    /**
     * Simply attaches a {@link NameFileFacet}.
     */
    public boolean process(final Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
    	if (!(holder instanceof Identified)) {
    		return false;
    	}
		Identified identifiedHolder = (Identified) holder;
        Class<?> declaringClass = method.getDeclaringClass();
        String memberName = identifiedHolder.getIdentifier().getMemberName();
        return FacetUtil.addFacet(create(declaringClass, memberName, holder));
    }

    private NameFileFacet create(final Class<?> declaringClass, final String memberName, FacetHolder holder) {
        String memberNameInFile = nameFileParser.getMemberName(declaringClass, memberName);
        return memberNameInFile!=null?new NameFileFacet(holder, memberNameInFile): null;
    }


    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        // nothing to do
        return false;
    }


}
