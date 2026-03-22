<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý Video</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .poster-thumb { width: 60px; height: 80px; object-fit: cover; }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="d-flex align-items-center gap-2">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-sm"> ← Trang chủ </a>
            <h4 class="fw-bold mb-0">🎬 QUẢN LÝ VIDEO</h4>
        </div>
        <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#createModal">+ Thêm Video</button>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="bg-white shadow rounded p-3">
        <table class="table table-hover align-middle">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Poster</th>
                    <th>Tiêu đề</th>
                    <th>Năm</th>
                    <th>Điểm</th>
                    <th class="text-center">Lượt xem</th>
                    <th class="text-end">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="v" items="${videos}">
                    <tr>
                        <td>${v.id}</td>
                        <td><img src="${v.poster != 'N/A' ? v.poster : 'https://via.placeholder.com/60x80'}" class="poster-thumb"></td>
                        <td>${v.title}</td>
                        <td>${v.year}</td>
                        <td>${v.imdbRating}</td>
                        <td class="text-center"><span class="badge bg-secondary">${v.views}</span></td>
                        <td class="text-end">
                            <button class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#editModal"
                                    onclick="editVideo(
                                        '${v.id}',
                                        '${v.title}',
                                        '${v.description}',
                                        '${v.poster}',
                                        '${v.year}',
                                        '${v.director}',
                                        '${v.actors}',
                                        '${v.genre}',
                                        '${v.imdbRating}'
                                    )">Sửa</button>
                            <a href="${pageContext.request.contextPath}/admin/video/delete?id=${v.id}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirm('Xóa video này?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty videos}">
                    <tr><td colspan="7" class="text-center text-muted">Chưa có video</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<!-- Modal thêm video -->
<div class="modal fade" id="createModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/video/create" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">➕ Thêm Video bằng IMDb ID</h5>
                    <button class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>IMDb ID (vd: tt1375666)</label>
                        <input name="imdbId" class="form-control" required>
                        <small class="text-muted">Sẽ tự động lấy thông tin từ OMDb và tạo link từ VidSrc</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal sửa video -->
<div class="modal fade" id="editModal">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/video/update" method="post">
                <input type="hidden" name="id" id="editId">
                <div class="modal-header">
                    <h5 class="modal-title">✏️ Sửa Video</h5>
                    <button class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label>Tiêu đề</label>
                            <input id="editTitle" name="title" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label>Năm</label>
                            <input id="editYear" name="year" class="form-control" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label>Đạo diễn</label>
                            <input id="editDirector" name="director" class="form-control">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label>Thể loại</label>
                            <input id="editGenre" name="genre" class="form-control">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label>Diễn viên</label>
                        <input id="editActors" name="actors" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label>Poster URL</label>
                        <input id="editPoster" name="poster" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label>Mô tả</label>
                        <textarea id="editDesc" name="description" rows="3" class="form-control"></textarea>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label>Điểm IMDb</label>
                            <input id="editRating" name="imdbRating" class="form-control" step="0.1">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button class="btn btn-primary">Cập nhật</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function editVideo(id, title, desc, poster, year, director, actors, genre, rating) {
        document.getElementById("editId").value = id;
        document.getElementById("editTitle").value = title;
        document.getElementById("editDesc").value = desc;
        document.getElementById("editPoster").value = poster;
        document.getElementById("editYear").value = year;
        document.getElementById("editDirector").value = director;
        document.getElementById("editActors").value = actors;
        document.getElementById("editGenre").value = genre;
        document.getElementById("editRating").value = rating;
    }
</script>
</body>
</html>