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
        </style>
        <a class="nav-brand" href="${pageContext.request.contextPath}/home" style="text-decoration: none; display: flex; align-items: center;">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="MixiMovies Logo" class="mixi-logo-nav">
        </a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/home" class="nav-link-item">Trang chủ</a></li>
            
            <li class="nav-dropdown">
                <a class="nav-link-item nav-dropdown-trigger">
                    Thể loại <i class="fas fa-chevron-down"></i>
                </a>
                <div class="dropdown-content">
                    <c:forEach var="cat" items="${globalCategoryList}">
                        <a href="${pageContext.request.contextPath}/home?cid=${cat.id}" class="dropdown-item">
                            ${cat.name}
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