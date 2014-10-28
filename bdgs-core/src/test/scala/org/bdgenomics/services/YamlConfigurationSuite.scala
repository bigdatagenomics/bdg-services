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

class YamlConfigurationSuite extends FunSuite {
  test("Can read YAML configuration") {
    val e = Configuration.parse(Thread.currentThread().getContextClassLoader.getResourceAsStream("test_config.yml"))
    assert(e.getPort === 9091)
    assert(e.getTransport === "TFramedTransport")
    assert(e.getProtocol === "TBinaryProtocol")
    assert(e.getServices.size === 2)
    assert(e.getServices.get(0).getClassName === "org.bdgenomics.services.Test")
    assert(e.getServices.get(0).getLocations.size === 2)
    assert(e.getServices.get(0).getLocations.get(0).getName === "test_reads")
    assert(e.getServices.get(0).getLocations.get(0).getLocation === "classpath://reads12.sam")
    assert(e.getServices.get(1).getClassName === "org.bdgenomics.services.Test2")
    assert(e.getServices.get(1).getLocations.size === 0)
  }
}