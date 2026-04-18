<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thành công — MixiMovies</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { background: #0a0a0f; color: #fff; font-family: 'Inter', sans-serif; display: flex; align-items: center; justify-content: center; min-height: 100vh; margin: 0; }
        .success-box { text-align: center; max-width: 500px; padding: 40px; }
        .success-icon { font-size: 5rem; color: #22c55e; margin-bottom: 30px; animation: bounce 1s; }
        @keyframes bounce { 0%, 20%, 50%, 80%, 100% {transform: translateY(0);} 40% {transform: translateY(-20px);} 60% {transform: translateY(-10px);} }
        h1 { font-size: 2.2rem; font-weight: 800; margin-bottom: 20px; }
        p { color: #999; font-size: 1.1rem; line-height: 1.6; margin-bottom: 40px; }
        .home-btn { background: #e50914; color: #fff; text-decoration: none; padding: 18px 40px; border-radius: 40px; font-weight: 700; display: inline-block; transition: all 0.2s; }
        .home-btn:hover { transform: scale(1.05); box-shadow: 0 10px 30px rgba(229, 9, 20, 0.4); }
    </style>
</head>
<body>
    <div class="success-box">
        <div class="success-icon"><i class="fas fa-check-circle"></i></div>
        <h1>Tuyệt vời!</h1>
        <p>${message}</p>
        <a href="${pageContext.request.contextPath}/home" class="home-btn">Bắt đầu trải nghiệm ngay</a>
    </div>
</body>
</html>
