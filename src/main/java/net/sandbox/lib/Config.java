package net.sandbox.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.Resource;

public class Config<T> {
	private static final Gson GSON = new Gson();
	private final Class<T> configClass;
	private T config;
	private File configFile;
	private Resource configResource;

	public Config(Class<T> type) {
		this.configClass = type;
	}

	public Config(Class<T> type, String configFilePath) {
		this(type);
		this.configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), configFilePath.endsWith(".json") ? configFilePath : (configFilePath + ".json"));
		this.readConfigFromFile();
	}

	public Config(Class<T> type, Resource resource) {
		this(type);
		this.configResource = resource;
		this.readConfigFromResource();
	}

	public T getConfig() {
		return this.config;
	}

	private void readConfig(byte[] bytes) {
		String file = new String(bytes);
		this.config = GSON.fromJson(file, this.configClass);
	}

	private void readConfigFromFile() {
    try (FileInputStream stream = new FileInputStream(this.configFile)) {
      byte[] bytes = new byte[stream.available()];
      stream.read(bytes);
      this.readConfig(bytes);
    } catch (FileNotFoundException e) {
      createEmptyConfigFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

	private void readConfigFromResource() {
		try (InputStream stream = this.configResource.getInputStream()) {
      byte[] bytes = new byte[stream.available()];
      stream.read(bytes);
      this.readConfig(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

	private void createEmptyConfigFile() {
    JsonObject object = new JsonObject();

    try (FileOutputStream stream = new FileOutputStream(configFile)) {
      stream.write(GSON.toJson(object).getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
