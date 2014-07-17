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

import org.scalatest.FunSuite

class TypedLocationTestSuite extends FunSuite {

  test("can parse a straightforward path alright") {
    val tl = TypedLocation("name", "location", "/foo/bar")
    assert(tl.scheme === "file")
    assert(tl.fullPath === "/foo/bar")
  }

  test("can parse a classpath:// URI correctly") {
    val tl = TypedLocation("name", "location", "classpath://test_config.yml")
    assert(tl.name === "name")
    assert(tl.locationType === "location")
    assert(tl.location === "classpath://test_config.yml")
    assert(tl.scheme === "classpath")
    assert(tl.bucketName === "test_config.yml")
    assert(tl.keyName === "")
  }

  test("can parse a file:// URI correctly") {
    val tl = TypedLocation("name", "location", "file://path1/path2")
    assert(tl.name === "name")
    assert(tl.locationType === "location")
    assert(tl.location === "file://path1/path2")
    assert(tl.scheme === "file")
    assert(tl.bucketName === "path1")
    assert(tl.keyName === "path2")
    assert(tl.fullPath === "/path1/path2")
  }

  test("can parse an s3:// URI correctly") {
    val tl = TypedLocation("name", "location", "s3://path1/path2")
    assert(tl.name === "name")
    assert(tl.locationType === "location")
    assert(tl.location === "s3://path1/path2")
    assert(tl.scheme === "s3")
    assert(tl.bucketName === "path1")
    assert(tl.keyName === "path2")
  }
}
