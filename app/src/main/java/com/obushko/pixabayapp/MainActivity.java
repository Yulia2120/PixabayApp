package com.obushko.pixabayapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PixabayAdapter pixabayAdapter;
    private ArrayList<Hit> hits;
    private Button button;
    private RequestQueue requestQueue;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hits = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        button = findViewById(R.id.searchButton);
        searchText = findViewById(R.id.searchEditText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getHits();
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private boolean getHits() throws UnsupportedEncodingException {

        String keyword = searchText.getText().toString();
        if(keyword.isBlank()){
            Toast.makeText(this, "Input text", Toast.LENGTH_SHORT).show();
            return true;
        }
        String encodeKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
        String key = "36380102-93dc6e2107cf2386bcc408e86&q";
        String url = "https://pixabay.com/api/?key="+key+"="+encodeKeyword+"&image_type=photo&pretty=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");

                    for(int i =0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String tags = jsonObject.getString("tags");
                        String previewURL = jsonObject.getString("webformatURL");
                        int likes = jsonObject.getInt("likes");
                        String user = jsonObject.getString("user");

                        Hit hit = new Hit();
                        hit.setTags(tags);
                        hit.setPreviewURL(previewURL);
                        hit.setLikes(likes);
                        hit.setUser(user);

                        hits.add(hit);
                    }
                    pixabayAdapter = new PixabayAdapter(MainActivity.this, hits);
                    recyclerView.setAdapter(pixabayAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

        return false;
    }
}