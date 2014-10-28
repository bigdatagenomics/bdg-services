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
package org.bdgenomics.services.cli

import java.util
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{ TFramedTransport, TSocket }
import org.bdgenomics.services.reads._
import scala.collection.JavaConversions._

object Main extends App {

  override def main(args: Array[String]) {
    val socket = new TSocket(args(0), 9090)
    val readStore = new ReadService.Client(new TBinaryProtocol(new TFramedTransport(socket)))
    socket.open()

    val samples: SamplesResult = readStore.retrieve_samples(0, 100)

    val query: ReadsQuery = new ReadsQuery(samples.getSamples).setRange(new RangeQuery("11", 0, 100000))
    val queryId = readStore.register_reads_query(query)

    val records: util.List[Record] = readStore.retrieve_reads(queryId, 1000)

    for (record <- records) {
      val align = record.getAlignment
      val read = record.getRead
      println("%s %s:%d\t%s\t%s".format(record.getSampleId,
        align.getReferenceName, align.getStart,
        read.getSequence, read.getQual))
    }
  }
}

trait ReadStoreClient {

  def samples(): Seq[String]
  def cache(sample: String)
  def reads(samples: Seq[String], ref: String, start: Long, end: Long): Seq[Record]
}

class ThriftReadStoreClient(socket: TSocket) extends ReadStoreClient {

  private val readStore = new ReadService.Client(new TBinaryProtocol(new TFramedTransport(socket)))
  socket.open()

  def samples(): Seq[String] = {
    var sampleList: Seq[String] = Seq()
    var start = 0
    var samplesResult = readStore.retrieve_samples(start, 100)
    while (samplesResult.getSamples.size() > 0) {
      start += samplesResult.getSamples.size()
      sampleList ++= samplesResult.getSamples
      samplesResult = readStore.retrieve_samples(start, 100)
    }
    sampleList
  }

  def cache(sample: String): Unit = readStore.cache_sample(sample)

  def reads(samples: Seq[String], referenceName: String, start: Long, end: Long): Seq[Record] = {
    val queryId = readStore.register_reads_query(new ReadsQuery(samples)
      .setRange(new RangeQuery(referenceName, start, end)))
    var readsList: Seq[Record] = Seq()
    var readsResult = readStore.retrieve_reads(queryId, 1024)
    while (readsResult.size() > 0) {
      readsList ++= readsResult
      readsResult = readStore.retrieve_reads(queryId, 1024)
    }
    readsList
  }

}

