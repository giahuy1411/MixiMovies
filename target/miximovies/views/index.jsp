<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trng Chủ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

    <div class="container vh-100 d-flex flex-column justify-content-center align-items-center">
        
        <a class="navbar-brand fs-1 fw-bold text-primary mb-4 text-decoration-none" 
           href="${pageContext.request.contextPath}/home">
           TRANG CHỦ
        </a>

        <a class="btn btn-danger btn-lg shadow-sm" 
           href="${pageContext.request.contextPath}/logout">
           🚪 Đăng xuất
        </a>

    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>