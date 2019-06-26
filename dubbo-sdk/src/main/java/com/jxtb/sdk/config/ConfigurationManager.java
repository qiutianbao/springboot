package com.jxtb.sdk.config;

import com.jxtb.sdk.Filter;
import com.jxtb.sdk.RequestProcess;
import com.jxtb.sdk.core.DefaultPipeline;
import com.jxtb.sdk.core.DefaultRequestProcess;
import com.jxtb.sdk.core.DefaultThreadFactory;
import com.jxtb.sdk.core.Pipeline;
import com.jxtb.sdk.filters.HeadFilter;
import com.jxtb.sdk.filters.ServiceControllerFilter;
import com.jxtb.sdk.filters.TailFilter;
import com.jxtb.sdk.model.ServiceConfigure;
import com.jxtb.sdk.util.ClassLoaderUtil;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dom4j.Document;
import org.xml.sax.InputSource;

/**
 * Created by jxtb on 2019/6/14.
 */
public class ConfigurationManager {

    private static final String SERVICE_MAPPING_NAME = "service-mapping.xml";
    private static ConfigurationManager instance = new ConfigurationManager();
    private ExecutorService executors;

    private ConfigurationManager(){
        super();
        executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4, new DefaultThreadFactory("gateway-pool"));
    }

    public static ConfigurationManager getInstance(){
        return instance;
    }

    public RequestProcess config() throws Exception{
        Resource resource = getResource();
        Document document = createDocument(new InputSource(resource.getInputStream()));
        Map<String, ServiceConfigure> mapping = parse(document);
        RequestProcess process = createRequestProcess(mapping);
        process.setExecutorService(executors);
        return process;
    }

    private RequestProcess createRequestProcess(Map<String, ServiceConfigure> mapping) throws IllegalAccessException, InstantiationException {
        Map<String, Pipeline> mappingMap = createKeyMappingPipeline(mapping);
        DefaultRequestProcess requestProcess = new DefaultRequestProcess();
        requestProcess.setTxnMapping(Collections.unmodifiableMap(mappingMap));
        return requestProcess;
    }

    private Map<String, Pipeline> createKeyMappingPipeline(Map<String, ServiceConfigure> mapping) throws InstantiationException, IllegalAccessException {
        Map<String, Pipeline> txnMapping = new HashMap<>();
        Set<Map.Entry<String, ServiceConfigure>> sets = mapping.entrySet();
        Iterator<Map.Entry<String, ServiceConfigure>> iter = sets.iterator();
        Pipeline pipeline;
        while (iter.hasNext()){
            Map.Entry<String, ServiceConfigure> entry = iter.next();
            ServiceConfigure serviceBean = entry.getValue();
            pipeline = new DefaultPipeline(serviceBean);
            txnMapping.putIfAbsent(entry.getKey(), pipeline);

            Filter headFilter = new HeadFilter();
            headFilter.setPipeline(pipeline);
            pipeline.addFilter(headFilter);

            Filter serviceFilter = new ServiceControllerFilter();
            serviceFilter.setPipeline(pipeline);
            pipeline.addFilter(serviceFilter);

            headFilter.setNext(serviceFilter);

            Filter last = buildFilter(pipeline, serviceBean);

            Filter tailFilter = new TailFilter();
            tailFilter.setPipeline(pipeline);
            last.setNext(tailFilter);

            pipeline.addFilter(tailFilter);
        }
        return txnMapping;
    }

    private Filter buildFilter(Pipeline pipeline, ServiceConfigure serviceBean) throws IllegalAccessException, InstantiationException {
        List<String> filterDef = serviceBean.getFilterDefs();
        Filter before = pipeline.getFirst().getNext();
        for(int i = 0,len = filterDef.size(); i < len; i ++){
            String filterNm = filterDef.get(i);
            Class<?> clazz = ClassLoaderUtil.loadClass(filterNm.trim());
            Filter filter = (Filter)clazz.newInstance();
            filter.setPipeline(pipeline);
            if(before != null){
                before.setNext(filter);
            }
            before = filter;
            pipeline.addFilter(filter);
        }
        return before;
    }

    private Resource getResource(){
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(SERVICE_MAPPING_NAME);
        return resource;
    }

    private Document createDocument(InputSource inputSource) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputSource);
        return document;
    }

    private Map<String,ServiceConfigure> parse(Document document) {
        Map<String,ServiceConfigure> mapping = new HashMap<>();
        Element root = document.getRootElement();
        List<Element> lists = root.elements("service");
        Iterator<Element> itr = lists.iterator();
        Element element;
        ServiceConfigure serviceBean;
        while (itr.hasNext()){
            element = itr.next();
            serviceBean = new ServiceConfigure();
            mapping.put(element.attributeValue("id"),serviceBean);

            serviceBean.setId(element.attributeValue("id"));
            serviceBean.setClassName(element.element("class-name").getText());
            serviceBean.setMethodName(element.element("method-name").getText());
            serviceBean.setKeyPropertyName(element.element("key-property-name").getText());
            serviceBean.setKeyPropertyType(element.element("key-property-type").getText());
            serviceBean.setIsCustId(element.element("isCustId").getText());
            serviceBean.setSystemType(element.element("system-name").getText());
            serviceBean.setRequestProtocol(element.element("request-protocol").getText());

            Element filterDef = element.element("filter-def");
            if(filterDef == null){
                serviceBean.setFilterDefs(new ArrayList<>());
                continue;
            }

            Element defList = filterDef.element("list");
            if(defList == null){
                serviceBean.setFilterDefs(new ArrayList<>());
                continue;
            }

            List<Element> values = defList.elements("value");
            List<String> filterDefs = new ArrayList<>();
            for(Iterator<Element> itrc = values.iterator(); itrc.hasNext();){
                filterDefs.add(itrc.next().getText());
            }
            serviceBean.setFilterDefs(filterDefs);
        }
        return mapping;
    }

}
