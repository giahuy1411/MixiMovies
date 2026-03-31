<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel — Quản lý Video | MixiMovies</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        :root {
            --bg: #0c0c14;
            --bg2: #111119;
            --bg3: #191924;
            --card: #15151f;
            --accent: #e50914;
            --accent2: #7c3aed;
            --gold: #f5c518;
            --green: #22c55e;
            --text: #e8e8f0;
            --text2: #9090b0;
            --text3: #55556a;
            --border: rgba(255,255,255,0.07);
            --sidebar-w: 240px;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: var(--bg);
            color: var(--text);
            display: flex; min-height: 100vh;
        }

        /* ─── SIDEBAR ─── */
        .admin-sidebar {
            width: var(--sidebar-w); flex-shrink: 0;
            background: var(--bg2);
            border-right: 1px solid var(--border);
            display: flex; flex-direction: column;
            position: fixed; top: 0; left: 0; bottom: 0; z-index: 100;
        }
        .sidebar-logo {
            padding: 28px 24px 24px;
            border-bottom: 1px solid var(--border);
        }
        .sidebar-logo .brand {
            font-size: 1.2rem; font-weight: 800;
            background: linear-gradient(135deg, #fff 0%, #e50914 100%);
            -webkit-background-clip: text; background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .sidebar-logo .sub {
            font-size: 0.72rem; color: var(--text3);
            margin-top: 2px; letter-spacing: 1px; text-transform: uppercase;
        }

        .sidebar-nav { flex: 1; padding: 20px 14px; }
        .nav-group-label {
            font-size: 0.68rem; color: var(--text3);
            text-transform: uppercase; letter-spacing: 1px;
            padding: 0 10px 8px; margin-top: 16px; font-weight: 600;
        }
        .nav-group-label:first-child { margin-top: 0; }
        .sidebar-link {
            display: flex; align-items: center; gap: 10px;
            padding: 10px 12px; border-radius: 8px;
            color: var(--text2); text-decoration: none;
            font-size: 0.88rem; font-weight: 500;
            transition: all 0.2s; margin-bottom: 2px;
        }
        .sidebar-link i { width: 16px; text-align: center; }
        .sidebar-link:hover, .sidebar-link.active {
            background: rgba(229,9,20,0.1); color: #fff;
        }
        .sidebar-link.active { color: var(--accent); }
        .sidebar-link .badge-count {
            margin-left: auto; background: var(--accent);
            color: #fff; font-size: 0.68rem; padding: 2px 8px;
            border-radius: 20px; font-weight: 700;
        }

        .sidebar-footer {
            padding: 16px 14px;
            border-top: 1px solid var(--border);
        }
        .sidebar-footer a {
            display: flex; align-items: center; gap: 10px;
            padding: 10px 12px; border-radius: 8px;
            color: var(--text2); text-decoration: none;
            font-size: 0.85rem; font-weight: 500; transition: all 0.2s;
        }
        .sidebar-footer a:hover { background: rgba(255,255,255,0.05); color: #fff; }

        /* ─── MAIN ─── */
        .admin-main {
            flex: 1;
            margin-left: var(--sidebar-w);
            display: flex; flex-direction: column;
            min-height: 100vh;
        }

        /* Top bar */
        .topbar {
            height: 64px;
            background: var(--bg2);
            border-bottom: 1px solid var(--border);
            display: flex; align-items: center; justify-content: space-between;
            padding: 0 28px;
            position: sticky; top: 0; z-index: 50;
        }
        .topbar-left h1 {
            font-size: 1rem; font-weight: 700;
        }
        .topbar-left p { font-size: 0.78rem; color: var(--text3); margin-top: 1px; }
        .topbar-right { display: flex; align-items: center; gap: 12px; }
        .topbar-user {
            display: flex; align-items: center; gap: 10px;
            background: var(--bg3); padding: 8px 14px; border-radius: 8px;
            font-size: 0.85rem;
        }
        .user-avatar {
            width: 30px; height: 30px; border-radius: 50%;
            background: linear-gradient(135deg, var(--accent), #ff6b35);
            display: flex; align-items: center; justify-content: center;
            font-size: 0.75rem; font-weight: 700; color: #fff;
        }

        /* Content Area */
        .content-area { padding: 28px; flex: 1; }

        /* ─── STATS CARDS ─── */
        .stats-grid {
            display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px;
            margin-bottom: 28px;
        }
        .stat-card {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 12px; padding: 20px;
            display: flex; align-items: center; gap: 16px;
            transition: transform 0.2s;
        }
        .stat-card:hover { transform: translateY(-2px); }
        .stat-icon {
            width: 46px; height: 46px; border-radius: 10px;
            display: flex; align-items: center; justify-content: center;
            font-size: 1.1rem; flex-shrink: 0;
        }
        .stat-icon.red   { background: rgba(229,9,20,0.15); color: var(--accent); }
        .stat-icon.purple{ background: rgba(124,58,237,0.15); color: #a78bfa; }
        .stat-icon.gold  { background: rgba(245,197,24,0.15); color: var(--gold); }
        .stat-icon.green { background: rgba(34,197,94,0.15); color: var(--green); }
        .stat-card h3 { font-size: 1.5rem; font-weight: 800; }
        .stat-card p { font-size: 0.78rem; color: var(--text3); margin-top: 2px; }

        /* ─── ERROR ALERT ─── */
        .admin-alert {
            display: flex; align-items: center; gap: 10px;
            background: rgba(229,9,20,0.1); border: 1px solid rgba(229,9,20,0.3);
            border-radius: 10px; padding: 13px 18px;
            margin-bottom: 20px; color: #ff8080; font-size: 0.9rem;
        }

        /* ─── TABLE SECTION ─── */
        .section-header {
            display: flex; align-items: center; justify-content: space-between;
            margin-bottom: 16px;
        }
        .section-title {
            font-size: 1rem; font-weight: 700;
            display: flex; align-items: center; gap: 10px;
        }
        .section-title::before {
            content: ''; width: 3px; height: 18px;
            background: var(--accent); border-radius: 3px;
        }
        .btn-add {
            display: inline-flex; align-items: center; gap: 8px;
            padding: 9px 20px; background: var(--accent); color: #fff;
            border: none; border-radius: 8px; font-size: 0.85rem; font-weight: 600;
            cursor: pointer; transition: all 0.2s; text-decoration: none;
        }
        .btn-add:hover { background: #c40812; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(229,9,20,0.35); }

        .table-wrapper {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 12px;
            overflow: hidden;
        }

        /* Table Filter */
        .table-filter {
            padding: 14px 20px;
            border-bottom: 1px solid var(--border);
            display: flex; align-items: center; gap: 12px;
        }
        .table-filter input {
            flex: 1; max-width: 320px;
            background: var(--bg3); border: 1px solid var(--border);
            border-radius: 8px; color: var(--text);
            padding: 9px 14px; font-size: 0.85rem; outline: none;
            transition: border-color 0.2s;
        }
        .table-filter input:focus { border-color: rgba(229,9,20,0.5); }
        .table-filter input::placeholder { color: var(--text3); }

        table { width: 100%; border-collapse: collapse; }
        thead th {
            padding: 13px 16px;
            text-align: left; font-size: 0.72rem; font-weight: 700;
            color: var(--text3); text-transform: uppercase; letter-spacing: 0.8px;
            border-bottom: 1px solid var(--border);
            background: var(--bg2);
        }
        tbody tr { transition: background 0.15s; }
        tbody tr:hover { background: rgba(255,255,255,0.02); }
        tbody td {
            padding: 14px 16px;
            font-size: 0.87rem; color: var(--text2);
            border-bottom: 1px solid var(--border);
            vertical-align: middle;
        }
        tbody tr:last-child td { border-bottom: none; }

        .col-poster { width: 56px; }
        .poster-thumb {
            width: 48px; height: 64px; object-fit: cover;
            border-radius: 6px; display: block;
        }
        .video-title { color: var(--text); font-weight: 600; font-size: 0.88rem; }
        .video-imdb { font-size: 0.75rem; color: var(--text3); margin-top: 2px; }

        .rating-pill {
            display: inline-flex; align-items: center; gap: 4px;
            background: rgba(245,197,24,0.12); color: var(--gold);
            padding: 3px 10px; border-radius: 20px; font-size: 0.78rem; font-weight: 700;
        }
        .views-pill {
            display: inline-flex; align-items: center; gap: 4px;
            background: var(--bg3); color: var(--text3);
            padding: 3px 10px; border-radius: 20px; font-size: 0.78rem;
        }

        .status-badge {
            display: inline-block; padding: 3px 10px; border-radius: 20px;
            font-size: 0.72rem; font-weight: 700;
        }
        .status-active { background: rgba(34,197,94,0.12); color: var(--green); }

        .action-btns { display: flex; gap: 6px; }
        .btn-edit, .btn-delete {
            display: inline-flex; align-items: center; gap: 5px;
            padding: 6px 14px; border-radius: 6px;
            font-size: 0.78rem; font-weight: 600; cursor: pointer;
            transition: all 0.2s; border: none; text-decoration: none;
        }
        .btn-edit {
            background: rgba(124,58,237,0.15); color: #a78bfa;
            border: 1px solid rgba(124,58,237,0.3);
        }
        .btn-edit:hover { background: rgba(124,58,237,0.3); }
        .btn-delete {
            background: rgba(229,9,20,0.1); color: #ff6b6b;
            border: 1px solid rgba(229,9,20,0.25);
        }
        .btn-delete:hover { background: rgba(229,9,20,0.25); }

        .empty-row td {
            text-align: center; padding: 60px; color: var(--text3); font-size: 0.9rem;
        }

        /* ─── MODAL ─── */
        .modal-overlay {
            display: none; position: fixed; inset: 0; z-index: 999;
            background: rgba(0,0,0,0.75); backdrop-filter: blur(6px);
            align-items: center; justify-content: center;
        }
        .modal-overlay.show { display: flex; }
        .modal-box {
            background: var(--bg2);
            border: 1px solid var(--border);
            border-radius: 16px;
            width: 100%; max-width: 520px; max-height: 88vh;
            overflow-y: auto; padding: 28px;
            animation: modalIn 0.25s ease;
        }
        .modal-box.wide { max-width: 680px; }
        @keyframes modalIn {
            from { opacity: 0; transform: translateY(-20px) scale(0.97); }
            to   { opacity: 1; transform: translateY(0)   scale(1);    }
        }
        .modal-header {
            display: flex; align-items: center; justify-content: space-between;
            margin-bottom: 22px;
        }
        .modal-title { font-size: 1rem; font-weight: 700; }
        .modal-close {
            width: 30px; height: 30px; background: var(--bg3);
            border: none; border-radius: 6px; color: var(--text2);
            cursor: pointer; font-size: 0.9rem; transition: all 0.2s;
            display: flex; align-items: center; justify-content: center;
        }
        .modal-close:hover { background: rgba(229,9,20,0.2); color: var(--accent); }

        .form-group { margin-bottom: 16px; }
        .form-group label {
            display: block; font-size: 0.78rem; color: var(--text3);
            font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;
            margin-bottom: 6px;
        }
        .form-group input, .form-group textarea {
            width: 100%; padding: 10px 14px;
            background: var(--bg3); border: 1px solid var(--border);
            border-radius: 8px; color: var(--text);
            font-family: 'Inter', sans-serif; font-size: 0.88rem;
            outline: none; transition: border-color 0.2s;
        }
        .form-group input:focus, .form-group textarea:focus {
            border-color: rgba(229,9,20,0.5);
        }
        .form-group input::placeholder, .form-group textarea::placeholder { color: var(--text3); }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
        .form-hint { font-size: 0.75rem; color: var(--text3); margin-top: 5px; }

        .modal-footer {
            display: flex; gap: 10px; justify-content: flex-end; margin-top: 22px;
        }
        .btn-cancel {
            padding: 10px 22px; background: var(--bg3); color: var(--text2);
            border: 1px solid var(--border); border-radius: 8px; font-size: 0.88rem;
            font-weight: 600; cursor: pointer; transition: all 0.2s;
        }
        .btn-cancel:hover { background: rgba(255,255,255,0.05); }
        .btn-submit {
            padding: 10px 24px; background: var(--accent); color: #fff;
            border: none; border-radius: 8px; font-size: 0.88rem; font-weight: 600;
            cursor: pointer; transition: all 0.2s;
        }
        .btn-submit:hover { background: #c40812; }

        /* scrollbar */
        ::-webkit-scrollbar { width: 5px; }
        ::-webkit-scrollbar-track { background: var(--bg); }
        ::-webkit-scrollbar-thumb { background: var(--bg3); border-radius: 3px; }
    </style>
</head>
<body>

<!-- ─── SIDEBAR ─── -->
<aside class="admin-sidebar">
    <style>
        .mixi-logo-admin {
            height: 100px;
            object-fit: contain;
            margin-bottom: 15px;
            filter: drop-shadow(0 6px 14px rgba(229,9,20,0.35));
            transition: all 0.3s ease;
        }
        .mixi-logo-admin:hover {
            transform: scale(1.08) rotate(-3deg);
            filter: drop-shadow(0 8px 20px rgba(229,9,20,0.5));
        }
    </style>
    <div class="sidebar-logo" style="text-align: center; padding-top: 30px;">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="MixiMovies Logo" class="mixi-logo-admin">
        <div class="sub">Admin Panel</div>
    </div>
    <nav class="sidebar-nav">
        <div class="nav-group-label">Quản lý</div>
        <a href="${pageContext.request.contextPath}/admin/video" class="sidebar-link active">
            <i class="fas fa-film"></i> Quản lý Video
            <span class="badge-count">${fn:length(seriesList)}</span>
        </a>
        <div class="nav-group-label">Điều hướng</div>
        <a href="${pageContext.request.contextPath}/home" class="sidebar-link">
            <i class="fas fa-home"></i> Trang chủ
        </a>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout">
            <i class="fas fa-sign-out-alt"></i> Đăng xuất
        </a>
    </div>
</aside>

<!-- ─── MAIN ─── -->
<div class="admin-main">
    <!-- Top bar -->
    <header class="topbar">
        <div class="topbar-left">
            <h1>Dashboard</h1>
            <p>Quản lý nội dung MixiMovies</p>
        </div>
        <div class="topbar-right">
            <div class="topbar-user">
                <div class="user-avatar">A</div>
                <span>${sessionScope.user.fullname}</span>
            </div>
        </div>
    </header>

    <div class="content-area">
        <!-- Stats -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon red"><i class="fas fa-film"></i></div>
                <div>
                    <h3>${fn:length(seriesList)}</h3>
                    <p>Tổng số phim</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon purple"><i class="fas fa-eye"></i></div>
                <div>
                    <h3>—</h3>
                    <p>Lượt xem hôm nay</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon gold"><i class="fas fa-star"></i></div>
                <div>
                    <h3>—</h3>
                    <p>Đánh giá TB</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green"><i class="fas fa-check-circle"></i></div>
                <div>
                    <h3>${fn:length(seriesList)}</h3>
                    <p>Phim đang hoạt động</p>
                </div>
            </div>
        </div>

        <!-- Error alert -->
        <c:if test="${not empty error}">
            <div class="admin-alert">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:if>

        <!-- Table Section -->
        <div class="section-header">
            <div class="section-title">Danh sách phim</div>
            <button class="btn-add" onclick="openModal('createModal')">
                <i class="fas fa-plus"></i> Thêm phim mới
            </button>
        </div>

        <div class="table-wrapper">
            <div class="table-filter">
                <input type="text" id="tableSearch" placeholder="🔍 Tìm theo tên phim..." oninput="filterTable(this.value)">
            </div>
            <table id="videoTable">
                <thead>
                    <tr>
                        <th class="col-poster">Poster</th>
                        <th>Tiêu đề / Slug</th>
                        <th>Năm</th>
                        <th>Thể loại</th>
                        <th>Số tập</th>
                        <th>Lượt xem</th>
                        <th>Trạng thái</th>
                        <th style="text-align:right;">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${seriesList}">
                        <tr data-title="${fn:escapeXml(s.title)}">
                            <td>
                                <img src="${s.poster != 'N/A' && !empty s.poster ? s.poster : 'https://via.placeholder.com/48x64/1a1a24/666?text=?'}"
                                     class="poster-thumb" alt="${s.title}">
                            </td>
                            <td>
                                <div class="video-title">${s.title}</div>
                                <div class="video-imdb">${s.slug}</div>
                            </td>
                            <td>${s.year}</td>
                            <td>${s.genre}</td>
                            <td>
                                <span class="rating-pill">
                                    <i class="fas fa-list" style="font-size:0.65rem;"></i>
                                    ${fn:length(s.episodes)}
                                </span>
                            </td>
                            <td>
                                <span class="views-pill">
                                    <i class="fas fa-eye" style="font-size:0.65rem;"></i>
                                    ${s.views != null ? s.views : 0}
                                </span>
                            </td>
                            <td><span class="status-badge status-active">Active</span></td>
                            <td>
                                <div class="action-btns" style="justify-content:flex-end;">
                                    <button class="btn-edit" 
                                            data-id="${s.id}"
                                            data-title="${fn:escapeXml(s.title)}"
                                            data-desc="${fn:escapeXml(s.description)}"
                                            data-poster="${s.poster}"
                                            data-year="${s.year}"
                                            data-director="${fn:escapeXml(s.director)}"
                                            data-actors="${fn:escapeXml(s.actors)}"
                                            data-genre="${fn:escapeXml(s.genre)}"
                                            onclick="openEdit(this)">
                                        <i class="fas fa-pen"></i> Sửa
                                    </button>
                                    <a href="${pageContext.request.contextPath}/admin/video/delete?id=${s.id}"
                                       class="btn-delete"
                                       onclick="return confirm('Xóa phim \'${s.title}\'?')">
                                        <i class="fas fa-trash"></i> Xóa
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty seriesList}">
                        <tr class="empty-row">
                            <td colspan="8">
                                <i class="fas fa-film" style="font-size:2rem;display:block;margin-bottom:10px;color:#333;"></i>
                                Chưa có phim nào. Hãy thêm phim mới bằng Slug KKPhim.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- ─── MODAL: CREATE ─── -->
<div class="modal-overlay" id="createModal" onclick="closeModalOutside(event,'createModal')">
    <div class="modal-box">
        <div class="modal-header">
            <div class="modal-title"><i class="fas fa-plus-circle" style="color:var(--accent);margin-right:8px;"></i>Thêm phim qua Slug KKPhim</div>
            <button class="modal-close" onclick="closeModal('createModal')"><i class="fas fa-times"></i></button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/video/create" method="post">
            <div class="form-group">
                <label>Slug (tên miền phụ KKPhim)</label>
                <input name="slug" placeholder="Ví dụ: venom-keo-cuoi-cung" required>
                <div class="form-hint">Hệ thống sẽ tự động lấy thông tin phim và toàn bộ tập phim từ KKPhim.</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeModal('createModal')">Hủy</button>
                <button type="submit" class="btn-submit"><i class="fas fa-cloud-download-alt"></i> Lấy & Thêm phim</button>
            </div>
        </form>
    </div>
</div>

<!-- ─── MODAL: EDIT ─── -->
<div class="modal-overlay" id="editModal" onclick="closeModalOutside(event,'editModal')">
    <div class="modal-box wide">
        <div class="modal-header">
            <div class="modal-title"><i class="fas fa-pen" style="color:#a78bfa;margin-right:8px;"></i>Chỉnh sửa phim</div>
            <button class="modal-close" onclick="closeModal('editModal')"><i class="fas fa-times"></i></button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/video/update" method="post">
            <input type="hidden" name="id" id="editId">
            <div class="form-row">
                <div class="form-group">
                    <label>Tiêu đề</label>
                    <input id="editTitle" name="title" required>
                </div>
                <div class="form-group">
                    <label>Năm</label>
                    <input id="editYear" name="year" type="number" required>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Đạo diễn</label>
                    <input id="editDirector" name="director">
                </div>
            </div>
            <div class="form-group">
                <label>Thể loại</label>
                <input id="editGenre" name="genre">
            </div>
            <div class="form-group">
                <label>Diễn viên</label>
                <input id="editActors" name="actors">
            </div>
            <div class="form-group">
                <label>Poster URL</label>
                <input id="editPoster" name="poster">
            </div>
            <div class="form-group">
                <label>Mô tả</label>
                <textarea id="editDesc" name="description" rows="4"></textarea>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeModal('editModal')">Hủy</button>
                <button type="submit" class="btn-submit"><i class="fas fa-save"></i> Lưu thay đổi</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openModal(id) { document.getElementById(id).classList.add('show'); }
    function closeModal(id) { document.getElementById(id).classList.remove('show'); }
    function closeModalOutside(e, id) {
        if (e.target === document.getElementById(id)) closeModal(id);
    }

    function openEdit(btn) {
        document.getElementById('editId').value = btn.dataset.id;
        document.getElementById('editTitle').value = btn.dataset.title;
        document.getElementById('editDesc').value = btn.dataset.desc;
        document.getElementById('editPoster').value = btn.dataset.poster;
        document.getElementById('editYear').value = btn.dataset.year;
        document.getElementById('editDirector').value = btn.dataset.director;
        document.getElementById('editActors').value = btn.dataset.actors;
        document.getElementById('editGenre').value = btn.dataset.genre;
        openModal('editModal');
    }

    function filterTable(q) {
        q = q.toLowerCase();
        document.querySelectorAll('#videoTable tbody tr[data-title]').forEach(row => {
            const title = (row.dataset.title || '').toLowerCase();
            row.style.display = title.includes(q) ? '' : 'none';
        });
    }

    // ESC to close modals
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') {
            closeModal('createModal');
            closeModal('editModal');
        }
    });
</script>
</body>
</html>