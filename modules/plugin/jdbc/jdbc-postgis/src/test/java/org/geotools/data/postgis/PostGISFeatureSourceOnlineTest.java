/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.postgis;

import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.spatial.Intersects;

public class PostGISFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    public PostGISFeatureSourceOnlineTest() {
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISTestSetup();
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
    }

    public void testBBOXOverlapsEncoding() throws Exception {
        // enable bbox envelope encoding
        ((PostGISDialect) dataStore.getSQLDialect()).setEncodeBBOXFilterAsEnvelope(true);

        GeometryFactory gf = dataStore.getGeometryFactory();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Intersects filter =
                ff.intersects(
                        ff.property("geometry"),
                        ff.literal(
                                gf.createPolygon(
                                        gf.createLinearRing(
                                                new Coordinate[] {
                                                    new Coordinate(0, 0),
                                                    new Coordinate(0, 2),
                                                    new Coordinate(2, 2),
                                                    new Coordinate(2, 0),
                                                    new Coordinate(0, 0)
                                                }))));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    public void testEstimatedBounds() throws Exception {
        // enable fast bbox
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    public void testEstimatedBoundsWithQuery() throws Exception {
        // enable fast bbox
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(1l, Math.round(bounds.getMinX()));
        assertEquals(1l, Math.round(bounds.getMinY()));
        assertEquals(1l, Math.round(bounds.getMaxX()));
        assertEquals(1l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    public void testEstimatedBoundsWithLimit() throws Exception {
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        super.testBoundsWithLimit();
    }

    public void testEstimatedBoundsWithOffset() throws Exception {
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        super.testBoundsWithOffset();
    }

    public void testSridFirstGeometry() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname("ft3"));
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertTrue(areCRSEqual(decodeEPSG(4326), gd.getCoordinateReferenceSystem()));
    }

    public void testCrsIsLongitudeFirst() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname("ft3"));
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertTrue(areCRSEqual(decodeEPSG(4326), gd.getCoordinateReferenceSystem()));
        // an explicit check to ensure we have a longitude first (XY) axis order.
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(gd.getCoordinateReferenceSystem()));
    }
}
