package org.nakedobjects.plugins.xml.objectstore.internal.adapter;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerDefault;

public class XmlAdapterManager extends AdapterManagerDefault {

	@Override
	protected NakedObject createAggregatedAdapter(
			Object pojo, NakedObject ownerAdapter, Identified identified) {
		if (identified instanceof OneToOneAssociation) {
			// do not yet support AggregatedOids for aggregated associations 
			return adapterFor(pojo);
		} else {
			return super.createAggregatedAdapter(pojo, ownerAdapter, identified);
		}
	}
}
