package com.edasaki.rpg.utils.gson;

import java.io.IOException;

import com.edasaki.core.utils.gson.RGson;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class RPGItemAdapter extends TypeAdapter<RPGItem> {

    public static void register() {
        RGson.registerModifier((builder) -> {
            builder.registerTypeAdapter(RPGItem.class, new RPGItemAdapter());
            return builder;
        });
    }

    @Override
    public RPGItem read(JsonReader in) throws IOException {
        String id = "";
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "identifier":
                    id = in.nextString();
                    break;
            }
        }
        in.endObject();
        return ItemManager.itemIdentifierToRPGItemMap.getOrDefault(id, null);
    }

    @Override
    public void write(JsonWriter out, RPGItem item) throws IOException {
        out.beginObject();
        out.name("identifier").value(item.identifier);
        out.endObject();
    }

}
