package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import entity.Video;

public class OMDbClient {

    private static final String API_KEY = "59b1eb62"; 
    private static final String API_URL = "http://www.omdbapi.com/";

    /**
     * Lấy thông tin phim theo IMDb ID và trả về đối tượng Video đã được fill dữ liệu.
     */
    public static Video fetchMovieByImdbId(String imdbId) throws Exception {
        String urlStr = API_URL + "?apikey=" + API_KEY + "&i=" + imdbId + "&plot=full";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        conn.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonBuilder.toString());

        if (!root.path("Response").asText().equals("True")) {
            throw new Exception("OMDb error: " + root.path("Error").asText());
        }

        Video video = new Video();
        video.setImdbId(imdbId);
        video.setTitle(root.path("Title").asText());

        // Xử lý năm (có thể là chuỗi "2023" hoặc "2023–2025")
        String yearStr = root.path("Year").asText();
        if (yearStr != null && !yearStr.isEmpty()) {
            try {
                video.setYear(Integer.parseInt(yearStr.substring(0, 4)));
            } catch (NumberFormatException e) {
                video.setYear(null);
            }
        }

        video.setPoster(root.path("Poster").asText());
        video.setDescription(root.path("Plot").asText());
        video.setDirector(root.path("Director").asText());
        video.setActors(root.path("Actors").asText());
        video.setGenre(root.path("Genre").asText());

        String ratingStr = root.path("imdbRating").asText();
        if (ratingStr != null && !ratingStr.equals("N/A")) {
            try {
                video.setImdbRating(Double.parseDouble(ratingStr));
            } catch (NumberFormatException e) {
                video.setImdbRating(null);
            }
        }

        // KHÔNG set videoUrl nữa
        video.setViews(0);
        video.setActive(true);

        return video;
    }
}