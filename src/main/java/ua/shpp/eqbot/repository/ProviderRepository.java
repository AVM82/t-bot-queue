package ua.shpp.eqbot.repository;


import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ProviderRepository {
    final HashMap<Long, String> Providers = new HashMap<Long, String>();
    public boolean findById (long id) {
        return Providers.containsKey(id);
    }

    public void saveProvider (long id, String name) {
        Providers.put(id, name);
    }
}
