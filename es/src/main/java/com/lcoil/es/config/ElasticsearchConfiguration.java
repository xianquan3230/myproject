package com.lcoil.es.config;

import com.lcoil.es.utils.EsUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.rest.host}")
    private String host;

    @Value("${spring.elasticsearch.rest.port}")
    private int port;


    @Value("${spring.elasticsearch.rest.username}")
    private String USERNAME;

    @Value("${spring.elasticsearch.rest.password}")
    private String PASSWORD;


    @Bean(destroyMethod = "close", name = "client")
    public RestHighLevelClient restHighLevelClient() {

        //如果没配置密码就可以不用下面这两部
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));

        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "http"))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                });
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        //  这里我写了一个静态的工具类，所以笨笨的写了一个ES的初始化，　　　　
        //  有大佬可以麻烦帮忙指点一下看有什么更好的方案让静态的工具类能拿到注入到spring中的bean
        new EsUtils().init(restHighLevelClient);
        return restHighLevelClient;
    }

}