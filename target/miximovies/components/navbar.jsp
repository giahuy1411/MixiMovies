<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

            /* ───── NAVBAR BASE ───── */
            .mixi-nav {
                position: sticky; top: 0; left: 0; right: 0; z-index: 999;
                height: 70px; background: rgba(10,10,15,0.92);
                backdrop-filter: blur(20px); border-bottom: 1px solid rgba(255,255,255,0.08);
            }
            .nav-inner {
                max-width: 1400px; margin: 0 auto; padding: 0 24px; height: 100%;
                display: flex; align-items: center; justify-content: space-between;
            }

            /* ───── NAVBAR DROPDOWN ───── */
            .nav-dropdown {
                position: relative;
            }
            .nav-dropdown-trigger {
                display: flex;
                align-items: center;
                gap: 6px;
                cursor: pointer;
            }
            .nav-dropdown-trigger i {
                font-size: 0.7rem;
                transition: transform 0.3s;
                margin-top: 2px;
            }
            .nav-dropdown:hover .nav-dropdown-trigger i {
                transform: rotate(180deg);
                color: var(--accent);
            }

            .dropdown-content {
                position: absolute;
                top: calc(100% + 15px);
                left: 50%;
                transform: translateX(-50%) translateY(10px);
                width: 520px;
                background: rgba(15, 15, 25, 0.95);
                backdrop-filter: blur(25px);
                border: 1px solid rgba(255, 255, 255, 0.08);
                border-radius: 14px;
                padding: 24px;
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 8px;
                opacity: 0;
                visibility: hidden;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                box-shadow: 0 20px 40px rgba(0,0,0,0.6);
                z-index: 1000;
            }
            /* Hidden bridge to prevent closing when moving to dropdown */
            .nav-dropdown::after {
                content: '';
                position: absolute;
                top: 100%;
                left: 0;
                width: 100%;
                height: 20px;
            }

            .nav-dropdown:hover .dropdown-content {
                opacity: 1;
                visibility: visible;
                transform: translateX(-50%) translateY(0);
            }

            .dropdown-item {
                color: #9999b3;
                text-decoration: none;
                font-size: 0.88rem;
                font-weight: 500;
                padding: 10px 14px;
                border-radius: 8px;
                transition: all 0.2s;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .dropdown-item:hover {
                background: rgba(229, 9, 20, 0.1);
                color: #fff;
                transform: translateX(4px);
            }

            /* ───── NAV LINKS & BUTTONS ───── */
            .nav-links {
                display: flex; align-items: center; gap: 30px; list-style: none; margin: 0; padding: 0;
            }
            .nav-link-item {
                color: var(--text2); text-decoration: none; font-size: 0.92rem; font-weight: 500;
                transition: all 0.2s; display: flex; align-items: center; gap: 8px;
            }
            .nav-link-item:hover, .nav-link-item.active { color: #fff; }

            .nav-btn-login, .nav-btn-logout {
                padding: 10px 22px; border-radius: 40px; font-size: 0.88rem; font-weight: 700;
                text-decoration: none; transition: all 0.2s; cursor: pointer;
            }
            .nav-btn-login {
                background: var(--accent); color: #fff;
                box-shadow: 0 4px 15px rgba(229, 9, 20, 0.3);
            }
            .nav-btn-login:hover { background: #ff1f29; transform: translateY(-2px); box-shadow: 0 6px 20px rgba(229, 9, 20, 0.4); }

            .nav-btn-logout {
                background: rgba(255,255,255,0.06); color: var(--text3);
                border: 1px solid rgba(255,255,255,0.1);
            }
            .nav-btn-logout:hover { background: rgba(255,255,255,0.1); color: #ff6b6b; border-color: rgba(255,107,107,0.3); }

            .nav-user { display: flex; align-items: center; gap: 10px; }

            @media (max-width: 768px) {
                .nav-links { display: none; }
            }
        </style>
        <a class="nav-brand" href="${pageContext.request.contextPath}/home" style="text-decoration: none; display: flex; align-items: center;">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="MixiMovies Logo" class="mixi-logo-nav">
        </a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/home" class="nav-link-item">Trang chủ</a></li>
            <c:if test="${empty sessionScope.user || !sessionScope.user.premium}">
                <li><a href="${pageContext.request.contextPath}/premium" class="nav-link-item" style="color:var(--gold); font-weight:800;"><i class="fas fa-crown"></i> Gói Premium</a></li>
            </c:if>
            
            <li class="nav-dropdown">
                <a class="nav-link-item nav-dropdown-trigger">
                    Thể loại <i class="fas fa-chevron-down"></i>
                </a>
                <div class="dropdown-content">
                    <c:forEach var="cat" items="${globalCategoryList}">
                        <a href="${pageContext.request.contextPath}/home?cid=${cat.id}" class="dropdown-item">
                            <c:out value="${cat.name}"/>
                        </a>
                    </c:forEach>
                    <c:if test="${empty globalCategoryList}">
                        <div style="grid-column: span 3; text-align: center; color: #555; padding: 20px;">
                            Chưa có thể loại nào
                        </div>
                    </c:if>
                </div>
            </li>
            <c:if test="${not empty sessionScope.user}">
                <li class="nav-user">
                    <a href="${pageContext.request.contextPath}/profile" class="nav-link-item" style="color:#fff; font-weight:700;">
                        <i class="fas fa-user-circle"></i> <c:out value="${sessionScope.user.fullname}"/>
                        <c:if test="${sessionScope.user.premium}">
                            <span style="color:#f5c518; margin-left:4px;" title="Premium"><i class="fas fa-crown"></i></span>
                        </c:if>
                    </a>
                </li>
                <c:if test="${sessionScope.user.admin}">
                    <li><a href="${pageContext.request.contextPath}/admin/video" class="nav-link-item">⚙ Quản trị</a></li>
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