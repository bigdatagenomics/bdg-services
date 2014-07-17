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

import com.amazonaws.auth.AWSCredentials
import org.apache.spark.SparkContext
import org.bdgenomics.adam.rdd.ADAMContext

import scala.collection.JavaConversions._

class ServiceContext(val config: ServiceConfiguration,
                     val sparkContext: SparkContext,
                     val credentials: AWSCredentials) {
  def locations: Seq[TypedLocation] = {
    config.getLocations.map(location => TypedLocation(location.name, location.locationType, location.location))
  }
}

object ServiceContext {
  def createSparkContext(configuration: Configuration): SparkContext =
    createSparkContext(configuration.appName, configuration.sparkMaster)

  def createSparkContext(appName: String, sparkMaster: String): SparkContext = {
    ADAMContext.createSparkContext(
      appName,
      sparkMaster,
      null,
      Seq(classOf[scala.math.BigInt].asInstanceOf[Class[Any]],
        classOf[org.slf4j.Logger].asInstanceOf[Class[Any]],
        classOf[akka.AkkaException].asInstanceOf[Class[Any]],
        classOf[org.apache.hadoop.conf.Configuration].asInstanceOf[Class[Any]],
        classOf[org.apache.spark.SparkContext].asInstanceOf[Class[Any]],
        classOf[org.apache.avro.Schema].asInstanceOf[Class[Any]],
        classOf[parquet.avro.AvroParquetInputFormat].asInstanceOf[Class[Any]],
        classOf[parquet.filter.UnboundRecordFilter].asInstanceOf[Class[Any]],
        classOf[parquet.hadoop.ParquetFileReader].asInstanceOf[Class[Any]],
        classOf[parquet.format.CompressionCodec].asInstanceOf[Class[Any]],
        classOf[net.sf.samtools.SAMFileReader].asInstanceOf[Class[Any]],
        classOf[org.codehaus.jackson.JsonFactory].asInstanceOf[Class[Any]],
        classOf[org.codehaus.jackson.map.ObjectMapper].asInstanceOf[Class[Any]],
        classOf[org.broadinstitute.variant.variantcontext.VariantContext].asInstanceOf[Class[Any]],
        classOf[org.bdgenomics.formats.avro.ADAM].asInstanceOf[Class[Any]],
        classOf[org.bdgenomics.adam.rdd.ADAMContext].asInstanceOf[Class[Any]])
        .map(_.getProtectionDomain.getCodeSource.getLocation.getPath)
        .distinct)
  }
}