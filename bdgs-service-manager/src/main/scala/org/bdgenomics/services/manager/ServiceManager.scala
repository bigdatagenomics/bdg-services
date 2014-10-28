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
package org.bdgenomics.services.manager

import java.io.File
import java.net.ServerSocket

import org.apache.thrift.TMultiplexedProcessor
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.server.{ TServer, TThreadPoolServer }
import org.apache.thrift.transport.{ TFramedTransport, TServerSocket }
import org.bdgenomics.adam.util.CredentialsProperties
import org.bdgenomics.services.{ ConfigurableServiceExecutor, Configuration, ServiceContext }

import scala.collection.JavaConversions._

/**
 * @param configuration This is the configuration that the manager is started with
 */
class ServiceManager(private val configuration: Configuration) {
  var server: Option[TServer] = None

  var port: Int = configuration.getPort

  /**
   * @note This is a blocking call, which will not exit until the service is stopped.
   */
  def start() = {
    if (server.isDefined)
      throw new IllegalStateException("Cannot start two servers using the same ServiceManager")
    val multiplexedProcessor = new TMultiplexedProcessor()
    val sparkContext = ServiceContext.createSparkContext(configuration)
    val credentials = Option(configuration.credentials)
      .map(creds => new CredentialsProperties(Option(creds.location).map(new File(_)))
        .awsCredentials(Option(creds.suffix)))
      .getOrElse(new CredentialsProperties(None).awsCredentials())

    configuration.getServices
      .map(serviceConfiguration => {
        val serviceContext = new ServiceContext(
          serviceConfiguration,
          sparkContext,
          credentials)
        (serviceConfiguration.getOverrideName,
          Class.forName(serviceConfiguration.getClassName)
          .newInstance()
          .asInstanceOf[ConfigurableServiceExecutor]
          .configure(serviceContext))
      })
      .foreach {
        case (overrideName, plugin) => multiplexedProcessor.registerProcessor(
          if (overrideName != null) overrideName else plugin.name, plugin.processor)
      }

    val serverSocket = new ServerSocket(port)
    port = serverSocket.getLocalPort

    val s = new TThreadPoolServer(new TThreadPoolServer.Args(new TServerSocket(serverSocket))
      .processor(multiplexedProcessor)
      .protocolFactory(new TBinaryProtocol.Factory())
      .transportFactory(new TFramedTransport.Factory()))
    server = Some(s)

    s.serve()
  }

  def isStarted = server.exists(_.isServing)

  /**
   * @note This must be called from a separate thread than [[start()]], and will cause the thread that [[start()]] was
   *       executing on to return
   */
  def stop() = {
    server.foreach(_.stop())
  }
}