<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${series.title}"/> (${series.year}) — MixiMovies</title>
    <meta name="description" content="<c:out value="${series.description}"/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        :root {
            --bg: #0a0a0f;
            --bg2: #111118;
            --bg3: #1a1a24;
            --card: #16161f;
            --accent: #e50914;
            --gold: #f5c518;
            --text: #e8e8f0;
            --text2: #9999b3;
            --text3: #5c5c7a;
            --border: rgba(255,255,255,0.07);
            --radius: 10px;
            --nav-h: 64px;
        }

        html, body { font-family: 'Inter', sans-serif; background: var(--bg); color: var(--text); }

        /* ───── MAIN CONTENT ───── */
        .main-content {
            max-width: 1400px; margin: 0 auto;
            padding: 40px 24px 60px;
        }
        .user-greeting { color: var(--text2); font-size: 0.88rem; padding: 6px 12px; }
        .nav-btn-login {
            text-decoration: none; padding: 8px 20px;
            background: var(--accent); color: #fff;
            border-radius: 20px; font-size: 0.88rem; font-weight: 600; transition: all 0.2s;
        }
        .nav-btn-login:hover { background: #c40812; transform: translateY(-1px); }
        .nav-btn-logout {
            text-decoration: none; padding: 7px 18px;
            border: 1px solid var(--border); color: var(--text2);
            border-radius: 20px; font-size: 0.85rem; font-weight: 500; transition: all 0.2s;
        }
        .nav-btn-logout:hover { border-color: var(--accent); color: var(--accent); }

        /* ─── LAYOUT ─── */
        .watch-page {
            padding-top: var(--nav-h);
            max-width: 1400px; margin: 0 auto; padding-left: 24px; padding-right: 24px;
        }

        /* ─── BREADCRUMB ─── */
        .breadcrumb {
            display: flex; align-items: center; gap: 8px;
            padding: 18px 0 14px;
            font-size: 0.82rem; color: var(--text3);
        }
        .breadcrumb a { color: var(--text3); text-decoration: none; transition: color 0.2s; }
        .breadcrumb a:hover { color: var(--text); }
        .breadcrumb .sep { color: var(--text3); }
        .breadcrumb .current { color: var(--text2); }

        /* ─── MAIN GRID ─── */
        .watch-grid {
            display: grid;
            grid-template-columns: 1fr 340px;
            gap: 24px;
            align-items: start;
        }
        @media (max-width: 900px) {
            .watch-grid { grid-template-columns: 1fr; }
        }

        /* ─── VIDEO PLAYER ─── */
        .player-wrapper {
            background: #000;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 0 60px rgba(0,0,0,0.6);
            position: relative;
        }
        .player-aspect {
            position: relative; width: 100%; padding-bottom: 56.25%;
        }
        .player-aspect iframe {
            position: absolute; inset: 0; width: 100%; height: 100%; border: none;
        }

        /* ─── MOVIE INFO (below player) ─── */
        .movie-header {
            margin-top: 20px;
        }
        .movie-title-row {
            display: flex; align-items: flex-start; justify-content: space-between; gap: 16px;
            margin-bottom: 14px;
        }
        .movie-title {
            font-size: clamp(1.5rem, 3vw, 2rem);
            font-weight: 800; letter-spacing: -0.5px; line-height: 1.2;
        }
        .movie-year-tag {
            background: var(--bg3); color: var(--text2);
            padding: 4px 12px; border-radius: 20px; font-size: 0.82rem;
            white-space: nowrap; margin-top: 4px;
        }

        .movie-badges {
            display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 18px;
            align-items: center;
        }
        .badge {
            display: inline-flex; align-items: center; gap: 5px;
            padding: 5px 12px; border-radius: 6px; font-size: 0.78rem; font-weight: 600;
        }
        .badge-rating { background: rgba(245,197,24,0.15); color: var(--gold); border: 1px solid rgba(245,197,24,0.3); }
        .badge-hd     { background: rgba(229,9,20,0.15);  color: #ff6b6b;     border: 1px solid rgba(229,9,20,0.3); }
        .badge-genre  { background: var(--bg3); color: var(--text2); }
        .badge-views  { background: var(--bg3); color: var(--text2); }

        /* Actions: Share & Favorite */
        .movie-actions {
            display: flex; gap: 12px; margin-bottom: 22px; flex-wrap: wrap;
        }
        .action-btn {
            display: flex; align-items: center; gap: 8px;
            padding: 10px 18px; border-radius: 8px; font-size: 0.85rem; font-weight: 600;
            cursor: pointer; transition: all 0.2s; border: none; text-decoration: none;
            background: rgba(255,255,255,0.06); color: var(--text); outline: none;
        }
        .action-btn:hover { background: rgba(255,255,255,0.1); transform: translateY(-1px); }
        
        .btn-favorite form { margin: 0; padding: 0; }
        .btn-favorite button {
            background: transparent; color: inherit; border: none; padding: 0; cursor: pointer;
            font: inherit; display: flex; align-items: center; gap: 8px; outline: none;
        }
        .action-btn.active-favorite {
            background: rgba(229,9,20,0.1); color: var(--accent); border: 1px solid rgba(229,9,20,0.3);
        }
        .action-btn.active-favorite:hover {
            background: rgba(229,9,20,0.15);
        }

        .movie-desc {
            color: var(--text2); font-size: 0.92rem; line-height: 1.75;
            margin-bottom: 22px;
            display: -webkit-box; -webkit-line-clamp: 4; line-clamp: 4; -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .movie-desc.expanded { -webkit-line-clamp: unset; line-clamp: unset; }
        .desc-toggle {
            background: none; border: none; color: var(--accent); cursor: pointer;
            font-size: 0.82rem; font-weight: 600; padding: 0; margin-bottom: 22px;
        }

        .episodes-section {
            margin-top: 20px;
        }
        .episodes-title {
            font-size: 1.1rem; font-weight: 700; margin-bottom: 12px;
            display: flex; align-items: center; gap: 8px;
        }
        .episodes-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(90px, 1fr));
            gap: 10px;
        }
        .ep-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            background: var(--bg3);
            color: var(--text2);
            padding: 10px 5px;
            border-radius: 8px;
            font-size: 0.82rem;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.2s;
            border: 1px solid var(--border);
            text-align: center;
            white-space: nowrap;
            min-height: 42px;
        }
        .ep-btn:hover {
            background: var(--accent);
            color: #fff;
            border-color: var(--accent);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(229, 9, 20, 0.2);
        }
        .ep-btn.active {
            background: var(--accent); color: #fff; border-color: var(--accent);
            box-shadow: 0 4px 12px rgba(229,9,20,0.3);
        }

        /* Responsive Improvements */
        @media (max-width: 600px) {
            .watch-page { padding-left: 12px; padding-right: 12px; }
            .movie-title { font-size: 1.4rem; }
            .episodes-grid { grid-template-columns: repeat(auto-fill, minmax(70px, 1fr)); gap: 8px; }
            .ep-btn { font-size: 0.75rem; padding: 8px 4px; }
            .auth-card { padding: 30px 20px; }
            .movie-actions { gap: 8px; }
            .action-btn { padding: 8px 12px; font-size: 0.8rem; }
        }

        .info-grid {
            display: grid; grid-template-columns: 1fr 1fr; gap: 14px;
        }
        .info-item label {
            display: block; font-size: 0.72rem; color: var(--text3);
            text-transform: uppercase; letter-spacing: 0.8px; margin-bottom: 4px;
            font-weight: 600;
        }
        .info-item p {
            font-size: 0.88rem; color: var(--text); font-weight: 500;
        }

        /* ─── RIGHT SIDEBAR: COMMENTS ─── */
        .sidebar {
            position: sticky; top: calc(var(--nav-h) + 20px);
        }
        .sidebar-box {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 24px 20px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.4);
        }
        .sidebar-title {
            font-size: 1.1rem; font-weight: 700; margin-bottom: 20px;
            display: flex; align-items: center; gap: 8px;
        }
        .sidebar-title::before {
            content: ''; width: 4px; height: 20px;
            background: var(--accent); border-radius: 4px;
        }

        /* Comment Form */
        .comment-form { margin-bottom: 24px; }
        .comment-form textarea {
            width: 100%; padding: 14px 16px;
            background: rgba(255,255,255,0.03); border: 1px solid var(--border);
            border-radius: 10px; color: var(--text);
            font-family: 'Inter', sans-serif; font-size: 0.9rem;
            resize: none; outline: none; transition: all 0.25s ease;
            min-height: 100px;
        }
        .comment-form textarea:focus {
            border-color: rgba(229,9,20,0.5);
            background: rgba(255,255,255,0.05);
            box-shadow: 0 0 0 3px rgba(229,9,20,0.1);
        }
        .comment-form textarea::placeholder { color: var(--text3); }
        .comment-form button {
            margin-top: 12px; width: 100%;
            padding: 12px; background: var(--accent); color: #fff;
            border: none; border-radius: 10px; font-size: 0.9rem; font-weight: 600;
            cursor: pointer; transition: all 0.2s;
        }
        .comment-form button:hover { background: #c40812; transform: translateY(-1px); }

        /* Login prompt */
        .login-prompt {
            background: rgba(229,9,20,0.07); border: 1px solid rgba(229,9,20,0.2);
            border-radius: 10px; padding: 16px;
            font-size: 0.9rem; color: var(--text2); text-align: center;
            margin-bottom: 24px;
        }
        .login-prompt a { color: var(--accent); font-weight: 600; text-decoration: none; }

        /* Comment list */
        .comment-list {
            display: flex; flex-direction: column; gap: 14px;
            max-height: 600px; overflow-y: auto; padding-right: 8px;
            margin-right: -8px;
        }
        .comment-item {
            padding: 16px; background: rgba(255,255,255,0.02);
            border-radius: 12px; border: 1px solid transparent;
            transition: all 0.2s ease;
        }
        .comment-item:hover {
            background: rgba(255,255,255,0.04);
            border-color: rgba(255,255,255,0.05);
            transform: translateX(2px);
        }
        .comment-meta {
            display: flex; justify-content: space-between; align-items: flex-start;
            margin-bottom: 8px;
        }
        .comment-user-box { display: flex; align-items: center; gap: 10px; }
        .comment-avatar {
            width: 36px; height: 36px;
            background: linear-gradient(135deg, var(--accent), #ff6b35);
            border-radius: 50%; display: flex; align-items: center; justify-content: center;
            font-size: 0.9rem; font-weight: 800; color: #fff; text-transform: uppercase;
        }
        .comment-user-info { display: flex; flex-direction: column; }
        .comment-user-name { font-size: 0.88rem; font-weight: 700; color: var(--text); }
        .comment-date { font-size: 0.72rem; color: var(--text3); margin-top: 2px;}

        .comment-content {
            font-size: 0.88rem; color: var(--text2); line-height: 1.6;
            margin-left: 46px; /* Align with text, not avatar */
        }

        .empty-comments {
            text-align: center; padding: 40px 0; color: var(--text3);
        }
        .empty-comments i { font-size: 2rem; margin-bottom: 12px; display: block; opacity: 0.5; }
        .empty-comments p { font-size: 0.88rem; line-height: 1.5; }

        /* ─── SCROLLBAR ─── */
        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: var(--bg); }
        ::-webkit-scrollbar-thumb { background: var(--bg3); border-radius: 3px; }

        /* ─── BOTTOM SPACER ─── */
        .spacer { height: 60px; }

        /* ─── PREMIUM LOCK ─── */
        .premium-lock-overlay {
            position: absolute; inset: 0;
            background: linear-gradient(135deg, rgba(10,10,15,0.95) 0%, rgba(20,10,30,0.98) 100%);
            display: flex; flex-direction: column; align-items: center; justify-content: center;
            z-index: 10; text-align: center; padding: 40px;
        }
        .premium-lock-icon {
            font-size: 3.5rem; color: var(--gold); margin-bottom: 16px;
            animation: pulse-gold 2s ease-in-out infinite;
        }
        @keyframes pulse-gold {
            0%, 100% { transform: scale(1); filter: drop-shadow(0 0 8px rgba(245,197,24,0.3)); }
            50% { transform: scale(1.1); filter: drop-shadow(0 0 20px rgba(245,197,24,0.6)); }
        }
        .premium-lock-title {
            font-size: 1.4rem; font-weight: 800; color: #fff; margin-bottom: 8px;
        }
        .premium-lock-desc {
            color: var(--text2); font-size: 0.9rem; max-width: 380px; line-height: 1.6; margin-bottom: 24px;
        }
        .premium-badge-tag {
            display: inline-flex; align-items: center; gap: 6px;
            background: linear-gradient(135deg, #f5c518, #ff8c00); color: #000;
            padding: 10px 24px; border-radius: 30px; font-weight: 700; font-size: 0.9rem;
            box-shadow: 0 4px 20px rgba(245,197,24,0.3);
        }
        .premium-card-badge {
            position: absolute; top: 10px; right: 10px;
            background: linear-gradient(135deg, #f5c518, #ff8c00); color: #000;
            font-size: 0.65rem; font-weight: 800; padding: 3px 8px;
            border-radius: 4px; letter-spacing: 0.5px; z-index: 5;
            display: flex; align-items: center; gap: 3px;
        }
    </style>
</head>
<body>

<%@ include file="/components/navbar.jsp" %>

<div class="watch-page">
    <!-- Breadcrumb -->
    <nav class="breadcrumb">
        <a href="${pageContext.request.contextPath}/home"><i class="fas fa-home"></i> Trang chủ</a>
        <span class="sep">/</span>
        <span class="current"><c:out value="${series.title}"/></span>
    </nav>

    <div class="watch-grid">
        <!-- LEFT: Player + Info -->
        <div>
            <!-- Player -->
            <div class="player-wrapper">
                <div class="player-aspect">
                    <c:choose>
                        <c:when test="${premiumLocked}">
                            <!-- Premium Lock Overlay -->
                            <div class="premium-lock-overlay">
                                <div class="premium-lock-icon"><i class="fas fa-crown"></i></div>
                                <div class="premium-lock-title">Nội dung Premium</div>
                                <div class="premium-lock-desc">
                                    Phim này vừa ra mắt và chỉ dành cho thành viên Premium.
                                    Vui lòng nâng cấp tài khoản hoặc quay lại sau 10 ngày để xem miễn phí.
                                </div>
                                <a href="${pageContext.request.contextPath}/premium" style="text-decoration: none;">
                                    <div class="premium-badge-tag">
                                        <i class="fas fa-crown"></i> Nâng cấp Premium
                                    </div>
                                </a>
                            </div>
                        </c:when>
                        <c:when test="${not empty currentEpisode}">
                            <c:if test="${fn:startsWith(currentEpisode.videoUrl, 'http') and not fn:contains(currentEpisode.videoUrl, 'javascript:')}">
                                <iframe src="<c:out value="${currentEpisode.videoUrl}"/>"
                                        allowfullscreen
                                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                        sandbox="allow-scripts allow-same-origin allow-forms">
                                </iframe>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <div style="position:absolute;inset:0;display:flex;align-items:center;justify-content:center;color:#666;">
                                Không có video
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Episodes Picker -->
            <c:if test="${not empty series.episodes}">
                <div class="episodes-section">
                    <div class="episodes-title"><i class="fas fa-list-ul"></i> Chọn tập phim</div>
                    <div class="episodes-grid">
                        <c:forEach var="ep" items="${series.episodes}">
                            <a href="${pageContext.request.contextPath}/watch?id=${series.id}&ep=${ep.id}"
                               class="ep-btn ${currentEpisode.id == ep.id ? 'active' : ''}">
                                <c:out value="${ep.title}"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <!-- Movie Header -->
            <div class="movie-header">
                <div class="movie-title-row">
                    <h1 class="movie-title"><c:out value="${series.title}"/> <c:if test="${not empty currentEpisode}">- <c:out value="${currentEpisode.title}"/></c:if></h1>
                    <span class="movie-year-tag">${series.year}</span>
                </div>

                <div class="movie-badges">
                    <span class="badge badge-hd">
                        <i class="fas fa-film"></i> ${series.type == 'tv' ? 'TV Series' : 'HD'}
                    </span>
                    <c:forTokens items="${series.genre}" delims="," var="g">
                        <span class="badge badge-genre"><c:out value="${g}"/></span>
                    </c:forTokens>
                    <span class="badge badge-views">
                        <i class="fas fa-eye"></i> ${series.views != null ? series.views : 0} lượt xem
                    </span>
                </div>

                    <!-- ACTIONS: Favorite & Share -->
                <div class="movie-actions">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <div class="action-btn btn-favorite ${isFavorite ? 'active-favorite' : ''}">
                                <form action="${pageContext.request.contextPath}/favorite/toggle" method="POST">
                                    <input type="hidden" name="seriesId" value="${series.id}">
                                    <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                                    <button type="submit">
                                        <i class="fa-${isFavorite ? 'solid' : 'regular'} fa-heart"></i>
                                        ${isFavorite ? 'Đã Yêu thích' : 'Yêu thích'}
                                    </button>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login?redirect=watch" class="action-btn btn-favorite">
                                <i class="fa-regular fa-heart"></i> Yêu thích
                            </a>
                        </c:otherwise>
                    </c:choose>

                    <c:set var="reqUrl" value="${pageContext.request.requestURL}" />
                    <c:set var="baseUrl" value="${fn:replace(reqUrl, pageContext.request.requestURI, '')}${pageContext.request.contextPath}" />

                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <a href="javascript:void(0)" onclick="openShareModal()" class="action-btn">
                                <i class="fas fa-envelope"></i> Gửi qua Email
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login?redirect=watch" class="action-btn">
                                <i class="fas fa-envelope"></i> Gửi qua Email
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Thông báo gửi mail thành công/thất bại -->
                <c:if test="${not empty sessionScope.shareSuccess}">
                    <div style="background: rgba(46, 204, 113, 0.2); border: 1px solid rgba(46, 204, 113, 0.4); color: #2ecc71; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: 500;">
                        <i class="fas fa-check-circle"></i> ${sessionScope.shareSuccess}
                    </div>
                    <c:remove var="shareSuccess" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.shareError}">
                    <div style="background: rgba(229, 9, 20, 0.2); border: 1px solid rgba(229, 9, 20, 0.4); color: #ff6b6b; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: 500;">
                        <i class="fas fa-exclamation-circle"></i> ${sessionScope.shareError}
                    </div>
                    <c:remove var="shareError" scope="session"/>
                </c:if>

                <!-- Modal Share Email -->
                <div id="shareModal" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.85); z-index:9999; align-items:center; justify-content:center; backdrop-filter:blur(5px);">
                    <div style="background:var(--card); width:100%; max-width:450px; border-radius:12px; padding:25px; border:1px solid var(--border); box-shadow: 0 10px 40px rgba(0,0,0,0.8); position:relative;">
                        <button onclick="closeShareModal()" style="position:absolute; top:20px; right:20px; background:none; border:none; color:var(--text3); font-size:1.2rem; cursor:pointer;"><i class="fas fa-times"></i></button>
                        <h3 style="font-size:1.2rem; font-weight:700; margin-bottom:15px; display:flex; align-items:center; gap:8px;"><i class="fas fa-share-alt" style="color:var(--accent);"></i> Chia sẻ phim này</h3>
                        <p style="color:var(--text2); font-size:0.9rem; margin-bottom:20px;">Gửi thông tin bộ phim <b><c:out value="${series.title}"/></b> tới bạn bè.</p>

                        <form action="${pageContext.request.contextPath}/share" method="POST">
                            <input type="hidden" name="seriesId" value="${series.id}">
                            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                            <div style="margin-bottom:16px;">
                                <label style="display:block; font-size:0.85rem; color:var(--text3); margin-bottom:8px; font-weight:600;">Email người nhận *</label>
                                <input type="email" name="email" required placeholder="nhapemail@gmail.com" style="width:100%; padding:12px 14px; background:rgba(255,255,255,0.05); border:1px solid var(--border); border-radius:8px; color:var(--text); outline:none;">
                            </div>
                            <div style="margin-bottom:24px;">
                                <label style="display:block; font-size:0.85rem; color:var(--text3); margin-bottom:8px; font-weight:600;">Lời nhắn (Tùy chọn)</label>
                                <textarea name="message" rows="3" placeholder="Phim này hay lắm nà..." style="width:100%; padding:12px 14px; background:rgba(255,255,255,0.05); border:1px solid var(--border); border-radius:8px; color:var(--text); outline:none; resize:none; font-family:'Inter',sans-serif;"></textarea>
                            </div>
                            <button type="submit" style="width:100%; padding:14px; background:var(--accent); color:#fff; border:none; border-radius:8px; font-weight:700; font-size:0.95rem; cursor:pointer; transition:all 0.2s;">Gửi Email <i class="fas fa-paper-plane" style="margin-left:5px;"></i></button>
                        </form>
                    </div>
                </div>

                <p class="movie-desc" id="movieDesc"><c:out value="${series.description}"/></p>
                <button class="desc-toggle" id="descToggle" onclick="toggleDesc()">Xem thêm ▼</button>

                <div class="info-grid">
                    <div class="info-item">
                        <label>Đạo diễn</label>
                        <p><c:out value="${empty series.director ? 'Đang cập nhật' : series.director}"></c:out></p>
                    </div>
                    <div class="info-item">
                        <label>Năm phát hành</label>
                        <p>${series.year}</p>
                    </div>
                    <div class="info-item" style="grid-column:1/-1;">
                        <label>Diễn viên</label>
                        <p><c:out value="${empty series.actors ? 'Đang cập nhật' : series.actors}"></c:out></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- RIGHT: Comments Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-box">
                <div class="sidebar-title">Bình luận</div>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <form class="comment-form" action="${pageContext.request.contextPath}/addComment" method="post">
                            <input type="hidden" name="seriesId" value="${series.id}">
                            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                            <textarea name="content" placeholder="Chia sẻ cảm nhận của bạn về phim..." required></textarea>
                            <button type="submit">
                                <i class="fas fa-paper-plane"></i> Gửi bình luận
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="login-prompt">
                            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a> để bình luận về phim này
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="comment-list">
                    <c:choose>
                        <c:when test="${not empty comments}">
                            <c:forEach var="c" items="${comments}">
                                <div class="comment-item">
                                    <div class="comment-meta">
                                        <div class="comment-user-box">
                                            <div class="comment-avatar">${fn:substring(c.user.fullname, 0, 1)}</div>
                                            <div class="comment-user-info">
                                                <span class="comment-user-name"><c:out value="${c.user.fullname}"/></span>
                                                <span class="comment-date">${c.createdAt}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="comment-content"><c:out value="${c.content}"/></div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-comments">
                                <i class="fas fa-comments"></i>
                                <p>Chưa có bình luận nào.<br>Hãy là người đầu tiên!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </aside>
    </div>

    <div class="spacer"></div>
</div>

<script>
    function toggleDesc() {
        const desc = document.getElementById('movieDesc');
        const btn  = document.getElementById('descToggle');
        if (desc.classList.contains('expanded')) {
            desc.classList.remove('expanded');
            btn.textContent = 'Xem thêm ▼';
        } else {
            desc.classList.add('expanded');
            btn.textContent = 'Thu gọn ▲';
        }
    }
    
    function openShareModal() {
        document.getElementById('shareModal').style.display = 'flex';
    }
    
    function closeShareModal() {
        document.getElementById('shareModal').style.display = 'none';
    }
</script>
</body>
</html>