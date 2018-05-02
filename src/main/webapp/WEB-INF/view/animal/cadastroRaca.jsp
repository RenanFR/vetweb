<%-- 
    Document   : cadastroRaca
    Created on : 14/02/2018, 17:34:56
    Author     : Maria J�ssica
--%>
<%@ taglib prefix="vetweb" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%><!--   Form c/ utilidades do spring    -->
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %><!--   Adc. token p/ prote��o contra csrf -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%><!--  tags �teis do spring framework   -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<vetweb:layout title="cadastroRaca">
    <form:form servletRelativeAction="/animais/addRaca" method="POST" modelAttribute="raca">
        <table class="table table-responsive">
            <caption>Nova Ra�a</caption>
            <tbody>
                <form:hidden path="racaId" id="racaId"></form:hidden>
                <tr>
                    <th><label for="descricao">Ra�a</label></th>
                    <td><form:input path="descricao" id="descricao"></form:input></td>                    
                </tr>
                <tr>
                    <th><label for="especie">Esp�cie</label></th>
                    <td><form:select path="especie" items="${especies}"></form:select></td>
                </tr>
            </tbody>
        </table>
        <input type="submit" value="submit"  />
        <input type="reset" value="reset"  />                
    </form:form>
</vetweb:layout>