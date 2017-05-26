package zzyq.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import zzyq.bean.Config;

public class ElasticSearchClient {
	private Logger logger = LogManager.getLogger(this.getClass());
	
	private Map<String, Client> clientMap = new ConcurrentHashMap<String, Client>();

	private ElasticSearchClient() {
		init();
	}

	public static final ElasticSearchClient getInstance() {
		return ClientHolder.INSTANCE;
	}

	private static class ClientHolder {
		private static final ElasticSearchClient INSTANCE = new ElasticSearchClient();
	}

	/**
	 * 初始化默认的client
	 */
	public void init() {
		String[] inetAddresses = Config.es_hostList.split(",");
		Settings settings = Settings.settingsBuilder().put("cluster.name", Config.es_clusterName)
				.put("client.transport.sniff", true).build();
		addClient(settings, getAllAddress(inetAddresses));
	}

	/**
	 * 获得所有的地址端口
	 *
	 * @return
	 */
	public List<InetSocketTransportAddress> getAllAddress(String[] ips) {
		List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
		for (String temp : ips) {
			String[] tmpArray = temp.split(":");
			String ip = tmpArray[0];
			int port = Integer.valueOf(tmpArray[1]);
			try {
				addressList.add(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return addressList;
	}

	public Client getClient() {
		return getClient(Config.es_clusterName);
	}

	public Client getClient(String clusterName) {
		return clientMap.get(clusterName);
	}

	public void addClient(Settings setting, List<InetSocketTransportAddress> transportAddress) {
		Client client = TransportClient.builder().settings(setting).build();
		((TransportClient) client).addTransportAddresses(transportAddress.toArray(new InetSocketTransportAddress[transportAddress.size()]));
		clientMap.put(setting.get("cluster.name"), client);
	}
	
	public void createIndex(String... index) {
		if (null == index || index.length == 0) {
			index = new String[] { Config.es_indexName };
		}
		IndicesAdminClient indicesAdminClient = getClient().admin().indices();
		for (String name : index) {
			if (!indicesAdminClient.exists(new IndicesExistsRequest(name)).actionGet().isExists()) {
				indicesAdminClient.create(new CreateIndexRequest(name)).actionGet();
				logger.info(String.format("索引创建成功！indexName:%s", name));
			}
		}
	}
	
	public void mapping(String index, String mapping) {
		try {
			createIndex(index);
			GetMappingsResponse getMappingsResponse = getClient().admin().indices().getMappings(new GetMappingsRequest().indices(index).types(mapping)).actionGet();
			if(getMappingsResponse.mappings().isEmpty()){
				String json = String.format(System.getProperty("user.dir")+"/resource/mapping/%s-mapping.json", mapping.toLowerCase());
				String mappingJson = FileUtils.readFileToString(new File(json), "UTF-8");
				PutMappingRequest mappingRequest = Requests.putMappingRequest(Config.es_indexName).type(mapping).source(mappingJson);
				getClient().admin().indices().putMapping(mappingRequest).actionGet();
				logger.info(String.format("%s的mapping创建成功！mapping:%s", index, mapping));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
