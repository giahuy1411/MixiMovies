<%@ page pageEncoding="UTF-8" %>
<nav class="mixi-nav">
    <div class="nav-inner">
        <a class="nav-brand" href="${pageContext.request.contextPath}/home">
            <span class="brand-icon">🎬</span>
            <span class="brand-text">MixiMovies</span>
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