The standard GSON view extension to support HAL responses designed to work nice with
Domain objects, but not with Map. Also it requires to put all elements of the HAL
structure (for example "self" inside "_links" which defined by HAL standard).
Missing them results in not useful HAL document.

In addition to that it is impossible to debug GSON view (with or without HAL extension).

This plug-in solves all these problems.

## HALM view builder

HALM plug-in defines the DSL to create HAL view from any objects and allows to include regular
Groovy code in the view. For debugging purpose developer can copy the content of the view inside
the Controller and debug it as regular code. Once code works as expected it can be coppied back to the view
and it should continue working.

There was no goal to make the HALM code looks like the HAL document this makes creation of the valid HAL document
more easy even with included Groovy snippets. Plug-in supports creation of the HAL document in the follow ways:

* automatically creating self link using the actual URL, mapped to the Controller
* regardless of the order of the elements they are sorted and combined as required
* multiple links/templates/embeddeds with the same name automatically combined into collection of
such objects
* special format of the URLs allows automatically use whatever servlet base URL to append relative
path or leave it relative

