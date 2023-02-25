

import org.nakedobjects.applib.AbstractFactoryAndRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the attribute is an alternate key or a part of an business key (sometimes called alternate key).
 * Eg
 *      @Searchable(repository=AbstractFactoryAndRepository.class, queryByExample=false)
 * <p>
 * Not yet implemented.
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Searchable {

    Class repository() default AbstractFactoryAndRepository.class;
    
    boolean queryByExample() default true;
}

// Copyright (c) Naked Objects Group Ltd.
