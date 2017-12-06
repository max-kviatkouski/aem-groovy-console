package com.icfolson.aem.groovy.console.components

import com.icfolson.aem.groovy.console.audit.AuditRecord
import com.icfolson.aem.groovy.console.audit.AuditService
import groovy.json.JsonBuilder
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.models.annotations.Model

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.jcr.Session

import static com.icfolson.aem.groovy.console.constants.GroovyConsoleConstants.PARAMETER_SCRIPT
import static com.icfolson.aem.groovy.console.constants.GroovyConsoleConstants.PARAMETER_USER_ID

@Model(adaptables = SlingHttpServletRequest)
class Body {

    @Inject
    private AuditService auditService

    @Inject
    private SlingHttpServletRequest request

    private AuditRecord auditRecord

    @PostConstruct
    void init() {
        def script = request.getParameter(PARAMETER_SCRIPT)
        def userId = request.getParameter(PARAMETER_USER_ID)

        if (script) {
            def session = request.resourceResolver.adaptTo(Session)

            auditRecord = auditService.getAuditRecord(session, userId, script)
        }
    }

    String getAuditRecordJson() {
        auditRecord ? new JsonBuilder(auditRecord).toString() : null
    }
}
