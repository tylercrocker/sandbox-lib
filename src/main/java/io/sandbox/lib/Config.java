package io.sandbox.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

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

		File baseFolder = FabricLoader.getInstance().getConfigDir().toFile();
		String[] filePath = configFilePath.split("/");
		

		// File name will be the last item, let's pull it off and ensure it ends in .json
		String fileName = filePath[filePath.length - 1];
		fileName = fileName.endsWith(".json") ? fileName : (fileName + ".json");

		if (filePath.length > 1) {
			// We need to ensure that any folders in the path exist.
			String[] foldersInPath = Arrays.copyOf(filePath, filePath.length - 1);
			baseFolder = new File(baseFolder, String.join("/", foldersInPath));
			baseFolder.mkdirs();
		}

		this.configFile = new File(baseFolder, fileName);
		this.readConfigFromFile();
	}

	public Config(Class<T> type, Resource resource) {
		this(type);
		this.configResource = resource;
		this.readConfigFromResource();
	}

	public T getConfig() {
		if (this.config == null) {
			try {
				// If we had no config (usually because the config file didn't exist yet) then we can just initialize the defaults from our config class.
				this.config = this.configClass.getConstructor().newInstance();
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
				// This catch is mostly just to make the compiler happy.
				e.printStackTrace();
			}
		}
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

    try (FileOutputStream stream = new FileOutputStream(this.configFile)) {
      stream.write(GSON.toJson(object).getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
