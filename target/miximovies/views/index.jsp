<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Xem phim</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .movie-card {
            transition: transform 0.2s;
        }
        .movie-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
        .card-img-top {
            height: 300px;
            object-fit: cover;
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-dark bg-dark">
        <div class="container">
            <span class="navbar-brand mb-0 h1">🎬 Movie Streaming</span>
            <div>
                <span class="text-light me-3">Xin chào, ${sessionScope.user.fullname}</span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <c:forEach var="video" items="${videos}">
                <div class="col-md-3 mb-4">
                    <div class="card movie-card h-100">
                        <img src="${video.poster != 'N/A' ? video.poster : 'https://via.placeholder.com/300x450?text=No+Poster'}" 
                             class="card-img-top" alt="${video.title}">
                        <div class="card-body">
                            <h5 class="card-title">${video.title}</h5>
                            <p class="card-text"><small class="text-muted">${video.year}</small></p>
                            <p class="card-text">⭐ ${video.imdbRating}</p>
                            <a href="${pageContext.request.contextPath}/watch?id=${video.id}" class="btn btn-primary btn-sm">Xem phim</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty videos}">
                <div class="col-12 text-center text-muted">
                    <p>Chưa có phim nào.</p>
                </div>
            </c:if>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>