package org.nakedobjects.metamodel.specloader.progmodelfacets;

import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.actions.ActionMethodsFacetFactory;
import org.nakedobjects.metamodel.facets.actions.IteratorFilteringFacetFactory;
import org.nakedobjects.metamodel.facets.actions.SyntheticMethodFilteringFacetFactory;
import org.nakedobjects.metamodel.facets.actions.debug.DebugAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedViaNamingConventionFacetFactory;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.collections.CollectionFacetFactory;
import org.nakedobjects.metamodel.facets.collections.CollectionFieldMethodsFacetFactory;
import org.nakedobjects.metamodel.facets.collections.aggregated.AggregatedIfCollectionFacetFactory;
import org.nakedobjects.metamodel.facets.disable.DisabledAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.hide.HiddenAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.ignore.RemoveIgnoreAnnotationMethodsFacetFactory;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.naming.named.NamedAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.bounded.BoundedAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.bounded.BoundedMarkerInterfaceFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.RemoveCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.SaveCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdateCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.defaults.DefaultedAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.dirty.DirtyMethodsFacetFactory;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.ident.icon.IconMethodFacetFactory;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralMethodFacetFactory;
import org.nakedobjects.metamodel.facets.object.ident.singular.SingularMethodFacetFactory;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleMethodFacetFactory;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableMarkerInterfacesFacetFactory;
import org.nakedobjects.metamodel.facets.object.notpersistable.NotPersistableAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.notpersistable.NotPersistableMarkerInterfacesFacetFactory;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacetFactory;
import org.nakedobjects.metamodel.facets.object.validate.ValidateObjectViaValidateMethodFacetFactory;
import org.nakedobjects.metamodel.facets.object.validprops.ObjectValidPropertiesFacetFactory;
import org.nakedobjects.metamodel.facets.object.value.ValueFacetFactory;
import org.nakedobjects.metamodel.facets.ordering.actionorder.ActionOrderAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.ordering.fieldorder.FieldOrderAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.ordering.memberorder.MemberOrderAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.propcoll.notpersisted.NotPersistedAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.properties.PropertyMethodsFacetFactory;
import org.nakedobjects.metamodel.facets.properties.validate.PropertyValidateDefaultFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.specification.MustSatisfySpecificationFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.typicallength.TypicalLengthAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.typicallength.TypicalLengthDerivedFromTypeFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryDefaultFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.OptionalAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.validate.mask.MaskAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.propparam.validate.regex.RegExAnnotationFacetFactory;
import org.nakedobjects.metamodel.java5.FallbackFacetFactory;
import org.nakedobjects.metamodel.java5.RemoveGetClassMethodFacetFactory;
import org.nakedobjects.metamodel.java5.RemoveInitMethodFacetFactory;
import org.nakedobjects.metamodel.java5.RemoveJavaLangObjectMethodsFacetFactory;
import org.nakedobjects.metamodel.java5.RemoveSetDomainObjectContainerMethodFacetFactory;
import org.nakedobjects.metamodel.java5.RemoveStaticGettersAndSettersFacetFactory;
import org.nakedobjects.metamodel.java5.RemoveSuperclassMethodsFacetFactory;
import org.nakedobjects.metamodel.value.BigDecimalValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.BigIntegerValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.BooleanPrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.BooleanWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.BytePrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.ByteWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.CharPrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.CharWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.ColorValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.DateTimeValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.DateValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.DoublePrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.DoubleWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.FloatPrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.FloatWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.ImageValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.IntPrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.IntWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.JavaAwtImageValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.JavaSqlDateValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.JavaSqlTimeStampValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.JavaSqlTimeValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.JavaUtilDateValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.LongPrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.LongWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.MoneyValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.PasswordValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.PercentageValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.ShortPrimitiveValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.ShortWrapperValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.StringValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.TimeStampValueTypeFacetFactory;
import org.nakedobjects.metamodel.value.TimeValueTypeFacetFactory;


public class ProgrammingModelFacetsJava5 extends ProgrammingModelFacetsAbstract {
	
	public ProgrammingModelFacetsJava5() {
		
        // must be first, so any Facets created can be replaced by other FacetFactorys later.
        addFactory(FallbackFacetFactory.class);

        addFactory(IteratorFilteringFacetFactory.class);
        addFactory(SyntheticMethodFilteringFacetFactory.class);
        addFactory(RemoveSuperclassMethodsFacetFactory.class);
        addFactory(RemoveJavaLangObjectMethodsFacetFactory.class);
        addFactory(RemoveSetDomainObjectContainerMethodFacetFactory.class);
        addFactory(RemoveInitMethodFacetFactory.class);
        addFactory(RemoveStaticGettersAndSettersFacetFactory.class);
        addFactory(RemoveGetClassMethodFacetFactory.class);
        addFactory(RemoveIgnoreAnnotationMethodsFacetFactory.class);

        // must be before any other FacetFactories that install MandatoryFacet.class facets
        addFactory(MandatoryDefaultFacetFactory.class);
        addFactory(PropertyValidateDefaultFacetFactory.class);

        addFactory(ActionMethodsFacetFactory.class);
        addFactory(CollectionFieldMethodsFacetFactory.class);
        addFactory(PropertyMethodsFacetFactory.class);
        addFactory(IconMethodFacetFactory.class);
        
        addFactory(CreatedCallbackFacetFactory.class);
        addFactory(LoadCallbackFacetFactory.class);
        addFactory(SaveCallbackFacetFactory.class);
        addFactory(PersistCallbackFacetFactory.class);
        addFactory(UpdateCallbackFacetFactory.class);
        addFactory(RemoveCallbackFacetFactory.class);
        
        addFactory(DirtyMethodsFacetFactory.class);
        addFactory(ValidateObjectViaValidateMethodFacetFactory.class);
        addFactory(ObjectValidPropertiesFacetFactory.class);
        addFactory(PluralMethodFacetFactory.class);
        addFactory(SingularMethodFacetFactory.class);
        addFactory(TitleMethodFacetFactory.class);

        addFactory(ActionOrderAnnotationFacetFactory.class);
        addFactory(AggregatedAnnotationFacetFactory.class);
        addFactory(BoundedAnnotationFacetFactory.class);
        addFactory(BoundedMarkerInterfaceFacetFactory.class);
        addFactory(DebugAnnotationFacetFactory.class);
        addFactory(DefaultedAnnotationFacetFactory.class);
        addFactory(DescribedAsAnnotationFacetFactory.class);
        addFactory(DisabledAnnotationFacetFactory.class);
        addFactory(EncodableAnnotationFacetFactory.class);
        addFactory(ExecutedAnnotationFacetFactory.class);
        addFactory(ExecutedViaNamingConventionFacetFactory.class);
        addFactory(ExplorationAnnotationFacetFactory.class);
        addFactory(FieldOrderAnnotationFacetFactory.class);
        addFactory(HiddenAnnotationFacetFactory.class);
        addFactory(ImmutableAnnotationFacetFactory.class);
        addFactory(ImmutableMarkerInterfacesFacetFactory.class);
        addFactory(MaxLengthAnnotationFacetFactory.class);
        addFactory(MemberOrderAnnotationFacetFactory.class);
        addFactory(MustSatisfySpecificationFacetFactory.class);
        addFactory(MultiLineAnnotationFacetFactory.class);
        addFactory(NamedAnnotationFacetFactory.class);
        addFactory(NotPersistableAnnotationFacetFactory.class);
        addFactory(NotPersistableMarkerInterfacesFacetFactory.class);
        addFactory(NotPersistedAnnotationFacetFactory.class);
        addFactory(OptionalAnnotationFacetFactory.class);
        addFactory(ParseableFacetFactory.class);
        addFactory(PluralAnnotationFacetFactory.class);
        // must come after any facets that install titles
        addFactory(MaskAnnotationFacetFactory.class);
        // must come after any facets that install titles, and after mask
        // if takes precedence over mask.
        addFactory(RegExAnnotationFacetFactory.class);
        addFactory(TypeOfAnnotationFacetFactory.class);
        addFactory(TypicalLengthAnnotationFacetFactory.class);
        addFactory(TypicalLengthDerivedFromTypeFacetFactory.class);

        // built-in value types for Java language
        addFactory(BooleanPrimitiveValueTypeFacetFactory.class);
        addFactory(BooleanWrapperValueTypeFacetFactory.class);
        addFactory(BytePrimitiveValueTypeFacetFactory.class);
        addFactory(ByteWrapperValueTypeFacetFactory.class);
        addFactory(ShortPrimitiveValueTypeFacetFactory.class);
        addFactory(ShortWrapperValueTypeFacetFactory.class);
        addFactory(IntPrimitiveValueTypeFacetFactory.class);
        addFactory(IntWrapperValueTypeFacetFactory.class);
        addFactory(LongPrimitiveValueTypeFacetFactory.class);
        addFactory(LongWrapperValueTypeFacetFactory.class);
        addFactory(FloatPrimitiveValueTypeFacetFactory.class);
        addFactory(FloatWrapperValueTypeFacetFactory.class);
        addFactory(DoublePrimitiveValueTypeFacetFactory.class);
        addFactory(DoubleWrapperValueTypeFacetFactory.class);
        addFactory(CharPrimitiveValueTypeFacetFactory.class);
        addFactory(CharWrapperValueTypeFacetFactory.class);
        addFactory(BigIntegerValueTypeFacetFactory.class);
        addFactory(BigDecimalValueTypeFacetFactory.class);
        addFactory(JavaSqlDateValueTypeFacetFactory.class);
        addFactory(JavaSqlTimeValueTypeFacetFactory.class);
        addFactory(JavaUtilDateValueTypeFacetFactory.class);
        addFactory(JavaSqlTimeStampValueTypeFacetFactory.class);
        addFactory(StringValueTypeFacetFactory.class);
        
        // applib values
        addFactory(DateValueTypeFacetFactory.class);
        addFactory(DateTimeValueTypeFacetFactory.class);
        addFactory(ColorValueTypeFacetFactory.class);
        addFactory(MoneyValueTypeFacetFactory.class);
        addFactory(PasswordValueTypeFacetFactory.class);
        addFactory(PercentageValueTypeFacetFactory.class);
        addFactory(TimeStampValueTypeFacetFactory.class);
        addFactory(TimeValueTypeFacetFactory.class);
        addFactory(ImageValueTypeFacetFactory.class);        
        addFactory(JavaAwtImageValueTypeFacetFactory.class);


        // written to not trample over TypeOf if already // installed
        addFactory(CollectionFacetFactory.class);
        // must come after CollectionFacetFactory
        addFactory(AggregatedIfCollectionFacetFactory.class);
        
        // so we can dogfood the NO applib "value" types
        addFactory(ValueFacetFactory.class);

        addFactory(FacetsAnnotationFacetFactory.class);
	}


}
