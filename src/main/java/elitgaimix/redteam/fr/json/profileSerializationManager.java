package elitgaimix.redteam.fr.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class profileSerializationManager {

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public static String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> Object deserialize(String json, Class<T> clss) {
        return gson.fromJson(json, clss);

    }
}
