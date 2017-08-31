The standard GSON view extension to support HAL responses designed to work nice with
Domain objects, but not with Map. Also it requires to put all elements of the HAL
structure (for example "self" inside "_links" which required by HAL standard).
Missing them results in invalid HAL document.

In addition to that it is impossible to debug GSON view (with or without HAL extension).

This plug-in solves all these problems.

## HALM view builder

HALM plug-in defines the DSL to create HAL view from any objects and allows to include regular
Groovy code in the view. For debugging purpose developer can copy the content of the view inside
the Controller and debug it as regular code. After code work as expected move code back to the view
and it should continue working.

Also the HALM code doesn't look like the HAL document it makes creation of the valid HAL document
more easy:

* automatically creating self link using the actual URL, mapped to the Controller
* regardless of the order of the elements they are sorted and combined as required
* multiple links/templates/embeddeds with the same name automatically combined into collection of
such objects
* special format of the URLs allows automatically use whatever servlet base URL to append relative
path or leave it relative

