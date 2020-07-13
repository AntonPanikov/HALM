package com.exa.halm

import groovy.transform.CompileDynamic

/**
 * Represents the HAL embedded resource
 *
 * Created by cbialik on 7/25/17.
 */
@CompileDynamic
class Embedded {
    Embedded(String baseUrl, Halm parent, String rel = null) {
        _baseURL = baseUrl
        _parent = parent
        _rel = rel
    }

    Embedded(Object element, String baseUrl, Halm parent, String rel = null) {
        _baseURL = baseUrl
        _parent = parent
        _rel = rel
        _element = element
        _withCollection = true
    }

    String  _baseURL
    Halm    _parent
    Halm    _hal
    boolean _withCollection = false
    Object  _element

    String _curie

    void curie(String curie) { _curie = curie }

    String _rel

    void rel(Object rel) {
        _rel = rel.toString()
    }

    String _href

    void href(String href) { _href = href }

    String _lang

    void lang(String lang) { _lang = lang }

    String _title

    void title(String title) { _title = title }

    String getCurieHref() {
        def parent = _parent
        while (parent) {
            String href = parent?.curieMap[_curie]?.href()
            if (href) {
                return href
            }
            parent = parent.parent
        }
    }

    def body(Map<?, ?> values = null, @DelegatesTo(Halm) Closure closure) {
        _hal = new Halm(_curie ? getCurieHref() : _baseURL, _href, '', _parent.jsonOnly, _parent.halOnly)
        _hal.parent = this
        _hal.valueMap << (values ?: [:])
        closure.delegate = _hal
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        if (_withCollection) {
            if (_element != null) {
                closure(_element)
            }
        } else {
            closure()
        }
        if (_curie) {
            _hal.defaultCurieName = _curie
        }
        _hal
    }

    def body(Map<?, ?> values) {
        _hal = new Halm(_curie ? getCurieHref() : _baseURL, _href, '', _parent.jsonOnly, _parent.halOnly)
        _hal.parent = this
        _hal.valueMap << (values ?: [:])
        if (_curie) {
            _hal.defaultCurieName = _curie
        }
        _hal
    }
}
