package ua.shpp.eqbot.repository;

import java.util.HashMap;

public class ProviderRepository {
    final HashMap<Long, String> providers = new HashMap<>();

    public boolean findById(long id) {
        return providers.containsKey(id);
    }

    public void saveProvider(long id) {
        providers.put(id, "Provider");
    }
}
