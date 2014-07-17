/**
 * Licensed to Big Data Genomics (BDG) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The BDG licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdgenomics.services

import java.io.File
import org.apache.avro.Schema
import org.apache.avro.generic.IndexedRecord
import org.apache.spark.rdd.RDD
import org.bdgenomics.adam.io._
import org.bdgenomics.adam.parquet_reimpl._
import org.bdgenomics.adam.parquet_reimpl.filters.CombinedFilter
import org.bdgenomics.adam.parquet_reimpl.index.IDRangeIndexEntry
import org.bdgenomics.formats.avro.{ ADAMFlatGenotype, ADAMRecord }

case class TypedLocation(name: String, locationType: String, location: String) {

  val regex = "^([^:]+)://?/?([^/]+)/?(.*)$".r
  val (scheme, bucketName, keyName, fullPath) = findMatch(location.toString)

  def findMatch(expr: String): (String, String, String, String) = {
    val matcher = regex.findFirstMatchIn(expr)
    if (matcher.isDefined) {
      (matcher.get.group(1), matcher.get.group(2), matcher.get.group(3), location.substring(matcher.get.start(2) - 1))
    } else {
      ("file", null, null, expr)
    }
  }

  def loadRDD[T <: IndexedRecord](sc: ServiceContext, proj: Option[Schema] = None, select: Option[CombinedFilter[T, IDRangeIndexEntry]] = None): RDD[T] = {

    locationType match {
      case "reads"             => loadADAMRecordsRDD(sc, proj, select.asInstanceOf[Option[CombinedFilter[ADAMRecord, IDRangeIndexEntry]]]).asInstanceOf[RDD[T]]
      case "genotypes"         => loadADAMGenotypesRDD(sc, proj, select.asInstanceOf[Option[CombinedFilter[ADAMFlatGenotype, IDRangeIndexEntry]]]).asInstanceOf[RDD[T]]
      case "indexed_genotypes" => loadIndexedADAMGenotypesRDD(sc, proj, select.asInstanceOf[Option[CombinedFilter[ADAMFlatGenotype, IDRangeIndexEntry]]]).asInstanceOf[RDD[T]]
    }
  }

  def createFileLocator(sc: ServiceContext): FileLocator =
    scheme match {
      case "s3" | "s3n" => new S3FileLocator(sc.credentials, bucketName, keyName)
      case "file"       => new LocalFileLocator(new File(fullPath))
      case "classpath"  => new ClasspathFileLocator(bucketName)
      case _            => throw new IllegalArgumentException("TypedLocation doesn't support a '%s' scheme".format(scheme))
    }

  def loadIndexedADAMGenotypesRDD(sc: ServiceContext,
                                  proj: Option[Schema] = None,
                                  select: Option[CombinedFilter[ADAMFlatGenotype, IDRangeIndexEntry]] = None): RDD[ADAMFlatGenotype] = {

    val index = createFileLocator(sc)
    val dataRoot = index.parentLocator().get
    new AvroIndexedParquetRDD[ADAMFlatGenotype](sc.sparkContext, select.get, index, dataRoot, proj)
  }

  def loadADAMGenotypesRDD(sc: ServiceContext,
                           proj: Option[Schema] = None,
                           select: Option[CombinedFilter[ADAMFlatGenotype, IDRangeIndexEntry]] = None): RDD[ADAMFlatGenotype] = {

    val loc = createFileLocator(sc)
    val filter = if (select.isDefined) select.get.recordFilter else null
    new AvroParquetRDD[ADAMFlatGenotype](sc.sparkContext, filter, loc, proj)
  }

  def loadADAMRecordsRDD(sc: ServiceContext,
                         proj: Option[Schema] = None,
                         select: Option[CombinedFilter[ADAMRecord, IDRangeIndexEntry]] = None): RDD[ADAMRecord] = {

    val loc = createFileLocator(sc)
    val filter = if (select.isDefined) select.get.recordFilter else null
    new AvroParquetRDD[ADAMRecord](sc.sparkContext, filter, loc, proj)
  }
}

