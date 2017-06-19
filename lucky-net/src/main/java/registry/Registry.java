package registry;

import rpc.Provider;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 15:31 2017/6/16
 */
public interface Registry {

    void register(Supplier<Provider> supplier) throws Exception;

    void remove(Provider provider);

    List<Provider> lookup(String serverName);

    void add(Provider provider);

}
