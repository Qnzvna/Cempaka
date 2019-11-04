package org.cempaka.cyclone.examples;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.cempaka.cyclone.annotations.Thunderbolt;

public class GoogleTest
{
    private HttpClient httpClient = HttpClients.createDefault();

    @Thunderbolt
    public void getGoogle() throws IOException
    {
        httpClient.execute(new HttpGet("http://google.pl"));
    }
}
