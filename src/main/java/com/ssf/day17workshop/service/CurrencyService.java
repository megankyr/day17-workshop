package com.ssf.day17workshop.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ssf.day17workshop.model.CountryCode;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class CurrencyService {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    @Qualifier("redis")
    private RedisTemplate<String, String> template;

    private List<CountryCode> codes = null;

    public List<CountryCode> getCountryCode() {
        if (codes == null) {
            String url = UriComponentsBuilder
                    .fromUriString("https://api.exchangeratesapi.io/v1/symbols")
                    .queryParam("access_key", apiKey)
                    .toUriString();

            RequestEntity<Void> req = RequestEntity
                    .get(url).build();

            RestTemplate template = new RestTemplate();

            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();

            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();
            JsonObject symbolsObject = result.getJsonObject("symbols");

            List<CountryCode> countryCodes = new ArrayList<>();
            for (String code : symbolsObject.keySet()) {
                String name = symbolsObject.getString(code);

                CountryCode countryCode = new CountryCode(code, name);
                countryCodes.add(countryCode);
            }
            Collections.sort(countryCodes, Comparator.comparing(CountryCode::name));
            codes = countryCodes;

            System.out.println("Retrieved Country Codes:");
            for (CountryCode code : codes) {
                System.out.println(code.code() + ":" + code.name());
            }

        }
        return codes;

    }
}
