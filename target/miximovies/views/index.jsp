<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MixiMovies — Xem phim trực tuyến miễn phí</title>
    <meta name="description" content="MixiMovies — Kho phim khổng lồ, xem phim HD miễn phí, cập nhật liên tục.">
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
            --card-hover: #1e1e2a;
            --accent: #e50914;
            --accent2: #ff6b35;
            --gold: #f5c518;
            --text: #e8e8f0;
            --text2: #9999b3;
            --text3: #5c5c7a;
            --border: rgba(255,255,255,0.07);
            --radius: 10px;
            --nav-h: 64px;
        }

        html { scroll-behavior: smooth; }
        body {
            font-family: 'Inter', sans-serif;
            background: var(--bg);
            color: var(--text);
            min-height: 100vh;
        }

        /* ───── HERO ───── */
        .hero {
            height: 480px;
            background: linear-gradient(135deg, #0d0d1a 0%, #1a0a20 40%, #0a0f1a 100%);
            display: flex; align-items: center; justify-content: center;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        /* ───── HERO ───── */
        .hero {
            height: 420px;
            background: linear-gradient(135deg, #0d0d1a 0%, #1a0a20 40%, #0a0f1a 100%);
            display: flex; align-items: center; justify-content: center;
            text-align: center;
            position: relative;
            overflow: hidden;
            padding-top: var(--nav-h);
        }
        .hero::before {
            content: '';
            position: absolute; inset: 0;
            background: radial-gradient(ellipse at 60% 50%, rgba(229,9,20,0.15) 0%, transparent 65%),
                        radial-gradient(ellipse at 20% 50%, rgba(111,0,255,0.1) 0%, transparent 60%);
        }
        .hero-particles {
            position: absolute; inset: 0; overflow: hidden;
        }
        .hero-particles span {
            position: absolute; width: 2px; height: 2px;
            background: rgba(255,255,255,0.5); border-radius: 50%;
            animation: float 8s infinite ease-in-out;
        }
        @keyframes float {
            0%, 100% { transform: translateY(0) translateX(0); opacity: 0; }
            10% { opacity: 1; }
            90% { opacity: 1; }
            100% { transform: translateY(-120px) translateX(30px); opacity: 0; }
        }
        .hero-content { position: relative; z-index: 1; }
        .hero-badge {
            display: inline-block; padding: 5px 14px;
            background: rgba(229,9,20,0.2); border: 1px solid rgba(229,9,20,0.4);
            border-radius: 20px; color: #ff6b6b;
            font-size: 0.78rem; font-weight: 600; letter-spacing: 1px; text-transform: uppercase;
            margin-bottom: 18px;
        }
        .hero h1 {
            font-size: clamp(2rem, 5vw, 3.2rem);
            font-weight: 800; letter-spacing: -1px;
            line-height: 1.15; margin-bottom: 16px;
        }
        .hero h1 span { color: var(--accent); }
        .hero p {
            color: var(--text2); font-size: 1rem; max-width: 500px;
            margin: 0 auto 28px;
        }
        .hero-search {
            display: flex; gap: 10px; justify-content: center;
            max-width: 480px; margin: 0 auto;
        }
        .hero-search input {
            flex: 1; padding: 13px 20px;
            background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.15);
            border-radius: 40px; color: #fff; font-size: 0.92rem;
            outline: none; transition: all 0.2s; backdrop-filter: blur(10px);
        }
        .hero-search input::placeholder { color: var(--text3); }
        .hero-search input:focus { border-color: rgba(229,9,20,0.5); background: rgba(255,255,255,0.12); }
        .hero-search button {
            padding: 13px 24px;
            background: var(--accent); color: #fff;
            border: none; border-radius: 40px; font-size: 0.9rem; font-weight: 600;
            cursor: pointer; transition: all 0.2s;
        }
        .hero-search button:hover { background: #c40812; transform: translateY(-1px); }

        /* ───── MAIN CONTENT ───── */
        .main-content { max-width: 1400px; margin: 0 auto; padding: 40px 24px 60px; }

        .section-header {
            display: flex; align-items: center; justify-content: space-between;
            margin-bottom: 22px;
        }
        .section-title {
            font-size: 1.25rem; font-weight: 700;
            display: flex; align-items: center; gap: 10px;
        }
        .section-title::before {
            content: ''; width: 4px; height: 22px;
            background: var(--accent); border-radius: 4px; display: block;
        }
        .section-count {
            font-size: 0.8rem; color: var(--text3);
            background: var(--bg3); padding: 3px 10px; border-radius: 20px;
        }

        /* ───── MOVIE GRID ───── */
        .movie-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 18px;
        }

        .movie-card {
            background: var(--card);
            border-radius: var(--radius);
            overflow: hidden;
            transition: transform 0.25s ease, box-shadow 0.25s ease;
            cursor: pointer;
            position: relative;
            border: 1px solid var(--border);
            text-decoration: none; color: inherit;
            display: block;
        }
        .movie-card:hover {
            transform: translateY(-6px) scale(1.02);
            box-shadow: 0 20px 40px rgba(0,0,0,0.5);
            border-color: rgba(229,9,20,0.3);
        }
        .movie-card:hover .card-overlay { opacity: 1; }
        .movie-card:hover .card-play { transform: scale(1); opacity: 1; }

        .card-poster {
            position: relative; width: 100%; aspect-ratio: 2/3; overflow: hidden;
        }
        .card-poster img {
            width: 100%; height: 100%; object-fit: cover;
            transition: transform 0.4s ease;
        }
        .movie-card:hover .card-poster img { transform: scale(1.06); }

        .card-overlay {
            position: absolute; inset: 0;
            background: linear-gradient(to top, rgba(0,0,0,0.9) 0%, rgba(0,0,0,0.2) 50%, rgba(0,0,0,0.5) 100%);
            opacity: 0; transition: opacity 0.3s;
            display: flex; align-items: center; justify-content: center;
        }
        .card-play {
            width: 48px; height: 48px;
            background: var(--accent); border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            transform: scale(0.8); opacity: 0;
            transition: transform 0.25s ease, opacity 0.25s ease;
            color: #fff; font-size: 1.1rem; padding-left: 3px;
        }

        .card-badge {
            position: absolute; top: 10px; left: 10px;
            background: var(--accent); color: #fff;
            font-size: 0.68rem; font-weight: 700; padding: 3px 8px;
            border-radius: 4px; letter-spacing: 0.5px;
        }
        .card-rating-badge {
            position: absolute; top: 10px; right: 10px;
            background: rgba(0,0,0,0.75); backdrop-filter: blur(6px);
            color: var(--gold);
            font-size: 0.72rem; font-weight: 700; padding: 3px 8px;
            border-radius: 4px; display: flex; align-items: center; gap: 4px;
        }

        .card-info {
            padding: 12px 14px 14px;
        }
        .card-title {
            font-size: 0.88rem; font-weight: 600;
            white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
            margin-bottom: 6px;
        }
        .card-meta {
            display: flex; align-items: center; justify-content: space-between;
            font-size: 0.75rem; color: var(--text2);
        }

        .card-views {
            display: flex; align-items: center; gap: 4px;
            color: var(--text3);
        }

        /* ───── EMPTY STATE ───── */
        .empty-state {
            grid-column: 1/-1;
            text-align: center; padding: 80px 20px;
            color: var(--text3);
        }
        .empty-state i { font-size: 3rem; margin-bottom: 16px; display: block; }
        .empty-state p { font-size: 1rem; }

        /* ───── FOOTER ───── */
        footer {
            background: var(--bg2);
            border-top: 1px solid var(--border);
            padding: 30px 24px;
            text-align: center;
            color: var(--text3);
            font-size: 0.82rem;
        }
        footer a { color: var(--accent); text-decoration: none; }

        /* ───── SCROLLBAR ───── */
        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: var(--bg); }
        ::-webkit-scrollbar-thumb { background: var(--bg3); border-radius: 3px; }
        ::-webkit-scrollbar-thumb:hover { background: var(--text3); }

        .filter-status {
            padding: 0 0 18px; display: flex; align-items: center; justify-content: space-between;
        }
        .clear-filter {
            color: var(--accent); font-size: 0.82rem; text-decoration: none; font-weight: 600;
        }
        .clear-filter:hover { text-decoration: underline; }
    </style>
</head>
<body>

<%@ include file="/components/navbar.jsp" %>

<!-- Hero Section -->
<section class="hero">
    <div class="hero-particles">
        <span style="left:10%; top:70%; animation-delay:0s; animation-duration:7s;"></span>
        <span style="left:25%; top:80%; animation-delay:1.5s; animation-duration:9s;"></span>
        <span style="left:50%; top:75%; animation-delay:0.5s; animation-duration:6s;"></span>
        <span style="left:70%; top:60%; animation-delay:2s; animation-duration:8s;"></span>
        <span style="left:85%; top:85%; animation-delay:1s; animation-duration:7.5s;"></span>
        <span style="left:40%; top:90%; animation-delay:3s; animation-duration:10s;"></span>
    </div>
    <div class="hero-content">
        <div class="hero-badge">🔥 Phim HD Miễn phí</div>
        <h1>Khám phá <span>vũ trụ phim</span><br>không giới hạn</h1>
        <p>Hàng nghìn bộ phim chất lượng cao, xem ngay — không cần đăng ký thẻ tín dụng.</p>
        <div class="hero-search">
            <input type="text" placeholder="Tìm kiếm phim, đạo diễn, diễn viên..." id="searchInput" oninput="filterMovies(this.value)">
            <button onclick="filterMovies(document.getElementById('searchInput').value)">
                <i class="fas fa-search"></i> Tìm
            </button>
        </div>
    </div>
</section>

<!-- Movie Listing -->
<main class="main-content">

    <div class="section-header">
        <div class="section-title">
            ${empty currentCategory ? 'Tất cả phim' : currentCategory.name}
        </div>
        <c:if test="${not empty currentCategory}">
            <a href="${pageContext.request.contextPath}/home" class="clear-filter"><i class="fas fa-times"></i> Xóa lọc (${fn:length(seriesList)} kết quả)</a>
        </c:if>
        <c:if test="${empty currentCategory}">
            <span class="section-count" id="movieCount">${fn:length(seriesList)} phim</span>
        </c:if>
    </div>

    <div class="movie-grid" id="movieGrid">
        <c:forEach var="series" items="${seriesList}">
            <a href="${pageContext.request.contextPath}/watch?id=${series.id}" class="movie-card"
               data-title="${series.title}" data-genre="${series.genre}" data-year="${series.year}">
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
                        <span class="card-views">
                            <i class="fas fa-eye" style="font-size:0.65rem;"></i>
                            ${series.views != null ? series.views : 0}
                        </span>
                    </div>
                </div>
            </a>
        </c:forEach>

        <c:if test="${empty seriesList}">
            <div class="empty-state">
                <i class="fas fa-film"></i>
                <p>Chưa có phim nào. Admin hãy thêm phim mới!</p>
            </div>
        </c:if>
    </div>
</main>

<footer>
    <p>© 2025 <a href="#">MixiMovies</a> — Xem phim trực tuyến miễn phí chất lượng HD</p>
</footer>

<script>
    // Live search filter
    function filterMovies(q) {
        q = q.toLowerCase().trim();
        const cards = document.querySelectorAll('.movie-card');
        let visible = 0;
        cards.forEach(card => {
            const title = (card.dataset.title || '').toLowerCase();
            const genre = (card.dataset.genre || '').toLowerCase();
            const year  = (card.dataset.year  || '').toLowerCase();
            const match = !q || title.includes(q) || genre.includes(q) || year.includes(q);
            card.style.display = match ? '' : 'none';
            if (match) visible++;
        });
        document.getElementById('movieCount').textContent = visible + ' phim';

        // show empty state
        const grid = document.getElementById('movieGrid');
        let emptyMsg = grid.querySelector('.search-empty');
        if (visible === 0 && q) {
            if (!emptyMsg) {
                emptyMsg = document.createElement('div');
                emptyMsg.className = 'empty-state search-empty';
                const icon = document.createElement('i');
                icon.className = 'fas fa-search';
                const p = document.createElement('p');
                p.textContent = 'Không tìm thấy phim nào cho "';
                const strong = document.createElement('strong');
                strong.textContent = q;
                p.appendChild(strong);
                p.appendChild(document.createTextNode('"'));
                emptyMsg.appendChild(icon);
                emptyMsg.appendChild(p);
                grid.appendChild(emptyMsg);
            }
        } else if (emptyMsg) {
            emptyMsg.remove();
        }
    }

    // Navbar scroll effect
    const nav = document.querySelector('.mixi-nav');
    window.addEventListener('scroll', () => {
        if (window.scrollY > 60) {
            nav.style.background = 'rgba(10,10,15,0.98)';
            nav.style.backdropFilter = 'blur(20px)';
        } else {
            nav.style.background = 'linear-gradient(to bottom, rgba(0,0,0,0.95) 0%, rgba(0,0,0,0) 100%)';
        }
    });
</script>
</body>
</html>