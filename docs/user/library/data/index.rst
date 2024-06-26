Main Data API
=============

Supports access to feature information (i.e. vector information) from a range of data sources.
Additional DataStore plugins are available from :doc:`gt-jdbc <../jdbc/index>` for database access.

The DataStore API is all about hoisting data (usually in the form of features) off of external
services, disk files etc... into your application. This is where you can finally start putting
the toolkit to work with real information.

.. figure:: /images/gt-main-data.svg
   
   gt-main module DataStore API
   
The ``gt-main`` module is the foundation for implementing additional :doc:`DataStore <../main/datastore>`
 formats:

* Abstract classes for :doc:`DataStore <../main/datastore>` implementers to start from such
  as **ContentDataStore** and **DirectoryDataStore**

The ``gt-main`` module requires plug ins for different formats to be supplied.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**


.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      internal

.. toctree::
   :maxdepth: 1
   
   datastore
   featuresource
   memory
   export

DataStore plugins:

* :doc:`/extension/app-schema`

.. toctree::
   :maxdepth: 1
   
   shape
   ogr
   pregeneralized
   property
   geopackage
   mongodb
   csv

Unsupported plugins:

.. toctree::
   :maxdepth: 1
   
   kml
   solr
   elasticsearch
   wfs-ng
