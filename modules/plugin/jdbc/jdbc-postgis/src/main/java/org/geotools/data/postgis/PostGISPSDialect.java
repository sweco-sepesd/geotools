/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Set;
import org.geotools.jdbc.ColumnMetadata;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.io.WKBWriter;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

public class PostGISPSDialect extends PreparedStatementSQLDialect {

    private PostGISDialect delegate;

    public PostGISPSDialect(JDBCDataStore store, PostGISDialect delegate) {
        super(store);
        this.delegate = delegate;
        // prepared statements enable native binary transfer, no need to base64 encode
        this.delegate.base64EncodingEnabled = false;
    }

    @Override
    protected void addSupportedHints(Set<Hints.Key> hints) {
        delegate.addSupportedHints(hints);
    }

    @Override
    public boolean isAggregatedSortSupported(String function) {
        return "distinct".equalsIgnoreCase(function);
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        return delegate.includeTable(schemaName, tableName, cx);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException {
        return delegate.decodeGeometryEnvelope(rs, column, cx);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return delegate.decodeGeometryValue(descriptor, rs, column, factory, cx, hints);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return delegate.decodeGeometryValue(descriptor, rs, column, factory, cx, hints);
    }

    @Override
    public void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        delegate.encodeGeometryColumn(gatt, prefix, srid, hints, sql);
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        delegate.encodeGeometryEnvelope(tableName, geometryColumn, sql);
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        delegate.encodePrimaryKey(column, sql);
    }

    @Override
    public Integer getGeometrySRID(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getGeometrySRID(schemaName, tableName, columnName, cx);
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return delegate.getGeometryTypeName(type);
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx) throws SQLException {
        return delegate.getMapping(columnMetaData, cx);
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return delegate.lookupGeneratedValuesPostInsert();
    }

    @Override
    public Object getNextAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getNextAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    @Override
    public Object getLastAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getLastAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx)
            throws SQLException {
        return delegate.getNextSequenceValue(schemaName, sequenceName, cx);
    }

    @Override
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        return delegate.encodeNextSequenceValue(schemaName, sequenceName);
    }

    @Override
    public String getSequenceForColumn(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getSequenceForColumn(schemaName, tableName, columnName, cx);
    }

    public boolean isLooseBBOXEnabled() {
        return delegate.isLooseBBOXEnabled();
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        delegate.postCreateTable(schemaName, featureType, cx);
    }

    @Override
    public void preDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        delegate.preDropTable(schemaName, featureType, cx);
    }

    @Override
    public void postDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        delegate.postDropTable(schemaName, featureType, cx);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        delegate.registerClassToSqlMappings(mappings);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        delegate.registerSqlTypeNameToClassMappings(mappings);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        delegate.registerSqlTypeToSqlTypeNameOverrides(overrides);
    }

    @Override
    public void handleUserDefinedType(
            ResultSet columnMetaData, ColumnMetadata metadata, Connection cx) throws SQLException {
        delegate.handleUserDefinedType(columnMetaData, metadata, cx);
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        delegate.setLooseBBOXEnabled(looseBBOXEnabled);
    }

    public boolean isEncodeBBOXFilterAsEnvelope() {
        return delegate.isEncodeBBOXFilterAsEnvelope();
    }

    public void setEncodeBBOXFilterAsEnvelope(boolean encodeBBOXFilterAsEnvelope) {
        delegate.setEncodeBBOXFilterAsEnvelope(encodeBBOXFilterAsEnvelope);
    }

    @Override
    public void prepareGeometryValue(
            Class<? extends Geometry> gClass,
            int dimension,
            int srid,
            Class binding,
            StringBuffer sql) {
        if (gClass != null) {
            sql.append("ST_GeomFromWKB(?, " + srid + ")");
        } else {
            sql.append("?");
        }
    }

    @Override
    public void setGeometryValue(
            Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
            throws SQLException {
        if (g != null && !g.isEmpty()) {
            if (g instanceof LinearRing) {
                // postgis does not handle linear rings, convert to just a line string
                g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
            }

            byte[] bytes = new WKBWriter(dimension).write(g);
            ps.setBytes(column, bytes);
        } else {
            ps.setNull(column, Types.OTHER, "Geometry");
        }
    }

    @Override
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        PostgisPSFilterToSql fts = new PostgisPSFilterToSql(this);
        fts.setFunctionEncodingEnabled(delegate.isFunctionEncodingEnabled());
        fts.setLooseBBOXEnabled(delegate.isLooseBBOXEnabled());
        fts.setEncodeBBOXFilterAsEnvelope(delegate.isEncodeBBOXFilterAsEnvelope());
        return fts;
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return delegate.isLimitOffsetSupported();
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        delegate.applyLimitOffset(sql, limit, offset);
    }

    @Override
    public int getGeometryDimension(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getGeometryDimension(schemaName, tableName, columnName, cx);
    }

    public boolean isFunctionEncodingEnabled() {
        return delegate.isFunctionEncodingEnabled();
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        delegate.setFunctionEncodingEnabled(functionEncodingEnabled);
    }

    @Override
    protected String getArrayComponentTypeName(AttributeDescriptor att) throws SQLException {
        if (att == null) {
            return null;
        }
        String name = (String) att.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME);
        // in postgresql jdbc the database metadata TYPE_NAME column contains the
        // array "base type" name, prefixed with an underscore
        if (name != null && name.startsWith("_")) {
            return name.substring(1);
        }
        return super.getArrayComponentTypeName(att);
    }

    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql)
            throws IOException {
        delegate.encodeGeometryValue(value, dimension, srid, sql);
    }

    @Override
    public void encodeGeometryColumnSimplified(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        delegate.encodeGeometryColumnSimplified(gatt, prefix, srid, sql, distance);
    }

    @Override
    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        delegate.encodeGeometryColumnGeneralized(gatt, prefix, srid, sql, distance);
    }

    PostGISDialect getDelegate() {
        return delegate;
    }

    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        delegate.initializeConnection(cx);
    }

    @Override
    public boolean canSimplifyPoints() {
        return delegate.canSimplifyPoints();
    }

    @Override
    public boolean canGroupOnGeometry() {
        return delegate.canGroupOnGeometry();
    }

    @Override
    public int getDefaultVarcharSize() {
        return delegate.getDefaultVarcharSize();
    }
}
