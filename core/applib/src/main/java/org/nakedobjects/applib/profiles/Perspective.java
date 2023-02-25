package org.nakedobjects.applib.profiles;




public interface Perspective {

    Object addToServices(Class<?> serviceClass);

    void addToServices(Class<?>... serviceClasses);
    void removeFromServices(Class<?>... serviceClasses);

    void addGenericRepository(Class<?>... domainClasses);
    
    void addToObjects(Object... object);

}


// Copyright (c) Naked Objects Group Ltd.
