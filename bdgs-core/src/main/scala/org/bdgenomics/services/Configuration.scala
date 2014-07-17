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

import java.io.{ InputStream, Reader }
import java.util

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.beans.BeanProperty

object Configuration {
  private def parse(function: Yaml => Object): Configuration = {
    function(new Yaml(new Constructor(classOf[Configuration]))).asInstanceOf[Configuration]
  }

  def parse(reader: Reader): Configuration = {
    parse(_.load(reader))
  }

  def parse(stream: InputStream): Configuration = {
    parse(_.load(stream))
  }
}

object ServiceConfiguration {
  private def parse(function: Yaml => Object): ServiceConfiguration = {
    function(new Yaml(new Constructor(classOf[ServiceConfiguration]))).asInstanceOf[ServiceConfiguration]
  }

  def parse(reader: Reader): ServiceConfiguration = {
    parse(_.load(reader))
  }

  def parse(stream: InputStream): ServiceConfiguration = {
    parse(_.load(stream))
  }

}

class Configuration {
  @BeanProperty var port: Int = -1
  @BeanProperty var transport: String = null
  @BeanProperty var protocol: String = null
  @BeanProperty var appName: String = "service"
  @BeanProperty var sparkMaster: String = "local"
  @BeanProperty var credentials: CredentialsConfiguration = null
  @BeanProperty var services: util.List[ServiceConfiguration] = new util.ArrayList[ServiceConfiguration]()
}

class CredentialsConfiguration {
  @BeanProperty var location: String = null
  @BeanProperty var suffix: String = null
}

class ServiceConfiguration {
  @BeanProperty var className: String = null
  @BeanProperty var overrideName: String = null
  @BeanProperty var locations: util.List[LocationConfiguration] = new util.ArrayList[LocationConfiguration]()
}

class LocationConfiguration {
  @BeanProperty var name: String = null
  @BeanProperty var locationType: String = null
  @BeanProperty var location: String = null
}