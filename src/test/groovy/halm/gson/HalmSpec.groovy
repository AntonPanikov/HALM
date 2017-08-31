package halm.gson

import com.exa.halm.Halm
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class HalmSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "simple HAL with absolute path"() {
        when:
            Halm hal = Halm.hal("base", '/path', 'params', null) {}

        then:
            hal?.asMap() == [
                    _links: [
                            self: [
                                    href: 'base/path?params',
                                    type: 'application/halm+json'
                            ]
                    ]
            ]
    }

    void "simple HAL with relative path"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', null) {}

        then:
            hal?.asMap() == [
                    _links: [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ]
            ]
    }

    void "top level HAL map"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', [
                    str: 'String value',
                    int: 42,
                    arr: [1, 2, 3, 5, 7],
                    map: [key1: 'val 1', key2: []]
            ]) {}

        then:
            hal?.asMap() == [
                    _links: [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    str   : 'String value',
                    int   : 42,
                    arr   : [1, 2, 3, 5, 7],
                    map   : [key1: 'val 1', key2: []]
            ]
    }

    void "top level HAL values"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params') {
                str 'String value'
                "int" 42
                arr([1, 2, 3, 5, 7])
                map([
                        key1: 'val 1',
                        key2: []
                ])
            }

        then:
            hal?.asMap() == [
                    _links: [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    str   : 'String value',
                    int   : 42,
                    arr   : [1, 2, 3, 5, 7],
                    map   : [key1: 'val 1', key2: []]
            ]
    }

    void "top level HAL links"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', null) {
                link 'simpleRelative', 'simple/relative'
                link 'simpleAbsolute', '/simple/absolute'
                link 'simpleComplete', 'simple/complete', 'param=simple', Locale.getInstance('ru', 'RU', ''), 'text/plane', 'simple title'
                link {
                    rel 'closure'
                    href 'closure/uri'
                }
                link 'mixed', {
                    href 'mixed/uri'
                }
                link {
                    rel 'comlete'
                    href 'complete/uri'
                    params 'param=complete'
                    hreflang Locale.JAPANESE
                    type 'image/*'
                    title 'complete title'
                }
            }

        then:
            def expect = [
                    _links: [
                            self          : [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ],
                            simpleRelative: [
                                    href: 'simple/relative',
                                    type: 'application/halm+json'
                            ],
                            simpleAbsolute: [
                                    href: 'base/simple/absolute',
                                    type: 'application/halm+json'
                            ],
                            simpleComplete: [
                                    href    : 'simple/complete?param=simple',
                                    hreflang: 'ru_RU',
                                    type    : 'text/plane',
                                    title   : 'simple title'
                            ],
                            closure       : [
                                    href: 'closure/uri',
                                    type: 'application/halm+json'
                            ],
                            mixed         : [
                                    href: 'mixed/uri',
                                    type: 'application/halm+json'
                            ],
                            comlete       : [
                                    href    : 'complete/uri?param=complete',
                                    hreflang: 'ja',
                                    type    : 'image/*',
                                    title   : 'complete title'
                            ]
                    ]
            ]
            def result = hal?.asMap()

            expect['_links'].keySet().each { String rel ->
                assert result['_links'][rel]['href'] == expect['_links'][rel]['href']
                assert result['_links'][rel]['type'] == expect['_links'][rel]['type']
                assert result['_links'][rel]['template'] == expect['_links'][rel]['template']
                assert result['_links'][rel]['hreflang'] == expect['_links'][rel]['hreflang']
                assert result['_links'][rel]['title'] == expect['_links'][rel]['title']
                assert result['_links'][rel] == expect['_links'][rel]
            }

            hal?.asMap() == expect
    }

    void "top level HAL templates"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', null) {
                template 'simpleRelative', 'simple/relative'
                template 'simpleAbsolute', '/simple/absolute'
                template 'simpleComplete', 'simple/complete', 'param=simple', Locale.getInstance('ru', 'RU', ''), 'text/plane', 'simple title'
                template {
                    rel 'closure'
                    href 'closure/uri'
                }
                template 'mixed', {
                    href 'mixed/uri'
                }
                template {
                    rel 'comlete'
                    href 'complete/uri'
                    params 'param=complete'
                    hreflang Locale.JAPANESE
                    type 'image/*'
                    title 'complete title'
                }
            }

        then:
            def expect = [
                    _links: [
                            self          : [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ],
                            simpleRelative: [
                                    href    : 'simple/relative',
                                    type    : 'application/halm+json',
                                    template: true
                            ],
                            simpleAbsolute: [
                                    href    : 'base/simple/absolute',
                                    type    : 'application/halm+json',
                                    template: true
                            ],
                            simpleComplete: [
                                    href    : 'simple/complete?param=simple',
                                    hreflang: 'ru_RU',
                                    type    : 'text/plane',
                                    template: true,
                                    title   : 'simple title'
                            ],
                            closure       : [
                                    href    : 'closure/uri',
                                    type    : 'application/halm+json',
                                    template: true
                            ],
                            mixed         : [
                                    href    : 'mixed/uri',
                                    type    : 'application/halm+json',
                                    template: true
                            ],
                            comlete       : [
                                    href    : 'complete/uri?param=complete',
                                    hreflang: 'ja',
                                    type    : 'image/*',
                                    template: true,
                                    title   : 'complete title'
                            ]
                    ]
            ]
            def result = hal?.asMap()

            expect['_links'].keySet().each { String rel ->
                assert result['_links'][rel]['href'] == expect['_links'][rel]['href']
                assert result['_links'][rel]['type'] == expect['_links'][rel]['type']
                assert result['_links'][rel]['template'] == expect['_links'][rel]['template']
                assert result['_links'][rel]['hreflang'] == expect['_links'][rel]['hreflang']
                assert result['_links'][rel]['title'] == expect['_links'][rel]['title']
                assert result['_links'][rel] == expect['_links'][rel]
            }

            hal?.asMap() == expect
    }

    void "top level simple embedded"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', null) {
                embedded 'inline rel', {
                    href 'inline/uri'
                    body {

                    }
                }
                embedded {
                    rel 'emb rel'
                    href 'emb/uri'
                    body {

                    }
                }
            }

        then:
            def expect = [
                    _links   : [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    _embedded: [
                            "inline rel": [
                                    _links: [
                                            self: [
                                                    href: 'inline/uri',
                                                    type: 'application/halm+json'
                                            ]
                                    ]
                            ],
                            "emb rel"   : [
                                    _links: [
                                            self: [
                                                    href: 'emb/uri',
                                                    type: 'application/halm+json'
                                            ]
                                    ]
                            ]
                    ]
            ]
            def result = hal?.asMap()

            expect['_embedded'].keySet().each { String rel ->
                assert result['_embedded'][rel]['_links']['self']['href'] == expect['_embedded'][rel]['_links']['self']['href']
                assert result['_embedded'][rel]['_links']['self']['type'] == expect['_embedded'][rel]['_links']['self']['type']
                assert result['_embedded'][rel]['_links']['self']['template'] == expect['_embedded'][rel]['_links']['self']['template']
                assert result['_embedded'][rel]['_links']['self']['hreflang'] == expect['_embedded'][rel]['_links']['self']['hreflang']
                assert result['_embedded'][rel]['_links']['self']['title'] == expect['_embedded'][rel]['_links']['self']['title']
                assert result['_embedded'][rel] == expect['_embedded'][rel]
            }

            hal?.asMap() == expect
    }

    void "top level links in embedded"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', null) {
                embedded 'emb', {
                    href 'emb/uri'
                    body {
                        link 'simpleRelative', 'simple/relative'
                        link 'simpleAbsolute', '/simple/absolute'
                        link 'simpleComplete', 'simple/complete', 'param=simple', Locale.getInstance('ru', 'RU', ''), 'text/plane', 'simple title'
                        link {
                            rel 'closure'
                            href 'closure/uri'
                        }
                        link 'mixed', {
                            href 'mixed/uri'
                        }
                        link {
                            rel 'comlete'
                            href 'complete/uri'
                            params 'param=complete'
                            hreflang Locale.JAPANESE
                            type 'image/*'
                            title 'complete title'
                        }
                    }
                }
            }

        then:
            def expect = [
                    _links   : [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    _embedded: [
                            emb: [
                                    _links: [
                                            self          : [
                                                    href: 'emb/uri',
                                                    type: 'application/halm+json'
                                            ],
                                            simpleRelative: [
                                                    href: 'simple/relative',
                                                    type: 'application/halm+json'
                                            ],
                                            simpleAbsolute: [
                                                    href: 'base/simple/absolute',
                                                    type: 'application/halm+json'
                                            ],
                                            simpleComplete: [
                                                    href    : 'simple/complete?param=simple',
                                                    hreflang: 'ru_RU',
                                                    type    : 'text/plane',
                                                    title   : 'simple title'
                                            ],
                                            closure       : [
                                                    href: 'closure/uri',
                                                    type: 'application/halm+json'
                                            ],
                                            mixed         : [
                                                    href: 'mixed/uri',
                                                    type: 'application/halm+json'
                                            ],
                                            comlete       : [
                                                    href    : 'complete/uri?param=complete',
                                                    hreflang: 'ja',
                                                    type    : 'image/*',
                                                    title   : 'complete title'
                                            ]
                                    ]
                            ]
                    ]
            ]
            def result = hal?.asMap()

            expect['_embedded']['emb']['_links'].keySet().each { String rel ->
                assert result['_embedded']['emb']['_links'][rel]['href'] == expect['_embedded']['emb']['_links'][rel]['href']
                assert result['_embedded']['emb']['_links'][rel]['type'] == expect['_embedded']['emb']['_links'][rel]['type']
                assert result['_embedded']['emb']['_links'][rel]['template'] == expect['_embedded']['emb']['_links'][rel]['template']
                assert result['_embedded']['emb']['_links'][rel]['hreflang'] == expect['_embedded']['emb']['_links'][rel]['hreflang']
                assert result['_embedded']['emb']['_links'][rel]['title'] == expect['_embedded']['emb']['_links'][rel]['title']
                assert result['_embedded']['emb']['_links'][rel] == expect['_embedded']['emb']['_links'][rel]
            }

            hal?.asMap() == expect
    }

    void "top level templates in embedded"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params', null) {
                embedded 'emb', {
                    href 'emb/uri'
                    body {
                        template 'simpleRelative', 'simple/relative'
                        template 'simpleAbsolute', '/simple/absolute'
                        template 'simpleComplete', 'simple/complete', 'param=simple', Locale.getInstance('ru', 'RU', ''), 'text/plane', 'simple title'
                        template {
                            rel 'closure'
                            href 'closure/uri'
                        }
                        template 'mixed', {
                            href 'mixed/uri'
                        }
                        template {
                            rel 'comlete'
                            href 'complete/uri'
                            params 'param=complete'
                            hreflang Locale.JAPANESE
                            type 'image/*'
                            title 'complete title'
                        }
                    }
                }
            }

        then:
            def expect = [
                    _links   : [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    _embedded: [
                            emb: [
                                    _links: [
                                            self          : [
                                                    href: 'emb/uri',
                                                    type: 'application/halm+json'
                                            ],
                                            simpleRelative: [
                                                    href    : 'simple/relative',
                                                    type    : 'application/halm+json',
                                                    template: true
                                            ],
                                            simpleAbsolute: [
                                                    href    : 'base/simple/absolute',
                                                    type    : 'application/halm+json',
                                                    template: true
                                            ],
                                            simpleComplete: [
                                                    href    : 'simple/complete?param=simple',
                                                    hreflang: 'ru_RU',
                                                    type    : 'text/plane',
                                                    title   : 'simple title',
                                                    template: true
                                            ],
                                            closure       : [
                                                    href    : 'closure/uri',
                                                    type    : 'application/halm+json',
                                                    template: true
                                            ],
                                            mixed         : [
                                                    href    : 'mixed/uri',
                                                    type    : 'application/halm+json',
                                                    template: true
                                            ],
                                            comlete       : [
                                                    href    : 'complete/uri?param=complete',
                                                    hreflang: 'ja',
                                                    type    : 'image/*',
                                                    title   : 'complete title',
                                                    template: true
                                            ]
                                    ]
                            ]
                    ]
            ]
            def result = hal?.asMap()

            expect['_embedded']['emb']['_links'].keySet().each { String rel ->
                assert result['_embedded']['emb']['_links'][rel]['href'] == expect['_embedded']['emb']['_links'][rel]['href']
                assert result['_embedded']['emb']['_links'][rel]['type'] == expect['_embedded']['emb']['_links'][rel]['type']
                assert result['_embedded']['emb']['_links'][rel]['template'] == expect['_embedded']['emb']['_links'][rel]['template']
                assert result['_embedded']['emb']['_links'][rel]['hreflang'] == expect['_embedded']['emb']['_links'][rel]['hreflang']
                assert result['_embedded']['emb']['_links'][rel]['title'] == expect['_embedded']['emb']['_links'][rel]['title']
                assert result['_embedded']['emb']['_links'][rel] == expect['_embedded']['emb']['_links'][rel]
            }

            hal?.asMap() == expect
    }

    void "top level embedded map"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params') {
                embedded 'emb', {
                    href 'emb/uri'
                    body([
                            str: 'String value',
                            int: 42,
                            arr: [1, 2, 3, 5, 7],
                            map: [key1: 'val 1', key2: []]
                    ])
                }
            }

        then:
            hal?.asMap() == [
                    _links   : [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    _embedded: [
                            emb: [
                                    _links: [
                                            self: [
                                                    href: 'emb/uri',
                                                    type: 'application/halm+json'
                                            ]
                                    ],
                                    str   : 'String value',
                                    int   : 42,
                                    arr   : [1, 2, 3, 5, 7],
                                    map   : [key1: 'val 1', key2: []]
                            ]
                    ]
            ]
    }

    void "top level embedded values"() {
        when:
            Halm hal = Halm.hal("base", 'path', 'params') {
                embedded 'emb', {
                    href 'emb/uri'
                    body {
                        str 'String value'
                        "int" 42
                        arr([1, 2, 3, 5, 7])
                        map([key1: 'val 1', key2: []])
                    }
                }
            }

        then:
            hal?.asMap() == [
                    _links   : [
                            self: [
                                    href: 'path?params',
                                    type: 'application/halm+json'
                            ]
                    ],
                    _embedded: [
                            emb: [
                                    _links: [
                                            self: [
                                                    href: 'emb/uri',
                                                    type: 'application/halm+json'
                                            ]
                                    ],
                                    str   : 'String value',
                                    int   : 42,
                                    arr   : [1, 2, 3, 5, 7],
                                    map   : [key1: 'val 1', key2: []]
                            ]
                    ]
            ]
    }
}
