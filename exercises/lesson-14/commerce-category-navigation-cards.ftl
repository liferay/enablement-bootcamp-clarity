<div class="row widget-mode-card">
    <#if entries?has_content>
        <#list entries as currentCategory>
            <#assign
                categoryId = currentCategory.getCategoryId()
                categoryName = currentCategory.getName()
                categoryHref = cpAssetCategoriesNavigationDisplayContext
                .getFriendlyURL(currentCategory.getCategoryId(), themeDisplay)
								propertyName = "alphaCode"
            />

            <#if cpAssetCategoriesNavigationDisplayContext.getDefaultImageSrc(categoryId)??>
                <#assign cardImage = true />
            <#else>
                <#assign cardImage = false />
            </#if>
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <#if cardImage>
                            <div class="aspect-ratio aspect-ratio-8-to-3">
                                <a href="${categoryHref}">${categoryName}
                                    <img alt="thumbnail" class="aspect-ratio-item-center-middle aspect-ratio-item-fluid"
                                        src="${cpAssetCategoriesNavigationDisplayContext.getDefaultImageSrc(categoryId)}">
                                </a>
                            </div>
                        </#if>
                    </div>
                    <div class="card-body widget-topbar">
                        <div class="autofit-row card-title">
                                <div class="autofit-col autofit-col-expand">
                                    <h3 class="title">
                                        <a class="title-link" href="${categoryHref}">${categoryName}</a>
                                    </h3>
                                </div>
                        </div>
                        <#if validator.isNotNull(currentCategory.getDescription())>
                            <#assign content = currentCategory.getDescription() />
                            <#if cardImage>
                                <p class="widget-resume">${stringUtil.shorten(htmlUtil.stripHtml(content), 150)}</p>
                            <#else>
                                <p class="widget-resume">${stringUtil.shorten(htmlUtil.stripHtml(content), 400)}</p>
                            </#if>
                        </#if>
                    </div>
                </div>
            </div>
        </#list>
    </#if>
</div>