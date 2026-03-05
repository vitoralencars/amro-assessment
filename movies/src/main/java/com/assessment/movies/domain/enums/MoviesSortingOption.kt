package com.assessment.movies.domain.enums

enum class MoviesSortingOption(val label: String) {
    POPULARITY("Popularity"),
    TITLE("Title"),
    RELEASE_DATE_DESC("Release date(desc)"),
    RELEASE_DATE_ASC("Release date(asc)"),
}
