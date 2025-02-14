package org.chelonix.daggersample;

import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Info {

  private Properties properties;

  Info() {
    properties = new Properties();
    try (InputStream input = Info.class.getClassLoader().getResourceAsStream("info.properties")) {
      if (input == null) {
        throw new IllegalStateException("Could not find info.properties");
      }
      properties.load(input);
    } catch (IOException ex) {
      throw new IllegalStateException("Could not load info.properties", ex);
    }
  }

  JsonObject toJson() {
    JsonObject json = new JsonObject();
    properties.forEach((key, value) -> json.put((String) key, (String) value));
    return json;
  }
}
