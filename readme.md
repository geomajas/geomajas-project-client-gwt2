Geomajas GWT Client 2.x
=======================

What is Geomajas
----------------

Geomajas is an Open Source Web Mapping Framework written in Java. It provides both server and client components. This project contains the Client 2.x. It is based upon the GWT technology.

For more details about the project, how to use it, manuals and other information, take a look at the website at http://www.geomajas.org/ .

For commercial support see http://www.geosparc.com/ .

Explanation of all folders in the root
--------------------------------------

* <b>common-gwt</b>: Libraries containing common code used in both Geomajas clients.
* <b>api</b>: The client API
* <b>impl</b>: The implementation of the API.
* <b>server-extension</b>: Extension of the client to include the Geomajas server.
* <b>documentation</b>: The main documentation artifact.
* <b>example-base</b>: Library with showcase layout. It is used by the showcase application.
* <b>example-jar</b>: Library containing samples on the API
* <b>example</b>: Showcase application. It bundles all samples from all plugins.
* <b>plugin</b>: Folder containing all plugins for this client, such as WMS client, editing, GIS widgets, ...

Build Process
-------------

In order to build Geomajas, we recommend using Maven (see http://maven.apache.org/). Following Maven best practices, the pom.xml files do not contain any Maven repositories.

You'll have to add the Geomajas Maven repository (http://maven.geomajas.org/) to your settings.xml file, which can be located in:
 
<pre>~/.m2/settings.xml</pre>

Next go to the root of the source code and run:

<pre>mvn install</pre>
