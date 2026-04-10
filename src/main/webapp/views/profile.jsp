<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Cá Nhân — MixiMovies</title>
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
            --text: #e8e8f0;
            --text2: #9999b3;
            --text3: #5c5c7a;
            --gold: #f5c518;
            --border: rgba(255,255,255,0.07);
            --radius: 10px;
            --nav-h: 64px;
        }

        html { scroll-behavior: smooth; }
        body { font-family: 'Inter', sans-serif; background: var(--bg); color: var(--text); min-height: 100vh; }

        /* ───── MAIN CONTENT ───── */

        /* ───── MAIN CONTENT ───── */
        .main-content { max-width: 1400px; margin: 0 auto; padding: calc(var(--nav-h) + 40px) 24px 60px; min-height: calc(100vh - 80px); }

        /* ───── PROFILE HEADER ───── */
        .profile-header {
            background: linear-gradient(135deg, var(--card) 0%, var(--bg2) 100%);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 40px;
            display: flex;
            align-items: center;
            gap: 30px;
            margin-bottom: 50px;
            position: relative;
            overflow: hidden;
        }
        .profile-header::before {
            content: '';
            position: absolute; right: 0; top: 0; bottom: 0; width: 40%;
            background: radial-gradient(circle at center right, rgba(229,9,20,0.1) 0%, transparent 70%);
            pointer-events: none;
        }
        .profile-avatar {
            width: 100px; height: 100px;
            background: linear-gradient(135deg, var(--accent), #ff6b35);
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 2.8rem; font-weight: 800; color: #fff; text-transform: uppercase;
            box-shadow: 0 10px 30px rgba(229,9,20,0.3);
            flex-shrink: 0;
        }
        .profile-info h1 { font-size: 2.2rem; font-weight: 800; margin-bottom: 8px; letter-spacing: -0.5px; }
        .profile-info p { color: var(--text2); font-size: 1rem; margin-bottom: 12px; display: flex; align-items: center; gap: 8px; }
        .role-badge {
            display: inline-block; padding: 4px 12px;
            background: rgba(255,255,255,0.08); border-radius: 20px;
            font-size: 0.75rem; font-weight: 600; text-transform: uppercase;
            letter-spacing: 1px; color: var(--gold); border: 1px solid rgba(245,197,24,0.3);
        }
        .role-member { color: #4facfe; border-color: rgba(79,172,254,0.3); }

        /* ───── SECTION TITLE ───── */
        .section-title {
            font-size: 1.4rem; font-weight: 700; margin-bottom: 24px;
            display: flex; align-items: center; justify-content: space-between;
        }
        .section-title-left {
            display: flex; align-items: center; gap: 10px;
        }
        .section-title-left i { color: var(--accent); }
        .movie-count {
            font-size: 0.85rem; color: var(--text2); background: var(--bg3);
            padding: 4px 14px; border-radius: 20px; font-weight: 500;
        }

        /* ───── MOVIE GRID ───── */
        .movie-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; }
        .movie-card {
            background: var(--card); border-radius: var(--radius); overflow: hidden;
            transition: transform 0.25s ease, box-shadow 0.25s ease; cursor: pointer;
            position: relative; border: 1px solid var(--border); text-decoration: none; color: inherit; display: block;
        }
        .movie-card:hover { transform: translateY(-6px) scale(1.02); box-shadow: 0 20px 40px rgba(0,0,0,0.5); border-color: rgba(229,9,20,0.3); }
        .movie-card:hover .card-overlay { opacity: 1; }
        .movie-card:hover .card-play { transform: scale(1); opacity: 1; }
        .card-poster { position: relative; width: 100%; aspect-ratio: 2/3; overflow: hidden; }
        .card-poster img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.4s ease; }
        .movie-card:hover .card-poster img { transform: scale(1.06); }
        .card-overlay { position: absolute; inset: 0; background: linear-gradient(to top, rgba(0,0,0,0.9) 0%, rgba(0,0,0,0.2) 50%, rgba(0,0,0,0.5) 100%); opacity: 0; transition: opacity 0.3s; display: flex; align-items: center; justify-content: center; }
        .card-play { width: 48px; height: 48px; background: var(--accent); border-radius: 50%; display: flex; align-items: center; justify-content: center; transform: scale(0.8); opacity: 0; transition: transform 0.25s ease, opacity 0.25s ease; color: #fff; font-size: 1.1rem; padding-left: 3px; }
        .card-badge { position: absolute; top: 10px; left: 10px; background: var(--accent); color: #fff; font-size: 0.68rem; font-weight: 700; padding: 3px 8px; border-radius: 4px; letter-spacing: 0.5px; }
        .card-info { padding: 12px 14px 14px; }
        .card-title { font-size: 0.95rem; font-weight: 600; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-bottom: 6px; }
        .card-meta { display: flex; align-items: center; justify-content: space-between; font-size: 0.8rem; color: var(--text2); }

        /* ───── EMPTY STATE ───── */
        .empty-state {
            grid-column: 1/-1; text-align: center; padding: 80px 20px;
            color: var(--text3); background: rgba(255,255,255,0.02);
            border-radius: 16px; border: 2px dashed rgba(255,255,255,0.05);
        }
        .empty-state i { font-size: 3.5rem; margin-bottom: 20px; color: rgba(255,255,255,0.1); }
        .empty-state h3 { color: var(--text); font-size: 1.3rem; margin-bottom: 8px; }
        .empty-state p { font-size: 0.95rem; margin-bottom: 24px; max-width: 400px; margin-left: auto; margin-right: auto; }
        .empty-btn {
            display: inline-block; padding: 12px 28px; background: var(--accent); color: #fff; text-decoration: none;
            border-radius: 30px; font-weight: 600; transition: all 0.2s;
        }
        .empty-btn:hover { background: #c40812; transform: translateY(-2px); box-shadow: 0 4px 15px rgba(229,9,20,0.4); }

        /* ───── FOOTER ───── */
        footer { background: var(--bg2); border-top: 1px solid var(--border); padding: 30px 24px; text-align: center; color: var(--text3); font-size: 0.82rem; }
        footer a { color: var(--accent); text-decoration: none; }
    </style>
</head>
<body>

<%@ include file="/components/navbar.jsp" %>

<main class="main-content">
    
    <!-- Tiêu điểm cá nhân -->
    <div class="profile-header">
        <div class="profile-avatar">
            <c:out value="${fn:substring(sessionScope.user.fullname, 0, 1)}"/>
        </div>
        <div class="profile-info">
            <h1><c:out value="${sessionScope.user.fullname}"/></h1>
            <p><i class="fas fa-envelope"></i> <c:out value="${sessionScope.user.email}"/></p>
            <c:choose>
                <c:when test="${sessionScope.user.admin}">
                    <span class="role-badge"><i class="fas fa-crown"></i> Quản trị viên</span>
                </c:when>
                <c:otherwise>
                    <span class="role-badge role-member"><i class="fas fa-user"></i> Thành viên</span>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Tủ phim yêu thích -->
    <div class="section-title">
        <div class="section-title-left">
            <i class="fas fa-heart"></i> Tủ phim yêu thích
        </div>
        <span class="movie-count">${fn:length(favoriteSeries)} phim</span>
    </div>

    <div class="movie-grid">
        <c:forEach var="series" items="${favoriteSeries}">
            <a href="${pageContext.request.contextPath}/watch?id=${series.id}" class="movie-card">
                <div class="card-poster">
                    <img src="${series.poster != 'N/A' && !empty series.poster ? series.poster : 'https://via.placeholder.com/200x300/1a1a24/666?text=No+Poster'}"
                         alt="${series.title}" loading="lazy">
                    <div class="card-badge">${series.type == 'tv' ? 'TV' : 'HD'}</div>
                    <div class="card-overlay">
                        <div class="card-play">
                            <i class="fas fa-play"></i>
                        </div>
                    </div>
                </div>
                <div class="card-info">
                    <div class="card-title"><c:out value="${series.title}"/></div>
                    <div class="card-meta">
                        <span class="card-year"><c:out value="${series.year}"/></span>
                    </div>
                </div>
            </a>
        </c:forEach>

        <c:if test="${empty favoriteSeries}">
            <div class="empty-state">
                <i class="fas fa-film"></i>
                <h3>Tủ phim trống</h3>
                <p>Bạn chưa thêm bộ phim nào vào danh sách yêu thích. Hãy khám phá kho phim khổng lồ của MixiMovies ngay!</p>
                <a href="${pageContext.request.contextPath}/home" class="empty-btn">Khám phá Phim</a>
            </div>
        </c:if>
    </div>
</main>

<footer>
    <p>© 2025 <a href="#">MixiMovies</a> — Xem phim trực tuyến miễn phí chất lượng HD</p>
</footer>

</body>
</html>
