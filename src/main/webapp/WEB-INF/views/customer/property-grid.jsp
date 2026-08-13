<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="row g-4">
    <c:choose>
        <c:when test="${not empty realEstates}">
            <c:forEach var="item" items="${realEstates}">
                <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.1s">
                    <div class="property-item rounded overflow-hidden">
                        <div class="position-relative overflow-hidden">
                            <a href="${pageContext.request.contextPath}/property/detail?id=${item.id}">
                                <c:choose>
                                    <c:when test="${not empty item.thumbnail}">
                                        <img class="img-fluid w-100"
                                             src="${pageContext.request.contextPath}/${item.thumbnail}"
                                             alt="${item.title}"
                                             style="height: 250px; object-fit: cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <img class="img-fluid w-100"
                                             src="${pageContext.request.contextPath}/assets/img/property-1.jpg"
                                             alt="No Image"
                                             style="height: 250px; object-fit: cover;">
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="bg-primary rounded text-white position-absolute start-0 top-0 m-4 py-1 px-3">
                                <c:out value="${item.propertyType}"/>
                            </div>
                        </div>
                        <div class="p-4 pb-0">
                            <h5 class="text-primary mb-3">
                                <fmt:formatNumber value="${item.price}" pattern="#,###"/> VNĐ
                            </h5>
                            <a class="d-block h5 mb-2 text-truncate" href="${pageContext.request.contextPath}/property/detail?id=${item.id}">
                                <c:out value="${item.title}"/>
                            </a>
                            <p class="text-truncate"><i class="fa fa-map-marker-alt text-primary me-2"></i><c:out value="${item.address}"/></p>
                        </div>
                        <div class="d-flex border-top">
                            <small class="flex-fill text-center border-end py-2">
                                <i class="fa fa-ruler-combined text-primary me-2"></i><c:out value="${item.area}"/> m²
                            </small>
                            <small class="flex-fill text-center py-2">
                                <i class="fa fa-tag text-primary me-2"></i><c:out value="${item.status}"/>
                            </small>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="col-12 text-center py-5">
                <h5 class="text-muted">Hiện chưa có dữ liệu bất động sản nào trong hệ thống.</h5>
            </div>
        </c:otherwise>
    </c:choose>

    <%-- Thanh phân trang nằm bên trong Grid --%>
    <c:if test="${totalPages > 1}">
        <div class="col-12 mt-5">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center mb-0">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link ajax-page" href="#" data-page="${currentPage - 1}">
                            <i class="fa fa-angle-left"></i> Trước
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link ajax-page" href="#" data-page="${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link ajax-page" href="#" data-page="${currentPage + 1}">
                            Sau <i class="fa fa-angle-right"></i>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </c:if>
</div>