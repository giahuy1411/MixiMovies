<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu | MixiMovies</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        :root {
            --bg: #0a0a0f; --bg2: #12121a; --card: #16161f; --accent: #e50914; --text: #e8e8f0; --text2: #9090b0; --text3: #55556a; --border: rgba(255,255,255,0.05);
        }
        body { font-family: 'Inter', sans-serif; background: var(--bg); color: var(--text); min-height: 100vh; }
        .auth-container { flex: 1; display: flex; align-items: center; justify-content: center; padding: 40px; margin-top: 20px; }
        .auth-card {
            background: rgba(22, 22, 31, 0.7); backdrop-filter: blur(20px); border: 1px solid var(--border); border-radius: 20px;
            padding: 40px 48px; width: 100%; max-width: 440px; box-shadow: 0 20px 40px rgba(0,0,0,0.4);
        }
        .auth-card h2 { font-size: 1.6rem; font-weight: 800; text-align: center; margin-bottom: 32px; letter-spacing: -0.5px; }
        .alert-error {
            background: rgba(229,9,20,0.1); border: 1px solid rgba(229,9,20,0.3); border-radius: 10px; padding: 12px 16px; color: #ff8080; font-size: 0.88rem; text-align: center; margin-bottom: 24px;
        }
        .input-group { margin-bottom: 20px; position: relative; }
        .input-group label { display: block; font-size: 0.78rem; font-weight: 600; color: var(--text3); text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px; }
        .input-group input {
            width: 100%; padding: 14px 16px; background: rgba(255,255,255,0.03); border: 1px solid var(--border); border-radius: 10px; color: var(--text); font-family: 'Inter', sans-serif; font-size: 0.95rem; outline: none; transition: all 0.25s ease;
        }
        .input-group input:focus { border-color: var(--accent); background: rgba(255,255,255,0.05); }
        .btn-submit {
            width: 100%; padding: 14px; margin-top: 10px; background: var(--accent); color: #fff; border: none; border-radius: 10px; font-size: 0.95rem; font-weight: 700; cursor: pointer; transition: all 0.2s;
        }
        .btn-submit:hover { background: #c40812; transform: translateY(-2px); }
    </style>
</head>
<body>
    <%@ include file="/components/navbar.jsp" %>
    <div class="auth-container">
        <div class="auth-card">
            <div style="text-align: center; margin-bottom: 20px;">
                <i class="fas fa-lock-open" style="font-size: 3rem; color: var(--accent); opacity: 0.8;"></i>
            </div>
            <h2>Mật khẩu mới</h2>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="post" id="resetForm">
                <div class="input-group">
                    <label for="newPassword">Mật khẩu mới</label>
                    <input type="password" name="newPassword" id="newPassword" placeholder="••••••••" required autofocus>
                </div>

                <div class="input-group">
                    <label for="confirmPassword">Xác nhận mật khẩu</label>
                    <input type="password" name="confirmPassword" id="confirmPassword" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn-submit">
                    Cập nhật mật khẩu
                </button>
            </form>
        </div>
    </div>

    <script>
        document.getElementById('resetForm').addEventListener('submit', function(e) {
            const p1 = document.getElementById('newPassword').value;
            const p2 = document.getElementById('confirmPassword').value;
            if (p1 !== p2) {
                e.preventDefault();
                alert('Mật khẩu xác nhận không khớp!');
            }
        });
    </script>
</body>
</html>
