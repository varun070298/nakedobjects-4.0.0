package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.ConsentAbstract;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;


public abstract class AbstractObjectContent extends AbstractContent implements ObjectContent {

    public static final class ExplorationInstances extends AbstractUserAction {

        public ExplorationInstances() {
            super("Instances", UserAction.EXPLORATION);
        }

        @Override
        public Consent disabled(final View view) {
            final NakedObject object = view.getContent().getNaked();
            return ConsentAbstract.allowIf(object != null);
        }

        @Override
        public void execute(final Workspace workspace, final View view, final Location at) {
            final NakedObject object = view.getContent().getNaked();
            final NakedObjectSpecification spec = object.getSpecification();
            final NakedObject instances = NakedObjectsContext.getPersistenceSession().findInstances(new QueryFindAllInstances(spec), QueryCardinality.MULTIPLE);
            workspace.objectActionResult(instances, at);
        }
    }

    public static final class ExplorationClone extends AbstractUserAction {

        public ExplorationClone() {
            super("Clone", UserAction.EXPLORATION);
        }

        @Override
        public Consent disabled(final View view) {
            final NakedObject object = view.getContent().getNaked();
            return ConsentAbstract.allowIf(object != null);
        }

        @Override
        public void execute(final Workspace workspace, final View view, final Location at) {
            final NakedObject original = view.getContent().getNaked();
            // NakedObject original = getObject();
            final NakedObjectSpecification spec = original.getSpecification();

            final NakedObject clone = getPersistenceSession().createInstance(spec);
            final NakedObjectAssociation[] fields = spec.getAssociations();
            for (int i = 0; i < fields.length; i++) {
                final NakedObject fld = fields[i].get(original);

                if (fields[i].isOneToOneAssociation()) {
                    ((OneToOneAssociation) fields[i]).setAssociation(clone, fld);
                } else if (fields[i].isOneToManyAssociation()) {
                    // clone.setValue((OneToOneAssociation) fields[i], fld.getObject());
                }
            }

            workspace.objectActionResult(clone, at);
        }

    }

    public static final class DebugClearResolvedOption extends AbstractUserAction {

        private DebugClearResolvedOption() {
            super("Clear resolved", UserAction.DEBUG);
        }

        @Override
        public Consent disabled(final View view) {
            final NakedObject object = view.getContent().getNaked();
            return ConsentAbstract.allowIf(object == null || object.getResolveState() != ResolveState.TRANSIENT
                    || object.getResolveState() == ResolveState.GHOST);
        }

        @Override
        public void execute(final Workspace workspace, final View view, final Location at) {
            final NakedObject object = view.getContent().getNaked();
            object.changeState(ResolveState.GHOST);
        }
    }

    public abstract Consent canClear();

    public Consent canDrop(final Content sourceContent) {
        final NakedObject target = getObject();
        if (!(sourceContent instanceof ObjectContent) || target == null) {
            // TODO: move logic into Facet
            return new Veto(String.format("Can't drop %s onto empty target", sourceContent.getNaked().titleString()));
        } else {
            final NakedObject source = ((ObjectContent) sourceContent).getObject();
            return canDropOntoObject(target, source);
        }
    }

    private Consent canDropOntoObject(final NakedObject target, final NakedObject source) {
        final NakedObjectAction action = dropAction(source, target);
        if (action != null) {
            final Consent parameterSetValid = action.isProposedArgumentSetValid(target, new NakedObject[] { source });
            parameterSetValid.setDescription("Execute '" + action.getName() + "' with " + source.titleString());
            return parameterSetValid;
        } else {
            return setFieldOfMatchingType(target, source);
        }
    }

    private Consent setFieldOfMatchingType(final NakedObject targetAdapter, final NakedObject sourceAdapter) {
        if (targetAdapter.isTransient() && sourceAdapter.isPersistent()) {
            // TODO: use Facet for this test instead.
            return new Veto("Can't set field in persistent object with reference to non-persistent object");
        }
        final NakedObjectAssociation[] fields = targetAdapter.getSpecification().getAssociations(
                NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), targetAdapter));
        for (final NakedObjectAssociation fld : fields) {
            if (!fld.isOneToOneAssociation()) {
                continue;
            }
            if (!sourceAdapter.getSpecification().isOfType(fld.getSpecification())) {
                continue;
            }
            if (fld.get(targetAdapter) != null) {
                continue;
            }
            final Consent associationValid = ((OneToOneAssociation) fld).isAssociationValid(targetAdapter, sourceAdapter);
            if (associationValid.isAllowed()) {
                return associationValid.setDescription("Set field " + fld.getName());
            }

        }
        // TODO: use Facet for this test instead
        return new Veto(String.format("No empty field accepting object of type %s in %s", sourceAdapter.getSpecification()
                .getSingularName(), title()));
    }

    public abstract Consent canSet(final NakedObject dragSource);

    public abstract void clear();

    public NakedObject drop(final Content sourceContent) {
        if (!(sourceContent instanceof ObjectContent)) {
            return null;
        } 

        final NakedObject source = sourceContent.getNaked();
        Assert.assertNotNull(source);

        final NakedObject target = getObject();
        Assert.assertNotNull(target);

        if (!canDrop(sourceContent).isAllowed()) {
            return null;
        }
        
        final NakedObjectAction action = dropAction(source, target);
        if ((action != null) && action.isProposedArgumentSetValid(target, new NakedObject[] { source }).isAllowed()) {
            return action.execute(target, new NakedObject[] { source });
        }
        
        final NakedObjectAssociation[] associations = target.getSpecification().getAssociations(
                NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), target));
        
        for (int i = 0; i < associations.length; i++) {
            final NakedObjectAssociation association = associations[i];
            if (association.isOneToOneAssociation() && source.getSpecification().isOfType(association.getSpecification())) {
                OneToOneAssociation otoa = (OneToOneAssociation) association;
                if (association.get(target) == null && 
                    otoa.isAssociationValid(target, source).isAllowed()) {
                    otoa.setAssociation(target, source);
                    break;
                }
            }
        }

        return null;
    }

    private NakedObjectAction dropAction(final NakedObject source, final NakedObject target) {
        NakedObjectAction action = target.getSpecification().getObjectAction(NakedObjectActionConstants.USER, null,
                new NakedObjectSpecification[] { source.getSpecification() });
        return action;
    }

    public abstract NakedObject getObject();

    @Override
    public boolean isPersistable() {
        return getObject().getSpecification().persistability() == Persistability.USER_PERSISTABLE;
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        final NakedObject object = getObject();
        OptionFactory.addObjectMenuOptions(object, options);

        if (getObject() == null) {
            OptionFactory.addCreateOptions(getSpecification(), options);
        } else {
            options.add(new ExplorationInstances());
        }

        options.add(new ExplorationClone());
        options.add(new DebugClearResolvedOption());
    }

    public void parseTextEntry(final String entryText) {
        throw new UnexpectedCallException();
    }

    public abstract void setObject(final NakedObject object);

    public String getIconName() {
        final NakedObject object = getObject();
        return object == null ? null : object.getIconName();
    }

    public Image getIconPicture(final int iconHeight) {
        final NakedObject nakedObject = getObject();
        if (nakedObject == null) {
            return ImageFactory.getInstance().loadIcon("empty-field", iconHeight, null);
        }
        final NakedObjectSpecification specification = nakedObject.getSpecification();
        final Image icon = ImageFactory.getInstance().loadIcon(specification, iconHeight, null);
        return icon;
    }

    
    //////////////////////////////////////////////////////////////
    // Dependencies (from context)
    //////////////////////////////////////////////////////////////
    
    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

}
// Copyright (c) Naked Objects Group Ltd.
