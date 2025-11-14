<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 10/16/2025
  Time: 11:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Bạn có chắc chắn muốn rời đi?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">

                </button>
            </div>
            <div class="modal-body">Chọn "Đăng xuất" để rời đi.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Hủy</button>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </div>
        </div>
    </div>
</div>
