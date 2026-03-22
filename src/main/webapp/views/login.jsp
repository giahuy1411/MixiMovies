<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

	<div class="container d-flex justify-content-center align-items-center vh-100">
		<div class="card shadow-lg p-4" style="width: 100%; max-width: 400px;">

			<h4 class="text-center mb-4 fw-bold">ĐĂNG NHẬP</h4>

			<c:if test="${not empty error}">
				<div class="alert alert-danger text-center">
					${error}
				</div>
			</c:if>

			<form action="${pageContext.request.contextPath}/login" method="post">

				<div class="form-floating mb-3">
					<input type="text" name="id" class="form-control" id="username" placeholder="Tên đăng nhập"
						required>
					<label for="username">Tên đăng nhập</label>
				</div>

				<div class="form-floating mb-3">
					<input type="password" name="password" class="form-control" id="password"
						placeholder="Mật khẩu" required>
					<label for="password">Mật khẩu</label>
				</div>

				<button type="submit" class="btn btn-dark w-100 mb-3">
					Đăng nhập
				</button>

			</form>

			<div class="text-center">
				<small>
					Chưa có tài khoản?
					<a href="${pageContext.request.contextPath}/register" class="fw-bold text-decoration-none">
						Đăng ký
					</a>
				</small>
			</div>
		</div>
	</div>

</body>

</html>