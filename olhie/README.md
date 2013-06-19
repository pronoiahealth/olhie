Open Library of Health Information Exchange
============================================================================
Author: John DeStefano

What is it?
-----------

The Open Library of Health Information Exchange project (OLHIE) is developing the requirements and framework for an Open Source Library of HIE assets to include interface code, and related documents (requirements, templates, data sets, technical and user documentation, training materials, etc.), RFPs and contract language, policies, and other source code created for connecting EHRs, laboratories, pharmacies, and other health entities with HIEs and other types of health information infrastructure. The Library will help to accelerate connections between Health Information Exchanges (HIE) by facilitating the reuse of HIE interfaces and other assets with a focus on assets developed with federal or state funding. Reusing existing assets rather than purchasing new ones, or starting from scratch, can reduce the cost of building HIE infrastructure and increases the potential for interoperability.

This project is part of the State Health Policy Consortium project, funded by the Office of the National Coordinator for Health IT (ONC) and managed by its contractor RTI International. The objective of ILP is to create tools that enable HIE entities to identify and download assets, customize or optimize those assets and then contribute that work back to the library. To realize this potential, the project team is charged with facilitating a collaborative and vibrant user community, which will provide support for the business needs of HIEs for years to come.
The first phase of the project was completed successfully and approved by ONC. Phase 2 is now underway. During Phase 2, the team will develop the library itself. It will be designed to foster growth, expansion, and the creation of a larger community of practice.  State and regional HIOs and vendors are invited to participate as stakeholders in this initiative.


System requirements
-------------------

Prototype development is currently being done with GWT, JBoss Errai Project, OrientDB, and Solr. At a future date we will explore using HingX for content management. The application is best run on the JBoss application server. Currently, that is the only deployment platform we support.

Development Set-up (work in progress)
------------------

Download OrientDB version 1.3.0. Unpack as directed in the Orient documentation. Start the server. Start the console app (see documentation). Import the db_test.json.gz file into the server using the console command. 

Download and deploy the JBoss application server, version 7.1.1. You will need to add additional modules to the JBoss modules directory. Consult the JBoss documentation for a more detailed overview. Basically, you need to add a module for the OrientDB and modify the org.javassit module. First, create a directory under modules in the Jboss server directory call <JBoss Server Directory>/modules/com/orienttechnologies/orient/main. In that directory place the OrientDB jars: orient-commons-1.3.0.jar, orientdb-client-1.3.0.jar, orientdb-core-1.3.0.jar, orientdb-distributed-1.3.0.jar, orientdb-enterprise-1.3.0.jar, orientdb-nativeos-1.3.0.jar, orientdb-object-1.3.0.jar, orientdb-server-1.3.0.jar. Add a file called module.xml to the directory. The contents of the file should be:

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="com.orienttechnologies.orient">

    <resources>
        <resource-root path="orient-commons-1.3.0.jar"/>
        <resource-root path="orientdb-client-1.3.0.jar"/>
        <resource-root path="orientdb-core-1.3.0.jar"/>
        <resource-root path="orientdb-distributed-1.3.0.jar"/>
        <resource-root path="orientdb-enterprise-1.3.0.jar"/>
        <resource-root path="orientdb-nativeos-1.3.0.jar"/>
        <resource-root path="orientdb-object-1.3.0.jar"/>
        <resource-root path="orientdb-server-1.3.0.jar"/>
        </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
        <module name="javax.servlet.api" optional="true"/>
        <module name="javax.xml.bind.api"/>
        <module name="org.javassist"/>
    </dependencies>
</module>
```

Next you need to modify the org.javassit module. This is because OrientDB requires a newer version of javassist than JBoss 7.1.1 uses. Newer versions of JBoss don't have this issue. Navigate to the directory <JBoss Server Directory>/modules/org/javassist/main. In that directory place the javassist-3.16.1-GA.jar. This jar can be found via the Maven default repository. Change the module.xml file to pick-up this new jar (from ```<resource-root path="javassist-3.15.0-GA.jar"/>``` to: ```<resource-root path="javassist-3.16.1-GA.jar"/>```). You have globally changed the version of javassist for the server. This is probably not optimal, although for this application I have not encountered any issues.

This link maybe useful as an alternative set-up: https://groups.google.com/forum/#!topic/orient-database/bw8HFdXOcC8.


Building
--------

For Eclipse users, import the project as a maven project using the included pom.xml All dependencies are included in the pom (I think, please let me know if this is not the case.) Development requires the  current GWT plugin for Eclipse. Deploy the built project to the configured JBoss server (as above).

test commit  