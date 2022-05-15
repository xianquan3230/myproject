package com.lcoil.es.controller;


import com.lcoil.es.model.FinancialRegulationsES;
import com.lcoil.es.utils.EsUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/financialRegulationsData")
public class FinancialRegulationsDataController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    /**
     * 根据名称进行查询
     */
    @RequestMapping(value = "/getByName", method = RequestMethod.GET)
    @ResponseBody
    public List<FinancialRegulationsES> getByName(@RequestParam String name) throws IOException {
        //根据一个值查询多个字段  并高亮显示  这里的查询是取并集，即多个字段只需要有一个字段满足即可
        //需要查询的字段
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("regulationsName", name));
        //构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                //设置查询条件
                .withHighlightFields(new HighlightBuilder.Field("regulationsName"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                //设置分页查询
                .withPageable(PageRequest.of(0, 10))
                .build();
        SearchHits<FinancialRegulationsES> search = elasticsearchTemplate.search(searchQuery, FinancialRegulationsES.class);
        //得到查询返回的内容
        List<SearchHit<FinancialRegulationsES>> searchHits = search.getSearchHits();
        //设置一个最后需要返回的实体类集合
        List<FinancialRegulationsES> users = new ArrayList<>();
        //遍历返回的内容进行处理
        for (SearchHit<FinancialRegulationsES> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setTextContent(highlightFields.get("regulationsName") == null ? searchHit.getContent().getRegulationsName() : highlightFields.get("regulationsName").get(0));
            //放到实体类中
            users.add(searchHit.getContent());
        }
        return users;
    }

    /**
     * 添加数据
     */
    @RequestMapping(value = "/addAll", method = RequestMethod.GET)
    @ResponseBody
    public String addAll() {
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("regulationsId", "1");
        map.put("regulationsName", "中国银保监会消费者权益保护局关于安心财险、轻松保经纪、津投经纪、保多多经纪侵害消费者权益案例的通报");
        map.put("timeliness", "现行有效");
        map.put("IssuedNumber", "银保监消保发〔2020〕14号");
        map.put("releaseDate", "2020-12-02");
        map.put("textContent", "中国银保监会消费者权益保护局关于安心财险、轻松保经纪、津投经纪、保多多经纪侵害消费者权益案例的通报\n" +
                "\n" +
                "\n" +
                "中国银保监会消费者权益保护局关于安心财险、轻松保经纪、津投经纪、保多多经纪侵害消费者权益案例的通报\n" +
                "银保监消保发〔2020〕14号\n" +
                "各银保监局，各政策性银行、大型银行、股份制银行，外资银行，各保险集团（控股）公司、保险公司，各会管单位：\n" +
                "为践行以人民为中心的发展思想，落实依法监管理念，切实维护银行保险消费者合法权益，我会对安心财产保险有限责任公司（以下简称安心财险）开展了专项检查，并根据检查线索，对广东轻松保保险经纪有限公司（原名广东宏广安保险经纪有限公司，以下简称轻松保经纪）、天津津投保险经纪有限公司（以下简称津投经纪）、保多多保险经纪有限公司（以下简称保多多经纪）开展了延伸检查。\n" +
                "检查发现，上述机构在宣传销售短期健康险产品中，存在“首月0元”“首月0.1元”等不实宣传（实际是将首月保费均摊至后期保费）,或首月多收保费等问题。上述行为涉嫌违反《保险法》中“未按照规定使用经批准或者备案的保险条款、保险费率”“欺骗投保人”等相关规定。现将有关问题通报如下： \n" +
                "一、欺骗投保人问题\n" +
                "（一）安心财险\n" +
                "经查，2019年1月至6月，安心财险通过轻松保经纪微信平台公众号“轻松保官方”销售“安享一生尊享版”产品时，宣传页面显示“首月0元”“限时特惠 首月立减**元”等内容，实际是首月不收取保费，将全年应交保费均摊至后11个月，消费者并未得到保费优惠。涉及保单16879笔，保费收入396.34万元。\n" +
                "（二）轻松保经纪（第三方网络平台为轻松保）\n" +
                "经查，2019年4月至10月，轻松保经纪在微信平台公众号“轻松保官方”销售众惠财产相互保险社“年轻保·600万医疗保障”产品时，销售页面显示“首月0.1元”“首月3元”“会员日补贴”等内容，实际是将全年应交保费扣除首月0.1元或3元的保费后，将剩余保费均摊至后11个月，消费者并未得到保费优惠。涉及保单377489笔，保费收入5188.97万元。\n" +
                "上述通过“限时特惠”“会员日补贴”等宣传，以“零首付”等方式，给投保人优惠（豁免或减少）应交保费错觉、诱导投保人购买保险的行为，属于虚假宣传、欺骗投保人。\n" +
                "二、未按照规定使用经批准或者备案的保险条款、保险费率问题\n" +
                "（一）津投经纪（第三方网络平台为京东）\n" +
                "经查，2018年10月至2019年6月，津投经纪在京东金融APP销售华泰财产保险有限公司“京英百万医疗险(福利版)”产品时，宣传页面显示“首月1元”等内容，实际是将首月应交的其余保费均摊到剩余的11期保费中收取，涉及保单16874笔，保费收入417.72万元。\n" +
                "2019年1月至2019年6月，津投经纪在京东金融APP销售华泰财产保险有限公司“京享重疾轻症险(福利版)”时，宣传页面显示“首月1元”等内容，实际是将首月应交的其余保费均摊到剩余11期保费中收取，涉及保单3601笔，保费收入30.74万元。\n" +
                "（二）保多多经纪（第三方网络平台为水滴）\n" +
                "经查，2019年3月至2019年6月，保多多经纪在微信平台公众号及“水滴保险商城”APP销售太平财产保险有限公司“太平综合医疗保险”产品时，首期保费按“首月3元”活动收取，但该产品在银保监会报备的条款费率表中仅有“按月缴费（首月投保0元，其余分11期支付）”描述。该行为涉及保单1547267笔，保费12682.91万元。\n" +
                "安心财险、轻松保经纪、津投经纪、保多多经纪等保险机构的上述行为，严重侵害了消费者的知情权、公平交易权等基本权利，损害了消费者的合法权益。我局将严格依法依规进行处理。各银行保险机构要引起警示，围绕营销宣传、产品销售等方面侵害消费者权益乱象开展自查自纠，严格按照相关法律法规和监管规定，依法、合规开展经营活动，切实保护消费者合法权益。\n" +
                "中国银保监会消费者权益保护局\n" +
                "2020年12月2日\n" +
                "\n");
        map.put("implementationDate", "2020-12-02");
        content.add(map);
        map = new HashMap<>();
        map.put("regulationsId", "2");
        map.put("regulationsName", "全国人民代表大会常务委员会关于修改《中华人民共和国个人所得税法》的决定(2011)");
        map.put("timeliness", "现行有效");
        map.put("IssuedNumber", "中华人民共和国主席令第48号");
        map.put("releaseDate", "2011-06-30");
        map.put("textContent", "全国人民代表大会常务委员会关于修改《中华人民共和国个人所得税法》的决定(2011)\n" +
                "中华人民共和国主席令\n" +
                "（第四十八号） \n" +
                "　　《全国人民代表大会常务委员会关于修改＜中华人民共和国个人所得税法＞的决定》已由中华人民共和国第十一届全国人民代表大会常务委员会第二十一次会议于2011年6月30日通过，现予公布，自2011年9月1日起施行。 \n" +
                "　　中华人民共和国主席　***\n" +
                "2011年6月30日\n" +
                "全国人民代表大会常务委员会关于修改《中华人民共和国个人所得税法》的决定\n" +
                "（2011年6月30日第十一届全国人民代表大会常务委员会第二十一次会议通过）\n" +
                "\n" +
                "　　第十一届全国人民代表大会常务委员会第二十一次会议决定对《中华人民共和国个人所得税法》作如下修改：\n" +
                "　　\n" +
                "　　一、第三条第一项修改为：“工资、薪金所得，适用超额累进税率，税率为百分之三至百分之四十五（税率表附后）。”\n" +
                "　　二、第六条第一款第一项修改为：“工资、薪金所得，以每月收入额减除费用三千五百元后的余额，为应纳税所得额。”\n" +
                "　　三、第九条中的“七日内”修改为“十五日内”。\n" +
                "　　四、个人所得税税率表一（工资、薪金所得适用）修改为：\n" +
                "　　级数 全月应纳税所得额　　　税率（%）\n" +
                "\n" +
                "　　1 不超过1500元的 　　　　　　3\n" +
                "\n" +
                "　　2 超过1500元至4500元的部分　 10\n" +
                "\n" +
                "　　3 超过4500元至9000元的部分　 20\n" +
                "\n" +
                "　　4 超过9000元至35000元的部分　25\n" +
                "\n" +
                "　　5 超过35000元至55000元的部分 30\n" +
                "\n" +
                "　　6 超过55000元至80000元的部分 35\n" +
                "\n" +
                "　　7 超过80000元的部分　　　　　45\n" +
                "　　（注：本表所称全月应纳税所得额是指依照本法第六条的规定，以每月收入额减除费用三千五百元以及附加减除费用后的余额。）\n" +
                "　　五、个人所得税税率表二（个体工商户的生产、经营所得和对企事业单位的承包经营、承租经营所得适用）修改为：\n" +
                "　　级数 全年应纳税所得额　　 税率（%）\n" +
                "\n" +
                "　　1 不超过15000元的 　　　　　　5\n" +
                "\n" +
                "　　2 超过15000元至30000元的部分　10\n" +
                "\n" +
                "　　3 超过30000元至60000元的部分　20\n" +
                "\n" +
                "　　4 超过60000元至100000元的部分 30\n" +
                "\n" +
                "　　5 超过100000元的部分　　　　　35\n" +
                "　　（注：本表所称全年应纳税所得额是指依照本法第六条的规定，以每一纳税年度的收入总额减除成本、费用以及损失后的余额。）\n" +
                "　　本决定自2011年9月1日起施行。\n" +
                "　　《中华人民共和国个人所得税法》根据本决定作相应修改，重新公布。\n");
        map.put("implementationDate", "2011-09-01");
        content.add(map);
        EsUtils.bulkRequest(new ArrayList<>(content), "financialregulations", "regulationsId");
        return "ok";
    }

    /**
     * 添加索引
     */
    @RequestMapping(value = "/addIndex", method = RequestMethod.GET)
    @ResponseBody
    public String addIndex() throws IOException {
        IndexOperations ops = elasticsearchTemplate.indexOps(FinancialRegulationsES.class);
        if (!ops.exists()) {
            ops.create();
            ops.refresh();
            ops.putMapping(ops.createMapping());
        }
        return "ok";
    }
}