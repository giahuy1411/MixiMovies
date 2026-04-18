package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Episode;
import entity.Series;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class KKPhimClient {

    private static final String BASE_URL = "https://phimapi.com";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Lấy thông tin series (phim bộ hoặc phim lẻ) và danh sách tập theo slug.
     * @param slug slug của phim (ví dụ: "ngoi-truong-xac-song")
     * @return Series object đã có episodes, hoặc null nếu không tìm thấy
     */
    public static Series fetchSeriesBySlug(String slug) throws Exception {
        String urlStr = BASE_URL + "/phim/" + slug;
        JsonNode root = callApi(urlStr);

        // Kiểm tra lỗi
        if (root.has("status") && root.path("status").asText().equals("error")) {
            throw new Exception("KKPhim API error: " + root.path("message").asText());
        }

        JsonNode movieNode = root.path("movie");
        if (movieNode.isMissingNode()) {
            throw new Exception("Không tìm thấy thông tin phim với slug: " + slug);
        }

        Series series = new Series();
        series.setSlug(slug);
        series.setTitle(movieNode.path("name").asText());
        series.setPoster(movieNode.path("poster_url").asText());
        series.setDescription(movieNode.path("content").asText());
        series.setYear(movieNode.path("year").asInt());
        series.setDirector(movieNode.path("director").asText());
        series.setActors(movieNode.path("actor").asText());
        // Lấy thể loại đầu tiên (có thể cải thiện sau)
        JsonNode categories = movieNode.path("category");
        if (categories.isArray() && categories.size() > 0) {
            series.setGenre(categories.get(0).path("name").asText());
        }
        series.setType(movieNode.path("type").asText()); // "movie" hoặc "tv"
        series.setTmdbId(movieNode.path("tmdb").path("id").asInt());

        // Xử lý episodes
        List<Episode> episodes = new ArrayList<>();
        JsonNode episodesNode = root.path("episodes");
        if (episodesNode.isArray() && episodesNode.size() > 0) {
            // Chỉ lấy episodes từ server đầu tiên để tránh trùng lặp
            JsonNode firstServer = episodesNode.get(0);
            JsonNode serverData = firstServer.path("server_data");
            if (serverData.isArray()) {
                for (JsonNode item : serverData) {
                    Episode episode = new Episode();
                    episode.setSeries(series);
                    episode.setSeasonNumber(item.path("season").asInt());
                    episode.setEpisodeNumber(item.path("episode").asInt());
                    episode.setTitle(item.path("name").asText());
                    // Lấy link phát (ưu tiên link_embed)
                    String videoUrl = item.path("link_embed").asText();
                    if (videoUrl.isEmpty()) {
                        videoUrl = item.path("link_m3u8").asText();
                    }
                    episode.setVideoUrl(videoUrl);
                    episode.setSubtitleUrl(null);
                    episodes.add(episode);
                }
            }
        }
        series.setEpisodes(episodes);
        return series;
    }

    /**
     * Tìm kiếm phim theo từ khóa.
     * @param keyword từ khóa tìm kiếm
     * @param page số trang (bắt đầu từ 1)
     * @return danh sách Series (chỉ chứa thông tin cơ bản, không có episodes)
     */
    public static List<Series> search(String keyword, int page) throws Exception {
        String urlStr = BASE_URL + "/v1/api/tim-kiem?keyword=" + keyword.replace(" ", "%20") + "&page=" + page;
        JsonNode root = callApi(urlStr);
        JsonNode items = root.path("data").path("items");
        List<Series> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode item : items) {
                Series s = new Series();
                s.setSlug(item.path("slug").asText());
                s.setTitle(item.path("name").asText());
                s.setPoster(item.path("poster_url").asText());
                s.setYear(item.path("year").asInt());
                s.setType(item.path("type").asText());
                list.add(s);
            }
        }
        return list;
    }

    /**
     * Lấy danh sách phim mới cập nhật.
     * @param page số trang
     * @return danh sách Series (chứa thông tin cơ bản)
     */
    public static List<Series> getNewMovies(int page) throws Exception {
        String urlStr = BASE_URL + "/danh-sach/phim-moi-cap-nhat?page=" + page;
        JsonNode root = callApi(urlStr);
        JsonNode items = root.path("items");
        List<Series> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode item : items) {
                Series s = new Series();
                s.setSlug(item.path("slug").asText());
                s.setTitle(item.path("name").asText());
                s.setPoster(item.path("poster_url").asText());
                s.setYear(item.path("year").asInt());
                s.setType(item.path("type").asText());
                list.add(s);
            }
        }
        return list;
    }

    /**
     * Lấy thông tin phim theo TMDB ID.
     * @param type "movie" hoặc "tv"
     * @param tmdbId TMDB ID
     * @return Series object (không có episodes)
     */
    public static Series fetchByTmdbId(String type, int tmdbId) throws Exception {
        String urlStr = BASE_URL + "/tmdb/" + type + "/" + tmdbId;
        JsonNode root = callApi(urlStr);
        // Cấu trúc trả về tương tự /phim/{slug} nhưng thiếu episodes, chỉ có metadata
        JsonNode movieNode = root.path("movie");
        Series series = new Series();
        series.setSlug(movieNode.path("slug").asText());
        series.setTitle(movieNode.path("name").asText());
        series.setPoster(movieNode.path("poster_url").asText());
        series.setDescription(movieNode.path("content").asText());
        series.setYear(movieNode.path("year").asInt());
        series.setDirector(movieNode.path("director").asText());
        series.setActors(movieNode.path("actor").asText());
        series.setType(movieNode.path("type").asText());
        series.setTmdbId(tmdbId);
        return series;
    }

    // Phương thức gọi API và parse JSON
    private static JsonNode callApi(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        int respCode = conn.getResponseCode();
        if (respCode != 200) {
            throw new Exception("KKPhim API returned HTTP " + respCode + " for URL: " + urlStr);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        conn.disconnect();

        return mapper.readTree(jsonBuilder.toString());
    }
}