package com.github.mmolimar.ksql.jdbc.resultset

import java.sql.{ResultSetMetaData, SQLFeatureNotSupportedException, Types}

import com.github.mmolimar.ksql.jdbc.HeaderField
import com.github.mmolimar.ksql.jdbc.utils.TestUtils._
import io.confluent.ksql.schema.ksql.{SqlBaseType => KsqlType}
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class KsqlResultSetMetaDataSpec extends AnyWordSpec with Matchers with MockFactory with OneInstancePerTest {

  "A KsqlResultSetMetaData" when {

    "validating specs" should {

      val resultSet = new KsqlResultSetMetaData(
        List(
          HeaderField("field1", Types.INTEGER, 8),
          HeaderField("field2", Types.BIGINT, 16),
          HeaderField("field3", Types.DOUBLE, 32),
          HeaderField("field4", Types.DECIMAL, 32),
          HeaderField("field5", Types.BOOLEAN, 5),
          HeaderField("field6", Types.VARCHAR, 128),
          HeaderField("field7", Types.JAVA_OBJECT, 255),
          HeaderField("field8", Types.ARRAY, 255),
          HeaderField("field9", Types.STRUCT, 512),
          HeaderField("field10", -999, 9)
        ))

      "throw not supported exception if not supported" in {

        val methods = implementedMethods[KsqlResultSetMetaData]
        reflectMethods[KsqlResultSetMetaData](methods, implemented = false, resultSet)
          .foreach(method => {
            assertThrows[SQLFeatureNotSupportedException] {
              method()
            }
          })
      }

      "work if implemented" in {

        resultSet.getColumnLabel(3) should be("FIELD3")
        resultSet.getColumnName(3) should be("field3")
        resultSet.getColumnTypeName(3) should be("DOUBLE")

        resultSet.getColumnClassName(1) should be("java.lang.Integer")
        resultSet.getColumnType(1) should be(java.sql.Types.INTEGER)
        resultSet.getColumnTypeName(1) should be(KsqlType.INTEGER.name)
        resultSet.getColumnDisplaySize(1) should be(8)

        resultSet.getColumnClassName(2) should be("java.lang.Long")
        resultSet.getColumnType(2) should be(java.sql.Types.BIGINT)
        resultSet.getColumnTypeName(2) should be(KsqlType.BIGINT.name)
        resultSet.getColumnDisplaySize(2) should be(16)

        resultSet.getColumnClassName(3) should be("java.lang.Double")
        resultSet.getColumnType(3) should be(java.sql.Types.DOUBLE)
        resultSet.getColumnTypeName(3) should be(KsqlType.DOUBLE.name)
        resultSet.getColumnDisplaySize(3) should be(32)

        resultSet.getColumnClassName(4) should be("java.math.BigDecimal")
        resultSet.getColumnType(4) should be(java.sql.Types.DECIMAL)
        resultSet.getColumnTypeName(4) should be(KsqlType.DECIMAL.name)
        resultSet.getColumnDisplaySize(4) should be(32)

        resultSet.getColumnClassName(5) should be("java.lang.Boolean")
        resultSet.getColumnType(5) should be(java.sql.Types.BOOLEAN)
        resultSet.getColumnTypeName(5) should be(KsqlType.BOOLEAN.name)
        resultSet.getColumnDisplaySize(5) should be(5)

        resultSet.getColumnClassName(6) should be("java.lang.String")
        resultSet.getColumnType(6) should be(java.sql.Types.VARCHAR)
        resultSet.getColumnTypeName(6) should be(KsqlType.STRING.name)
        resultSet.getColumnDisplaySize(6) should be(128)

        resultSet.getColumnClassName(7) should be("java.util.Map")
        resultSet.getColumnType(7) should be(java.sql.Types.JAVA_OBJECT)
        resultSet.getColumnTypeName(7) should be(KsqlType.MAP.name)
        resultSet.getColumnDisplaySize(7) should be(255)

        resultSet.getColumnClassName(8) should be("java.sql.Array")
        resultSet.getColumnType(8) should be(java.sql.Types.ARRAY)
        resultSet.getColumnTypeName(8) should be(KsqlType.ARRAY.name)
        resultSet.getColumnDisplaySize(8) should be(255)

        resultSet.getColumnClassName(9) should be("java.sql.Struct")
        resultSet.getColumnType(9) should be(java.sql.Types.STRUCT)
        resultSet.getColumnTypeName(9) should be(KsqlType.STRUCT.name)
        resultSet.getColumnDisplaySize(9) should be(512)

        resultSet.getColumnClassName(10) should be("java.lang.String")
        resultSet.getColumnType(10) should be(-999)
        resultSet.getColumnTypeName(10) should be(KsqlType.STRING.name)
        resultSet.getColumnDisplaySize(10) should be(9)

        resultSet.getColumnType(3) should be(java.sql.Types.DOUBLE)
        resultSet.getColumnCount should be(10)
        resultSet.getPrecision(3) should be(-1)
        resultSet.getPrecision(2) should be(0)
        resultSet.getScale(3) should be(-1)
        resultSet.getScale(4) should be(0)

        resultSet.isCaseSensitive(2) should be(false)
        resultSet.isCaseSensitive(6) should be(true)
        resultSet.isNullable(1) should be(ResultSetMetaData.columnNullableUnknown)
        resultSet.isCurrency(6) should be(false)
        resultSet.isAutoIncrement(6) should be(false)
        resultSet.isSearchable(6) should be(true)
        resultSet.isReadOnly(6) should be(false)
        resultSet.isWritable(6) should be(true)
        resultSet.isDefinitelyWritable(6) should be(true)
        resultSet.isSigned(2) should be(true)
        resultSet.isSigned(6) should be(false)
      }
    }
  }

  "A ResultSetMetaDataNotSupported" when {

    "validating specs" should {

      "throw not supported exception if not supported" in {

        val resultSet = new ResultSetMetaDataNotSupported
        reflectMethods[ResultSetMetaDataNotSupported](Seq.empty, implemented = false, resultSet)
          .foreach(method => {
            assertThrows[SQLFeatureNotSupportedException] {
              method()
            }
          })
      }
    }
  }
}
