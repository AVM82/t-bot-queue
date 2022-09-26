package ua.shpp.eqbot.repository;

import java.util.HashMap;

public class ProviderRepository {
    final HashMap<Long, String> Providers = new HashMap<Long, String>();
    public boolean findById (long id) {
        return Providers.containsKey(id);
    }

    public void saveProvider (long id) {
        Providers.put(id, "Provider");
    }
}
