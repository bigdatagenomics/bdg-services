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

import org.bdgenomics.adam.models.ReferenceRegion
import org.bdgenomics.adam.parquet_reimpl.filters.CombinedFilter
import org.bdgenomics.adam.parquet_reimpl.index.{ IDRangeIndexEntry, IndexEntryPredicate }
import org.bdgenomics.adam.rich.ReferenceMappingContext.AlignmentRecordReferenceMapping
import org.bdgenomics.formats.avro.AlignmentRecord
import org.bdgenomics.services.{ AllPredicate, SerializableUnboundRecordPredicateFilter }
import parquet.filter.UnboundRecordFilter

class ReadServiceFilter(referenceRegion: Option[ReferenceRegion])
    extends CombinedFilter[AlignmentRecord, IDRangeIndexEntry]
    with Serializable {

  override def recordFilter: UnboundRecordFilter = {
    import org.bdgenomics.services.RecordPredicate._

    new SerializableUnboundRecordPredicateFilter(
      and(
        referenceRegion.map(rr => Seq(
          equalsString(Seq("contig", "contigName"), rr.referenceName),
          lessThanLong(Seq("start"), rr.end),
          or(
            // todo: These should really be end
            greaterThanLong(Seq("start"), rr.start),
            equalsLong(Seq("start"), rr.start))))
          .getOrElse(Seq())
          ++
          Seq(
            isTrue(Seq("readMapped")),
            isTrue(Seq("primaryAlignment")),
            or(
              isTrue(Seq("firstOfPair")),
              isFalse(Seq("readPaired")))): _*))
  }

  override def predicate: (AlignmentRecord) => Boolean =
    ar => referenceRegion.exists(_.overlaps(AlignmentRecordReferenceMapping.getReferenceRegion(ar)))

  override def indexPredicate: IndexEntryPredicate[IDRangeIndexEntry] = {
    import org.bdgenomics.adam.parquet_reimpl.index.IndexEntryPredicate.and
    import org.bdgenomics.services.RangeIndexEntryFilter._

    referenceRegion.map(
      rr =>
        and(referenceNameEquals(rr.referenceName),
          startLessThan(rr.end),
          endGreaterThan(rr.start)))
      .getOrElse(new AllPredicate[IDRangeIndexEntry])
  }
}
