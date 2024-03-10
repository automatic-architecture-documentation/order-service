package application.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController {

    @PostMapping
    fun post(@RequestBody request: SearchRequest): List<SearchResult> {
        return emptyList()
    }

    data class SearchRequest(val userName: String?)
    data class SearchResult(val id: String)
}
