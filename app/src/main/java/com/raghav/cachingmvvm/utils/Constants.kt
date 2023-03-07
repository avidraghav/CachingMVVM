package com.raghav.cachingmvvm.utils

class Constants {
    companion object {
        const val BASE_URL_SPACEFLIGHT = "https://api.spaceflightnewsapi.net/v3/"
        const val ARTICLE_DATE_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val LAUNCH_DATE_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val DATE_OUTPUT_FORMAT = "MMMM dd,yyyy HH:mm z"
        const val CHANNEL_ID = "id"
        const val CHANNEL_NAME = "channel"
        const val NOTIFICATION_ID: Int = 0
        const val MinutestoMiliseconds: Long = 900000 // 15 minutes
        const val STATUS_SET = "Reminder Set"
        const val SPACE_FLIGHT_API = "space_flight_api"
        const val LAUNCH_LIBRARY_API = "retrofit_client_for_launch_library_api"
        const val DATABASE_NAME = "reminder_db.db"
        const val LAUNCHES_INCREMENT = 10
        const val SKIP_ARTICLES_COUNT = 10
        const val ID_VIEW_GITHUB_REPO = "github_repo"
        const val ID_RATE_ON_PLAYSTORE = "rate_on_playstore"
        const val ID_MORE_APPS_FROM_DEVELOPER = "more_apps_from_developer"
        const val QSOL_PLAYSTORE_LINK =
            "https://play.google.com/store/apps/details?id=com.application.kurukshetrauniversitypapers"
        const val SPACE_DAWN_PLAYSTORE_LINK = "https://github.com/avidraghav/SpaceFlightNewsApp"

        const val SAMPLE_KEY = "SAMPLE_KEY"
    }
}
