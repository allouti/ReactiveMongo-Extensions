// Copyright (C) 2014 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package reactivemongo.extensions.fixtures

import scala.concurrent.{ Future, ExecutionContext }
import reactivemongo.extensions.util.Logger
import reactivemongo.api.DB
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.core.commands.LastError
import reactivemongo.bson.BSONDocument
import play.api.libs.iteratee.Enumerator
import play.api.libs.json.JsObject
import reactivemongo.extensions.json.BSONFormats

class BsonFixtures(db: DB)(implicit ec: ExecutionContext) extends Fixtures[BSONDocument] {

  def map(document: JsObject): BSONDocument = BSONFormats.BSONDocumentFormat.reads(document).get

  def bulkInsert(collectionName: String, enumerator: Enumerator[BSONDocument]): Future[Int] = {
    db.collection[BSONCollection](collectionName).bulkInsert(enumerator)
  }

  def removeAll(collectionName: String): Future[LastError] = {
    db.collection[BSONCollection](collectionName).remove(query = BSONDocument.empty, firstMatchOnly = false)
  }

  def drop(collectionName: String): Future[Boolean] = {
    db.collection[BSONCollection](collectionName).drop()
  }

}

object BsonFixtures {
  def apply(db: DB)(implicit ec: ExecutionContext): BsonFixtures = new BsonFixtures(db)
}
