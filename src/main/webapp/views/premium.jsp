<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Nâng cấp Premium — MixiMovies</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        :root {
            --bg: #0a0a0f;
            --accent: #e50914;
            --gold: #f5c518;
            --card: #16161f;
            --text: #ffffff;
            --text2: #9999b3;
        }
        body {
            background-color: var(--bg);
            color: var(--text);
            font-family: 'Inter', sans-serif;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }
        .premium-container {
            max-width: 1200px;
            margin: 80px auto;
            padding: 0 24px;
            text-align: center;
        }
        .premium-header h1 {
            font-size: 3rem;
            font-weight: 800;
            margin-bottom: 16px;
            background: linear-gradient(135deg, #fff 0%, #aaa 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .premium-header p {
            font-size: 1.1rem;
            color: var(--text2);
            max-width: 600px;
            margin: 0 auto 50px;
            line-height: 1.6;
        }
        .tiers-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
            gap: 30px;
            margin-top: 40px;
        }
        .tier-card {
            background: var(--card);
            border-radius: 24px;
            padding: 48px 32px;
            border: 1px solid rgba(255,255,255,0.05);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            display: flex;
            flex-direction: column;
        }
        .tier-card:hover {
            transform: translateY(-12px);
            border-color: var(--gold);
            box-shadow: 0 20px 40px rgba(245, 197, 24, 0.1);
        }
        .tier-card.popular {
            border-color: var(--accent);
            background: linear-gradient(180deg, rgba(229, 9, 20, 0.05) 0%, var(--card) 100%);
        }
        .popular-badge {
            position: absolute;
            top: -15px;
            left: 50%;
            transform: translateX(-50%);
            background: var(--accent);
            padding: 6px 20px;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .tier-icon {
            font-size: 2.5rem;
            color: var(--gold);
            margin-bottom: 24px;
        }
        .tier-name {
            font-size: 1.5rem;
            font-weight: 800;
            margin-bottom: 12px;
        }
        .tier-price {
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 30px;
        }
        .tier-price span {
            font-size: 1rem;
            color: var(--text2);
            font-weight: 400;
        }
        .features-list {
            list-style: none;
            padding: 0;
            margin: 0 0 40px;
            text-align: left;
            flex-grow: 1;
        }
        .features-list li {
            padding: 12px 0;
            color: var(--text2);
            font-size: 0.95rem;
            display: flex;
            align-items: center;
            gap: 12px;
        }
        .features-list li i {
            color: #22c55e;
            font-size: 1.1rem;
        }
        .tier-btn {
            display: block;
            text-decoration: none;
            background: #fff;
            color: #000;
            padding: 16px;
            border-radius: 12px;
            font-weight: 700;
            transition: all 0.2s;
        }
        .tier-card.popular .tier-btn {
            background: var(--gold);
        }
        .tier-btn:hover {
            opacity: 0.9;
            transform: scale(1.02);
        }
        .back-link {
            display: inline-block;
            margin-top: 60px;
            color: var(--text2);
            text-decoration: none;
            font-size: 0.9rem;
            transition: color 0.2s;
        }
        .back-link:hover {
            color: #fff;
        }
    </style>
</head>
<body>
    <%@ include file="/components/navbar.jsp" %>

    <div class="premium-container">
        <div class="premium-header">
            <h1>Mở khóa trải nghiệm điện ảnh tối thượng</h1>
            <p>Đăng ký gói Premium để xem ngay mọi bộ phim bom tấn ngay khi vừa ra mắt, xem chất lượng 4K và không quảng cáo.</p>
        </div>

        <div class="tiers-grid">
            <!-- Gói Tháng -->
            <div class="tier-card">
                <div class="tier-icon"><i class="fas fa-gem"></i></div>
                <div class="tier-name">Gói Tháng</div>
                <div class="tier-price">10.000đ <span>/tháng</span></div>
                <ul class="features-list">
                    <li><i class="fas fa-check-circle"></i> Xem ngay phim vừa ra mắt</li>
                    <li><i class="fas fa-check-circle"></i> Chất lượng Full HD / 4K</li>
                    <li><i class="fas fa-check-circle"></i> Không giới hạn thiết bị</li>
                    <li><i class="fas fa-check-circle"></i> Hỗ trợ ưu tiên 24/7</li>
                </ul>
                <a href="${pageContext.request.contextPath}/premium/checkout?plan=month" class="tier-btn">Đăng ký ngay</a>
            </div>

            <!-- Gói Năm -->
            <div class="tier-card popular">
                <div class="popular-badge">Tiết kiệm 20%</div>
                <div class="tier-icon"><i class="fas fa-crown"></i></div>
                <div class="tier-name">Gói Năm</div>
                <div class="tier-price">20.000đ <span>/năm</span></div>
                <ul class="features-list">
                    <li><i class="fas fa-check-circle"></i> Tất cả quyền lợi gói tháng</li>
                    <li><i class="fas fa-check-circle"></i> Tiết kiệm chi phí hơn</li>
                    <li><i class="fas fa-check-circle"></i> Huy hiệu Crown vĩnh viễn</li>
                    <li><i class="fas fa-check-circle"></i> Xem trước trailer độc quyền</li>
                </ul>
                <a href="${pageContext.request.contextPath}/premium/checkout?plan=year" class="tier-btn" style="background: var(--gold);">Đăng ký ngay</a>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/home" class="back-link"><i class="fas fa-arrow-left"></i> Quay lại trang chủ</a>
    </div>

    <footer style="margin-top: 100px; padding: 40px; text-align: center; border-top: 1px solid rgba(255,255,255,0.05); color: #555;">
        MixiMovies Premium Integration — Secure Payment Simulation
    </footer>
</body>
</html>
