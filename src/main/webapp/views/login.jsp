<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập | MixiMovies</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        
        :root {
            --bg: #0a0a0f;
            --bg2: #12121a;
            --card: #16161f;
            --accent: #e50914;
            --text: #e8e8f0;
            --text2: #9090b0;
            --text3: #55556a;
            --border: rgba(255,255,255,0.05);
            --nav-h: 70px;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: var(--bg);
            background-image: radial-gradient(circle at top right, rgba(229,9,20,0.05), transparent 40%),
                              radial-gradient(circle at bottom left, rgba(124,58,237,0.05), transparent 40%);
            color: var(--text);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* Navbar basic embed CSS to ensure it looks ok if loaded standalone without global css */
        .mixi-nav {
            height: var(--nav-h); background: rgba(10,10,15,0.85); backdrop-filter: blur(12px);
            border-bottom: 1px solid var(--border);
            position: sticky; top: 0; z-index: 100;
        }
        .nav-inner {
            max-width: 1300px; margin: 0 auto; padding: 0 40px; height: 100%;
            display: flex; align-items: center; justify-content: space-between;
        }
        .nav-brand {
            display: flex; align-items: center; gap: 10px; text-decoration: none;
        }
        .brand-icon { font-size: 1.4rem; }
        .brand-text {
            font-size: 1.35rem; font-weight: 800;
            background: linear-gradient(135deg, #fff 0%, #e50914 100%);
            -webkit-background-clip: text; background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .nav-links { display: flex; align-items: center; gap: 8px; list-style: none; }
        .nav-link-item { color: var(--text2); text-decoration: none; padding: 6px 14px; border-radius: 20px; font-size: 0.9rem; font-weight: 500; transition: all 0.2s; }
        .nav-link-item:hover { color: #fff; background: rgba(255,255,255,0.05); }
        .nav-btn-login { text-decoration: none; background: var(--accent); color: #fff; padding: 8px 18px; border-radius: 8px; font-size: 0.88rem; font-weight: 600; transition: all 0.2s; }
        .nav-btn-login:hover { background: #c40812; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(229,9,20,0.3); }

        /* Auth Container */
        .auth-container {
            flex: 1;
            display: flex; align-items: center; justify-content: center;
            padding: 40px;
        }

        .auth-card {
            background: rgba(22, 22, 31, 0.7);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 40px 48px;
            width: 100%;
            max-width: 440px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.4);
            animation: slideUp 0.4s ease-out;
        }

        @keyframes slideUp {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .auth-logo {
            text-align: center; margin-bottom: 10px;
            font-size: 2.5rem;
        }

        .auth-card h2 {
            font-size: 1.6rem; font-weight: 800;
            text-align: center; margin-bottom: 32px;
            letter-spacing: -0.5px;
        }

        .alert-error {
            background: rgba(229,9,20,0.1);
            border: 1px solid rgba(229,9,20,0.3);
            border-radius: 10px; padding: 12px 16px;
            color: #ff8080; font-size: 0.88rem;
            text-align: center; margin-bottom: 24px;
            display: flex; align-items: center; justify-content: center; gap: 8px;
        }

        .input-group {
            margin-bottom: 20px;
            position: relative;
        }

        .input-group label {
            display: block; font-size: 0.78rem; font-weight: 600;
            color: var(--text3); text-transform: uppercase; letter-spacing: 0.5px;
            margin-bottom: 8px; margin-left: 2px;
        }

        .input-group input {
            width: 100%; padding: 14px 16px;
            background: rgba(255,255,255,0.03);
            border: 1px solid var(--border);
            border-radius: 10px; color: var(--text);
            font-family: 'Inter', sans-serif; font-size: 0.95rem;
            outline: none; transition: all 0.25s ease;
        }

        .input-group input:focus {
            background: rgba(255,255,255,0.05);
            border-color: rgba(229,9,20,0.5);
            box-shadow: 0 0 0 4px rgba(229,9,20,0.1);
        }

        .input-group input::placeholder { color: var(--text3); }

        .btn-submit {
            width: 100%; padding: 14px; margin-top: 10px;
            background: var(--accent); color: #fff;
            border: none; border-radius: 10px; font-size: 0.95rem; font-weight: 700;
            cursor: pointer; transition: all 0.2s;
            box-shadow: 0 4px 15px rgba(229,9,20,0.3);
        }

        .btn-submit:hover {
            background: #c40812;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(229,9,20,0.4);
        }

        .auth-footer {
            margin-top: 24px; text-align: center;
            font-size: 0.88rem; color: var(--text2);
        }

        .auth-footer a {
            color: #fff; font-weight: 600; text-decoration: none;
            transition: color 0.2s; margin-left: 4px;
        }

        .auth-footer a:hover { color: var(--accent); text-decoration: underline; }

        @media (max-width: 600px) {
            .auth-card { padding: 30px 24px; }
            .nav-inner { padding: 0 20px; }
        }
    </style>
</head>
<body>

    <%@ include file="/components/navbar.jsp" %>

    <div class="auth-container">
        <div class="auth-card">
            <style>
                .mixi-logo-auth {
                    height: 140px;
                    object-fit: contain;
                    filter: drop-shadow(0 8px 24px rgba(229,9,20,0.5));
                    animation: floatLogo 4s ease-in-out infinite;
                }
                @keyframes floatLogo {
                    0% { transform: translateY(0px) scale(1); }
                    50% { transform: translateY(-12px) scale(1.03); }
                    100% { transform: translateY(0px) scale(1); }
                }
            </style>
            <div class="auth-logo" style="text-align: center; margin-bottom: 25px;">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="MixiMovies Logo" class="mixi-logo-auth">
            </div>
            <h2>Chào mừng trở lại</h2>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="input-group">
                    <label for="username">Tên đăng nhập</label>
                    <input type="text" name="id" id="username" placeholder="Nhập tên đăng nhập của bạn" required>
                </div>

                <div class="input-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" name="password" id="password" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn-submit">
                    Đăng nhập
                </button>
            </form>

            <div class="auth-footer">
                Chưa có tài khoản? 
                <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
            </div>
        </div>
    </div>

</body>
</html>