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
package org.bdgenomics.services.reads

import java.util

import org.apache.spark.SparkContext
import org.bdgenomics.adam.rdd.ADAMContext
import org.bdgenomics.adam.util.CredentialsProperties
import org.bdgenomics.services.{ Configuration, ServiceContext }
import org.scalatest.{ BeforeAndAfter, FunSuite }

class ADAMReadStoreSuite extends FunSuite with BeforeAndAfter {
  var sparkContext: SparkContext = null
  var readStore: ReadService.Iface = null

  before {
    val configLocation = Thread.currentThread().getContextClassLoader.getResourceAsStream("test_config.yml")
    val config = Configuration.parse(configLocation)

    sparkContext = ADAMContext.createSparkContext("ADAMReadStoreSuite", "local")

    readStore = new ADAMReadStore(
      new ServiceContext(
        config.getServices.get(0),
        sparkContext,
        new CredentialsProperties(None).awsCredentials()))

  }

  test("Returns calls from test bam") {
    val samples = readStore.retrieve_samples(0, 1024)
    assert(samples.getSamplesSize === 1)
    assert(samples.getSamples.get(0) === "test_reads")
    val readId = readStore.register_reads_query(new ReadsQuery(util.Arrays.asList("test_reads"))
      .setRange(new RangeQuery("1", 0, 1000000000)))
    assert(readId !== null)
    val reads = readStore.retrieve_reads(readId, 1024)
    assert(reads.get(0).isSetAlignment)
    assert(reads.get(0).isSetRead)
    assert(reads.get(0).isSetSampleId)
  }

  after {
    sparkContext.stop()
  }
}
