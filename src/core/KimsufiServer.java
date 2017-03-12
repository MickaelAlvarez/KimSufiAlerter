package core;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KimsufiServer {
	// METADATA
	private static String AVAILABILITY_METADATA = "availability";
	private static String REGION_METADATA = "region";
	private static String DATACENTERS_METADATA = "datacenters";
	private static String DATACENTER_METADATA = "datacenter";
	// DATA
	private static String REGION = "europe";
	private static String DATACENTER = "default";
	private static String UNAVAILABLE = "unavailable";

	private JsonReader reader;

	public KimsufiServer(String model) {
		reader = new JsonReader("https://www.ovh.com/engine/api/dedicated/server/availabilities?country=fr?&hardware=" + model);
	}

	public boolean isAvailable() {
		try {
			JSONArray availabilities = reader.getJsonArray();

			for (int i = 0; i < availabilities.length(); i++) {
				JSONObject regionAvailable = availabilities.getJSONObject(i);
				if (haveDatacenterAvailable(regionAvailable)) {
					return true;
				}
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean haveDatacenterAvailable(JSONObject regionAvailable) {
		if (regionAvailable.has(REGION_METADATA) && regionAvailable.getString(REGION_METADATA).equals(REGION)) {
			JSONArray datacenters = regionAvailable.getJSONArray(DATACENTERS_METADATA);
			for (int c = 0; c < datacenters.length(); c++) {
				JSONObject datacenter = datacenters.getJSONObject(c);
				if (isDatacenterAvailable(datacenter)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isDatacenterAvailable(JSONObject datacenter) {
		if (datacenter.has(DATACENTER_METADATA) && datacenter.getString(DATACENTER_METADATA).equals(DATACENTER)) {
			if (datacenter.has(AVAILABILITY_METADATA)) {
				return !datacenter.getString(AVAILABILITY_METADATA).equals(UNAVAILABLE);
			}
		}

		return false;
	}
}
