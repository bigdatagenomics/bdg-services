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

import org.bdgenomics.adam.parquet_reimpl.index.{ IDRangeIndexEntry, IndexEntryPredicate, RowGroupIndexEntry }
import parquet.column.ColumnReader
import parquet.filter.{ RecordFilter, UnboundRecordFilter }

class SerializableUnboundRecordPredicateFilter(predicate: RecordPredicate) extends UnboundRecordFilter with Serializable {
  import scala.collection.JavaConversions._

  override def bind(readers: java.lang.Iterable[ColumnReader]): RecordFilter =
    new RecordFilter {
      def isMatch: Boolean = {
        predicate.matches(readers.map(cr => cr.getDescriptor.getPath.toSeq -> cr).toMap)
      }
    }
}

trait RecordPredicate extends Serializable {
  def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean
}

object RangeIndexEntryFilter {

  def referenceNameEquals(targetValue: String): IndexEntryPredicate[IDRangeIndexEntry] =
    new IndexEntryPredicate[IDRangeIndexEntry] {
      override def accepts(entry: IDRangeIndexEntry): Boolean =
        entry.range.referenceName == targetValue
    }

  def startGreaterThan(targetValue: Long): IndexEntryPredicate[IDRangeIndexEntry] =
    new IndexEntryPredicate[IDRangeIndexEntry] {
      override def accepts(entry: IDRangeIndexEntry): Boolean =
        entry.range.start >= targetValue
    }

  def startLessThan(targetValue: Long): IndexEntryPredicate[IDRangeIndexEntry] =
    new IndexEntryPredicate[IDRangeIndexEntry] {
      override def accepts(entry: IDRangeIndexEntry): Boolean =
        entry.range.start <= targetValue
    }

  def endLessThan(targetValue: Long): IndexEntryPredicate[IDRangeIndexEntry] =
    new IndexEntryPredicate[IDRangeIndexEntry] {
      override def accepts(entry: IDRangeIndexEntry): Boolean =
        entry.range.end <= targetValue
    }

  def endGreaterThan(targetValue: Long): IndexEntryPredicate[IDRangeIndexEntry] =
    new IndexEntryPredicate[IDRangeIndexEntry] {
      override def accepts(entry: IDRangeIndexEntry): Boolean =
        entry.range.end >= targetValue
    }
}

object RecordPredicate {

  def and(preds: RecordPredicate*): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        preds.forall(_.matches(columnReaders))
      }
    }
  }
  def or(preds: RecordPredicate*): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        preds.exists(_.matches(columnReaders))
      }
    }
  }

  def not(pred: RecordPredicate): RecordPredicate = {
    new RecordPredicate {
      override def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean =
        !pred.matches(columnReaders)
    }
  }

  def isTrue(column: Seq[String]): RecordPredicate = {
    new RecordPredicate {
      override def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean =
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => reader.getBoolean
        }
    }
  }

  def isFalse(column: Seq[String]): RecordPredicate = {
    new RecordPredicate {
      override def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean =
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => !reader.getBoolean
        }
    }
  }

  def memberStringSet(column: Seq[String], targetSet: Set[String]): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => targetSet.contains(reader.getBinary.toStringUsingUTF8)
        }
      }
    }
  }

  def equalsString(column: Seq[String], value: String): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => reader.getBinary.toStringUsingUTF8 == value
        }
      }
    }
  }

  def equalsLong(column: Seq[String], value: Long): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => reader.getLong == value
        }
      }
    }
  }
  def lessThanLong(column: Seq[String], value: Long): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => reader.getLong < value
        }
      }
    }
  }
  def greaterThanLong(column: Seq[String], value: Long): RecordPredicate = {
    new RecordPredicate {
      def matches(columnReaders: Map[Seq[String], ColumnReader]): Boolean = {
        columnReaders.get(column) match {
          case None         => false
          case Some(reader) => reader.getLong > value
        }
      }
    }
  }
}

class AllPredicate[E <: RowGroupIndexEntry] extends IndexEntryPredicate[E] {
  override def accepts(entry: E): Boolean = true
}
