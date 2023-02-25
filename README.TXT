Overview
--------
Naked Objects for Java is an open source application development platform. It's 
called Naked Objects because all you need to develop are your domain objects - 
the Naked Objects platform auto-creates an object-oriented user interface (giving 
you the choice of different styles) and the underlying persistent storage 
(typically using Hibernate).

Naked Objects is made available via the Naked Objects website (see
http://www.nakedobjects.org/) and it project page on Source Forge (see 
http://sourceforge.net/projects/nakedobjects/).

Version 4.0
-----------
Version 4.0 of Naked Objects is the result of 18 months' of development:
restructuring and redesigning the version 3.0 framework, while providing 
some significant new functionality.

Previous versions of Naked Objects have been intended to be used a platform for 
writing domain-driven applications.  This new version offers the same 
capabilities, but also offers many more options for extensions at a number of
different levels.

At the highest and most coarse-grained level, Naked Objects has been 
restructured into a "core" component, along with a set of independent "plug-in" 
components.  These plug-ins provide different user interfaces (dnd and html), 
persistors (xml, hibernate) and remoting capabilities.  Others will be added in 
the future.  While each component will be developed and released independently, 
the main release will be based on the core component with the latest compatible 
versions of each of the plug-in components. 

Naked Objects also provides extensibility at lower levels.  One of the most
significant is within the meta-model (part of the core component), which has
been completely redesigned.  This makes it easy for you to extend the
programming model to your own requirements, for example, to provide
interoperability with other frameworks.

From a programmer's perspective, the most notable change is support for 
user-defined value types, using a new @Value annotation.  It is also possible
to support third-party value types by registering adapters to mediate between
the value type and Naked Objects.  It is also no longer necessary to 
explicitly call resolve() or objectChanged(); this is done automatically using 
bytecode enhancement (either cglib or javassist).

Another major change is the way in which the framework is distributed.  Previously 
we only provided the framework as a downloadable ZIP that contained all its 
dependencies.  We are now encouraging the use of Maven to build your applications 
and manage its dependencies.  One of the main benefits is that you can now easily 
create Naked Objects prototypes using a Maven archetype, bringing together the 
needed components plus a basic project structure.  (Note that the ZIP with 
dependencies is still available if you want to go that route).

The DND viewer plug-in has been enhanced to provide more views and will be one 
of the  components that will be improved separately over the coming months.

And, as you might expect, there have also been numerous bug fixes ;-)


Other Resources
---------------
V4.0 also sees a number of other resources available.  In particular, there 
are a collection of sister projects in development that extend the capabilities
of Naked Objects.  These include:

 * http://scimpi.org - an alternative, but extensible web-based viewer

 * http://starobjects.org - a collection of projects providing components
   such as JPA/Hibernate persistor, a RESTful viewer, and FitNesse integration.
   (Others such as an Eclipse RCP viewer are in development).

 * http://pragprog.com/titles/dhnako - Dan Haywood's new book (in beta)
   Domain-Driven Design using Naked Objects (which also covers some of the 
   sister projects mentioned above).
   
