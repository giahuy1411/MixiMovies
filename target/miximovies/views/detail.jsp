<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${video.title} - Xem phim</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .video-container { position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; background: #000; margin-bottom: 20px; }
        .video-container iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: none; }
        .movie-info { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
    <div class="container mt-4">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary mb-3">← Trang chủ</a>
        <div class="video-container">
            <iframe src="${video.embedUrl}" allowfullscreen></iframe>
        </div>
        <div class="movie-info">
            <div class="row">
                <div class="col-md-3">
                    <img src="${video.poster != 'N/A' ? video.poster : 'https://via.placeholder.com/300x450'}" class="img-fluid rounded">
                </div>
                <div class="col-md-9">
                    <h2>${video.title} (${video.year})</h2>
                    <p><strong>Đạo diễn:</strong> ${video.director}</p>
                    <p><strong>Diễn viên:</strong> ${video.actors}</p>
                    <p><strong>Thể loại:</strong> ${video.genre}</p>
                    <p><strong>Điểm IMDb:</strong> ⭐ ${video.imdbRating}</p>
                    <p><strong>Mô tả:</strong> ${video.description}</p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>