This is the Geomajas GWT client 2.x

This project is part of the Geomajas web mapping framework. This is an open source project. For the licensing details, look at LICENSE.txt

For more details about the project, how to use it, manuals and other information,
take a look at the website at http://www.geomajas.org/ .
For commercial support see http://www.geosparc.com/ .

The build process is as follows:
- install maven (see http://maven.apache.org/)
- add the repositories in this folder's settings.xml to your own settings.xml or put this file in the default .m2 location
- run 'mvn install' on the projects you want to build

Explanation of all folders in the root:
* geomajas-client-common-gwt-parent: Libraries containing common code used in both Geomajas clients.
* geomajas-client-gwt2-api: The client API
* geomajas-client-gwt2-impl: The implementation of the API.
* geomajas-client-gwt2-server-extension: Extension of the client to include the Geomajas server.
* geomajas-client-gwt2-documentation: The main documentation artifact.
* geomajas-client-gwt2-example-base: Library with showcase layout.
* geomajas-client-gwt2-example-jar: Library containing showcases on the API
* geomajas-client-gwt2-example: Showcase application.
* geomajas-client-gwt2-plugin-parent: Folder containing all plugins for this client.