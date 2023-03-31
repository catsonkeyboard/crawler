package org.catsonkeyboard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String baseUrl = "https://www.5688.cn";
        String url = "https://www.5688.cn/airport/";
        Map<String, List<String>> airportMap = new LinkedHashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements countries = doc.select("html body div.tool_model_box div.left div.port_list div.category ul li");
            for (Element country : countries) {
                String countryName = country.select("a").text();
                String subUrl = country.select("a").attr("href");
                var airports = getAirports(baseUrl + subUrl);
                airportMap.put(countryName, airports);
            }
            System.out.println(airportMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAirports(String subUrl) throws IOException {
        List<String> airportStrList = new ArrayList<>();
        Document doc = Jsoup.connect(subUrl).get();
        Elements airports = doc.select("html body div.tool_model_box div.left div.port_list tbody tr");
        for (Element airport : airports) {
            if(airport.select("td a").size() > 0) {
                String airportIATA = airport.select("td a").get(0).text().replace("海关","");
                airportStrList.add(airportIATA);
            }
        }
        return airportStrList;
    }
}