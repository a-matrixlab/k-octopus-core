/* 
 * Copyright (C) 2019 Lisa Park, Inc. (www.lisa-park.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lisapark.koctopus.core.runtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author alexmy
 */
public class RuntimeUtils {

    static final Logger LOG = Logger.getLogger(RuntimeUtils.class.getName());

    public static List<String> runRemoteModel(String serviceUrl, String graph) {
        List<String> output = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(serviceUrl);
            StringEntity reqEntity = new StringEntity(graph);
            httppost.setEntity(reqEntity);

            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                output.add("\n----------------------------------------\n");
                output.add(response.getStatusLine().getReasonPhrase() + "\n\n");

                String entity = EntityUtils.toString(response.getEntity());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jparser = new JsonParser();
                JsonElement jelem = jparser.parse(entity);
                String json = gson.toJson(jelem);
                output.add(json);
            }
        } catch (IOException | NullPointerException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
            output.add("\n----------------------------------------");
            output.add("\nError processing Request: " + ex.getMessage());
            output.add("\nInvalid URL:\n");
            output.add(serviceUrl);
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
                output.add(ex.getMessage());
            }
        }
        return output;
    }
}
