<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký | MixiMovies</title>
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

        /* Auth Container */

        /* Auth Container */
        .auth-container {
            flex: 1;
            display: flex; align-items: center; justify-content: center;
            padding: 40px 20px;
        }

        .auth-card {
            background: rgba(22, 22, 31, 0.7);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 40px 48px;
            width: 100%;
            max-width: 480px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.4);
            animation: slideUp 0.4s ease-out;
        }

        @keyframes slideUp {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .auth-card h2 {
            font-size: 1.6rem; font-weight: 800;
            text-align: center; margin-bottom: 30px;
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
        
        .form-row {
            display: grid; grid-template-columns: 1fr 1fr; gap: 16px;
        }

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
            .form-row { grid-template-columns: 1fr; gap: 0; }
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
            <h2>Đăng ký tài khoản</h2>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="input-group">
                    <label for="id">Tên đăng nhập</label>
                    <input type="text" name="id" id="id" placeholder="Tên đăng nhập" required>
                </div>

                <div class="input-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" name="password" id="password" placeholder="••••••••" required>
                </div>

                <div class="input-group">
                    <label for="fullname">Họ và tên</label>
                    <input type="text" name="fullname" id="fullname" placeholder="Nhập họ và tên của bạn" required>
                </div>
                
                <div class="input-group">
                    <label for="email">Địa chỉ Email</label>
                    <input type="email" name="email" id="email" placeholder="example@email.com" required>
                </div>

                <button type="submit" class="btn-submit">
                    Tạo tài khoản
                </button>
            </form>

            <div class="auth-footer">
                Đã có tài khoản? 
                <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
            </div>
        </div>
    </div>

</body>
</html>