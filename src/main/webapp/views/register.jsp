<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng ký</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container d-flex justify-content-center align-items-center vh-100">
	<div class="card shadow-lg p-4" style="width: 100%; max-width: 420px;">

		<h4 class="text-center fw-bold mb-4">ĐĂNG KÝ</h4>

		<c:if test="${not empty error}">
			<div class="alert alert-danger text-center">
				${error}
			</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/register" method="post">

			<div class="form-floating mb-3">
				<input type="text" name="id" class="form-control" id="id" placeholder="Tên đăng nhập" required>
				<label for="id">Tên đăng nhập</label>
			</div>

			<div class="form-floating mb-3">
				<input type="password" name="password" class="form-control" id="password" placeholder="Mật khẩu" required>
				<label for="password">Mật khẩu</label>
			</div>

			<div class="form-floating mb-3">
				<input type="email" name="email" class="form-control" id="email" placeholder="Email" required>
				<label for="email">Email</label>
			</div>

			<div class="form-floating mb-4">
				<input type="text" name="fullname" class="form-control" id="fullname" placeholder="Họ và tên" required>
				<label for="fullname">Họ và tên</label>
			</div>

			<button type="submit" class="btn btn-dark w-100 mb-3">
				Tạo tài khoản
			</button>

		</form>

		<div class="text-center">
			<small>
				Đã có tài khoản?
				<a href="${pageContext.request.contextPath}/login" class="fw-bold text-decoration-none">
					Đăng nhập
				</a>
			</small>
		</div>

	</div>
</div>

</body>
</html>