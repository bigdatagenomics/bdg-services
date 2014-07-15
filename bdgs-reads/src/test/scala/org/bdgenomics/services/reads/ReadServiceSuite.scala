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

import org.apache.thrift.protocol.{ TMultiplexedProtocol, TBinaryProtocol }
import org.apache.thrift.transport.{ TFramedTransport, TSocket }
import org.bdgenomics.services.Configuration
import org.bdgenomics.services.manager.ServiceManager
import org.scalatest.{ BeforeAndAfter, FunSuite }

class ReadServiceSuite extends FunSuite with BeforeAndAfter {
  var serviceManager: ServiceManager = null
  var readStore: ReadService.Client = null

  before {
    val configLocation = Thread.currentThread().getContextClassLoader.getResourceAsStream("test_config.yml")
    val config = Configuration.parse(configLocation)

    serviceManager = new ServiceManager(config)

    new Thread(new Runnable() {
      override def run() {
        serviceManager.start()
      }
    }).start()

    while (!serviceManager.isStarted) {
      Thread.`yield`()
    }

    val socket = new TSocket("localhost", serviceManager.port)
    readStore = new ReadService.Client(
      new TMultiplexedProtocol(new TBinaryProtocol(new TFramedTransport(socket)), "reads"))
    socket.open()
  }

  test("Can communicate over Thrift") {
    println("Starting query")
    val samples = readStore.retrieve_samples(0, 1024)
    println("Sent retrieve_samples")
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
    serviceManager.stop()
  }

}
