/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ESTest {


  @Test
  public void testES() throws IOException {
    RestHighLevelClient client = ElasticsearchClientProvider.createClient();
    int pageSize = 10; // 每页的文档数
    int from = 0; // 查询的起始位置

    // 构建查询条件
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery("serviceName", "ZOOKEEPER"))
        .must(QueryBuilders.termQuery("roleName", "ZOOKEEPER_SERVER"))
        .must(QueryBuilders.termQuery("logLevel", "INFO"));

    SearchRequest request = new SearchRequest("filebeat-7.16.3");
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
        .query(boolQuery)
        .from(from)
        .size(pageSize);

    request.source(sourceBuilder);

    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    long totalHits = response.getHits().getTotalHits().value; // 获取总匹配的文档数量
    long totalPages = (totalHits + pageSize - 1) / pageSize; // 计算总页数
    System.out.println("Total Pages: " + totalPages);

    for (SearchHit hit : response.getHits().getHits()) {
      String message = (String) hit.getSourceAsMap().get("message");
      String hostip = (String) hit.getSourceAsMap().get("hostip");
      String bizTime = (String) hit.getSourceAsMap().get("bizTime");

      System.out.println("Message: " + message);
      System.out.println("Host IP: " + hostip);
      System.out.println("Business Time: " + bizTime);
      System.out.println("------------------------");
    }
    client.close();
  }
}
class ElasticsearchClientProvider {

  public static RestHighLevelClient createClient() {
    return new RestHighLevelClient(
        RestClient.builder(
            new HttpHost("192.168.197.150", 9200, "http") // Elasticsearch主机和端口
        )
    );
  }
}