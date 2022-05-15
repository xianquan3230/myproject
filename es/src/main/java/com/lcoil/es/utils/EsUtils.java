package com.lcoil.es.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;


@Slf4j
public class EsUtils {

    private static RestHighLevelClient restHighLevelClient;


    public void init(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * @description: 创建索引
     * @param: indexName 索引名
     * @return: boolean 返回对象*/
    public static boolean createIndex(String indexName) {
        //返回结果
        boolean exists = true;
        try {
            // 1、创建索引请求
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // 2、客户端执行请求 indexResponse, 请求后获得相应
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            //判断响应对象是否为空
            if (createIndexResponse.equals("") || createIndexResponse != null) {
                exists = false;
            }
        } catch (IOException e) {
            exists = false;
        }
        return exists;
    }

    /**
     * @description: 测试获取索引，只能判断其是否存在
     * @param: indexName  需要判断的对象
     * @return: 执行结果*/
    public static boolean isIndexExists(String indexName) {
        boolean exists = true;
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            exists = false;
        }
        return exists;
    }


    /**
     * @description: 删除索引
     * @param: indexName 需要删除的索引对象
     * @return: 执行结果*/
    public static boolean delIndex(String indexName) {
        boolean exists = true;

        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            exists = delete.isAcknowledged();
        } catch (IOException e) {
            exists = false;
        }

        return exists;
    }

    /**
     * @description: 创建文档
     * @param: indexName  索引名称
     * @param: obj 文档对象
     * @param: id 文档对象id (不可重复)
     * @return: 执行结果*/
    public static boolean addDocument(String indexName, Object obj, String id) {
        boolean exists = true;
        IndexResponse indexResponse = null;

        try {
            // 创建请求
            IndexRequest request = new IndexRequest(indexName);
            // 规则 put /kuang_index/_doc/1
            request.id(id);
            request.timeout(TimeValue.timeValueHours(24));
            // 将我们的数据放入请求 json
            request.source(JSON.toJSONString(obj), XContentType.JSON);
            // 客户端发送请求，获取响应结果
            indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            if (!indexResponse.equals("CREATED")) {//判断响应结果对象是否为CREATED
                exists = false;
            }
        } catch (IOException e) {
            exists = false;
        }
        return exists;
    }

    /**
     * @description: 获取文档，判断是否存在
     * @param: indexName  索引名称
     * @param: id 文档对象id
     * @return: 执行结果*/
    public static boolean isExists(String indexName, String id,String type) {
        boolean exists = true;
        try {
            GetRequest request = new GetRequest(indexName,type,id);
            // 不获取返回的 _source 的上下文了
            request.fetchSourceContext(new FetchSourceContext(false));
            request.storedFields("_none_");
            exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * @description: 根据id获取文档信息
     * @param: indexName  索引名称
     * @param: id 文档对象id
     * @return: 执行结果*/
    public static Map getDocument(String indexName,String type, String id) {
        Map strToMap = null;
        try {
            GetRequest request = new GetRequest(indexName,type, id);
            GetResponse getResponse = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            strToMap = JSONObject.parseObject(getResponse.getSourceAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strToMap;
    }

    /**
     * @description: 更新文档的信息
     * @param: indexName  索引名称
     * @param: obj 文档对象
     * @param: id 文档对象id (不可重复)
     * @return: 执行结果*/
    public static boolean updateDocument(String indexName, Object obj,String type, String id) {
        boolean exists = true;
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName,type,id);
            updateRequest.timeout("1s");
            updateRequest.doc(JSON.toJSONString(obj), XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            if (!updateResponse.status().equals("OK")) {
                exists = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * @description: 删除文档记录
     * @param: indexName  索引名称
     * @param: id 文档对象id (不可重复)
     * @return: 执行结果*/
    public static boolean deleteRequest(String indexName,String type, String id) {
        boolean exists = true;
        try {
            DeleteRequest request = new DeleteRequest(indexName,type,id);
            request.timeout("1s");
            DeleteResponse delete = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            if (!delete.status().equals("OK")) {
                exists = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * @description: 批量插入
     * @param: indexName  索引名称
     * @param: objectArrayList 需要添加的数据
     * @return:*/
    public static boolean bulkRequest(ArrayList<Map<String, Object>> objectArrayList, String indexName, String value) {
        boolean exists = true;
        BulkProcessor bulkProcessor = getBulkProcessor(restHighLevelClient);
        try {
            for (int i = 0; i < objectArrayList.size(); i++) {
                bulkProcessor.add(new IndexRequest(indexName)
                        .id(objectArrayList.get(i).get(value).toString())
                        .source(JSON.toJSONString(objectArrayList.get(i)), XContentType.JSON));
            }
            // 将数据刷新到es, 注意这一步执行后并不会立即生效，取决于bulkProcessor设置的刷新时间
            bulkProcessor.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                boolean terminatedFlag = bulkProcessor.awaitClose(150L, TimeUnit.SECONDS);
                log.info(String.valueOf(terminatedFlag));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return exists;
    }

    /**
     * 创建bulkProcessor并初始化
     *
     * @param client
     * @return
     */
    private static BulkProcessor getBulkProcessor(RestHighLevelClient client) {

        BulkProcessor bulkProcessor = null;
        try {

            BulkProcessor.Listener listener = new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long executionId, BulkRequest request) {
                    log.info("Try to insert data number : " + request.numberOfActions());
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                    log.info("************** Success insert data number : " + request.numberOfActions() + " , id: "
                            + executionId);
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                    log.error("Bulk is unsuccess : " + failure + ", executionId: " + executionId);
                }
            };

            BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) -> client
                    .bulkAsync(request, RequestOptions.DEFAULT, bulkListener);

            BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, listener);
            // 设置最大的上传数量
            builder.setBulkActions(1000);
            builder.setBulkSize(new ByteSizeValue(100L, ByteSizeUnit.MB));
            // 设置最多的线程并发数
            builder.setConcurrentRequests(2);
            builder.setFlushInterval(TimeValue.timeValueSeconds(100L));
            builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
            // 注意点：在这里感觉有点坑，官网样例并没有这一步，而笔者因一时粗心也没注意，在调试时注意看才发现，上面对builder设置的属性没有生效
            bulkProcessor = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                bulkProcessor.awaitClose(100L, TimeUnit.SECONDS);
                client.close();
            } catch (Exception e1) {
                log.error(e1.getMessage());
            }
        }
        return bulkProcessor;
    }

    /**
     * @description: 模糊查询
     * @param: indexName 索引名
     * @param: key 字段名
     * @param: value 查询值
     * @return:*/
    public static List<Map<String, Object>> searchMatch(String indexName, String key, String value) throws IOException {
        List<Map<String, Object>> map = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexName);
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder termQueryBuilder = new MatchQueryBuilder(key, value);
        termQueryBuilder.fuzziness(Fuzziness.AUTO);
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            map.add(documentFields.getSourceAsMap());
        }
        return map;
    }

    /**
     * @description: 精确查询
     * @param: indexName 索引名
     * @param: key 字段名
     * @param: value 查询值
     * @return:*/
    public static List<Map<String, Object>> searchQuery(String indexName, String key, String value) throws IOException {
        List<Map<String, Object>> map = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexName);
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key, value);
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            map.add(documentFields.getSourceAsMap());
        }
        return map;
    }
}