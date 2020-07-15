package com.exa.halm

import grails.artefact.Enhances
import grails.util.Environment
import grails.views.Views
import grails.views.api.HttpView
import groovy.transform.CompileDynamic

/**
 * A trait that enhances the gson views to have access to the halm builder syntax.
 */
@Enhances(Views.TYPE)
@CompileDynamic
trait HalmJsonView extends HttpView {
    /**
     * A prefix that comes with the servletPath, that should be moved from the relative url and added to the base url
     */
    static enum PREFIX {
        API('/api')

        final String prefix

        PREFIX(String prefix) {
            this.prefix = prefix
        }
    }

    /**
     * Helping method to allow values Map without queryParams
     *
     * @param href
     * @param queryParams
     * @param values
     * @param closure
     */
    Object hal(String href = null, Map values, @DelegatesTo(Halm) final Closure closure) {
        hal(href, null, values, closure)
    }

    /**
     * This method allows us to take advantage of the Halm from gson views
     *
     * @param href
     * @param queryParams
     * @param values
     * @param closure
     */
    Object hal(String href = null, String queryParams = null, Map values = null, @DelegatesTo(Halm) final Closure closure) {
        String servletPath = request.uri - request.contextPath
        PREFIX prefix = PREFIX.values().find { PREFIX prefix -> servletPath == prefix.prefix || servletPath.startsWith("$prefix.prefix/") }

        // +------------------------------------------------------------+
        // | there are 2 unofficial ways to get request URL. Choose one |
        // +------------------------------------------------------------+
//        String requestURL = request.request.getRequestURL().toString()
//        int relPathLen = servletPath.length()
//        if (prefix) {
//            relPathLen -= prefix.prefix.length() + 1
//        }
//        String baseURL = requestURL.substring(0, requestURL.length() - relPathLen)
//        def hrefRoot = href ?: requestURL.substring(baseURL.length() + (href == null ? 0 : 1))

        String requestURL = jsonapi.viewHelper.getServerBaseURL()
        String offset = prefix?.prefix ?: ''
        String baseURL = requestURL + offset
        def hrefRoot = href ?: (servletPath - offset - (href == null ? '' : '/'))

        Halm hal = Halm.hal(baseURL, hrefRoot, queryParams ?: '', values, closure)
        String format = params.get('format') ?: request.getHeader('Accept')

        if (Environment.current == Environment.DEVELOPMENT && params.debug) {
            hal.value([
                    ''               : '>>>>>>>> request values <<<<<<<<',
                    url              : requestURL,
                    baseURL          : baseURL,
                    uri              : request.uri,
                    servlet          : servletPath,
                    context          : request.getContextPath(),
                    params           : params.parameterMap,
                    headerNames      : request.getHeaderNames(),
                    method           : request.getMethod(),
                    'response.format': response.httpServletResponse.format ?: '--',
                    'contentType'    : response.contentType()
            ])
        }

        json(hal.asMap(format))
    }

}
