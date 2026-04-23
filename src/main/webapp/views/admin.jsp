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
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
            display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px;
            flex: 1;
        }
        .stat-card {
            background: var(--bg3);
            border: 1px solid var(--border);
            border-radius: 12px; padding: 18px;
            display: flex; align-items: center; gap: 14px;
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

        /* Pagination Styles */
        .pagination {
            display: flex; justify-content: center; align-items: center;
            gap: 8px; margin-top: 24px; padding-bottom: 20px;
        }
        .page-link {
            padding: 8px 14px; background: var(--bg3); color: var(--text2);
            border: 1px solid var(--border); border-radius: 6px;
            text-decoration: none; font-size: 0.85rem; font-weight: 600;
            transition: all 0.2s;
        }
        .page-link:hover, .page-link.active {
            background: var(--accent); color: #fff; border-color: var(--accent);
        }
        .page-link.disabled {
            opacity: 0.4; cursor: not-allowed; pointer-events: none;
        }
        
        .sort-header {
            cursor: pointer; position: relative;
            user-select: none; transition: color 0.2s;
        }
        .sort-header:hover { color: var(--accent) !important; }
        .sort-icon { font-size: 0.7rem; margin-left: 4px; opacity: 0.5; }
        .sort-header.active { color: #fff !important; }
        .sort-header.active .sort-icon { opacity: 1; color: var(--accent); }

        .dashboard-header-flex {
            display: flex; gap: 24px; margin-bottom: 32px;
            align-items: stretch;
        }
        .chart-box {
            flex: 0 0 400px;
            background: var(--card); border: 1px solid var(--border);
            border-radius: 12px; padding: 24px;
            display: flex; flex-direction: column;
        }
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
        <a href="${pageContext.request.contextPath}/admin/video?tab=video" class="sidebar-link ${currentTab == 'video' ? 'active' : ''}">
            <i class="fas fa-film"></i> Quản lý Video
            <span class="badge-count">${totalVideos}</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/users?tab=users" class="sidebar-link ${currentTab == 'users' ? 'active' : ''}">
            <i class="fas fa-users"></i> Quản lý Người dùng
            <span class="badge-count">${totalUsers}</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/categories?tab=category" class="sidebar-link ${currentTab == 'category' ? 'active' : ''}">
            <i class="fas fa-list"></i> Quản lý Thể loại
            <span class="badge-count">${fn:length(categoryList)}</span>
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
                <span><c:out value="${sessionScope.user.fullname}"/></span>
            </div>
        </div>
    </header>

    <div class="content-area">
        <!-- Stats -->
        <div class="dashboard-header-flex">
            <!-- Stats (Left) -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon red"><i class="fas fa-film"></i></div>
                    <div>
                        <h3>${totalVideos}</h3>
                        <p>Tổng số phim</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon purple"><i class="fas fa-eye"></i></div>
                    <div>
                        <h3>${totalViews}</h3>
                        <p>Tổng lượt xem</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon gold"><i class="fas fa-users"></i></div>
                    <div>
                        <h3>${totalUsers}</h3>
                        <p>Tổng người dùng</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon green"><i class="fas fa-check-circle"></i></div>
                    <div>
                        <h3>${activeVideos}</h3>
                        <p>Phim đang hoạt động</p>
                    </div>
                </div>
            </div>

            <!-- Analytics Chart (Right) -->
            <c:if test="${currentTab == 'video'}">
                <div class="chart-box">
                    <div class="section-title" style="margin-bottom: 15px; font-size: 0.9rem;">
                        <i class="fas fa-chart-pie" style="color:var(--accent); margin-right:8px;"></i>
                        Phân tích Thể loại
                    </div>
                    <div style="flex: 1; min-height: 240px; position: relative;">
                        <canvas id="viewsGenreChart"></canvas>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Error alert -->
        <c:if test="${not empty error}">
            <div class="admin-alert">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${currentTab == 'users'}">
                <!-- User Table Section -->
                <div class="section-header">
                    <div class="section-title">Danh sách người dùng</div>
                </div>

                <div class="table-wrapper">
                    <div class="table-filter">
                        <input type="text" id="userSearch" placeholder="🔍 Tìm theo username hoặc email..." oninput="filterUserTable(this.value)">
                    </div>
                    <table id="userTable">
                        <thead>
                            <tr>
                                <th style="width: 50px;">Avatar</th>
                                <th>Username</th>
                                <th>Họ và tên</th>
                                <th>Email</th>
                                <th>Vai trò</th>
                                <th>Trạng thái</th>
                                <th style="text-align:right;">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${userList}">
                                <tr data-username="${fn:escapeXml(u.id)}" data-email="${u.email}">
                                    <td>
                                        <div class="user-avatar" style="width:36px; height:36px; font-size: 0.9rem;">
                                            ${fn:substring(u.fullname, 0, 1)}
                                        </div>
                                    </td>
                                    <td><span style="font-weight:700; color:var(--text);"><c:out value="${u.id}"/></span></td>
                                    <td><c:out value="${u.fullname}"/></td>
                                    <td><c:out value="${u.email}"/></td>
                                    <td>
                                        <c:if test="${u.admin}">
                                            <span class="status-badge" style="background:rgba(124,58,237,0.12); color:#a78bfa;">Admin</span>
                                        </c:if>
                                        <c:if test="${!u.admin}">
                                            <span class="status-badge" style="background:var(--bg3); color:var(--text3);">User</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.active}">
                                                <span class="status-badge status-active">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge" style="background:rgba(229,9,20,0.12); color:#ff6b6b;">Đã khóa</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="action-btns" style="justify-content:flex-end;">
                                            <c:if test="${u.id != sessionScope.user.id}">
                                                <button class="btn-edit" style="background:rgba(124,58,237,0.1); border-color:rgba(124,58,237,0.2);"
                                                        onclick="submitAction('users/role', '${u.id}', 'Thay đổi quyền của người dùng này?')">
                                                    <i class="fas fa-user-shield"></i> Quyền
                                                </button>
                                                <button class="btn-edit" style="background:rgba(245,197,24,0.1); border-color:rgba(245,197,24,0.3); color:var(--gold);"
                                                        onclick="submitAction('users/premium', '${u.id}', 'Thay đổi trạng thái Premium của người dùng này?')">
                                                    <i class="fas fa-crown"></i> ${u.premium ? 'Hủy Premium' : 'Bật Premium'}
                                                </button>
                                                <c:choose>
                                                    <c:when test="${u.active}">
                                                        <button class="btn-delete" onclick="toggleUserStatus('${u.id}', false)">
                                                            <i class="fas fa-lock"></i> Khóa
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn-edit" style="background:rgba(34,197,94,0.15); color:var(--green); border-color:rgba(34,197,94,0.3);" 
                                                                onclick="toggleUserStatus('${u.id}', true)">
                                                            <i class="fas fa-unlock"></i> Mở khóa
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Hidden form for toggling user status -->
                <form id="userToggleForm" action="${pageContext.request.contextPath}/admin/users/toggle" method="post" style="display:none;">
                    <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                    <input type="hidden" name="id" id="toggleUserId">
                    <input type="hidden" name="action" id="toggleUserAction">
                    <input type="hidden" name="reason" id="toggleUserReason">
                </form>
            </c:when>
            <c:when test="${currentTab == 'category'}">
                <!-- Category Management Section -->
                <div class="section-header">
                    <div class="section-title">Danh sách Thể loại</div>
                    <div style="display:flex; gap:12px; align-items:center;">
                        <input type="text" placeholder="Tìm thể loại..." oninput="filterCategoryTable(this.value)" 
                               style="background:rgba(255,255,255,0.05); border:1px solid var(--border); padding:8px 12px; border-radius:8px; color:#fff; font-size:0.85rem; width:200px;">
                        <button class="btn-add" onclick="openModal('addCategoryModal')">
                            <i class="fas fa-plus"></i> Thêm thể loại
                        </button>
                    </div>
                </div>
                <div class="table-wrapper">
                    <table id="categoryTable">
                        <thead>
                            <tr>
                                <th>Tên thể loại</th>
                                <th>Slug</th>
                                <th>Trạng thái</th>
                                <th style="text-align:right;">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cat" items="${categoryList}">
                                <tr>
                                    <td><strong><c:out value="${cat.name}"/></strong></td>
                                    <td><code>${cat.slug}</code></td>
                                    <td>
                                        <span class="status-badge ${cat.active ? 'status-active' : ''}">
                                            ${cat.active ? 'Hoạt động' : 'Tạm ẩn'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-btns" style="justify-content:flex-end;">
                                            <button class="btn-edit" 
                                                    data-id="${cat.id}" 
                                                    data-name="${fn:escapeXml(cat.name)}" 
                                                    data-slug="${cat.slug}"
                                                    data-desc="${fn:escapeXml(cat.description)}"
                                                    data-order="${cat.order}"
                                                    data-active="${cat.active}"
                                                    onclick="openEditCat(this)">
                                                <i class="fas fa-pen"></i> Sửa
                                            </button>
                                            <button class="btn-delete" onclick="submitAction('categories/delete', '${cat.id}', 'Xóa thể loại này?')">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Video Table Section (Default) -->
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
                                <th class="sort-header ${sortBy == 'title' ? 'active' : ''}" 
                                    onclick="location.href='?tab=video&sortBy=title&sortDir=${sortBy == 'title' && sortDir == 'asc' ? 'desc' : 'asc'}&p=${currentPage}'">
                                    Tiêu đề / Slug <i class="fas fa-sort sort-icon"></i>
                                </th>
                                <th class="sort-header ${sortBy == 'year' ? 'active' : ''}"
                                    onclick="location.href='?tab=video&sortBy=year&sortDir=${sortBy == 'year' && sortDir == 'asc' ? 'desc' : 'asc'}&p=${currentPage}'">
                                    Năm <i class="fas fa-sort sort-icon"></i>
                                </th>
                                <th>Thể loại</th>
                                <th>Số tập</th>
                                <th class="sort-header ${sortBy == 'views' ? 'active' : ''}"
                                    onclick="location.href='?tab=video&sortBy=views&sortDir=${sortBy == 'views' && sortDir == 'asc' ? 'desc' : 'asc'}&p=${currentPage}'">
                                    Lượt xem <i class="fas fa-sort sort-icon"></i>
                                </th>
                                <th class="sort-header ${sortBy == 'createdAt' ? 'active' : ''}"
                                    onclick="location.href='?tab=video&sortBy=createdAt&sortDir=${sortBy == 'createdAt' && sortDir == 'asc' ? 'desc' : 'asc'}&p=${currentPage}'">
                                    Ngày đăng <i class="fas fa-sort sort-icon"></i>
                                </th>
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
                                        <div class="video-title"><c:out value="${s.title}"/></div>
                                        <div class="video-imdb"><c:out value="${s.slug}"/></div>
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
                                            <td>
                                        <c:choose>
                                            <c:when test="${s.active}">
                                                <span class="status-badge status-active">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge" style="background:rgba(229,9,20,0.12); color:#ff6b6b;">Inactive</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
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
                                                    data-category-id="${s.category != null ? s.category.id : ''}"
                                                    data-active="${s.active}"
                                                    onclick="openEdit(this)">
                                                <i class="fas fa-pen"></i> Sửa
                                            </button>
                                            <button class="btn-delete" onclick="submitAction('video/delete', '${s.id}', 'Xóa phim \'${fn:escapeXml(s.title)}\'?')">
                                                <i class="fas fa-trash"></i> Xóa
                                            </button>
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

                <!-- Pagination -->
                <div class="pagination">
                    <a href="?tab=video&sortBy=${sortBy}&sortDir=${sortDir}&p=${currentPage - 1}" 
                       class="page-link ${currentPage <= 1 ? 'disabled' : ''}">
                        <i class="fas fa-chevron-left"></i> Trước
                    </a>
                    
                    <c:forEach begin="1" end="${totalPages}" var="page">
                        <a href="?tab=video&sortBy=${sortBy}&sortDir=${sortDir}&p=${page}" 
                           class="page-link ${currentPage == page ? 'active' : ''}">${page}</a>
                    </c:forEach>
                    
                    <a href="?tab=video&sortBy=${sortBy}&sortDir=${sortDir}&p=${currentPage + 1}" 
                       class="page-link ${currentPage >= totalPages ? 'disabled' : ''}">
                        Sau <i class="fas fa-chevron-right"></i>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
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
            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
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
            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
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
                <div class="form-group">
                    <label>Thể loại chính</label>
                    <select name="categoryId" id="editCategoryId">
                        <option value="">-- Chọn thể loại --</option>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.id}">${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label>Thể loại (String)</label>
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
            <div class="form-group" style="display:flex; align-items:center; gap:10px;">
                <input type="checkbox" id="editActive" name="active" style="width:auto; cursor:pointer;">
                <label for="editActive" style="margin-bottom:0; cursor:pointer;">Phim đang hoạt động (Active)</label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeModal('editModal')">Hủy</button>
                <button type="submit" class="btn-submit"><i class="fas fa-save"></i> Lưu thay đổi</button>
            </div>
        </form>
    </div>
</div>

<!-- ─── MODAL: EDIT CATEGORY ─── -->
<div class="modal-overlay" id="editCategoryModal" onclick="closeModalOutside(event,'editCategoryModal')">
    <div class="modal-box">
        <div class="modal-header">
            <div class="modal-title">Chỉnh sửa Thể loại</div>
            <button class="modal-close" onclick="closeModal('editCategoryModal')"><i class="fas fa-times"></i></button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/categories/update" method="post">
            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
            <input type="hidden" name="id" id="editCatId">
            <div class="form-group">
                <label>Tên thể loại</label>
                <input name="name" id="editCatName" required>
            </div>
            <div class="form-group">
                <label>Slug</label>
                <input name="slug" id="editCatSlug" required>
            </div>
            <div class="form-group">
                <label>Mô tả</label>
                <textarea name="description" id="editCatDesc" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label>Thứ tự (Order)</label>
                <input name="order" id="editCatOrder" type="number" value="0">
            </div>
            <div class="form-group" style="display:flex; align-items:center; gap:10px;">
                <input type="checkbox" id="editCatActive" name="active" style="width:auto; cursor:pointer;">
                <label for="editCatActive" style="margin-bottom:0; cursor:pointer;">Hoạt động</label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeModal('editCategoryModal')">Hủy</button>
                <button type="submit" class="btn-submit">Lưu</button>
            </div>
        </form>
    </div>
</div>

<!-- ─── MODAL: ADD CATEGORY ─── -->
<div class="modal-overlay" id="addCategoryModal" onclick="closeModalOutside(event,'addCategoryModal')">
    <div class="modal-box">
        <div class="modal-header">
            <div class="modal-title">Thêm Thể loại mới</div>
            <button class="modal-close" onclick="closeModal('addCategoryModal')"><i class="fas fa-times"></i></button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/categories/create" method="post">
            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
            <div class="form-group">
                <label>Tên thể loại</label>
                <input name="name" placeholder="Ví dụ: Hành động" required>
            </div>
            <div class="form-group">
                <label>Slug</label>
                <input name="slug" placeholder="Ví dụ: hanh-dong" required>
            </div>
            <div class="form-group">
                <label>Thứ tự (Order)</label>
                <input name="order" type="number" value="0">
            </div>
            <div class="form-group" style="display:flex; align-items:center; gap:10px;">
                <input type="checkbox" name="active" checked style="width:auto; cursor:pointer;">
                <label style="margin-bottom:0; cursor:pointer;">Hiển thị trên trang chủ</label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeModal('addCategoryModal')">Hủy</button>
                <button type="submit" class="btn-submit">Thêm ngay</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openModal(id) { document.getElementById(id).classList.add('show'); }
    function closeModal(id) { document.getElementById(id).classList.remove('show'); }
    function closeModalOutside(e, id) { if(e.target.id === id) closeModal(id); }

    function openEdit(btn) {
        document.getElementById('editId').value = btn.dataset.id;
        document.getElementById('editTitle').value = btn.dataset.title;
        document.getElementById('editYear').value = btn.dataset.year;
        document.getElementById('editPoster').value = btn.dataset.poster;
        document.getElementById('editDesc').value = btn.dataset.desc;
        document.getElementById('editDirector').value = btn.dataset.director;
        document.getElementById('editActors').value = btn.dataset.actors;
        document.getElementById('editGenre').value = btn.dataset.genre;
        document.getElementById('editCategoryId').value = btn.dataset.categoryId;
        document.getElementById('editActive').checked = btn.dataset.active === 'true';
        openModal('editModal');
    }

    function openEditCat(btn) {
        document.getElementById('editCatId').value = btn.dataset.id;
        document.getElementById('editCatName').value = btn.dataset.name;
        document.getElementById('editCatSlug').value = btn.dataset.slug;
        document.getElementById('editCatDesc').value = btn.dataset.desc || '';
        document.getElementById('editCatOrder').value = btn.dataset.order || 0;
        document.getElementById('editCatActive').checked = btn.dataset.active === 'true';
        openModal('editCategoryModal');
    }

    function submitAction(path, id, msg) {
        if (confirm(msg)) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/admin/' + path;
            
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'id';
            input.value = id;
            form.appendChild(input);

            const csrfToken = document.createElement('input');
            csrfToken.type = 'hidden';
            csrfToken.name = 'csrf_token';
            csrfToken.value = '${sessionScope.csrf_token}';
            form.appendChild(csrfToken);

            document.body.appendChild(form);
            form.submit();
        }
    }

    function filterTable(q) {
        q = q.toLowerCase();
        document.querySelectorAll('#videoTable tbody tr[data-title]').forEach(row => {
            const title = row.dataset.title.toLowerCase();
            row.style.display = title.includes(q) ? '' : 'none';
        });
    }

    function filterCategoryTable(q) {
        q = q.toLowerCase();
        document.querySelectorAll('#categoryTable tbody tr').forEach(row => {
            const text = row.innerText.toLowerCase();
            row.style.display = text.includes(q) ? '' : 'none';
        });
    }

    function toggleUserStatus(id, active) {
        const action = active ? "unlock" : "lock";
        let reason = "";
        if (!active) {
            reason = prompt("Nhập lý do khóa tài khoản này:");
            if (reason === null) return;
            if (reason.trim() === "") {
                alert("Vui lòng nhập lý do khóa!");
                return;
            }
        } else {
            if (!confirm("Bạn có chắc chắn muốn mở khóa cho người dùng này?")) return;
        }

        document.getElementById('toggleUserId').value = id;
        document.getElementById('toggleUserAction').value = action;
        document.getElementById('toggleUserReason').value = reason;
        document.getElementById('userToggleForm').submit();
    }

    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') {
            closeModal('createModal');
            closeModal('editModal');
            closeModal('addCategoryModal');
            closeModal('editCategoryModal');
        }
    });

    // Initialize chart only if the canvas exists (which means we are in the video tab)
    const chartCanvas = document.getElementById('viewsGenreChart');
    if (chartCanvas) {
        const ctx = chartCanvas.getContext('2d');
        // Avoid JS linting errors by keeping EL within quotes
        const labelsStr = '${empty chartLabels ? "[]" : chartLabels}';
        const dataStr = '${empty chartData ? "[]" : chartData}';
        const labels = JSON.parse(labelsStr);
        const data = JSON.parse(dataStr);
        
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    data: data,
                    backgroundColor: [
                        '#e50914', '#7c3aed', '#f5c518', '#22c55e', '#3b82f6', 
                        '#ef4444', '#8b5cf6', '#facc15', '#10b981', '#60a5fa'
                    ],
                    borderWidth: 1,
                    borderColor: '#15151f',
                    hoverOffset: 15
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                layout: { padding: 10 },
                plugins: {
                    legend: {
                        position: 'right',
                        labels: { color: '#9090b0', font: { family: 'Inter', size: 12 } }
                    }
                }
            }
        });
    }
</script>
</body>
</html>