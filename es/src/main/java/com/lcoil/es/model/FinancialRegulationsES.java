package com.lcoil.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
@Data
@AllArgsConstructor
@NoArgsConstructor// 参数依次数：索引名称，主分片区个数，拷贝分区个数
@Document(indexName = "financialregulations", shards = 1, replicas = 0)
public class FinancialRegulationsES {

    @Id
    private String regulationsId;
   // 需要分词、查询的字段需要加上这个注解　　
   // 字符串类型（text：支持分词，全文检索,支持模糊、精确查询,不支持聚合,排序操作;text类型的最大支持的字符长度无限制,适合大字段存储；)，　　
   // 存储时的分词器、搜索时用的分词器（这里用的都是ik分词器，IK提供了两个分词算法: (ik_smart和ik_max_word )，其中ik_smart为最少切分，ik_max_word为最细粒度划分!）
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String regulationsName;

    private String timeliness;

    private String IssuedNumber;

    private String releaseDate;

    @Field(type = FieldType.Text ,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String textContent;

    private String implementationDate;

    private String file;
}