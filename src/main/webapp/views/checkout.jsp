<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán — MixiMovies</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        :root {
            --bg: #0a0a0f;
            --card: #ffffff;
            --accent: #e50914;
            --text: #1a1a1a;
        }
        body {
            background-color: var(--bg);
            font-family: 'Inter', sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
        }
        .checkout-box {
            background: var(--card);
            color: var(--text);
            width: 100%;
            max-width: 450px;
            border-radius: 24px;
            overflow: hidden;
            box-shadow: 0 40px 100px rgba(0,0,0,0.8);
            animation: slideUp 0.6s cubic-bezier(0.23, 1, 0.32, 1);
        }
        @keyframes slideUp {
            from { opacity: 0; transform: translateY(40px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .checkout-header {
            background: #f8f9fa;
            padding: 30px;
            text-align: center;
            border-bottom: 1px solid #eee;
        }
        .checkout-header h2 { margin: 0; font-size: 1.25rem; font-weight: 800; }
        .checkout-header p { margin: 5px 0 0; color: #666; font-size: 0.9rem; }
        
        .checkout-body {
            padding: 40px;
            text-align: center;
        }
        .qr-wrapper {
            background: #fff;
            padding: 15px;
            border: 2px solid #eee;
            border-radius: 16px;
            display: inline-block;
            margin-bottom: 25px;
        }
        .qr-wrapper img {
            width: 250px;
            height: 250px;
            display: block;
        }
        .amount-display {
            font-size: 1.8rem;
            font-weight: 800;
            color: var(--accent);
            margin-bottom: 10px;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            font-size: 0.9rem;
            color: #555;
        }
        .info-row span:last-child {
            font-weight: 700;
            color: #000;
        }
        .payment-instructions {
            background: #fff8f0;
            border: 1px solid #ffeeba;
            padding: 15px;
            border-radius: 12px;
            font-size: 0.85rem;
            color: #856404;
            margin-top: 25px;
            text-align: left;
        }
        .confirm-btn {
            display: block;
            width: 100%;
            background: #000;
            color: #fff;
            border: none;
            padding: 18px;
            border-radius: 12px;
            font-weight: 700;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.2s;
            margin-top: 30px;
            text-decoration: none;
        }
        .confirm-btn:hover {
            background: #333;
            transform: translateY(-2px);
        }
        .cancel-link {
            display: block;
            margin-top: 20px;
            color: #999;
            text-decoration: none;
            font-size: 0.85rem;
        }
        .cancel-link:hover { color: var(--accent); }
        .checkout-alert {
            background: rgba(229, 9, 20, 0.1); border: 1px solid rgba(229, 9, 20, 0.2);
            color: #ff4d4d; padding: 15px; border-radius: 12px; margin: 20px;
            font-size: 0.88rem; text-align: left; line-height: 1.5;
        }
    </style>
</head>
<body>
    <div class="checkout-box">
        <div class="checkout-header">
            <h2>Quét mã để thanh toán</h2>
            <p>Sử dụng App Ngân hàng hoặc MoMo để quét</p>
        </div>

        <c:if test="${not empty error}">
            <div class="checkout-alert">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <div class="checkout-body">
            <div class="amount-display">${amount}đ</div>
            <div class="qr-wrapper">
                <img src="${qrUrl}" alt="Mã QR Thanh toán SePay">
            </div>
            
            <div style="margin: 20px 0; border-top: 1px dashed #ddd; padding-top: 20px;">
                <div class="info-row">
                    <span>Gói dịch vụ</span>
                    <span>${planName}</span>
                </div>
                <div class="info-row">
                    <span>Tài khoản hưởng</span>
                    <span>${applicationScope.sepAccountName}</span>
                </div>
                <div class="info-row">
                    <span>Nội dung CK</span>
                    <span style="color: var(--accent); font-size: 1.1rem; letter-spacing: 1px;">${orderCode}</span>
                </div>
            </div>

            <div class="payment-instructions">
                <i class="fas fa-info-circle"></i> Vui lòng <strong>giữ nguyên nội dung chuyển khoản</strong>. Sau khi chuyển xong, đợi 30s-1p rồi mới nhấn nút xác nhận để hệ thống tra soát.
            </div>

            <form action="${pageContext.request.contextPath}/premium/confirm" method="POST">
                <input type="hidden" name="plan" value="${plan}">
                <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                <button type="submit" class="confirm-btn">Tôi đã chuyển khoản xong</button>
            </form>

            <a href="${pageContext.request.contextPath}/premium" class="cancel-link">Hủy giao dịch</a>
        </div>
    </div>
</body>
</html>
