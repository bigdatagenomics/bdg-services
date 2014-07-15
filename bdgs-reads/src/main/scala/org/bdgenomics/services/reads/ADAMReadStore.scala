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

import org.apache.spark.rdd.RDD
import org.bdgenomics.adam.models.ReferenceRegion
import org.bdgenomics.adam.projections.AlignmentRecordField._
import org.bdgenomics.adam.projections.Projection
import org.bdgenomics.adam.rdd.ADAMContext._
import org.bdgenomics.formats.avro.AlignmentRecord
import org.bdgenomics.services.ServiceContext

class ADAMReadStore(@transient sc: ServiceContext) extends AbstractReadService with Serializable {

  implicit def rangeToReferenceRegion(range: RangeQuery): ReferenceRegion =
    new ReferenceRegion(range.getReferenceName, range.getStart, range.getStop)

  type RDDMap[T] = Map[String, RDD[T]]

  @transient
  val proj = Projection(contig, sequence, qual, cigar, start, end, readMapped, primaryAlignment, readPaired, firstOfPair)

  def query(readsQuery: ReadsQuery): Option[Iterable[Record]] = {
    val sampleLocations = sc.locations.filter(loc => readsQuery.getSamples.contains(loc.name))

    if (sampleLocations.isEmpty) {
      return None
    }

    val filter = new ReadServiceFilter(if (readsQuery.isSetRange) Some(readsQuery.getRange) else None)

    val allRecords = sampleLocations.flatMap(loc => loc.loadRDD[AlignmentRecord](sc, Some(proj), Some(filter))
      .map(ar =>
        new Record(loc.name)
          .setAlignment(new Alignment(ar.getContig.getContigName, ar.getStart, ar.getEnd - ar.getStart, ar.getCigar))
          .setRead(new Read(ar.getSequence, ar.getQual)))
      .collect())

    Some(allRecords)
  }

  def samples: Seq[String] = {
    sc.locations.map(_.name)
  }
}