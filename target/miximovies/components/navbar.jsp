<%@ page pageEncoding="UTF-8" %>
<nav class="mixi-nav">
    <div class="nav-inner">
        <style>
            .mixi-logo-nav {
                height: 80px;
                object-fit: contain;
                transform: scale(1.35) translateY(4px);
                transform-origin: left center;
                filter: drop-shadow(0 4px 6px rgba(0,0,0,0.6));
                transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            }
            .mixi-logo-nav:hover {
                transform: scale(1.45) translateY(3px) rotate(-2deg);
                filter: drop-shadow(0 6px 10px rgba(0,0,0,0.8));
            }
        </style>
        <a class="nav-brand" href="${pageContext.request.contextPath}/home" style="text-decoration: none; display: flex; align-items: center;">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="MixiMovies Logo" class="mixi-logo-nav">
        </a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/home" class="nav-link-item">Trang chủ</a></li>
            <c:if test="${not empty sessionScope.user}">
                <li class="nav-user">
                    <span class="user-greeting">👤 ${sessionScope.user.fullname}</span>
                </li>   
                <c:if test="${sessionScope.user.admin}">
					<li><a href="${pageContext.request.contextPath}/admin/video" class="nav-link-item">⚙ Quản lý phim</a></li>
				</c:if>
                <li>
                    <a href="${pageContext.request.contextPath}/logout" class="nav-btn-logout">Đăng xuất</a>
                </li>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <li>
                    <a href="${pageContext.request.contextPath}/login" class="nav-btn-login">Đăng nhập</a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>