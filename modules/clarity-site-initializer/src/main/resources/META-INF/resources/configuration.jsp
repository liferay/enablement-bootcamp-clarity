
<%@ include file="init.jsp"%>

<style>
    .form-group {
        margin-bottom: 0px;
    }

    .js-task.over {
        border: 3px dotted #666;
        background-color: lightgoldenrodyellow;
    }

</style>

<liferay-portlet:actionURL var="saveSettingsMVCActionCommand" name="/system/settings/clarity/save" portletName="${systemSettingsPortlet}"/>
<liferay-portlet:renderURL var="cancelSettingsRenderURL" portletName="${systemSettingsPortlet}" windowState="maximized" portletMode="view"/>

<div class="sheet sheet-full">
    <h2><liferay-ui:message key="clarity-theme-initializer-tasks"/></h2>

    <p><liferay-ui:message key="clarity-theme-initializer-tasks-description"/></p>
    <p class="small text-muted mb-4"><liferay-ui:message key="clarity-theme-initializer-tasks-note"/></p>

    <aui:form name="fm" action="${saveSettingsMVCActionCommand}">
        <hr/>
        <div class="mb-4">
            <aui:input name="enabled" type="toggle-switch" label="Enabled" inlineLabel="right" checked="${enabled}"/>
        </div>
        <hr/>
        <div class="mt-3">&nbsp;</div>
        <h3><liferay-ui:message key="available-tasks"/> (${fn:length(taskList)})</h3>
        <div class="mb-1">&nbsp;</div>
        <div class="task-container">
            <c:forEach items="${currentConfigurationTaskList}" var="c">
                <c:forEach items="${taskList}" var="t">
                    <c:if test="${t.class.canonicalName eq c}">
                        <div class="border p-4 mb-3 js-task" draggable="true">
                            <aui:input name="task" type="toggle-switch" value="${t.class.canonicalName}" label="${t.name}" inlineLabel="right" checked="true"/>
                            <div class="d-none">${taskList.remove(t)}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </c:forEach>

            <c:forEach items="${taskList}" var="t">
                <div class="border p-4 mb-3 js-task" draggable="true">
                    <aui:input name="task" type="toggle-switch" value="${t.class.canonicalName}" label="${t.name}" inlineLabel="right"/>
                </div>
            </c:forEach>

        </div>
        <aui:button-row cssClass="mt-5">
            <aui:button type="submit" value="update"/>
            <aui:button type="cancel" value="cancel" href="${cancelSettingsRenderURL}"/>
        </aui:button-row>
    </aui:form>
</div>

<script>
    // document.addEventListener('DOMContentLoaded', (event) => {

    console.log("DOM content loaded");

    function handleDragStart(e) {
        console.log("drag start event handler running");
        this.style.opacity = '0.4';

        dragSrcEl = this;

        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('text/html', this.innerHTML);
    }

    function handleDragEnd(e) {
        console.log("drag end event handler running");
        this.style.opacity = '1';

        items.forEach(function (item) {
            item.classList.remove('over');
        });
    }

    function handleDragOver(e) {
        e.preventDefault();
        return false;
    }

    function handleDragEnter(e) {
        this.classList.add('over');
    }

    function handleDragLeave(e) {
        this.classList.remove('over');
    }

    function handleDrop(e) {
        console.log("drop event handler running");
        e.stopPropagation();

        if (dragSrcEl !== this) {
            dragSrcEl.innerHTML = this.innerHTML;
            this.innerHTML = e.dataTransfer.getData('text/html');
        }

        return false;
    }

    var items = document.querySelectorAll('.js-task');

    items.forEach(function(item) {
        item.addEventListener('dragstart', handleDragStart);
        item.addEventListener('dragover', handleDragOver);
        item.addEventListener('dragenter', handleDragEnter);
        item.addEventListener('dragleave', handleDragLeave);
        item.addEventListener('dragend', handleDragEnd);
        item.addEventListener('drop', handleDrop);
    });
    // });
</script>
