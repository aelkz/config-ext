package org.microprofileext.config.source.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.microprofileext.config.source.base.EnabledConfigSource;

/**
 * Etcd config source
 * @author <a href="mailto:phillip.kruger@phillip-kruger.com">Phillip Kruger</a>
 */
@Log
public class EtcdConfigSource extends EnabledConfigSource {
    
    private static final String NAME = "EtcdConfigSource";

    private static final String KEY_PREFIX = "configsource.etcd.";
    
    private static final String KEY_SCHEME = KEY_PREFIX + "scheme";
    private static final String DEFAULT_SCHEME = "http";

    private static final String KEY_HOST = KEY_PREFIX + "host";
    private static final String DEFAULT_HOST = "localhost";

    private static final String KEY_PORT = KEY_PREFIX + "port";
    private static final String DEFAULT_PORT = "2379";

    private Client client = null;

    public EtcdConfigSource(){
        super.initOrdinal(320);
    }
    
    @Override
    public Map<String, String> getPropertiesIfEnabled() {
        Map<String,String> m = new HashMap<>();
        
        ByteSequence bsKey = ByteSequence.fromString("");

        CompletableFuture<GetResponse> getFuture = getClient().getKVClient().get(bsKey);
        try {
            GetResponse response = getFuture.get();
            List<KeyValue> kvs = response.getKvs();

            for(KeyValue kv:kvs){
                String key = kv.getKey().toStringUtf8();
                String value = kv.getValue().toStringUtf8();
                m.put(key, value);
            }
        } catch (InterruptedException | ExecutionException ex) {
            log.log(Level.FINEST, "Can not get all config keys and values from etcd Config source: {1}", new Object[]{ex.getMessage()});
        }
        
        return m;
    }

    @Override
    public String getValue(String key) {
        if (key.startsWith(KEY_PREFIX)) {
            // in case we are about to configure ourselves we simply ignore that key
            return null;
        }
        if(super.isEnabled()){
            ByteSequence bsKey = ByteSequence.fromString(key);
            CompletableFuture<GetResponse> getFuture = getClient().getKVClient().get(bsKey);
            try {
                GetResponse response = getFuture.get();
                String value = toString(response);
                return value;
            } catch (InterruptedException | ExecutionException ex) {
                log.log(Level.FINEST, "Can not get config value for [{0}] from etcd Config source: {1}", new Object[]{key, ex.getMessage()});
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
    
    private String toString(GetResponse response){
        if(response.getCount()>0){
            return response.getKvs().get(0).getValue().toStringUtf8();
        }
        return null;
    }
    
    private Client getClient(){
        if(this.client == null ){
            log.info("Loading [etcd] MicroProfile ConfigSource");

            String scheme = getConfig().getOptionalValue(KEY_SCHEME, String.class).orElse(DEFAULT_SCHEME);
            String host = getConfig().getOptionalValue(KEY_HOST, String.class).orElse(DEFAULT_HOST);
            String port = getConfig().getOptionalValue(KEY_PORT, String.class).orElse(DEFAULT_PORT);
            
            String endpoint = String.format("%s://%s:%s",scheme,host,port);
            log.log(Level.INFO, "Using [{0}] as etcd server endpoint", endpoint);
            this.client = Client.builder().endpoints(endpoint).build();
        }
        return this.client;
    }
    

}
