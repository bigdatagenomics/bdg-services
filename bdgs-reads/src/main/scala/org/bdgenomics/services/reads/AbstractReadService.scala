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

import java.nio.ByteBuffer
import java.util.UUID
import scala.collection.JavaConversions._
import scala.collection.mutable

abstract class AbstractReadService extends ReadService.Iface {

  private val idToIterable: mutable.Map[UUID, Iterable[Record]] = mutable.HashMap()

  override def cache_sample(sampleId: String) {}

  override def retrieve_reads(requestId: ByteBuffer, count: Int): java.util.List[Record] = {
    if (requestId.remaining() < 16)
      throw new InvalidRequestException("Invalid Request ID: incomplete bytes sent")

    val uuid = new UUID(requestId.getLong, requestId.getLong)
    val iterable = idToIterable.getOrElse(
      uuid,
      throw new InvalidRequestException("Invalid Request ID: not found on this machine"))
    iterable.take(count).toList
  }

  override def register_reads_query(readsQuery: ReadsQuery): ByteBuffer = {
    val result = UUID.randomUUID()
    query(readsQuery) match {
      case None    => throw new InvalidRequestException("SampleID not found")
      case Some(t) => idToIterable.put(result, t)
    }

    val byteBuffer = ByteBuffer.allocate(16)
    byteBuffer.putLong(result.getMostSignificantBits)
      .putLong(result.getLeastSignificantBits)
      .position(0)
    byteBuffer
  }

  def query(readsQuery: ReadsQuery): Option[Iterable[Record]]
  def samples: Seq[String]

  override def retrieve_samples(start: Int, count: Int): SamplesResult =
    new SamplesResult(samples.drop(start).take(count)).setNext_start(start + count)
}
